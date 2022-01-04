package com.nopalyer.newsapp;

import com.nopalyer.newsapp.Clases.Noticia;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiI {


    @GET("top-headlines")
    Call<Noticia> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Noticia> getSpecificData(
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to,
            @Query("apiKey") String apiKey
    );

}
