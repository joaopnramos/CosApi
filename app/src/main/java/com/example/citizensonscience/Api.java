package com.example.citizensonscience;


import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @Headers( {"content-type: application/x-www-form-urlencoded;charset=UTF-8", })
        @POST("api-token-auth")
        Call<LoginResponse> login(
                @Field("username") String username, @Field("password") String password
    );
}
