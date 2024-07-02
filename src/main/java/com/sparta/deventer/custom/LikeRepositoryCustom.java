package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.LikeableEntityType;
import java.util.List;
import java.util.Optional;

public interface LikeRepositoryCustom {

    Optional<Like> findByLikeableEntityIdAndLikeableEntityTypeAndUserId(Long likeableEntityId,
            LikeableEntityType likeableEntityType, Long userId);

    List<Like> findAllByLikeableEntityIdAndLikeableEntityType(Long likableEntityId,
            LikeableEntityType likeableEntityType);
}
