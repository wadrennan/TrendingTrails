package com.example.trendingtrails.Weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Models.Location;
import com.example.trendingtrails.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class WeatherMenuActivity extends BaseActivity implements WeatherViewAdapter.OnCardClickListener {
    private double lat;
    private double lon;
    private Connection conn;
    private LocationTrack lt;
    private RecyclerView weatherCards;
    WeatherViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_locations_updated);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermissions();
        lt = new LocationTrack(this);
        if (lt.canGetLocation()) {
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        } else {
            lat = 0;
            lon = 0;
        }
        weatherCards = findViewById(R.id.weather_cards);
        conn = Database.connect();
        if (conn == null) {
            System.out.println("Connection Null in WeatherMenu");
            //function to output trails not available
            //sqlError();
        } else {
            new WeatherMenuActivity.QueryDb().execute();
        }
        findViewById(R.id.add_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), SelectCityActivity.class);
                startActivity(activityIntent);
            }
        });
        findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), WeatherActivity.class);
                activityIntent.putExtra("lat", lat);
                activityIntent.putExtra("lng", lon);
                startActivity(activityIntent);
            }
        });
    }

    private class QueryDb extends AsyncTask<String, Void, List<Location>> {
        //Queries db and populates a list of results asynchronously
        @Override
        protected List<Location> doInBackground(String... query) {
            try {
                return Queries.getLocations(conn);
            } catch (SQLException e) {
                System.out.println("Error in SELECT in WeatherMenu");
                //sqlError();
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(List<Location> locations) {
            if (locations == null) {
                Toast.makeText(getApplicationContext(), "Error Has Occured", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(locations);
                for (Location location: locations) {
                    if(location.currentWeather == 0)
                        getWeather(location);
                }
                adapter = new WeatherViewAdapter(locations, WeatherMenuActivity.this);
                weatherCards.setAdapter(adapter);
                weatherCards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
    }

    @Override
    public void onCardClick(String zip) {
        //Intent intent = new Intent(this, MapExistingTrailActivity.class);
        String code = zip;
        System.out.println("Trail id " + code + " clicked!");
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("zip", code);
        startActivity(intent);
        // String name = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.trail_name)).getText().toString();
        //System.out.println("Trail "+name+" clicked! id = "+id);

    }

    private void getWeather(final Location location) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = "https://api.weatherbit.io/v2.0/current?postal_code=" + location.zipCode + "&key=603e03f3bfb042fca8b3c593c3da5a6b&units=I";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public synchronized void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject context = jsonArray.getJSONObject(0);
                    JSONObject desc = context.getJSONObject("weather");
                    location.currentWeather = desc.getInt("code");
                    adapter.notifyDataSetChanged(location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(jsObjRequest);
    }
}
