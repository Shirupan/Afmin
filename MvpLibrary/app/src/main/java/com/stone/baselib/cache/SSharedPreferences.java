package com.stone.baselib.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.stone.baselib.SConfig;

/**
 * Stone
 * 2019/4/3
 **/
public class SSharedPreferences implements SCacheible {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String SP_NAME = SConfig.SP_NAME;

    private static SSharedPreferences instance;

    private SSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SSharedPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (SSharedPreferences.class) {
                if (instance == null) {
                    instance = new SSharedPreferences(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void put(String key, Object value) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public void clear() {
        editor.clear().apply();
    }


    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);

        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float value) {
        return sharedPreferences.getFloat(key, value);
    }

    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

}
