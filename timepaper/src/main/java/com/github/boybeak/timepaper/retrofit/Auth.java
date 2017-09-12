package com.github.boybeak.timepaper.retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.github.boybeak.timepaper.Finals;
import com.github.boybeak.timepaper.model.TokenInfo;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.utils.MetaData;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Auth {

    private static final String TAG = Auth.class.getSimpleName();

    private static final String AUTH_PREFERENCE = "auth_info", USER_PREFERENCE = "user_info";

    private static AuthService sAuthService;

    private static TokenInfo sToken;

    private static User sMe;

    static {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder ()
                .baseUrl("https://unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        sAuthService = retrofit.create(AuthService.class);
    }

    public static boolean isTokenExist (Context context) {
        if (sToken == null) {
            sToken = readTokenInfo(context);
        }
        return sToken != null && !TextUtils.isEmpty(sToken.access_token);
    }


    public static String getAccessToken (Context context) {
        if (sToken == null) {
            sToken = readTokenInfo(context);
        }
        return sToken.access_token;
    }

    private static TokenInfo readTokenInfo (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AUTH_PREFERENCE,
                Context.MODE_PRIVATE);

        TokenInfo info = new TokenInfo();
        info.access_token = preferences.getString("access_token", "");
        info.scope = preferences.getString("scope", "");
        info.token_type = preferences.getString("token_type", "");
        info.created_at = preferences.getLong("created_at", 0);

        return info;
    }

    private static void writeTokenInfo (Context context, TokenInfo tokenInfo) {
        SharedPreferences preferences = context.getSharedPreferences(AUTH_PREFERENCE,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("access_token", tokenInfo.access_token);
        editor.putString("scope", tokenInfo.scope);
        editor.putString("token_type", tokenInfo.token_type);
        editor.putLong("created_at", tokenInfo.created_at);

        editor.apply();
    }

    private static void writeMe (Context context, User me) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(me);
        editor.putString("me", json);
        editor.apply();
    }

    private static User readMe (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCE,
                Context.MODE_PRIVATE);
        String json = preferences.getString("me", null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, User.class);
    }

    public static User getMe (Context context) {
        if (sMe == null) {
            sMe = readMe(context);
        }

        return sMe;
    }

    public static boolean isMe (Context context, User user) {
        return getMe(context) != null && getMe(context).equals(user);
    }

    public static boolean hasMe (Context context) {
        if (sMe == null) {
            sMe = readMe(context);
        }
        return sMe != null;
    }

    public static void removeMe (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void logout (Context context) {
        sMe = null;
        sToken = null;
        removeMe(context);
        removeTokenInfo(context);

        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Finals.ACTION_LOGOUT));
    }

    private static void removeTokenInfo (Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AUTH_PREFERENCE,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void syncMe (final Context context, final UserListener listener) {
        Api.getMe(context).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User me = response.body();
                    if (me != null) {
                        sMe = me;
                        writeMe(context, me);
                        listener.onUserInfo(me);
                        return;
                    }
                }
                listener.onUserNotExist();
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                listener.onUserNotExist();
            }
        });
    }

    public static void getAuthToken (final Context context, String code, @NonNull final AuthListener authListener) {

        String clientId = MetaData.getClientId(context);
        String clientSecret = MetaData.getClientSecret(context);
        String redirectUri = MetaData.getRedirectUri(context);

        sAuthService.oauthToken(clientId, clientSecret, redirectUri, code, "authorization_code")
                .enqueue(new Callback<TokenInfo>() {
            @Override
            public void onResponse(@NonNull Call<TokenInfo> call, @NonNull Response<TokenInfo> response) {
                if (response.isSuccessful()) {
                    sToken = response.body();
                    writeTokenInfo(context, sToken);

                    syncMe(context, authListener);

                    authListener.onSuccess(sToken);
                } else {
                    authListener.onFailed(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenInfo> call, @NonNull Throwable t) {
                authListener.onFailed(t.getMessage());
            }
        });
    }

    public interface AuthListener extends UserListener {
        void onSuccess (TokenInfo tokenInfo);
        void onFailed (String message);

        void onUserInfo (User user);

    }

    public interface UserListener {
        void onUserInfo (User user);
        void onUserNotExist ();
    }

}
