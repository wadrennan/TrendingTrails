package com.example.trendingtrails.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.HomeActivity;
import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MapMenuActivity extends BaseActivity {

    private Connection conn;
    private RecyclerView trailCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_map_menu);
        //On click start map activity
        findViewById(R.id.new_trail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(activityIntent);
            }
        });
        trailCards = (RecyclerView) findViewById(R.id.recycler_view);


        //On click get Trail list

        //Connect to database

        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null in Map Menu");
            //function to output trails not available
            sqlError();
        }
        else{
           populateScrollView();
        }
    }

    private void sqlError(){
        TextView err = new TextView(this);
        err.setText("Popular Trails Unavailable");
        err.setGravity(Gravity.CENTER);
        err.setTextSize(18);
        //((LinearLayout) scroll).addView(err);
    }

    //Executes query and populates scroll view with results
    private void populateScrollView(){
        String query = "SELECT name, distance FROM AllTrails WHERE rating > 6 AND distance != 0";
        try{
            String name;
            double dist;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
           // rs.next();
            List<Trail> trailList = new ArrayList<>();
            int counter = 0;
            //Button Ids will be id%3==0 TextViews %1 %2
            /*while(rs.next()){
                name =  rs.getString("name");
                dist = rs.getDouble("distance");
                Trail t = new Trail(name,0,0,dist);
                trailList.add(t);
                rs.next();
                counter++;
            }*/
            System.out.println(counter);
            TrailsViewAdapter adapter = new TrailsViewAdapter(trailList);
            trailCards.setAdapter(adapter);
            trailCards.setLayoutManager(new LinearLayoutManager(this));
            //Toast.makeText(getApplicationContext(), ""+name+" "+rate+"", Toast.LENGTH_LONG).show();
        }
        catch(SQLException e){
            System.out.println("Error in SELECT in MapMenu");
            //function to output trails not available
            sqlError();
        }
    }

}
