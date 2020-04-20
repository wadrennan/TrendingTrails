package com.example.trendingtrails.Data;

import android.content.Intent;

import com.example.trendingtrails.Models.AddedTrail;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Leader;
import com.example.trendingtrails.Models.Location;
import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.Models.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.trendingtrails.Models.Global.AccountInfo;

public class Queries {
    public static void insertUser(Connection conn, User user) throws SQLException {
        String query = "INSERT INTO [dbo].[Profile] (email, name, experience) VALUES ('" + user.email + "', '" + user.displayName + "', " + user.rank + ") ";
        Statement stmt = conn.createStatement();
        stmt.execute(query);
    }

    public static User getUser(Connection conn, String email) throws SQLException {
        String query = "SELECT email, name, experience FROM [dbo].[Profile] WHERE email = '" + email + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        User user = new User();
        if (rs.next()) {
            user.email = rs.getString("email");
            user.displayName = rs.getString("name");
            user.rank = rs.getInt("experience");
            return user;
        }
        return null;
    }

    public static void updateUser(Connection conn, String name, int exp, User user) throws SQLException {
        String query = "Update [dbo].[Profile] SET name = '" + name + "', experience = " + exp + " where email= '" + user.email + "' ";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }

    public static Trail getTrail(Connection conn, int id) throws SQLException {
        String query = "SELECT name, distance, lat, long FROM AllTrails t " +
                " where trail_id= " + id + " ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            String name = rs.getString("name");
            double distance = rs.getDouble("distance");
            return new Trail(id, name, distance);
        }
        return null;
    }

    public static List<Trail> getTrails(Connection conn) throws SQLException {
        String query = "SELECT trail_id, name, lat, long, encoded_polyline FROM AllTrails";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Trail> trails = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("trail_id");
            String name = rs.getString("name");
            double latitude = rs.getDouble("lat");
            double longitude = rs.getDouble("long");
            String polyline = rs.getString("encoded_polyline");
            trails.add(new Trail(id, name, latitude, longitude, polyline));
        }
        return trails;
    }

    public static List<Review> getReviews(Connection conn, int id) throws SQLException {
        String query = "SELECT name, intensity, rating, review FROM CompletedTrails ct " +
                " JOIN Profile p on p.email = ct.email " +
                " where TrailId = " + id + " ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Review> reviews = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            int intensity = rs.getInt("intensity");
            int rating = rs.getInt("rating");
            String review = rs.getString("review");
            reviews.add(new Review(name, intensity, rating, review));
        }
        return reviews;
    }

    public static List<AddedTrail> getAddedTrails(Connection conn) throws SQLException {
        String query = "SELECT trail_id, name, ct.intensity, ct.rating, distance, lat, long FROM AllTrails t " +
                " JOIN AddedTrails added " +
                " ON added.TrailId = t.trail_id " +
                " JOIN CompletedTrails ct " +
                " ON ct.TrailId = t.trail_id " +
                " where added.email= '" + Global.AccountInfo.personEmail + "' ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<AddedTrail> trails = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("trail_id");
            String name = rs.getString("name");
            int intensity = rs.getInt("intensity");
            int rating = rs.getInt("rating");
            double distance = rs.getDouble("distance");
            trails.add(new AddedTrail(id, name, distance, intensity, rating));
        }
        return trails;
    }

    public static List<AddedTrail> getCompletedTrails(Connection conn) throws SQLException {
        String query = "SELECT trail_id, name, intensity, rating, distance, lat, long FROM AllTrails t " +
                " JOIN CompletedTrails ct " +
                " ON ct.TrailId = t.trail_id " +
                " where email= '" + Global.AccountInfo.personEmail + "' ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<AddedTrail> trails = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("trail_id");
            String name = rs.getString("name");
            int intensity = rs.getInt("intensity");
            int rating = rs.getInt("rating");
            double distance = rs.getDouble("distance");
            trails.add(new AddedTrail(id, name, distance, intensity, rating));
        }
        return trails;
    }

    public static void insertLocation(Connection conn, String zipCode, double latitude, double longitude, String name) {
        String query = " INSERT INTO ZipCodes (code, email, LocationName, lat, lon) " +
                " Values ('" + zipCode + "','" + AccountInfo.personEmail + "' " +
                ", '" + name + "', " + latitude + ", " + longitude + ")";

        System.out.println(query);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (Exception e) {
            System.out.println("Error in adding Location");
            System.out.println(e);
        }
    }

    public static List<Integer> getZipCodes(Connection conn) throws SQLException {
        String query = "SELECT code FROM ZipCodes WHERE email='"+ Global.AccountInfo.personEmail+"';";
        System.out.println(query);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Integer> zipList = new ArrayList<>();
        rs.next();
        while(!rs.isAfterLast()){
            int z = rs.getInt("code");
            zipList.add(z);
            rs.next();
        }
        return zipList;
    }

    public static List<Location> getLocations(Connection conn) throws SQLException {
        String query = "SELECT code, lat, lon, LocationName FROM ZipCodes WHERE email='"+ Global.AccountInfo.personEmail+"';";
        System.out.println(query);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Location> locations = new ArrayList<>();
        rs.next();
        while(!rs.isAfterLast()){
            String z = rs.getString("LocationName");
            String code = rs.getString("code");
            double lat = rs.getDouble("lat");
            double lon = rs.getDouble("lon");
            locations.add(new Location(code, lat, lon, z));
            rs.next();
        }
        return locations;
    }

    public static void insertNewTrail(Connection conn, String name, double lat, double lon, double distance, String encodedPoly, int rating, int intensity, String comments){
        String query = " BEGIN TRANSACTION " +
                " DECLARE @TrailId int; " +
                " INSERT INTO AllTrails (name, lat, long, distance,encoded_polyline) " +
                " Values ('"+name+"',"+lat+","+lon+","+distance+",'"+encodedPoly+"'); " + //TODO NOT DONE
                " SELECT @TrailId = scope_identity(); " +
                " INSERT INTO AddedTrails (TrailId, email) " +
                " VALUES (@TrailId, '" + AccountInfo.personEmail + "'); " +
                " INSERT INTO CompletedTrails (TrailId, email, rating, intensity, review, date) " +
                " VALUES (@TrailId, '" + AccountInfo.personEmail + "',"+rating+","
                +intensity+",'"+comments+"', CURRENT_TIMESTAMP); "+
                " COMMIT";

        System.out.println(query);
        try {
            System.out.println("trying to execute");
            Statement stmt = conn.createStatement();
            stmt.execute(query);

        }
        catch(Exception e){
            System.out.println("Error in saveTrail");
            System.out.println(e);
        }
    }

    public static void insertCompletedTrail(Connection conn, int id, int rating, int intensity, String comments){
        String query = " INSERT INTO CompletedTrails (TrailId, email, rating, intensity, review, date) " +
                " VALUES ("+id+", '" + AccountInfo.personEmail + "',"+rating+","
                +intensity+",'"+comments+"', CURRENT_TIMESTAMP);";
        System.out.println(query);
        try{
            Statement stmt = conn.createStatement();
            //ResultSet rs = stmt.executeQuery(query);
            stmt.execute(query);
        }
        catch(SQLException e){
            System.out.println("Error in review insert");
        }
    }

    public static List<Trail> getPopularTrails(Connection conn) throws SQLException {
        String query = "SELECT trail_id, name, distance FROM AllTrails t " +
                " JOIN CompletedTrails ct " +
                "ON ct.TrailId = t.trail_id " +
                "GROUP BY trail_id, name, distance " +
                "HAVING AVG(rating) > 6";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Trail> trailList = new ArrayList<>();
        rs.next();
        int id;
        String name;
        double dist;
        while(!rs.isAfterLast()){
            id = rs.getInt("trail_id");
            name =  rs.getString("name");
            dist = rs.getDouble("distance");
            //Truncate the decimal value
            BigDecimal truncatedDist = new BigDecimal(Double.toString(dist));
            truncatedDist = truncatedDist.setScale(2, RoundingMode.HALF_UP);
            dist = truncatedDist.doubleValue();
            Trail t = new Trail(id, name,dist);
            trailList.add(t);
            rs.next();
        }
        return trailList;
    }

    public static List<Leader> getLeaders(Connection conn, Object start, Object end) throws SQLException {
        String query = "SELECT p.name, SUM(t.distance) as dist " +
                "  FROM [dbo].[Profile] p " +
                "  JOIN CompletedTrails ct " +
                "  ON ct.email = p.email " +
                "  JOIN AllTrails t " +
                "  ON t.trail_id = ct.TrailId " +
                "  WHERE ct.date >= '" + start + "' AND ct.date < '" + end + "'" +
                "  GROUP BY p.name " +
                "  ORDER BY dist desc ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Leader> leaders = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            double distance = rs.getDouble("dist");
            leaders.add(new Leader(name, distance));
        }
        return leaders;
    }

    public static void deleteZipCode(Connection conn, String zip){
        String query = " DELETE TOP(1) FROM ZipCodes WHERE code = '" + zip +
                "' AND email = '" + AccountInfo.personEmail + "' ";

        System.out.println(query);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (Exception e) {
            System.out.println("Error in removing Location");
            System.out.println(e);
        }
    }

    public static List<Trail> getNearbyTrails(Connection conn, double latRad, double lonRad, String op, double dist) throws SQLException {
        String query = "SELECT trail_id, name, distance, lat, long FROM AllTrails where acos(sin(" + latRad + ") * sin(RADIANS(lat)) + cos(" + latRad + ") * cos(RADIANS(lat)) * cos(RADIANS(long) - (" + lonRad + "))) * 6371 " + op + " " + dist + "";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Trail> trailList = new ArrayList<>();
        rs.next();
        int id;
        String name;
        double distance;
        while (!rs.isAfterLast()) {
            id = rs.getInt("trail_id");
            name = rs.getString("name");
            distance = rs.getDouble("distance");
            double latitude = rs.getDouble("lat");
            double longitude = rs.getDouble("long");
            BigDecimal truncatedDist = new BigDecimal(Double.toString(dist));
            truncatedDist = truncatedDist.setScale(2, RoundingMode.HALF_UP);
            dist = truncatedDist.doubleValue();
            Trail t = new Trail(id, name, distance, latitude, longitude);
            trailList.add(t);
            rs.next();
        }
        return trailList;
    }

    public static boolean locationExists(Connection conn, String zip, double lat, double lon) throws SQLException {
        String query = "SELECT code FROM ZipCodes where (lat = " + lat + " AND " +
                " lon = " + lon + ") AND email = '" + AccountInfo.personEmail + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next())
            return true;
        return false;
    }
}
