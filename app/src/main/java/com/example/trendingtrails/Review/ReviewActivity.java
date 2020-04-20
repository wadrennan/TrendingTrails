package com.example.trendingtrails.Review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.R;

import java.sql.Connection;

public class ReviewActivity extends BaseActivity {

    private RatingBar ratingValue;
    private SeekBar intensityValue;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent i = getIntent();
        id = i.getIntExtra("TRAIL_ID", -1);
        ratingValue = findViewById(R.id.rating_spinner);
        intensityValue = findViewById(R.id.intensity_spinner);
        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Submit Clicked");
                postToDatabase();
                Toast.makeText(getApplicationContext(), "Submit Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void postToDatabase() {
        Connection conn = Database.connect();
        EditText comm = findViewById(R.id.comments);
        String comments = comm.getText().toString();
        if (conn == null) {
            Toast.makeText(getApplicationContext(), "Submission unsuccessful. Try again", Toast.LENGTH_SHORT);
        } else {
            Queries.insertCompletedTrail(conn, id, (int) (ratingValue.getRating()/ratingValue.getStepSize()), intensityValue.getProgress(), comments);
        }
    }

}
