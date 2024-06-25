package com.sparta.deventer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private long likeableEntityId;

    @Enumerated(EnumType.STRING)
    private LikeableEntityType likeableEntityType;

    @Builder
    public Like(long typeId, String type, User user) {
        this.likeableEntityId = typeId;
        this.likeableEntityType = LikeableEntityType.getByType(type);
        this.user = user;
    }
}