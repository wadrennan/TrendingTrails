package com.example.trendingtrails.Location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.R;
import com.example.trendingtrails.Weather.SelectCityActivity;
import com.example.trendingtrails.Weather.WeatherActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.trendingtrails.Models.Global.AccountInfo;


public class LocationsMenuActivity extends BaseActivity {
    //Location Tracking Service
    LocationTrack locationTrack;
    String code1;
    String code2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_locations);
        checkLocationPermissions();

        Button btn = (Button) findViewById(R.id.current_location);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTrack(LocationsMenuActivity.this);


                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LocationsMenuActivity.this, WeatherActivity.class);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lng", longitude);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {

                    locationTrack.showSettingsAlert();
                }

            }
        });
        //add location button on click listener
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ZipCodeTasks().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationTrack != null)
            locationTrack.stopListener();
    }

    private class LocationReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("GET_SIGNAL_STRENGTH")) {
                int level = intent.getIntExtra("LEVEL_DATA", 0);

                // Show it in GraphView
            }

        }
    }

    public class ZipCodeTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView location1 = findViewById(R.id.location1);
            Button location1Btn = findViewById(R.id.location1_forecast);
            Button delete1Btn = findViewById(R.id.location1_delete);
            if (code1 != null) {
                location1.setText("Zip Code: " + code1);
                location1Btn.setVisibility(View.VISIBLE);
                delete1Btn.setVisibility(View.VISIBLE);
            } else {
                location1.setText("Location 1");
                location1Btn.setVisibility(View.INVISIBLE);
                delete1Btn.setVisibility(View.INVISIBLE);
            }
            TextView location2 = findViewById(R.id.location2);
            Button location2Btn = findViewById(R.id.location2_forecast);
            Button delete2Btn = findViewById(R.id.location2_delete);

            if (code2 != null) {
                location2.setText("Zip Code: " + code2);
                location2Btn.setVisibility(View.VISIBLE);
                delete2Btn.setVisibility(View.VISIBLE);
            } else {
                location2.setText("Location 2");
                location2Btn.setVisibility(View.INVISIBLE);
                delete2Btn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection conn = Database.connect();
                if (conn == null) {
                    return null;
                } else {
                    String query = "Select code from [dbo].[ZipCodes] where email= '" + Global.AccountInfo.personEmail + "' " +
                            " Order by id desc ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    code1 = null;
                    code2 = null;
                    if (rs.next()) {
                        code1 = rs.getString("code");
                        if (rs.next()) {
                            code2 = rs.getString("code");
                        }
                    }
                }
                conn.close();
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }
}

