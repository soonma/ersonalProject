package com.sparta.deventer.service;

import com.sparta.deventer.dto.GitHubTokenResponseDto;
import com.sparta.deventer.dto.GitHubUserDto;
import com.sparta.deventer.dto.LoginRequestDto;
import com.sparta.deventer.dto.SignUpRequestDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.enums.UserLoginType;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.GitHubTokenException;
import com.sparta.deventer.exception.InvalidAdminCodeException;
import com.sparta.deventer.exception.InvalidTokenException;
import com.sparta.deventer.exception.UserInfoDuplicateException;
import com.sparta.deventer.jwt.JwtProvider;
import com.sparta.deventer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 환경변수 저장
     */
    @Value("${admin.code}")
    private String adminCode;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUri;

    @Value("${github.token.url}")
    private String tokenUrl;

    @Value("${github.user.info.url}")
    private String userInfoUrl;

    @Value("${github.user.password}")
    private String githubPassword;

    /**
     * 사용자가 회원가입을 합니다.
     *
     * @param signUpRequestDto 회원가입 요청 DTO
     * @return 회원가입 완료 메세지
     */
    public String userSignUp(SignUpRequestDto signUpRequestDto) {
        validateDuplicateUser(signUpRequestDto.getUsername(), signUpRequestDto.getNickname(),
            signUpRequestDto.getEmail());

        UserRole role = UserRole.USER;
        if (signUpRequestDto.isAdminStatus()) {
            if (Objects.equals(adminCode, signUpRequestDto.getAdminCode())) {
                role = UserRole.ADMIN;
            } else {
                throw new InvalidAdminCodeException("관리자 키가 일치하지 않습니다.");
            }
        }

        User user = new User(
            signUpRequestDto.getUsername(),
            passwordEncoder.encode(signUpRequestDto.getPassword()),
            signUpRequestDto.getNickname(),
            role,
            signUpRequestDto.getEmail(),
            UserLoginType.DEFAULT
        );
        userRepository.save(user);

        return "회원가입이 완료되었습니다.";
    }

    /**
     * 사용자가 로그인합니다.
     *
     * @param loginRequestDto 로그인 요청 DTO
     * @param response        억세스 토큰과 리프레시 토큰을 담아 줄 응답
     * @return 로그인 완료 메세지
     */
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = getUserByUsernameOrThrow(loginRequestDto.getUsername());
        user.checkUserWithdrawn();
        user.validatePassword(passwordEncoder, loginRequestDto.getPassword());

        // 인증 매니저를 통해서 아이디, 비밀번호를 통해 인증을 진행하고 Security Context에 저장
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        issueTokens(response, user);

        return "로그인에 성공했습니다";
    }

    /**
     * 사용자가 Github 계정으로 로그인합니다.
     *
     * @param code     사용자가 Github으로부터 받은 인증코드
     * @param response 사용자에게 Deventer 서비스의 액세스 토큰과 리프레시 토큰을 반환할 Response
     */
    @Transactional
    public void processGitHubCallback(String code, HttpServletResponse response) {
        GitHubTokenResponseDto accessToken = getAccessToken(code);
        GitHubUserDto gitHubUserDto = getUserInfo(accessToken);
        User user = registerOrUpdateUser(gitHubUserDto);
        issueTokens(response, user);
    }

    /**
     * 토큰을 재발행합니다.
     *
     * @param request  기존의 토큰을 확인하기 위해 담을 Request
     * @param response 새로운 토큰을 전달하기 위해 담을 Response
     * @return 토큰 발행 완료 메세지
     */
    @Transactional
    public String tokenReissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtProvider.getJwtFromHeader(request, JwtProvider.REFRESH_HEADER);
        jwtProvider.validateToken(refreshToken);
        String refreshUsername = jwtProvider.getUsername(refreshToken);
        User user = getUserByUsernameOrThrow(refreshUsername);

        validateRefreshToken(user.getRefreshToken(), request.getHeader(JwtProvider.REFRESH_HEADER));
        issueTokens(response, user);

        return "토큰이 재발행 되었습니다.";
    }

    /**
     * 사용자가 로그아웃합니다.
     *
     * @param userId Security Context에서 가져온 사용자의 PK (사용자를 직접 가져와도 준영속성 상태이므로 영속성 상태로 만들어야함)
     * @return 로그아웃 완료 메세지
     */
    @Transactional
    public String logout(Long userId) {
        User user = getUserByIdOrThrow(userId);
        user.saveRefreshToken(null);
        return "로그아웃을 성공했습니다";
    }

    /**
     * 사용자가 회원을 탈퇴합니다.
     *
     * @param userId Security Context에서 가져온 유저의 PK
     * @return 회원 탈퇴 완료 메시지
     */
    @Transactional
    public String withdraw(Long userId) {
        User user = getUserByIdOrThrow(userId);
        user.softWithdrawUser();
        return "계졍이 정상적으로 탈퇴되었습니다.";
    }

    /**
     * 사용자의 인증코드로부터 Github으로부터 액세스 토큰을 발급 받습니다.
     *
     * @param code 사용자의 Github으로부터 받은 인증 코드
     * @return 액세스토큰
     */
    private GitHubTokenResponseDto getAccessToken(String code) {
        String accessTokenUrl = UriComponentsBuilder.fromUriString(tokenUrl)
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .queryParam("redirect_uri", redirectUri)
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitHubTokenResponseDto> tokenResponse = restTemplate.postForEntity(
            accessTokenUrl, entity, GitHubTokenResponseDto.class);

        if (tokenResponse.getBody() == null || tokenResponse.getBody().getAccess_token() == null) {
            throw new GitHubTokenException("Github 토큰에 문제가 발생했습니다.");
        }

        return tokenResponse.getBody();
    }

    /**
     * 발급받은 액세스 토큰으로 사용자의 정보를 Github에게 다시 요청합니다.
     *
     * @param gitHubTokenResponseDto 사용자의 인증코드로 발급 받은 액세스토큰
     * @return 사용자의 정보를 담은 DTO
     */
    private GitHubUserDto getUserInfo(GitHubTokenResponseDto gitHubTokenResponseDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtProvider.ACCESS_HEADER,
            JwtProvider.BEARER_PREFIX + gitHubTokenResponseDto.getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitHubUserDto> userResponse = restTemplate.exchange(userInfoUrl,
            HttpMethod.GET, entity, GitHubUserDto.class);
        return userResponse.getBody();
    }

    /**
     * 받아 온 사용자의 정보를 통해 회원인지 이미 가입된 사용자인지 확인 후 이미 가입된 사용자라면 기존 정보를 전달하고 아니라면 회원가입을 합니다.
     *
     * @param gitHubUserDto 억세스토큰으로 깃허브로부터 받은 유저정보 DTO
     * @return 우리 서비스의 유저 객체
     */
    private User registerOrUpdateUser(GitHubUserDto gitHubUserDto) {
        String username = gitHubUserDto.getUsername();
        String email = gitHubUserDto.getEmail();

        UserLoginType loginType = UserLoginType.GITHUB;
        Optional<User> optionalUser = userRepository.findByEmailAndLoginType(email, loginType);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            User newUser = new User(
                username,
                passwordEncoder.encode(githubPassword),
                username,
                UserRole.USER,
                email,
                UserLoginType.GITHUB
            );

            return userRepository.save(newUser);
        }
    }

    /**
     * 아이디, 닉네임, 이메일의 중복을 확인합니다.
     *
     * @param username 유저의 로그인 ID
     * @param nickname 유저의 닉네임
     * @param email    유저의 이메일
     */
    private void validateDuplicateUser(String username, String nickname, String email) {
        List<String> duplicateMessages = new ArrayList<>(3);

        if (userRepository.existsByUsername(username)) {
            duplicateMessages.add("사용자 ID가 중복됩니다.");
        }

        if (userRepository.existsByNickname(nickname)) {
            duplicateMessages.add("닉네임이 중복됩니다.");
        }

        if (userRepository.existsByEmail(email)) {
            duplicateMessages.add("이메일이 중복됩니다.");
        }

        if (!duplicateMessages.isEmpty()) {
            throw new UserInfoDuplicateException(duplicateMessages);
        }
    }

    /**
     * 사용자 객체를 PK로 찾고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param userId 유저 고유번호 (PK)
     * @return 사용자 객체
     */
    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));
    }

    /**
     * 사용자 객체를 사용자 로그인 ID로 찾아고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param username 사용자 로그인 ID
     * @return 사용자 객체
     */
    private User getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));
    }

    /**
     * 토큰을 발행합니다.
     *
     * @param response 토큰을 담아줄 Response
     * @param user     리프레시 토큰정보를 저장할 User 객체
     */
    private void issueTokens(HttpServletResponse response, User user) {
        String accessToken = jwtProvider.createAccessToken(user.getUsername(),
            user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername());

        user.saveRefreshToken(refreshToken);

        // 토큰 담아주기
        response.addHeader(JwtProvider.ACCESS_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_HEADER, refreshToken);
    }

    /**
     * 리프레시 토큰을 비교하여 검증합니다.
     *
     * @param userRefreshToken 저장되어있는 리프레시 토큰
     * @param refreshToken     비교할 리프레시 토큰
     */
    private void validateRefreshToken(String userRefreshToken, String refreshToken) {
        if (!Objects.equals(userRefreshToken, refreshToken)) {
            throw new InvalidTokenException("토큰 정보가 일치하지 않습니다.");
        }
    }
}