package com.sampp.weatherapp.api;


import com.google.gson.GsonBuilder;
import com.sampp.weatherapp.api.deserializers.CityDeserializer;
import com.sampp.weatherapp.models.City;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAppApi {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String KEY = "19bb222583cf6d14426b31687c4256ad";
    private static Retrofit retrofit = null;

    // singleton design pattern to get the retrofit instance
    public static Retrofit getApi(){
        if(retrofit == null){

            // Gson Builder to add the deserializer class to the retrofit builder.
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(City.class, new CityDeserializer());

            // retrofit instance.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();
        }
        return retrofit;
    }
}
