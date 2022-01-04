package com.nopalyer.newsapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static Api api;
    private static Retrofit retrofit;

    private Api(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized Api getInstance(){
        if (api == null){
            api = new Api();
        }
        return api;
    }


    public ApiI getApi(){
        return retrofit.create(ApiI.class);
    }
}
