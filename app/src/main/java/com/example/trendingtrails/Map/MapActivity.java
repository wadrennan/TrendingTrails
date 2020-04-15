package com.example.trendingtrails.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Models.AccountInfo;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity
        implements OnMapReadyCallback{
    private GoogleApiClient googleApiClient;
    LocationTrack lt;  //Location Service
    private double lat; //latitude
    private double lon; //longitude
    private GoogleMap map;
    private boolean trackingFlag; // boolean to tell receiver when to call the update Points Function
    LocationReciever broadReceiver; //Receives broadcasts from Location Service
    private List<LatLng> pointList;
    public Trail trail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        checkLocationPermissions();
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
            }
        });
        findViewById(R.id.finish_tracking).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(trackingFlag == true) {
                    trackingFlag = false;
                    drawTrail();
                }
            }
        });
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        LatLng sydney = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Your Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setMyLocationEnabled(true);

    }



    protected void onDestroy() {
        super.onDestroy();
        lt.stopListener();
        unregisterReceiver(broadReceiver);
    }
    private void updateMap(GoogleMap googleMap){
        System.out.println("Update Map");
        LatLng sydney = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
    }
    public class LocationReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("In receive");
            if(intent.getAction().equals("LocationData"))
            {
                System.out.println("Receiving Broadcast");
                lat = intent.getDoubleExtra("lat",0);
                lon = intent.getDoubleExtra("lng", 0);
                updateMap(map);

                //Are we tracking for a trail? if so we add the points to be used when the tracking is finished
                if(trackingFlag == true){
                    LatLng pt = new LatLng(lat, lon);
                    pointList.add(pt);
                }
            }
        }
    }

    private void drawTrail(){
        PolylineOptions polylineOptions = new PolylineOptions();
        System.out.println(pointList);
        String encodedPoly = PolyUtil.encode(pointList);
        System.out.println("Encoded polyline = "+encodedPoly+"");
// Create polyline options with existing LatLng ArrayList
        polylineOptions.addAll(pointList);
        polylineOptions
                .width(5)
                .color(Color.RED);
        map.addPolyline(polylineOptions);
        trail = new Trail();
        double dist = trail.getDistance(pointList);
        System.out.println("dist is " +dist+"");

        askToSave(dist, encodedPoly);
    }


    private void askToSave(final double dist, final String encodedPoly){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you like to save?");
        builder.setMessage("Your total distance was "+dist+" miles. Would you like to save this trail?");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        System.out.println("Save Button Clicked");
                        Intent intent = new Intent(getBaseContext(),SaveTrailActivity.class);
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

}
