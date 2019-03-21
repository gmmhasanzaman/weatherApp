package com.sampp.weatherapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAppApi {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String KEY = "19bb222583cf6d14426b31687c4256ad";
    private static Retrofit retrofit = null;

    // singleton design pattern to get the retrofit instance
    public static Retrofit getApi(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
