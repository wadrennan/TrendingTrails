package com.example.trendingtrails.Models;

public class Location {

    public String zipCode;
    public String locationName;
    public int currentWeather;

    public Location(String zip, String name){
        zipCode = zip;
        locationName = name;
    }
}
