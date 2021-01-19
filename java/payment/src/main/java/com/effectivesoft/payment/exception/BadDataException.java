package com.effectivesoft.payment.exception;

import java.util.Collections;
import java.util.List;

public class BadDataException extends RuntimeException {

    private List<BadDataMessage> messages;

    public BadDataException(String field, String errorCode, Object... arguments) {
        this(new BadDataMessage(field, errorCode, arguments));
    }

    public BadDataException(BadDataMessage message) {
        this(Collections.singletonList(message));
    }

    public BadDataException(List<BadDataMessage> messages) {
        super(null, null, true, false);
        this.messages = messages;
    }

    public List<BadDataMessage> getMessages() {
        return messages;
    }
}
