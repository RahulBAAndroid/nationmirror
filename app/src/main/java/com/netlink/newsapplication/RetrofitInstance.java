package com.netlink.newsapplication;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    ApiInterface apiInterface;
    public static RetrofitInstance instance;
    RetrofitInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nationmirror.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiInterface=retrofit.create(ApiInterface.class);
    }
    public static RetrofitInstance getInstance(){
        if(instance==null){
            instance=new RetrofitInstance();
        }
        return instance;
    }
    public ApiInterface getApiInterface() {
        return apiInterface;
    }
}