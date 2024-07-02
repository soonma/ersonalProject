package com.sparta.deventer.repository;

import com.sparta.deventer.custom.PasswordHistoryRepositoryCustom;
import com.sparta.deventer.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long>,
        PasswordHistoryRepositoryCustom {

//    List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user);
}