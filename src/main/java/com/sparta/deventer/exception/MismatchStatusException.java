package com.sparta.deventer.exception;

import com.sparta.deventer.enums.UserActionError;

public class MismatchStatusException extends RuntimeException {

    public MismatchStatusException(UserActionError userActionError) {
        super(userActionError.getMessage());
    }
}
