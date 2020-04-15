package com.example.trendingtrails.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Info.ReviewActivity;
import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MapExistingTrailActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Connection conn;
    private String mPolyline;
    private double startLat;
    private double startLon;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Retrieve the content view that renders the map.
        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null in MapExistingTrail");
            //function to output trails not available
        }
        else{
            Intent i = getIntent();
            id = i.getIntExtra("TRAIL_ID", -1);
            if(id == -1){
                System.out.println("Error: Default used for id in MapExistingTrail");
            }
            else {
                String query = "SELECT encoded_polyline, lat, long FROM AllTrails WHERE trail_id = "+id+"";
                System.out.println("query = "+query);
                System.out.println(query);
                new MapExistingTrailActivity.QueryDb().execute(query);
            }
        }

        setContentView(R.layout.activity_extisting_trail_map);
        //Set onclick for completed trail button
        findViewById(R.id.completed_trail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // askForReview();
                Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                intent.putExtra("TRAIL_ID", id);
                startActivity(intent);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("Callback");
        map = googleMap;
        List<LatLng> path = PolyUtil.decode(mPolyline);
        PolylineOptions pathOptions = new PolylineOptions().addAll(path);
        map.addPolyline(pathOptions);
        LatLng startLocation = new LatLng(startLat, startLon);
        googleMap.addMarker(new MarkerOptions().position(startLocation)
                .title("Start of trail!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,16));
        googleMap.setMyLocationEnabled(true);
    }

    private class QueryDb extends AsyncTask<String,Void,String> {
        //Queries db and populates a list of results asynchronously
        @Override
        protected String doInBackground(String... query){
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query[0]);
                rs.next();
                String polyline = rs.getString("encoded_polyline");
                startLat = rs.getDouble("lat");
                startLon = rs.getDouble("long");
                return polyline;
            }
            catch(SQLException e){
                System.out.println("Error in SELECT in MapActivity");
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(String polyline){
            mPolyline = polyline;
            System.out.println(mPolyline);
        }
    }

    private void askForReview(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Review");
        builder.setMessage("Would you like to leave a review?");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                        intent.putExtra("TRAIL_ID", id);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User clicked the No button
                        System.out.println("Cancel Button Clicked");
                        //TODO insert into completed trails
                        break;
                }
            }
        };
        // add the buttons
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
