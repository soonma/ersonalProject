package com.sparta.deventer.custom;

import com.sparta.deventer.entity.PasswordHistory;
import com.sparta.deventer.entity.User;
import java.util.List;

public interface PasswordHistoryRepositoryCustom {

    List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user);

}
