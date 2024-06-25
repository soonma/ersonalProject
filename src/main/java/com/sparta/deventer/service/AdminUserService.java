package com.sparta.deventer.service;

import com.sparta.deventer.dto.ChangeNicknameRequestDto;
import com.sparta.deventer.dto.UserResponseDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.enums.UserStatus;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.InvalidOperationException;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    /**
     * 사용자 전체 목록을 조회합니다.
     *
     * @return 사용자 목록 응답 DTO 리스트
     */
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    /**
     * 사용자에게 관리자 권한을 부여합니다.
     *
     * @param userId  권한을 업데이트할 사용자 ID
     * @param newRole 새로운 권한
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponseDto updateUserRole(Long userId, UserRole newRole) {
        User user = getUserByIdOrThrow(userId);
        validateNotAdmin(user);
        user.setRole(newRole);
        return new UserResponseDto(user);
    }

    /**
     * 사용자의 닉네임을 수정합니다.
     *
     * @param userId                   닉네임을 업데이트할 사용자 ID
     * @param changeNicknameRequestDto 닉네임 변경 요청 DTO
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponseDto updateUserNickname(Long userId,
        ChangeNicknameRequestDto changeNicknameRequestDto) {

        User user = getUserByIdOrThrow(userId);
        validateNotAdmin(user);

        String newNickname = changeNicknameRequestDto.getNewNickname();
        user.setNickname(newNickname);
        return new UserResponseDto(user);
    }

    /**
     * 사용자를 차단합니다.
     *
     * @param userId 차단할 사용자 ID
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponseDto blockUser(Long userId) {
        User user = getUserByIdOrThrow(userId);
        validateNotAdmin(user);
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidOperationException("차단할 수 있는 상태가 아닙니다.");
        }
        return updateUserStatus(user, UserStatus.BLOCKED);
    }

    /**
     * 사용자를 활성화합니다.
     *
     * @param userId 활성화할 사용자 ID
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponseDto activateUser(Long userId) {
        User user = getUserByIdOrThrow(userId);
        validateNotAdmin(user);
        if (user.getStatus() != UserStatus.BLOCKED) {
            throw new InvalidOperationException("활성화할 수 있는 상태가 아닙니다.");
        }
        return updateUserStatus(user, UserStatus.ACTIVE);
    }

    /**
     * (관리자) 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자 ID
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponseDto deleteUser(Long userId) {
        User user = getUserByIdOrThrow(userId);
        validateNotAdmin(user);
        if (user.getStatus() != UserStatus.ACTIVE && user.getStatus() != UserStatus.BLOCKED) {
            throw new InvalidOperationException("삭제할 수 있는 상태가 아닙니다.");
        }
        return updateUserStatus(user, UserStatus.DELETED);
    }

    /**
     * 사용자 ID로 사용자를 찾고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     * @throws EntityNotFoundException 사용자를 찾을 수 없는 경우
     */
    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));
    }

    /**
     * 사용자의 상태를 업데이트합니다.
     *
     * @param user   사용자 엔티티
     * @param status 새로운 사용자 상태
     * @return 업데이트된 사용자 응답 DTO
     */
    private UserResponseDto updateUserStatus(User user, UserStatus status) {
        user.setStatus(status);
        return new UserResponseDto(user);
    }

    /**
     * 관리자인지 여부를 검증합니다.
     *
     * @param user 사용자 엔티티
     * @throws InvalidOperationException 사용자가 관리자인 경우
     */
    private void validateNotAdmin(User user) {
        if (user.getRole() == UserRole.ADMIN) {
            throw new InvalidOperationException("관리자의 상태는 변경할 수 없습니다.");
        }
    }
}