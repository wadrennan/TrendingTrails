package com.example.trendingtrails.Models;

public class User {
    public String email;
    public String displayName;
    public int rank;

    public User(){
        email = null;
        displayName = null;
        rank = 0;
    }

    public User(String email, String name, int rank){
        this.email = email;
        displayName = name;
        this.rank = rank;
    }
}
