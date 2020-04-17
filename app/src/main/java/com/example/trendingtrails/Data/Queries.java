package com.example.trendingtrails.Data;

import com.example.trendingtrails.Models.AddedTrail;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.Models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Queries {
    public static void insertUser(Connection conn, User user) throws SQLException {
        String query = "INSERT INTO [dbo].[Profile] (email, name, experience) VALUES ('" + user.email + "', '" + user.displayName +"', "+ user.rank + ") ";
        Statement stmt = conn.createStatement();
        stmt.execute(query);
    }

    public static User getUser(Connection conn, String email) throws SQLException {
        String query = "SELECT email, name, experience FROM [dbo].[Profile] WHERE email = '" + email + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        User user = new User();
        if(rs.next()){
            user.email = rs.getString("email");
            user.displayName =  rs.getString("name");
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

    public static List<Trail> getTrails(Connection conn) throws SQLException{
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
}
