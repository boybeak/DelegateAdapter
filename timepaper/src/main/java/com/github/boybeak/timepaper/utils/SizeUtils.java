package com.github.boybeak.timepaper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

/**
 * Created by gaoyunfei on 2017/9/7.
 */

public class SizeUtils {

    private static final String PREF_NAME = "size.pref";

    private static int WIDTH_P = 0, HEIGHT_P = 0, WIDTH_L = 0, HEIGHT_L = 0;

    public static void saveScreenSize (Activity activity, int width, int height) {
        SharedPreferences preferences = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (activity.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                WIDTH_L = width;
                HEIGHT_L = height;

                WIDTH_P = HEIGHT_L;
                HEIGHT_P = WIDTH_L;
                break;
            default:
                WIDTH_P = width;
                HEIGHT_P = height;

                WIDTH_L = HEIGHT_P;
                HEIGHT_L = WIDTH_P;
                break;
        }
        editor.putInt("width_p", WIDTH_P);
        editor.putInt("height_p", HEIGHT_P);
        editor.putInt("width_l", WIDTH_L);
        editor.putInt("height_l", HEIGHT_L);

        editor.apply();
    }

    public static int getScreenWidth (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (WIDTH_L == 0) {
                WIDTH_L = preferences.getInt("width_l", 0);
            }
            return WIDTH_L;
        } else {
            if (WIDTH_P == 0) {
                WIDTH_P = preferences.getInt("width_p", 0);
            }
            return WIDTH_P;
        }
    }

    public static int getScreenHeight (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (HEIGHT_L == 0) {
                HEIGHT_L = preferences.getInt("height_l", 0);
            }
            return HEIGHT_L;
        } else {
            if (HEIGHT_P == 0) {
                HEIGHT_P = preferences.getInt("height_p", 0);
            }
            return HEIGHT_P;
        }
    }
}
