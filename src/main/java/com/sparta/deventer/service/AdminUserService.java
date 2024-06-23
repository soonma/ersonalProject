package com.sparta.deventer.service;

import com.sparta.deventer.dto.UserResponseDto;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return null;
    }

    public UserResponseDto updateUserRole(Long userId, UserRole newRole) {
        return null;
    }

    public UserResponseDto updateUserNickname(Long userId, String newNickname) {
        return null;
    }

    public UserResponseDto blockUser(Long userId) {
        return null;
    }

    public void deleteUser(Long userId) {
    }
}