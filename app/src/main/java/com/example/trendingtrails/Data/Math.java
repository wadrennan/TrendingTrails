package com.example.trendingtrails.Data;

import java.util.List;

public class Math {
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = java.lang.Math.sin(java.lang.Math.toRadians(lat1)) * java.lang.Math.sin(java.lang.Math.toRadians(lat2)) + java.lang.Math.cos(java.lang.Math.toRadians(lat1)) * java.lang.Math.cos(java.lang.Math.toRadians(lat2)) * java.lang.Math.cos(java.lang.Math.toRadians(theta));
            dist = java.lang.Math.acos(dist);
            dist = java.lang.Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }

    public static double deg2rad(double deg) {
        return (deg * java.lang.Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / java.lang.Math.PI);
    }

    public static double average(List<Integer> values){
        double sum = 0.0;
        for (int value: values) {
            sum += value;
        }
        return sum / values.size();
    }
}
