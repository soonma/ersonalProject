package com.sparta.deventer.exception;

import java.util.List;

public class DuplicateException extends RuntimeException {

    private List<String> errorMessages;

    public DuplicateException(List<String> errorMessageList) {
        this.errorMessages = errorMessageList;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
