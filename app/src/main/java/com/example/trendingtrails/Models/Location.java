package com.example.trendingtrails.Models;

public class Location {

    public String zipCode;
    public String locationName;
    public double latitude;
    public double longitude;
    public int currentWeather;

    public Location(String zip, double lat, double lon, String name){
        zipCode = zip;
        latitude = lat;
        longitude = lon;
        locationName = name;
    }

    public Location(double lat, double lon, String name){
        latitude = lat;
        longitude = lon;
        locationName = name;
    }
}
