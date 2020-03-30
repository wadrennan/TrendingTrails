package com.example.trendingtrails.Models;

public class Trail {
    public String name;
    public int intensity;
    public int rating;
    public double distance; //miles

    public Trail(){
        name = "";
        intensity = 0;
        rating = 0;
        distance = 0;
    }
    public Trail(String name, int intense, int rate, double dist){
        this.name = name;
        intensity = intense;
        rating = rate;
        distance = dist;
    }
}
