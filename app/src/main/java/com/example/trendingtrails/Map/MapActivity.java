package com.example.trendingtrails.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Review.ReviewActivity;
import com.example.trendingtrails.Info.TrailInfoActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;
import com.example.trendingtrails.Review.SaveTrailActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleApiClient googleApiClient;
    LocationTrack lt;  //Location Service
    private double lat; //latitude
    private double lon; //longitude
    private GoogleMap map;
    private boolean trackingFlag; // boolean to tell receiver when to call the update Points Function
    LocationReciever broadReceiver; //Receives broadcasts from Location Service
    private List<LatLng> pointList;
    private List<Trail> trails;
    public Trail trail;
    Polyline mPolyline;
    protected int trailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        checkLocationPermissions();
        Intent i = getIntent();
        trailId = i.getIntExtra("TRAIL_ID", -1);
        lt = new LocationTrack(MapActivity.this);
        if (lt.canGetLocation()) {
            System.out.println("Can get location");
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        }
        // headers so that the intent message can be viewed by receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LocationData");


        //register that the receiver is receiving
        broadReceiver = new LocationReciever();
        registerReceiver(broadReceiver, intentFilter);


        findViewById(R.id.start_tracking).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackingFlag = true;
                pointList = new ArrayList<>();
                findViewById(R.id.start_tracking).setVisibility(View.INVISIBLE);
                findViewById(R.id.finish_tracking).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.finish_tracking).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (trackingFlag == true) {
                    trackingFlag = false;
                    drawTrail();
                    findViewById(R.id.start_tracking).setVisibility(View.VISIBLE);
                    findViewById(R.id.finish_tracking).setVisibility(View.INVISIBLE);
                }
            }
        });
        findViewById(R.id.completed_trail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askToReview();
            }
        });
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MapTasks().execute();
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("Callback");
        System.out.println(lat);
        System.out.println(lon);
        map = googleMap;
        new MapTasks().execute();
        LatLng sydney = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }

    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        int id = (int) marker.getTag();
        Trail t = null;
        for (Trail trail : trails) {
            t = trail;
            if (trail.id == id)
                break;
        }
        trailId = t.id;
        List<LatLng> path = PolyUtil.decode(t.encodedPolyline);
        PolylineOptions pathOptions = new PolylineOptions().addAll(path);
        if (mPolyline != null)
            mPolyline.remove();
        mPolyline = map.addPolyline(pathOptions);
        findViewById(R.id.completed_trail).setVisibility(View.VISIBLE);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int id = (int) marker.getTag();
        Trail t = null;
        for (Trail trail : trails) {
            t = trail;
            if (trail.id == id)
                break;
        }
        Intent intent = new Intent(getApplicationContext(), TrailInfoActivity.class);
        intent.putExtra("TRAIL_ID", t.id);
        startActivity(intent);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        findViewById(R.id.completed_trail).setVisibility(View.INVISIBLE);
        if (mPolyline != null)
            mPolyline.remove();
    }

    protected void onDestroy() {
        super.onDestroy();
        lt.stopListener();
        unregisterReceiver(broadReceiver);
    }

    private void updateMap(GoogleMap googleMap) {
        System.out.println("Update Map");
        LatLng sydney = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }

    public class LocationReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("In receive");
            if (intent.getAction().equals("LocationData")) {
                System.out.println("Receiving Broadcast");
                double currentLat = intent.getDoubleExtra("lat", 0);
                double currentLon = intent.getDoubleExtra("lng", 0);

                //Are we tracking for a trail? if so we add the points to be used when the tracking is finished
                if (trackingFlag == true) {
                    LatLng pt = new LatLng(currentLat, currentLon);
                    pointList.add(pt);
                }
            }
        }
    }

    private void drawTrail() {
        PolylineOptions polylineOptions = new PolylineOptions();
        System.out.println(pointList);
        String encodedPoly = PolyUtil.encode(pointList);
        System.out.println("Encoded polyline = " + encodedPoly + "");
// Create polyline options with existing LatLng ArrayList
        polylineOptions.addAll(pointList);
        polylineOptions
                .width(5)
                .color(Color.RED);
        map.addPolyline(polylineOptions);
        trail = new Trail();
        double dist = trail.getDistance(pointList);
        System.out.println("dist is " + dist + "");

        if(dist == 0){
            failToSave();
        }
        else {
            askToSave(dist, encodedPoly);
        }
    }

    private void askToReview(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you like to review this trail?");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                        intent.putExtra("TRAIL_ID", trailId);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User clicked the No button
                        Connection conn = Database.connect();
                        Queries.insertCompletedTrail(conn, trailId);
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


    private void failToSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to save!");
        builder.setMessage("Your total distance was " + 0.0 + " miles. Try tracking a real trail!");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    default: break;
                }
            }
        };

        // add the buttons
        builder.setPositiveButton("Ok", dialogClickListener);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void askToSave(final double dist, final String encodedPoly) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you like to save?");
        builder.setMessage("Your total distance was " + dist + " miles. Would you like to save this trail?");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        System.out.println("Save Button Clicked");
                        Intent intent = new Intent(getBaseContext(), SaveTrailActivity.class);
                        intent.putExtra("Distance", dist);
                        intent.putExtra("encodedPoly", encodedPoly);
                        intent.putExtra("startLat", pointList.get(0).latitude);
                        intent.putExtra("startLon", pointList.get(0).longitude);
                        System.out.println(Global.AccountInfo.personEmail);
                        startActivity(intent);


                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User clicked the No button
                        System.out.println("Cancel Button Clicked");
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

    public class MapTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            for (Trail trail : trails) {
                if (trailId == trail.id) {
                    lat = trail.latitude;
                    lon = trail.longitude;
                }
                Marker mark = map.addMarker(new MarkerOptions().position(new LatLng(trail.latitude, trail.longitude))
                        .title(trail.name));
                mark.setTag(trail.id);
            }
            updateMap(map);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection conn = Database.connect();
                if (conn == null) {
                    return null;
                } else {
                    trails = Queries.getTrails(conn);
                }
                conn.close();
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }

}
