package com.sparta.deventer.repository;


import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.LikeableEntityType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByLikeableEntityIdAndLikeableEntityTypeAndUserId(Long likeableEntityId,
        LikeableEntityType likeableEntityType, Long userId);

    List<Like> findAllByLikeableEntityIdAndLikeableEntityType(Long likableEntityId,
        LikeableEntityType likeableEntityType);
}
