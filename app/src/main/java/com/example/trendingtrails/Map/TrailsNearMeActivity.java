package com.example.trendingtrails.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrailsNearMeActivity extends BaseActivity implements TrailsViewAdapter.OnMapListener {
    private Connection conn;
    private RecyclerView trailCards;
    private double lat;
    private double lon;


    LocationTrack lt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails_near_me);
        checkLocationPermissions();
        lt = new LocationTrack(this);

        if (lt.canGetLocation()) {
            System.out.println("Can get location");
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        }
        System.out.println(lat+" "+lon);
        double latRad = deg2rad(lat);
        double lonRad = deg2rad(lon);
        trailCards = (RecyclerView) findViewById(R.id.recycler_view);
        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null in Map Menu");
            //function to output trails not available
            sqlError();
        }
        else{
            String query = "SELECT trail_id, name, distance FROM AllTrails where acos(sin("+latRad+") * sin(RADIANS(lat)) + cos("+latRad+") * cos(RADIANS(lat)) * cos(RADIANS(long) - ("+lonRad+"))) * 6371 <= 80.4672";
            System.out.println(query);
            new TrailsNearMeActivity.QueryDb().execute(query);
        }
    }

    @Override
    public void onMapClick(int position) {
        //Intent intent = new Intent(this, MapExistingTrailActivity.class);
        String id = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_number)).getText().toString();
        int id_num = Integer.parseInt(id);
        System.out.println("Trail id "+id_num+" clicked!");
        Intent intent = new Intent(this, MapExistingTrailActivity.class);
        intent.putExtra("TRAIL_ID", id_num);
        startActivity(intent);
       // String name = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.trail_name)).getText().toString();
        //System.out.println("Trail "+name+" clicked! id = "+id);

    }
   //TODO: Implement function to navigate to map existing trail

    private class QueryDb extends AsyncTask<String,Void, List<Trail>> {
        //Queries db and populates a list of results asynchronously
        @Override
        protected List<Trail> doInBackground(String... query){
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query[0]);
                List<Trail> trailList = new ArrayList<>();
                rs.next();
                int id;
                String name;
                double dist;
                while(!rs.isAfterLast()){
                    id = rs.getInt("trail_id");
                    name =  rs.getString("name");
                    dist = rs.getDouble("distance");
                    Trail t = new Trail(id, name,0,0,dist);
                    trailList.add(t);
                    rs.next();
                }
                return trailList;
            }
            catch(SQLException e){
                System.out.println("Error in SELECT in TrailsNearMe");
                sqlError();
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(List<Trail> trailList){
            super.onPostExecute(trailList);
            TrailsViewAdapter adapter = new TrailsViewAdapter(trailList, TrailsNearMeActivity.this);
            trailCards.setAdapter(adapter);
            trailCards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void sqlError(){
        Toast.makeText(getApplicationContext(), "Trails Near Me Unavailable", Toast.LENGTH_LONG).show();
    }

}
