package com.example.trendingtrails.Info;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.MyMath;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        final int id = (int) getIntent().getSerializableExtra("TRAIL_ID");
        reviewCards = findViewById(R.id.infoReviewCards);
        new TrailInfoTask().execute(Integer.toString(id));
        findViewById(R.id.infoMapBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("TRAIL_ID", id);
                startActivity(intent);
                finish();
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
            List<Integer> ratings = new ArrayList<>();
            for (Review r:reviews) {
                ratings.add(r.rating);
            }
            double rating = MyMath.average(ratings);
            List<Integer> intensities = new ArrayList<>();
            for (Review r:reviews) {
                intensities.add(r.intensity);
            }
            double intensity = MyMath.average(intensities);
            TextView ratingText = findViewById(R.id.ratingNumTxt);
            TextView intensityText = findViewById(R.id.intensityNumTxt);
            ratingText.setText(String.format("%.2f", rating) + "/10");
            intensityText.setText(String.format("%.2f", intensity) + "/10");
            TrailInfoViewAdapter adapter = new TrailInfoViewAdapter(reviews, TrailInfoActivity.this);
            reviewCards.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            reviewCards.setLayoutManager(llm);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection conn = Database.connect();
                if (conn == null) {
                    return null;
                } else {
                    trail = Queries.getTrail(conn, Integer.parseInt(params[0]));
                    reviews = Queries.getReviews(conn, Integer.parseInt(params[0]));
                }
                conn.close();
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }
}
