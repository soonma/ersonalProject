package com.sparta.deventer.service;

import com.sparta.deventer.dto.LoginDto;
import com.sparta.deventer.dto.SignUpUserDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.exception.DuplicateException;
import com.sparta.deventer.exception.InvalidException;
import com.sparta.deventer.jwt.JwtProvider;
import com.sparta.deventer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String signUp(SignUpUserDto requestDto) {

        checkDuplicateUser(requestDto);

        User user = new User(
                requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname(),
                UserRole.USER,
                requestDto.getEmail()
        );

        userRepository.save(user);

        return "회원가입이 완료되었습니다.";
    }

    @Transactional
    public String login(LoginDto requestDto, HttpServletResponse response) {

        User user = getUserByUsername(requestDto.getUsername());

        user.checkUserWithdrawn();

        user.validatePassword(passwordEncoder, requestDto.getPassword());

        // 인증 매니저를 통해서 아이디, 비번을 통해 인증 진행하고 Security Context 에 저장
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                        requestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        tokenIssuanceAndSave(response, user);

        return "로그인 성공했습니다";
    }

    @Transactional
    public String tokenReissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtProvider.getJwtFromHeader(request, JwtProvider.REFRESH_HEADER);

        jwtProvider.validateToken(refreshToken);

        String refreshUsername = jwtProvider.getUsername(refreshToken);

        User user = getUserByUsername(refreshUsername);

        validateRefreshToken(user, request.getHeader(JwtProvider.REFRESH_HEADER));

        tokenIssuanceAndSave(response, user);

        return "토큰이 재발행 되었습니다.";
    }

    @Transactional
    public String logout(Long userId) {

        User user = getUserByUserId(userId);

        user.saveRefreshToken(null);

        return "로그아웃을 성공했습니다";
    }

    @Transactional
    public String withdraw(Long userId) {

        User user = getUserByUserId(userId);

        user.softWithdrawUser();

        return "회원 탈퇴가 정상적으로 되었습니다.";
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
    }

    private void checkDuplicateUser(SignUpUserDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateException("ID가 중복됩니다.");
        }

        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new DuplicateException("닉네임이 중복됩니다.");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateException("이메일이 중복됩니다.");
        }
    }

    private void tokenIssuanceAndSave(HttpServletResponse response, User user) {
        String accessToken = jwtProvider.createAccessToken(user.getUsername(),
                user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername());

        user.saveRefreshToken(refreshToken);

        // 토큰 담아주기
        response.addHeader(JwtProvider.ACCESS_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_HEADER, refreshToken);
    }

    private void validateRefreshToken(User user, String refreshToken) {
        if (!Objects.equals(user.getRefreshToken(), refreshToken)) {
            throw new InvalidException("토큰 정보가 일치하지 않습니다.");
        }
    }
}