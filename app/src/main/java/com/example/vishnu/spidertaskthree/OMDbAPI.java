package com.example.vishnu.spidertaskthree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by vishnu on 4/7/16.
 */
public interface OMDbAPI {

    String ENDPOINT = "http://www.omdbapi.com";


    @GET("/")
    rx.Observable<Movie> getMovie(@QueryMap(encoded = false) Map<String, String> options);

}