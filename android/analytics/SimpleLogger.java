package com.effectivesoft.analytics;

import android.app.Application;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stub/no-op implementations of all methods of {@link Logger}.
 * Override this if you only care about a few of the available callback methods.
 */
public class SimpleLogger implements Logger {

    protected static final String START_TIMED_EVENT_POSTFIX = "start timed event";
    protected static final String END_TIMED_EVENT_POSTFIX = "stop timed event";

    private final static String EVENT_NAME_PARAMETER_SEPARATOR = " - ";

    @Override
    public void init(Application application) {
    }

    @Override
    public void logEvent(String eventType, String eventName) {
    }

    @Override
    public void logEvent(String eventType, String eventName, String paramName, String paramValue) {
    }


    @Override
    public void startTimedEvent(String eventType, String eventName) {
        logEvent(eventType, eventName, START_TIMED_EVENT_POSTFIX, null);
    }

    @Override
    public void endTimedEvent(String eventType, String eventName) {
        logEvent(eventType, eventName, END_TIMED_EVENT_POSTFIX, null);
    }

    @Override
    public void signIn(String email, String name, int accountId, boolean isMatchmaker) {
    }

    @Override
    public void signOut() {
    }

    protected static String getEventFullName(String eventType, String eventName) {
        return eventType + (TextUtils.isEmpty(eventName) ? "" : (EVENT_NAME_PARAMETER_SEPARATOR + eventName));
    }

    protected static Map<String, String> getParamsMap(String paramName, String paramValue) {
        Map<String, String> params = new HashMap<>(1);
        if (!TextUtils.isEmpty(paramName)) {
            params.put(paramName, TextUtils.isEmpty(paramValue) ? "" : paramValue);
        }
        return params;
    }
}
