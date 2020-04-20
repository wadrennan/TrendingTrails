package com.example.trendingtrails.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class TrailStats {
    private Connection conn;

    public TrailStats(){
        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null!");
        }
    }
    public float getMaxDistance(){
        float result;
        String query = "SELECT MAX(distance) AS Distance FROM AllTrails";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getFloat("Distance");

        }
        catch(Exception e){
            System.out.println("Error in max distance!");
            System.out.println(e);
            result = 0;
        }
        return result;
    }
   /* public String getEmail(){
        return Global.AccountInfo.personEmail;
    }*/
    public int getNumberOfTrails(){
        int result;
        String query = "SELECT COUNT(trail_id) AS number FROM AllTrails";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getInt("number");
        }
        catch(Exception e){
            System.out.println("Error in NumberOfTrails!");
            System.out.println(e);
            result = 0;
        }
        return result;
    }
}
