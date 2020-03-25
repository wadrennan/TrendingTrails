package com.example.trendingtrails.Models;

public class Trail {
    public int intensity;
    public int rating;
    public double distance; //miles

    public Trail(){
        intensity = 0;
        rating = 0;
        distance = 0;
    }
    public Trail(int intense, int rate, double dist){
        intensity = intense;
        rating = rate;
        distance = dist;
    }
}
