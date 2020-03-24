package com.example.trendingtrails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.Models.Global;
import com.squareup.picasso.Picasso;

public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.weatherButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weatherButton:
                        Intent activityIntent = new Intent(getBaseContext(), LocationsMenuActivity.class);
                        startActivity(activityIntent);
                        break;
                }
            }
        });
        findViewById(R.id.mapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mapButton:
                        Intent activityIntent = new Intent(getBaseContext(), MapActivity.class);
                        startActivity(activityIntent);
                        break;
                }
            }
        });
        ImageView img = findViewById(R.id.profileImg);
        Picasso.with(getApplicationContext()).load(Global.accountInfo.personPhoto).transform(new CircleTransform()).into(img);

    }
}
