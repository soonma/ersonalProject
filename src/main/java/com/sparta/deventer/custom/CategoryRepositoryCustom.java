package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Category;
import java.util.Optional;

public interface CategoryRepositoryCustom {

    Optional<Category> findByTopic(String categoryTopic);

    boolean existsByTopic(String topic);
}
