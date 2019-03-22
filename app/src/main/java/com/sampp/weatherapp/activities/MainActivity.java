package com.sampp.weatherapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sampp.weatherapp.R;
import com.sampp.weatherapp.api.WeatherAppApi;
import com.sampp.weatherapp.api.services.WeatherService;
import com.sampp.weatherapp.models.City;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String METRIC_UNIT = "Metric";
    ImageView weatherImg;
    TextView weatherTemperatureTxt,weatherMinTemperatureTxt,weatherMaxTemperatureTxt,weatherDescriptionTxt,weatherCityTxt,weatherHumidity;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDayNightMode();
        initializeVisuals();
        getCityFromService("Santo Domingo,DOM");

        sharedPref = getPreferences(Context.MODE_PRIVATE);

    }
    public void initializeVisuals(){
        weatherImg = findViewById(R.id.weather_img);
        weatherTemperatureTxt = findViewById(R.id.weather_temperature);
        weatherDescriptionTxt = findViewById(R.id.weather_description);
        weatherCityTxt = findViewById(R.id.weather_city);
        weatherMinTemperatureTxt = findViewById(R.id.weather_min_temp);
        weatherMaxTemperatureTxt = findViewById(R.id.weather_max_temp);
        weatherHumidity = findViewById(R.id.weather_humidity);
    }

    public void setupDayNightMode(){
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    // update the UI with the result.
    public void setResult(City city){
        saveResult(city);
        weatherCityTxt.setText(city.getName());
        weatherTemperatureTxt.setText(String.valueOf(city.getTemperature()));
        weatherMinTemperatureTxt.setText(String.valueOf(city.getMinTemperature()));
        weatherMaxTemperatureTxt.setText(String.valueOf(city.getMaxTemperature()));
        weatherHumidity.setText(String.valueOf(city.getHumidity())+"%");
        weatherDescriptionTxt.setText(city.getDescription());
        Picasso.get().load(WeatherAppApi.BASE_ICONS_URL + city.getIcon() + WeatherAppApi.ICON_EXTENSION).into(weatherImg);
    }

    // saves the result into a Gson object.
    public void saveResult(City city){
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String savedCity = gson.toJson(city);
        editor.putString("city",savedCity);
        editor.apply();
    }

    // returns the last saved city.
    public City getLastSavedResult(){
        Gson gson = new Gson();
        String json = sharedPref.getString("city", null);
        return gson.fromJson(json, City.class);
    }

    public void getCityFromService(String city){
        WeatherService service = WeatherAppApi.getApi().create(WeatherService.class);

        Call<City> cityCall = service.getCityByName(city, WeatherAppApi.KEY,METRIC_UNIT, Locale.getDefault().getLanguage());

        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                setResult(response.body());
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_SHORT).show();
                // if fails set the result with the last saved.
                setResult(getLastSavedResult());
            }
        });
    }
}
