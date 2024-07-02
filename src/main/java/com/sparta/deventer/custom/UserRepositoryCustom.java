package com.sparta.deventer.custom;

import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.UserLoginType;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType);
}
