package com.sampp.weatherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final int LOCATION_PERMISSION = 1150;
    ImageView weatherImg;
    TextView weatherTemperatureTxt, weatherMinTemperatureTxt, weatherMaxTemperatureTxt, weatherDescriptionTxt, weatherCityTxt, weatherHumidity;
    SharedPreferences sharedPref;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDayNightMode();
        initializeVisuals();

        // get the location client.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // permission checker.
        requestPermission();
        // initialize the SharedPreferences instance.
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

    public void getCityFromService(String latitude,String longitude){
        WeatherService service = WeatherAppApi.getApi().create(WeatherService.class);

        // get the city object with coordinates
        Call<City> cityCall = service.getCityByCoordinates(latitude,longitude, WeatherAppApi.KEY,METRIC_UNIT, Locale.getDefault().getLanguage());

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

    public void requestPermission(){
        if (!isPermissionEnabled()) {

            // permission not granted. Call the Request Permission callback to manage the permissions.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }
        else{
            // permission granted.
            getWeather();
        }
    }

    public boolean isPermissionEnabled(){
        // check app location permission
        return (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    public void getDeviceLocation(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                              // Logic to handle location object
//                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                            String ciyName = addresses.get(0).getLocality();
//                            String countryCode = Locale.getDefault().getISO3Country();
                            getCityFromService(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getWeather(){
        getDeviceLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the location-related task you need to do.
                    getWeather();

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                    setResult(City.getDefaultCity());
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPermissionEnabled()){
            getWeather();
        }else if(!isPermissionEnabled() && getLastSavedResult() !=null){
            setResult(getLastSavedResult());
        }else if(getLastSavedResult() == null){
            setResult(City.getDefaultCity());
        }
    }
}
