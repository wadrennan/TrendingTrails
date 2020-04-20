package com.example.trendingtrails.Review;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.R;

import java.sql.Connection;

public class SaveTrailActivity extends MapActivity {
    private double distance;
    LocationTrack lt;
    private RatingBar ratingValue;
    private SeekBar intensityValue;
    EditText name;
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
        ratingValue = findViewById(R.id.rating_spinner);
        intensityValue = findViewById(R.id.intensity_spinner);
        name = findViewById(R.id.name_field);

        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!name.getText().toString().trim().isEmpty()) {
                    System.out.println("Submit Clicked");
                    postToDatabase();
                    Toast.makeText(getApplicationContext(), "Submit Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    System.out.println("Selection Not detected!");
                    Toast.makeText(getApplicationContext(), "Submission unsuccessful.\nPlease enter a name", Toast.LENGTH_SHORT).show();
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
        Queries.insertNewTrail(conn, name, lat, lon, distance, encodedPoly, (int) (ratingValue.getRating()/ratingValue.getStepSize()), intensityValue.getProgress(), comments);
    }
}
