package com.example.trendingtrails.Location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.HomeActivity;
import com.example.trendingtrails.R;
import com.example.trendingtrails.Weather.SelectCityActivity;
import com.example.trendingtrails.Weather.WeatherActivity;


public class LocationsMenuActivity extends BaseActivity {
    //Location Tracking Service
    LocationTrack locationTrack;

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
        findViewById(R.id.add_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_location:
                        Intent weatherIntent = new Intent(getBaseContext(), SelectCityActivity.class);
                        startActivity(weatherIntent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationTrack != null)
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
}

