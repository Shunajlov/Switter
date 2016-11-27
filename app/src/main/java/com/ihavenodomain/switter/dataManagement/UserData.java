package com.ihavenodomain.switter.dataManagement;

import android.content.Context;
import android.content.SharedPreferences;

import com.ihavenodomain.switter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Для хранения данных
 */

public class UserData {
    private Context ctx;
    private int mode;
    private String prefPath;
    private String bearerToken;
    public static boolean dataLoading;
    private boolean serviceRunning;

    private static final String SERVICE_RUNNING = "is_service_running";
    private static final String BEARER_TOKEN = "wow_such_app_token";

    public UserData(Context ctx) {
        this.ctx = ctx;
        this.prefPath = ctx.getString(R.string.preferencesFile);
        this.mode = Context.MODE_PRIVATE;
    }

    public String getBearerToken() {
        bearerToken = readStringPreference(BEARER_TOKEN);
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
        saveStringPreference(BEARER_TOKEN, bearerToken);
    }

    public boolean isServiceRunning() {
        serviceRunning = readBooleanPreference(SERVICE_RUNNING);
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
        saveBooleanPreference(SERVICE_RUNNING, serviceRunning);
    }

    /**
     * Прочитать из настроек строковое значение
     * @param tag идентификатор, по которому определяется нужное значение
     * @return хранимая строка
     */
    private String readStringPreference(String tag) {
        SharedPreferences pref = ctx.getSharedPreferences(prefPath, mode);
        return pref.getString(tag, "");
    }

    /**
     * Сохранить в настройках строковое значение
     * @param tag идентификатор, по которому потом можно прочитать настройку
     * @param str значение, которое нужно сохранить
     */
    private void saveStringPreference(String tag, String str) {
        SharedPreferences pref = ctx.getSharedPreferences(prefPath, mode);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(tag, str);
        editor.apply();
    }

    private void saveBooleanPreference(String tag, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(prefPath, mode);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

    private boolean readBooleanPreference(String tag) {
        SharedPreferences pref = ctx.getSharedPreferences(prefPath, mode);
        return pref.getBoolean(tag, false);
    }
}
