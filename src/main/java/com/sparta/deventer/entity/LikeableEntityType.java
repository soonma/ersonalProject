package com.sparta.deventer.entity;

import java.util.Objects;
import lombok.Getter;

@Getter
public enum LikeableEntityType {
    POST("post"),
    COMMENT("comment");

    private final String type;

    LikeableEntityType(String type) {
        this.type = type;
    }

    public static LikeableEntityType getByType(String type) {
        if (Objects.equals(type, POST.type)) {
            return POST;
        } else if (Objects.equals(type, COMMENT.type)) {
            return COMMENT;
        } else {
            throw new IllegalArgumentException("좋아요 할 수 없는 엔티티입니다.");
        }
    }
}
