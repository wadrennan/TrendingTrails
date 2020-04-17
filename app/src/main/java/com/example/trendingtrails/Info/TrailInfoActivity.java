package com.example.trendingtrails.Info;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.Map.MapExistingTrailActivity;
import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class TrailInfoActivity extends BaseActivity implements TrailInfoViewAdapter.OnCardClickListener {
    List<Review> reviews = new ArrayList<Review>();
    Trail trail;
    RecyclerView reviewCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_info);
        final int id = (int) getIntent().getSerializableExtra("TRAIL_ID");
        reviewCards = findViewById(R.id.infoReviewCards);
        new TrailInfoTask().execute(Integer.toString(id));
        findViewById(R.id.infoMapBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("TRAIL_ID", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCardClick(int position) {

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
            TrailInfoViewAdapter adapter = new TrailInfoViewAdapter(reviews, TrailInfoActivity.this);
            reviewCards.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            //llm.setReverseLayout(true);
            reviewCards.setLayoutManager(llm);
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
