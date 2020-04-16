package com.example.trendingtrails.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Trail implements Serializable {
    public int id;
    public String name;
    public double distance; //miles
    public String encodedPolyline;
    public double latitude;
    public double longitude;

    public Trail(){
        name = "";
        distance = 0;
    }
    public Trail(int id, String name, double dist){
        this.id = id;
        this.name = name;
        distance = dist;
    }
    public Trail(int id, String name, double lat, double lon, String polyline){
        this.id = id;
        this.name = name;
        latitude = lat;
        longitude = lon;
        encodedPolyline = polyline;
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
