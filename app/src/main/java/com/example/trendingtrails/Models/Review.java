package com.example.trendingtrails.Models;

public class Review {
    public String name;
    public int intensity;
    public int rating;
    public String review;

    public Review(String name, int intensity, int rating, String review){
        this.name = name;
        this.intensity = intensity;
        this.rating = rating;
        this.review = review;
    }
}
