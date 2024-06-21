package com.sparta.deventer.service;

import com.sparta.deventer.dto.ChangePasswordRequestDto;
import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.dto.UpdateProfileRequestDto;
import com.sparta.deventer.entity.PasswordHistory;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.InvalidPasswordException;
import com.sparta.deventer.repository.PasswordHistoryRepository;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDto getProfile(Long userId, User user) {
        user.validateId(userId);
        return new ProfileResponseDto(user);
    }

    public ProfileResponseDto updateProfile(Long userId,
        UpdateProfileRequestDto updateProfileRequestDto, User user) {

        user.validateId(userId);

        user.setNickname(updateProfileRequestDto.getNickname());
        user.setEmail(updateProfileRequestDto.getEmail());
        userRepository.save(user);
        return new ProfileResponseDto(user);
    }

    public void changePassword(Long userId, ChangePasswordRequestDto changePasswordRequestDto,
        User user) {

        user.validateId(userId);

        String currentPassword = changePasswordRequestDto.getCurrentPassword();
        String newPassword = changePasswordRequestDto.getNewPassword();
        List<PasswordHistory> passwordHistoryList =
            passwordHistoryRepository.findByUserOrderByCreatedAtAsc(user);

        user.validatePassword(passwordEncoder, currentPassword);
        validateNewPassword(user, newPassword);
        validatePasswordHistory(user, newPassword, passwordHistoryList);

        PasswordHistory newHistory = new PasswordHistory(newPassword, user);
        passwordHistoryRepository.save(newHistory);

        if (passwordHistoryList.size() > 3) {
            passwordHistoryRepository.delete(passwordHistoryList.get(0));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidPasswordException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
    }

    public void validatePasswordHistory(User user, String newPassword,
        List<PasswordHistory> passwordHistoryList) {

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordEncoder.matches(newPassword, passwordHistory.getPassword())) {
                throw new InvalidPasswordException("새 비밀번호는 최근 사용한 비밀번호와 달라야 합니다.");
            }
        }
    }
}