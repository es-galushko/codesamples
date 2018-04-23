package com.effectivesoft.analytics;

import android.app.Application;

public interface Logger {

    void init(Application application);

    void logEvent(String eventType, String eventName);

    void logEvent(String eventType, String eventName, String paramName, String paramValue);

    void startTimedEvent(String eventType, String eventName);

    void endTimedEvent(String eventType, String eventName);

    void signIn(String email, String name, int accountId, boolean isMatchmaker);

    void signOut();
}
