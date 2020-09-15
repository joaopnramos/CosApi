package com.example.citizensonscience.Netwowk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //http://www.citizensonscience.xyz/webapp/
    private static final String Base_URL = "http://192.168.100.64:799/webapp/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized RetrofitClient getmInstance(){
        if (mInstance == null){
            mInstance = new RetrofitClient();

        }
        return mInstance;

    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
