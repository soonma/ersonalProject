package com.sparta.deventer.enums;

import lombok.Getter;

@Getter
public enum UserActionError {
    BLOCKED_USER("차단된 사용자입니다."),
    DELETED_USER("삭제된 사용자입니다."),
    NOT_AUTHORIZED("본인이 아닙니다."),
    SELF_ACTION_NOT_ALLOWED("본인을 대상으로 할 수 없습니다.");

    private final String message;

    UserActionError(String message) {
        this.message = message;
    }
}