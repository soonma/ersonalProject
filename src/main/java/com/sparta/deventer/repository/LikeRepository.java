package com.sparta.deventer.repository;


import com.sparta.deventer.custom.LikeRepositoryCustom;
import com.sparta.deventer.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

//    Optional<Like> findByLikeableEntityIdAndLikeableEntityTypeAndUserId(Long likeableEntityId,
//            LikeableEntityType likeableEntityType, Long userId);
//
//    List<Like> findAllByLikeableEntityIdAndLikeableEntityType(Long likableEntityId,
//            LikeableEntityType likeableEntityType);
}
