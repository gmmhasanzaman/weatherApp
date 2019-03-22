package com.sampp.weatherapp.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sampp.weatherapp.models.City;

import java.lang.reflect.Type;

/**
 * Created by samuelpena on 3/21/19.
 */

public class CityDeserializer implements JsonDeserializer<City> {
    @Override
    public City deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        String name = json.getAsJsonObject().get("name").getAsString();

        // To get properties directly from objects inside the json, use this.
        double lat = json.getAsJsonObject().get("coord").getAsJsonObject().get("lat").getAsDouble();
        double lon = json.getAsJsonObject().get("coord").getAsJsonObject().get("lon").getAsDouble();

        int temperature = json.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsInt();
        int mintTemperature = json.getAsJsonObject().get("main").getAsJsonObject().get("temp_min").getAsInt();
        int maxTemperature = json.getAsJsonObject().get("main").getAsJsonObject().get("temp_max").getAsInt();
        String icon = json.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
        String description = json.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

        return new City(id,name,lat,lon,icon,temperature,mintTemperature,maxTemperature,description);
    }
}
