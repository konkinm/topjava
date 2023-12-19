package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorType {
    APP_ERROR("Application error"),
    DATA_NOT_FOUND("Data not found"),
    DATA_ERROR("Data error"),
    VALIDATION_ERROR("Validation error");

    public final String label;

    private ErrorType(String label) {
        this.label = label;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.label;
    }
}