package com.example.trendingtrails;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.example.trendingtrails.Models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    @SuppressLint("NewApi")
    public static Connection connect()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://trendingtrails.database.windows.net:1433;databaseName=TrendingTrails;user=androidUser@trendingtrails;password=TrendingTrails1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("SQLException : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("Class : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("Other : ", e.getMessage());
        }
        return connection;
    }

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
}
