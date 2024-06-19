package com.sparta.notfound.repository;

import com.sparta.notfound.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
