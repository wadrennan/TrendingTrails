package com.example.trendingtrails.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Trail {
    public String name;
    public int intensity;
    public int rating;
    public double distance; //miles
    public String encodedPolyline;

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
    public double getDistance(List<LatLng> pointList){
        if(distance != 0){
            //do nothing
        }
        else{
            for(int i = 0; i+1 < pointList.size(); i++){
                distance+= getDistanceOfTrail(pointList.get(i).latitude, pointList.get(i).longitude, pointList.get(i+1).latitude, pointList.get(i+1).longitude);
            }
        }
        return distance;
    }
    private double getDistanceOfTrail(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
