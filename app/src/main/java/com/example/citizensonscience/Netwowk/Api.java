package com.example.citizensonscience.Netwowk;


import com.example.citizensonscience.classes.DonatorResponse;
import com.example.citizensonscience.classes.LoginResponse;
import com.example.citizensonscience.classes.DataGiveResponse;
import com.example.citizensonscience.classes.Project;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @Headers( {"content-type: application/x-www-form-urlencoded;charset=UTF-8", })
        @POST("api-token-auth/")
        Call<LoginResponse> login(
                @Field("username") String username, @Field("password") String password
    );

    @GET("dataGiveStart/")
    Call<List<DataGiveResponse>>DataDataGive(
            @Query("search") String id , @Header("Authorization")  String Token
    );

    @GET("projectStart/{id}")
    Call<Project> infoProjeto(@Path("id") String id, @Header("Authorization")  String Token
    );

    @GET("DonatorStart/")
    Call<List<DonatorResponse>> userDonator(@Query("search") String id, @Header("Authorization")  String Token
    );




}
