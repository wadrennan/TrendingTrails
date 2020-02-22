package com.example.trendingtrails;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class WeatherActivity extends AppCompatActivity {
    //private RequestQueue queue;
    //private TextView myTextView;
    //@Override
    /*protected void onCreate(Bundle savedInstanceState) {
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
        }*/
    private TextView txtDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        txtDisplay = (TextView) findViewById(R.id.weatherData);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.weather.gov/points/39.7456,-97.0892";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
// TODO Auto-generated method stub
                txtDisplay.setText("Response => " + response.toString());
              //  findViewById(R.id.progressBar1).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtDisplay.setText("That didn't work!");
            }
        });
        queue.add(jsObjRequest);

    }

}







