package com.example.trendingtrails.Models;

import java.io.Serializable;

public class AddedTrail implements Serializable {
    public int id;
    public String name;
    public double distance;
    public int intensity;
    public int rating;
    public String review;

    public AddedTrail(){
        name = null;
        distance = 0.0;
        intensity = 0;
        rating = 0;
    }

    public AddedTrail(int id, String name, double distance, int intensity, int rating){
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.intensity = intensity;
        this.rating = rating;
    }

    public AddedTrail(int id, String name, double distance, int intensity, int rating, String rev){
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.intensity = intensity;
        this.rating = rating;
        review = rev;
    }
}
