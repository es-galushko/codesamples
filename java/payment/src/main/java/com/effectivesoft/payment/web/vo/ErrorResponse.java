package com.effectivesoft.payment.web.vo;

import java.util.List;

public class ErrorResponse {

    private List<FieldMessage> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(List<FieldMessage> errors) {
        this.errors = errors;
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldMessage> errors) {
        this.errors = errors;
    }
}
