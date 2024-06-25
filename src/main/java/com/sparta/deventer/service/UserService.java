package com.sparta.deventer.service;

import com.sparta.deventer.dto.ChangePasswordRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.dto.UpdateProfileRequestDto;
import com.sparta.deventer.entity.PasswordHistory;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.InvalidPasswordException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.PasswordHistoryRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자의 프로필을 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @param user   현재 인증된 사용자
     * @return 프로필 응답 DTO
     */
    public ProfileResponseDto getProfile(Long userId, User user) {
        validateUserId(userId, user);
        return new ProfileResponseDto(user);
    }

    /**
     * 사용자의 모든 게시물을 조회합니다.
     *
     * @param userId   조회할 사용자 ID
     * @param user     현재 인증된 사용자
     * @param pageable 페이지 정보
     * @return 페이지 단위로 나눠진 게시물 응답 DTO
     */
    public Page<PostResponseDto> getAllPosts(Long userId, User user, Pageable pageable) {
        validateUserId(userId, user);
        return postRepository.findByUserId(userId, pageable).map(PostResponseDto::new);
    }

    /**
     * 사용자의 모든 댓글을 조회합니다.
     *
     * @param userId   조회할 사용자 ID
     * @param user     현재 인증된 사용자
     * @param pageable 페이지 정보
     * @return 페이지 단위로 나눠진 댓글 응답 DTO
     */
    public Page<CommentResponseDto> getAllComments(Long userId, User user, Pageable pageable) {
        validateUserId(userId, user);
        return commentRepository.findByUserId(userId, pageable).map(CommentResponseDto::new);
    }

    /**
     * 사용자의 프로필을 수정합니다.
     *
     * @param userId                  수정할 사용자 ID
     * @param updateProfileRequestDto 프로필 수정 요청 DTO
     * @param user                    현재 인증된 사용자
     * @return 수정된 프로필 응답 DTO
     */
    public ProfileResponseDto updateProfile(
        Long userId,
        UpdateProfileRequestDto updateProfileRequestDto,
        User user) {

        validateUserId(userId, user);

        user.setNickname(updateProfileRequestDto.getNickname());
        user.setEmail(updateProfileRequestDto.getEmail());
        userRepository.save(user);
        return new ProfileResponseDto(user);
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId                   변경할 사용자 ID
     * @param changePasswordRequestDto 비밀번호 변경 요청 DTO
     * @param user                     현재 인증된 사용자
     */
    public void changePassword(
        Long userId,
        ChangePasswordRequestDto changePasswordRequestDto,
        User user) {

        validateUserId(userId, user);

        String currentPassword = changePasswordRequestDto.getCurrentPassword();
        String newPassword = changePasswordRequestDto.getNewPassword();
        List<PasswordHistory> passwordHistoryList =
            passwordHistoryRepository.findByUserOrderByCreatedAtAsc(user);

        user.validatePassword(passwordEncoder, currentPassword);
        validateNewPassword(user, newPassword);
        validatePasswordHistory(newPassword, passwordHistoryList);

        PasswordHistory newHistory = new PasswordHistory(passwordEncoder.encode(newPassword), user);
        passwordHistoryRepository.save(newHistory);

        if (passwordHistoryList.size() > 2) {
            passwordHistoryRepository.delete(passwordHistoryList.get(0));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 새로운 비밀번호가 현재 비밀번호와 다른지 검증합니다.
     *
     * @param user        현재 사용자
     * @param newPassword 새로운 비밀번호
     */
    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidPasswordException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
    }

    /**
     * 새로운 비밀번호가 이전에 사용한 비밀번호와 다른지 검증합니다.
     *
     * @param newPassword         새로운 비밀번호
     * @param passwordHistoryList 비밀번호 변경 이력 목록
     */
    private void validatePasswordHistory(
        String newPassword,
        List<PasswordHistory> passwordHistoryList) {

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordEncoder.matches(newPassword, passwordHistory.getPassword())) {
                throw new InvalidPasswordException("새 비밀번호는 최근 사용한 비밀번호와 달라야 합니다.");
            }
        }
    }

    /**
     * 사용자 ID를 검증합니다.
     *
     * @param userId 검증할 사용자 ID
     * @param user   현재 인증된 사용자
     */
    private void validateUserId(Long userId, User user) {
        user.validateId(userId);
    }
}