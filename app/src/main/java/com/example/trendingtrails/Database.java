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
}
