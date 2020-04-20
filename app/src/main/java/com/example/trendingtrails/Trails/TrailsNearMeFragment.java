package com.example.trendingtrails.Trails;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Info.TrailInfoActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Data.Math;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TrailsNearMeFragment extends Fragment implements TrailsViewAdapter.OnMapListener {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trails_near_me, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        lt = new LocationTrack(getContext());

        if (lt.canGetLocation()) {
            System.out.println("Can get location");
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        }
        System.out.println(lat + " " + lon);
        latRad = Math.deg2rad(lat);
        lonRad = Math.deg2rad(lon);
        trailCards = (RecyclerView) getView().findViewById(R.id.recycler_view);
        conn = Database.connect();
        if (conn == null) {
            System.out.println("Connection Null in Map Menu");
            //function to output trails not available
            sqlError();
        } else {
            new QueryDb().execute("<=", "40.2336");
        }
        initSpinner();
    }

    @Override
    public void onMapClick(int position) {
        //Intent intent = new Intent(this, MapExistingTrailActivity.class);
        String id = ((TextView) trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_number)).getText().toString();
        int id_num = Integer.parseInt(id);
        System.out.println("Trail id " + id_num + " clicked!");
        Intent intent = new Intent(getContext(), TrailInfoActivity.class);
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
            if (trailList != null) {
                super.onPostExecute(trailList);
                for (Trail trail : trailList) {
                    trail.distanceAway = Math.distance(lat, lon, trail.latitude, trail.longitude);
                }
                trailList.sort(Trail.distaneComparator);
                TrailsViewAdapter adapter = new TrailsViewAdapter(trailList, TrailsNearMeFragment.this);
                trailCards.setAdapter(adapter);
                trailCards.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }

    private void sqlError() {
        Toast.makeText(getContext(), "Trails Near Me Unavailable", Toast.LENGTH_LONG).show();
    }

    private void initSpinner() {
        List<String> distances = new ArrayList<String>();
        distances.add("25 miles");
        distances.add("50 miles");
        distances.add("100 miles");
        distances.add("All trails");
        selectDistance = getView().findViewById(R.id.distance_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, distances);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDistance.setAdapter(spinnerAdapter);
        selectDistance.setSelection(0);
        currentSelected = "25 miles";

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
                                dist = 0;
                                op = ">=";
                            }

                            trailCards.removeAllViews();
                            trailCards.setAdapter(null);
                            new QueryDb().execute(op, Double.toString(dist));

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
