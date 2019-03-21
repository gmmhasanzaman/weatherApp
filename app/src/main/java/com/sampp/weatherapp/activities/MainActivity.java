package com.sampp.weatherapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sampp.weatherapp.R;
import com.sampp.weatherapp.api.WeatherAppApi;
import com.sampp.weatherapp.api.services.WeatherService;
import com.sampp.weatherapp.models.City;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView weatherImg;
    TextView weatherTemperatureTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVisuals();
        WeatherService service = WeatherAppApi.getApi().create(WeatherService.class);

        Call<City> cityCall = service.getCityByName("Seville,ES", WeatherAppApi.KEY,"Metric");

        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                setResult(response.body());
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void initializeVisuals(){
        weatherImg = findViewById(R.id.weather_img);
        weatherTemperatureTxt = findViewById(R.id.weather_temperature);
    }

    // update the UI with the result.
    public void setResult(City city){
        weatherTemperatureTxt.setText(String.valueOf(city.getTemperature()));
        Picasso.get().load(WeatherAppApi.BASE_ICONS_URL + city.getIcon() + WeatherAppApi.ICON_EXTENSION).into(weatherImg);
    }
}
