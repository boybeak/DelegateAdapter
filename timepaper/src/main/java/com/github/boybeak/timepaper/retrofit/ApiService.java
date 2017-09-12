package com.github.boybeak.timepaper.retrofit;

import com.github.boybeak.timepaper.model.Photo;
import com.github.boybeak.timepaper.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public interface ApiService {

    @GET("photos")
    Call<List<Photo>> photos (@Query("access_token") String access_token,
                              @Query("page") int page,
                              @Query("per_page") int per_page,
                              @Query("order_by") String order_by);

    @GET("photos/{id}")
    Call<Photo> photoDetail (@Path("id") String id,
                             @Query("access_token") String access_token,
                             @Query("w") int w, @Query("h") int h,
                             @Query("rect") String rect);

    @GET("photos/random")
    Call<List<Photo>> randomPhotos (@Query("access_token") String access_token,
                                    @Query("w") int w,
                                    @Query("h") int h,
                                    @Query("orientation") String orientation,
                                    @Query("count") int count);

    @GET("photos/curated")
    Call<List<Photo>> curatedPhotos (@Query("access_token") String access_token,
                                          @Query("page") int page,
                                          @Query("per_page") int per_page,
                                          @Query("order_by") String order_by);

    @GET("me")
    Call<User> me (@Query("access_token") String access_token);

    @GET("users/{username}")
    Call<User> userProfile (@Path("username") String username,
                            @Query("access_token") String access_token,
                            @Query("w") int w,
                            @Query("h") int h);

    @GET("/users/{username}/photos")
    Call<List<Photo>> userPhotos (@Path("username") String username,
                                  @Query("access_token") String access_token,
                                  @Query("page") int page,
                                  @Query("per_page") int per_page);

}