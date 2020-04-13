package com.example.trendingtrails.Info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

public class TrailInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_info);
        Trail trail = (Trail) getIntent().getSerializableExtra("Trail");
        TextView name = findViewById(R.id.infoNameTxt);
        name.setText(trail.name);
    }
}
