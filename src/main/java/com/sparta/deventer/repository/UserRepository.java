package com.sparta.deventer.repository;

import com.sparta.deventer.custom.UserRepositoryCustom;
import com.sparta.deventer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

//    @Override
//    Optional<User> findByUsername(String username);
//
//    @Override
//    boolean existsByUsername(String username);
//
//    @Override
//    boolean existsByNickname(String nickname);
//
//    @Override
//    boolean existsByEmail(String email);
//
//    @Override
//    Optional<User> findByEmail(String email);
//
//    @Override
//    Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType);
}
