package com.github.boybeak.timepaper.retrofit;

import android.content.Context;
import android.support.annotation.StringDef;

import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.model.Photo;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.utils.SizeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Api {

    public static final String LANDSCAPE = "landscape", PORTRAIT = "portrait", SQUARISH = "squarish";

    public static final String LATEST = "latest", OLDEST = "oldest", POPULAR = "popular";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({LANDSCAPE, PORTRAIT, SQUARISH})
    public @interface Orientation{}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({LATEST, OLDEST, POPULAR})
    public @interface OrderBy{}

    private static ApiService sApiService;

    static {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(40, TimeUnit.SECONDS)
                .build();

        /*GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")*/

        Retrofit retrofit = new Retrofit.Builder ()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        sApiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return sApiService;
    }

    public static Call<List<Photo>> randomPhotos (Context context, @Orientation String orientation) {
        final int width = SizeUtils.getScreenWidth(context);
        final int height = SizeUtils.getScreenHeight(context);
        return sApiService.randomPhotos(Auth.getAccessToken(context), width, height, orientation, 10);
    }

    public static Call<User> getMe (Context context) {
        return sApiService.me(Auth.getAccessToken(context));
    }

    public static Call<User> userProfile (Context context, User user) {
        final int size = context.getResources().getDimensionPixelSize(R.dimen.profile_size_big);
        return sApiService.userProfile(user.username, Auth.getAccessToken(context), size, size);
    }

    public static Call<List<Photo>> userPhotos (Context context, User user, int page) {
        return sApiService.userPhotos(user.username, Auth.getAccessToken(context), page, 12);
    }

    public static Call<List<Photo>> photos (Context context, int page, int perPage, @OrderBy String orderBy) {
        return sApiService.photos(Auth.getAccessToken(context), page, perPage, orderBy);
    }

    public static Call<Photo> photoDetail (Context context, Photo photo/*, boolean crop*/) {
        final int photoWidth = photo.width;
        final int photoHeight = photo.height;

        final int screenWidth = SizeUtils.getScreenWidth(context);
        final int screenHeight = SizeUtils.getScreenHeight(context);

        int x, y, width, height;

        float scaleW = photoWidth * 1f / screenWidth;
        float scaleH = photoHeight * 1f / screenHeight;

        float scale = Math.min(scaleW, scaleH);

        width = (int)(screenWidth * scale);
        height = (int)(screenHeight * scale);

        x = (photoWidth - width) / 2;
        y = (photoHeight - height) / 2;

        String rect = x + "," + y + "," + width + "," + height;

        /*if (crop) {

        } else {

        }*/
        return sApiService.photoDetail(photo.id, Auth.getAccessToken(context), width, height, rect);
    }

}
