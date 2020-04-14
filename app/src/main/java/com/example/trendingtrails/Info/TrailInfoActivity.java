package com.example.trendingtrails.Info;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendingtrails.Database;
import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Map.MapExistingTrailActivity;
import com.example.trendingtrails.Models.AddedTrail;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class TrailInfoActivity extends AppCompatActivity {
    List<Review> reviews = new ArrayList<Review>();
    Trail trail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_info);
        final int id = (int) getIntent().getSerializableExtra("Trail");
        new TrailInfoTask().execute(Integer.toString(id));
        findViewById(R.id.infoMapBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapExistingTrailActivity.class);
                intent.putExtra("TRAIL_ID", id);
                startActivity(intent);
            }
        });
    }

    public class TrailInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView name = findViewById(R.id.infoNameTxt);
            TextView distance = findViewById(R.id.distanceTxt);
            name.setText(trail.name);
            distance.setText(String.format("%.2f", trail.distance) + " miles");
            double rating = calculateRating(reviews);
            double intensity = calculateIntensity(reviews);
            TextView ratingText = findViewById(R.id.ratingNumTxt);
            TextView intensityText = findViewById(R.id.intensityNumTxt);
            ratingText.setText(String.format("%.2f", rating) + "/10");
            intensityText.setText(String.format("%.2f", intensity) + "/10");
            LinearLayout lm = findViewById(R.id.trailInfoLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 50, 50, 0);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(10, 0, 0, 0);
            for (Review review : reviews) {
                LinearLayout ll = new LinearLayout(getApplicationContext());
                ll.setLayoutParams(params);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackground(getResources().getDrawable(R.drawable.border));
                ll.setPadding(25, 25, 25, 25);
                LinearLayout l1 = new LinearLayout(getApplicationContext());
                TextView nameLocal = new TextView(getApplicationContext());
                nameLocal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                nameLocal.setLayoutParams(textParams);
                nameLocal.setText("-"+review.name);
                TextView comment = new TextView(getApplicationContext());
                comment.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                comment.setLayoutParams(textParams);
                comment.setText("Review: " + review.review);
                TextView intensityLocal = new TextView(getApplicationContext());
                intensityLocal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                intensityLocal.setLayoutParams(textParams);
                intensityLocal.setText("Intensity: " + Integer.toString(review.intensity));
                TextView ratingLocal = new TextView(getApplicationContext());
                ratingLocal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                ratingLocal.setLayoutParams(textParams);
                ratingLocal.setText("Rating: " + Integer.toString(review.rating));
                ll.addView(ratingLocal);
                ll.addView(intensityLocal);
                ll.addView(comment);
                ll.addView(nameLocal);
                lm.addView(ll);
            }
        }

        private double calculateIntensity(List<Review> trails) {
            double sum = 0.0;
            for (Review review : trails) {
                sum += review.intensity;
            }
            return sum / trails.size();
        }

        private double calculateRating(List<Review> trails) {
            double sum = 0.0;
            for (Review review : trails) {
                sum += review.rating;
            }
            return sum / trails.size();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection conn = Database.connect();
                if (conn == null) {
                    return null;
                } else {
                    String query = "SELECT trail_id, name, distance, lat, long FROM AllTrails t " +
                            " where trail_id= " + params[0] + " ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int id = rs.getInt("trail_id");
                        String name = rs.getString("name");
                        double distance = rs.getDouble("distance");
                        trail = new Trail(id, name, distance);
                    }
                    query = "SELECT name, intensity, rating, review FROM CompletedTrails ct " +
                            " JOIN Profile p on p.email = ct.email " +
                            " where TrailId = " + params[0] + " ";
                    rs = stmt.executeQuery(query);
                    reviews.clear();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        int intensity = rs.getInt("intensity");
                        int rating = rs.getInt("rating");
                        String review = rs.getString("review");
                        reviews.add(new Review(name, intensity, rating, review));
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
