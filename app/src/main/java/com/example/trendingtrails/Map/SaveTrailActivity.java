package com.example.trendingtrails.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.R;

import java.sql.Connection;

public class SaveTrailActivity extends MapActivity {
    private double distance;
    LocationTrack lt;
    private Spinner ratingSpinner;
    private Spinner intensitySpinner;
    private String encodedPoly;
    private double lat;
    private double lon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.save_trail_form);
        distance = getIntent().getDoubleExtra("Distance", 0);
        encodedPoly = getIntent().getStringExtra("encodedPoly");
        lat = getIntent().getDoubleExtra("startLat", 0);
        lon = getIntent().getDoubleExtra("startLon", 0);
        System.out.println("Distance = " + distance + "");
        lt = new LocationTrack(this);
        //fill spinners with values
        ratingSpinner = (Spinner) findViewById(R.id.rating_spinner);
        intensitySpinner = (Spinner) findViewById(R.id.intensity_spinner);
        Integer[] elements = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> ratingAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, elements);
        final ArrayAdapter<Integer> intensityAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, elements);
        ratingSpinner.setAdapter(ratingAdapter);
        intensitySpinner.setAdapter(intensityAdapter);


        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (intensitySpinner.getSelectedItem() != null && ratingSpinner.getSelectedItem() != null) {
                    System.out.println("Submit Clicked");
                    Toast.makeText(getApplicationContext(), "Submit Successful", Toast.LENGTH_SHORT).show();
                    postToDatabase();
                    finish();
                } else {
                    System.out.println("Selection Not detected!");
                    Toast.makeText(getApplicationContext(), "Submission unsuccessful. Try again", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void postToDatabase() {
        Connection conn = Database.connect();
        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        EditText comm = findViewById(R.id.comments);
        String comments = comm.getText().toString();
        //Fixed to take start point not endpoints
        //double lat = lt.getLatitude();
        //double lng = lt.getLongitude();
        Queries.insertNewTrail(conn, name, lat, lon, distance, encodedPoly, (int) ratingSpinner.getSelectedItem(), (int) intensitySpinner.getSelectedItem(), comments);
    }
}
