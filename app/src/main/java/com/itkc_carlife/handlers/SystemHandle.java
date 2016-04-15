package com.itkc_carlife.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SystemHandle {

    public final static String NAME = "CSH";
    public final static int MODE = 2;

    private final static String IS_SHOW = "IS_SHOW";

    public static void saveIntMessage(Context context, String ksy, int i) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putInt(ksy, i);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getInt(key, 0);
    }

    // *************************************************************************

    public static void saveStringMessage(Context context, String ksy, String v) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putString(ksy, v);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getString(key, "");
    }

    // *************************************************************************

    public static void saveBooleanMessage(Context context, String ksy, boolean b) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putBoolean(ksy, b);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getBoolean(key, false);
    }

    // *************************************************************************

    public static void saveLongMessage(Context context, String ksy, long l) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putLong(ksy, l);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getLong(key, 0);
    }

    // *************************************************************************

    public static boolean getIsGoneShow(Context context) {
        return getBoolean(context, IS_SHOW);
    }

    public static void saveIsGoneShow(Context context, boolean b) {
        saveBooleanMessage(context, IS_SHOW, b);
    }

    //*************************************************************************
    public static void saveDoubleMessage(Context context, String key, double d) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putString(key, String.valueOf(d));
        editor.commit();
    }

    public static double getDouble(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        String d = pref.getString(key, "0");
        try {
            return Double.valueOf(d);
        } catch (Exception e) {
            return 0;
        }
    }
}
