package com.effectivesoft.analytics;

import android.app.Application;
import android.text.TextUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.effectivesoft.utils.EndPoint;

import org.json.JSONException;
import org.json.JSONObject;

public class MixpanelLogger extends SimpleLogger {

    private static MixpanelAPI mixpanelAPI;

    public void init(Application application) {
        mixpanelAPI = MixpanelAPI.getInstance(application, EndPoint.MIXPANEL_TOKEN);
    }

    @Override
    public void logEvent(String eventType, String eventName) {
        mixpanelAPI.track(getEventFullName(eventType, eventName));
    }

    @Override
    public void logEvent(String eventType, String eventName, String paramName, String paramValue) {
        mixpanelAPI.track(getEventFullName(eventType, eventName), getJSONProperties(paramName, paramValue));
    }

    @Override
    public void startTimedEvent(String eventType, String eventName) {
        mixpanelAPI.timeEvent(getEventFullName(eventType, eventName));
    }

    @Override
    public void endTimedEvent(String eventType, String eventName) {
        mixpanelAPI.track(getEventFullName(eventType, eventName));
    }

    private static JSONObject getJSONProperties(String paramName, String paramValue) {
        JSONObject properties = new JSONObject();
        if (!TextUtils.isEmpty(paramName)) {
            try {
                properties.put(paramName, TextUtils.isEmpty(paramValue) ? "" : paramValue);
            } catch (JSONException e) {
                CrashlyticsLogger.logError(e);
            }
        }
        return properties;
    }
}
