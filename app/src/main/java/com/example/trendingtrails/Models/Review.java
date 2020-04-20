package com.example.trendingtrails.Models;

import java.util.Date;

public class Review {
    public String name;
    public int intensity;
    public int rating;
    public String review;
    public Date date;

    public Review(String name, int intensity, int rating, String review, Date date){
        this.name = name;
        this.intensity = intensity;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }
}
