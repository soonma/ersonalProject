package com.sparta.deventer.repository;

import com.sparta.deventer.custom.CategoryRepositoryCustom;
import com.sparta.deventer.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        CategoryRepositoryCustom {

//    Optional<Category> findByTopic(String categoryTopic);
//
//    boolean existsByTopic(String topic);
}
