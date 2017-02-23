package com.example.hiennguyen.firebaseexample.utilities;

import android.content.SharedPreferences;

public class PreferenceUtil {

    public static final String PREF_TOKEN = "cPrefToken";
    public static final String PREF_USER_UUID = "mPrefUserUUID";
    public static final String PREF_GROCERY_ITEMS = "groceryItems";

    private static SharedPreferences sharedPreferences = null;

    public static void setSharedPreferences(SharedPreferences preferences) {
        sharedPreferences = preferences;
    }

    protected static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void saveFCMToken(String token) {
        putString(PREF_TOKEN, token);
    }

    public static String getFCMToken() {
        return getString(PREF_TOKEN);
    }

    public static void saveFirebaseUUID(String key) {
        putString(PREF_USER_UUID, key);
    }

    public static void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

}