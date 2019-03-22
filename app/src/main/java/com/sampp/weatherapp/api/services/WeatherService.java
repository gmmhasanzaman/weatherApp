package com.sampp.weatherapp.api.services;

import com.sampp.weatherapp.models.City;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    /*
     * This is the retrofit interface service.
     * This interface defines the methods to call the API
     */
    // http://api.openweathermap.org/data/2.5/weather?q=Seville,es&appid=19bb222583cf6d14426b31687c4256ad

    @GET("weather")
    Call<City> getCityByName(@Query("q") String city, @Query("appid") String key, @Query("units") String value);

    @GET("weather")
    Call<City> getCityByName(@Query("q") String city, @Query("appid") String key, @Query("units") String value,@Query("lang") String language);
}
