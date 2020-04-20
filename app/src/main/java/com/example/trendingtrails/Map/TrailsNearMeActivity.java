package com.example.trendingtrails.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Info.TrailInfoActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrailsNearMeActivity extends BaseActivity implements TrailsViewAdapter.OnMapListener {
    private Connection conn;
    private RecyclerView trailCards;
    private double lat;
    private double lon;
    private Spinner selectDistance;
    private String currentSelected;
    private double lonRad;
    private double latRad;


    LocationTrack lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails_near_me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermissions();
        lt = new LocationTrack(this);

        if (lt.canGetLocation()) {
            System.out.println("Can get location");
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        }
        System.out.println(lat + " " + lon);
        latRad = deg2rad(lat);
        lonRad = deg2rad(lon);
        trailCards = (RecyclerView) findViewById(R.id.recycler_view);
        conn = Database.connect();
        if (conn == null) {
            System.out.println("Connection Null in Map Menu");
            //function to output trails not available
            sqlError();
        } else {
            new TrailsNearMeActivity.QueryDb().execute("<=", "80.4672");
        }
        initSpinner();
    }

    @Override
    public void onMapClick(int position) {
        //Intent intent = new Intent(this, MapExistingTrailActivity.class);
        String id = ((TextView) trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_number)).getText().toString();
        int id_num = Integer.parseInt(id);
        System.out.println("Trail id " + id_num + " clicked!");
        Intent intent = new Intent(this, TrailInfoActivity.class);
        intent.putExtra("TRAIL_ID", id_num);
        startActivity(intent);
        // String name = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.trail_name)).getText().toString();
        //System.out.println("Trail "+name+" clicked! id = "+id);

    }

    private class QueryDb extends AsyncTask<String, Void, List<Trail>> {
        //Queries db and populates a list of results asynchronously
        @Override
        protected List<Trail> doInBackground(String... query) {
            try {
                return Queries.getNearbyTrails(conn, latRad, lonRad, query[0], Double.parseDouble(query[1]));
            } catch (SQLException e) {
                System.out.println("Error in SELECT in TrailsNearMe");
                //sqlError();
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(List<Trail> trailList) {
            if (trailList == null) {
                Toast.makeText(getApplicationContext(), "No Trails within this distance!", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(trailList);
                for (Trail trail : trailList) {
                    trail.distanceAway = distance(lat, lon, trail.latitude, trail.longitude);
                }
                trailList.sort(Trail.distaneComparator);
                TrailsViewAdapter adapter = new TrailsViewAdapter(trailList, TrailsNearMeActivity.this);
                trailCards.setAdapter(adapter);
                trailCards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void sqlError() {
        Toast.makeText(getApplicationContext(), "Trails Near Me Unavailable", Toast.LENGTH_LONG).show();
    }

    private void initSpinner() {
        List<String> distances = new ArrayList<String>();
        distances.add("25 miles");
        distances.add("50 miles");
        distances.add("100 miles");
        distances.add("100 miles +");
        selectDistance = findViewById(R.id.distance_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distances);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDistance.setAdapter(spinnerAdapter);
        selectDistance.setSelection(1);
        currentSelected = "50 miles";

        //Listener
        selectDistance.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (selectDistance.getSelectedItem().toString() == currentSelected) {
                            // do nothing
                            System.out.println("This item was already selected");
                        } else {
                            String op;
                            currentSelected = selectDistance.getSelectedItem().toString();
                            double dist;
                            if (currentSelected == "25 miles") {
                                dist = 40.2336;
                                op = "<=";
                            } else if (currentSelected == "50 miles") {
                                dist = 80.4672;
                                op = "<=";
                            } else if (currentSelected == "100 miles") {
                                dist = 160.934;
                                op = "<=";
                            } else {
                                dist = 160.934;
                                op = ">=";
                            }

                            trailCards.removeAllViews();
                            trailCards.setAdapter(null);
                            new TrailsNearMeActivity.QueryDb().execute(op, Double.toString(dist));

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println("Nothing Selected");
                    }
                }
        );
    }


}
