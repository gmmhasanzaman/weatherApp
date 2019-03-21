package com.sampp.weatherapp.models;

public class City {
    private int id;
    private String name;
    private double lat;
    private double lon;
    private String icon;
    private int temperature;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(int id, String name, double lat, double lon, String icon, int temperature) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.icon = icon;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
