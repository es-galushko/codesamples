package com.effectivesoft.analytics;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.effectivesoft.AppApp;

public class FirebaseLogger extends SimpleLogger {

    @Override
    public void init(Application application) {
        FirebaseAnalytics.getInstance(application);
    }

    @Override
    public void logEvent(String eventType, String eventName) {
        FirebaseAnalytics.getInstance(AppApp.getContext()).logEvent(getEventFullName(eventType, eventName), null);
    }

    @Override
    public void logEvent(String eventType, String eventName, String paramName, String paramValue) {
        Bundle params = new Bundle();
        params.putString(paramName, paramValue);
        FirebaseAnalytics.getInstance(AppApp.getContext()).logEvent(getEventFullName(eventType, eventName), params);
    }
}
