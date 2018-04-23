package com.effectivesoft.analytics;

import android.app.Application;
import android.util.Log;

public class LocalLogger extends SimpleLogger {

    private static final String LOG_TAG = "AppLocalLogger";

    @Override
    public void init(Application application) {
        Log.d(LOG_TAG, "Init");
    }

    @Override
    public void logEvent(String eventType, String eventName) {
        Log.d(LOG_TAG, getEventFullName(eventType, eventName));
    }

    @Override
    public void logEvent(String eventType, String eventName, String paramName, String paramValue) {
        String logMessage = getEventFullName(eventType, eventName) + " {" + paramName + " = " + paramValue + "}";
        Log.d(LOG_TAG, logMessage);
    }

    @Override
    public void startTimedEvent(String eventType, String eventName) {
        String logMessage = getEventFullName(eventType, eventName) + " (start timed event)";
        Log.d(LOG_TAG, logMessage);
    }

    @Override
    public void endTimedEvent(String eventType, String eventName) {
        String logMessage = getEventFullName(eventType, eventName) + " (end timed event)";
        Log.d(LOG_TAG, logMessage);
    }

    @Override
    public void signIn(String email, String name, int accountId, boolean isMatchmaker) {
        String logMessage = "Sign In: " + email + ", " + name + ", " + accountId +
                ", is matchmaker = " + isMatchmaker;
        Log.d(LOG_TAG, logMessage);
    }

    @Override
    public void signOut() {
        Log.d(LOG_TAG, "Sign Out");
    }
}
