package com.example.trendingtrails.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.trendingtrails.HomeActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends HomeActivity
        implements OnMapReadyCallback{
    private GoogleApiClient googleApiClient;
    LocationTrack lt;
    private double lat;
    private double lon;
    private GoogleMap map;
    LocationReciever broadReceiver;



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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LocationData");

        broadReceiver = new LocationReciever();
        registerReceiver(broadReceiver, intentFilter);

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
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        System.out.println("Callback");
        //double lat = lt.getLatitude();
        //double lon = lt.getLongitude();
        System.out.println(lat);
        System.out.println(lon);
        map = googleMap;
        LatLng sydney = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setMyLocationEnabled(true);

    }



    protected void onDestroy() {
        super.onDestroy();
        lt.stopListener();
    }
    private void updateMap(GoogleMap googleMap){
        System.out.println("Update Map");
        LatLng sydney = new LatLng(lat, lon);
        //googleMap.addMarker(new MarkerOptions().position(sydney)
          //      .title("Marker in Sydney"));
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
                // Show it in GraphView
            }
        }
    }



}
