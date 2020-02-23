package com.example.trendingtrails;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    //List<String> list;
    List<String> dates;
    List<String> maxTemps;
    List<String> lowTemps;
    int descriptors [];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_main);

        viewPager2 = findViewById(R.id.viewPager2);
        dates= new ArrayList<>();
        maxTemps=new ArrayList<>();
        lowTemps = new ArrayList<>();
        descriptors = new int[8];
       /* list = new ArrayList<>();
        list.add("First Screen");
        list.add("Second Screen");
        list.add("Third Screen");
        list.add("Fourth Screen");
        */

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city=Raleigh,NC&key=603e03f3bfb042fca8b3c593c3da5a6b&units=I";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public synchronized void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    for(int i = 0; i<8; i++){
                        JSONObject context = jsonArray.getJSONObject(i);
                        String finalresult=context.getString("valid_date");
                        //String finalresult = String.valueOf(context.getDouble("valid_date"));
                       // list.add(finalresult);
                        dates.add(finalresult);
                        finalresult = String.valueOf(context.getDouble("max_temp"));
                        maxTemps.add(finalresult);
                        finalresult = String.valueOf(context.getDouble("low_temp"));
                        lowTemps.add(finalresult);
                        JSONObject desc = context.getJSONObject("weather");
                        int d = desc.getInt("code");
                        descriptors[i] = (d);
                        Log.d("LIST","Element Added");
                        //txtDisplay.append(finalresult + " C\n");
                    }


                    //  findViewById(R.id.progressBar1).setVisibility(View.GONE);
                    setViewPager2Adapter(dates, maxTemps, lowTemps, descriptors);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtDisplay.setText("That didn't work!");
            }
        });
        queue.add(jsObjRequest);


    }
    private void setViewPager2Adapter(List<String> dates, List<String> highs, List<String> lows, int descriptor []){
        viewPager2.setAdapter(new ViewPagerAdapter(this, dates, highs,lows, descriptor, viewPager2));
    }
}

/*import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


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
        }
    private TextView txtDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //txtDisplay = (TextView) findViewById(R.id.weatherData);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city=Raleigh,NC&key=603e03f3bfb042fca8b3c593c3da5a6b";
        /*JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject context = jsonArray.getJSONObject(i);
                        String finalresult = String.valueOf(context.getDouble("max_temp"));
                        txtDisplay.append(finalresult + " C\n");
                    }


                    //  findViewById(R.id.progressBar1).setVisibility(View.GONE);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtDisplay.setText("That didn't work!");
            }
        });
        queue.add(jsObjRequest);

    }

}*/







