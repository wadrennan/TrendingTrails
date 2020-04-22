package com.example.trendingtrails.Models;

import com.google.android.gms.maps.model.LatLng;
import com.example.trendingtrails.Data.MyMath;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Trail implements Serializable {
    public int id;
    public String name;
    public double distance; //miles
    public String encodedPolyline;
    public double latitude;
    public double longitude;
    public double distanceAway;

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

    public Trail(int id, String name, double dist, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.distance = dist;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Comparator<Trail> distaneComparator = new Comparator<Trail>() {

        public int compare(Trail t1, Trail t2) {
            double distance1 = t1.distanceAway;
            double distance2 = t2.distanceAway;

            return distance1 >= distance2 ? 1 : -1;
        }};

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
        double dist = Math.sin(MyMath.deg2rad(lat1))
                * Math.sin(MyMath.deg2rad(lat2))
                + Math.cos(MyMath.deg2rad(lat1))
                * Math.cos(MyMath.deg2rad(lat2))
                * Math.cos(MyMath.deg2rad(theta));
        dist = Math.acos(dist);
        dist = MyMath.rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
}
