package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTopic(String categoryTopic);

}
