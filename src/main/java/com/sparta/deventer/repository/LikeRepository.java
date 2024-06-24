package com.sparta.deventer.repository;


import com.sparta.deventer.entity.ContentEnumType;
import com.sparta.deventer.entity.Like;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByContentIdAndContentTypeAndUserId(Long contentId,
            ContentEnumType contentType,
            Long id);

    List<Like> findByContentIdAndContentType(Long contentId, ContentEnumType contentType);
}
