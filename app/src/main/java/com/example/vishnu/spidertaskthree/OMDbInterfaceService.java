package com.example.vishnu.spidertaskthree;

import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishnu on 4/7/16.
 */
public class OMDbInterfaceService {

    public static final String LOG_TAG = "OMDbInterfaceService";
    String ENDPOINT = "http://www.omdbapi.com";

    private OMDbAPI omDbAPI;

    public OMDbInterfaceService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        omDbAPI = retrofit.create(OMDbAPI.class);
    }

    public void getData(String title){

        Map<String, String> stringMap = new HashMap<>();

        stringMap.put("t", title);
        stringMap.put("plot", "long");
        stringMap.put("r", "json");

        omDbAPI.getMovie(stringMap).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response != null) {

                    Movie movie = (Movie) response.body();

                    Log.d(LOG_TAG, "" + movie.getTitle());
                }

                else
                    Log.d(LOG_TAG, "Didn't get response");
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

                Log.d(LOG_TAG, "Failure");

            }
        });
    }
}
