package com.example.trendingtrails.Info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.R;

import java.sql.Connection;

public class ReviewActivity extends BaseActivity {

    private Spinner ratingSpinner;
    private Spinner intensitySpinner;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        populateSpinners();
        Intent i = getIntent();
        id = i.getIntExtra("TRAIL_ID", -1);
        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (intensitySpinner.getSelectedItem() != null && ratingSpinner.getSelectedItem() != null) {
                    System.out.println("Submit Clicked");
                    //Toast.makeText(getApplicationContext(), "Submit Successful", Toast.LENGTH_SHORT).show();
                    postToDatabase();
                    finish();
                } else {
                    System.out.println("Selection Not detected!");
                    Toast.makeText(getApplicationContext(), "Submission unsuccessful. Try again", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Populates Spinner With Values
    private void populateSpinners() {
        ratingSpinner = (Spinner) findViewById(R.id.rating_spinner);
        intensitySpinner = (Spinner) findViewById(R.id.intensity_spinner);
        Integer[] elements = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final ArrayAdapter<Integer> ratingAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, elements);
        final ArrayAdapter<Integer> intensityAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, elements);
        ratingSpinner.setAdapter(ratingAdapter);
        intensitySpinner.setAdapter(intensityAdapter);
    }

    private void postToDatabase() {
        Connection conn = Database.connect();
        EditText comm = findViewById(R.id.comments);
        String comments = comm.getText().toString();
        if (conn == null) {
            Toast.makeText(getApplicationContext(), "Submission unsuccessful. Try again", Toast.LENGTH_SHORT);
        } else {
            Queries.insertCompletedTrail(conn, id, (int) ratingSpinner.getSelectedItem(), (int) intensitySpinner.getSelectedItem(), comments);
        }
    }

}
