package com.github.boybeak.timepaper.retrofit;

import com.github.boybeak.timepaper.model.TokenInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public interface AuthService {

    @FormUrlEncoded
    @POST("oauth/token")
    Call<TokenInfo> oauthToken (@Field("client_id") String client_id,
                                @Field("client_secret") String client_secret,
                                @Field("redirect_uri") String redirect_uri,
                                @Field("code") String code,
                                @Field("grant_type") String grant_type);
}
