package com.effectivesoft.payment.exception;

public class BadDataMessage {

    private String field;
    private String errorCode;
    private Object[] arguments;

    public BadDataMessage() {
    }

    public BadDataMessage(String field, String errorCode) {
        this.field = field;
        this.errorCode = errorCode;
    }

    public BadDataMessage(String field, String errorCode, Object... arguments) {
        this(field, errorCode);
        this.arguments = arguments;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
