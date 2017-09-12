package com.github.boybeak.timepaper.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class MetaData {

    private static String sClientId, sClientSecret, sRedirectUri;

    public static String getClientId (Context context) {
        if (TextUtils.isEmpty(sClientId)) {
            sClientId = getStringMetaData(context, "CLIENT_ID");
        }
        return sClientId;
    }

    public static String getClientSecret (Context context) {
        if (TextUtils.isEmpty(sClientSecret)) {
            sClientSecret = getStringMetaData(context, "CLIENT_SECRET");
        }
        return sClientSecret;
    }

    public static String getRedirectUri (Context context) {
        if (TextUtils.isEmpty(sRedirectUri)) {
            sRedirectUri = getStringMetaData(context, "REDIRECT_URI");
        }
        return sRedirectUri;
    }

    public static String getStringMetaData (Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String value = bundle.getString(name);
            return value;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
