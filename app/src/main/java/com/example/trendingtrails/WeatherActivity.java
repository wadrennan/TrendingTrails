package com.example.trendingtrails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WeatherActivity extends AppCompatActivity {
    private RequestQueue queue;
    private TextView myTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final TextView myTextView = (TextView) findViewById(R.id.weatherData);
       // Button B = findViewById(R.id.weather_button);
        // initializing the queue object
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.weather_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("hello");
                }
            });
        }
}







