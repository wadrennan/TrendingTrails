package com.example.trendingtrails.Weather;

import android.os.Bundle;
import android.util.Log;

import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.HomeActivity;
import com.example.trendingtrails.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends BaseActivity {

    ViewPager2 viewPager2;
    //List<String> list;
    private List<String> dates;
    private List<String> maxTemps;
    private List<String> lowTemps;
    int descriptors [];
    //private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_main);
        Bundle b = getIntent().getExtras();
        String zip = "37027";
        double lat =0;
        double lng = 0;
        if(b!=null){
                zip = b.getString("zip");
                lat = b.getDouble("lat");
                lng = b.getDouble("lng");
        }
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
        String url;
        if(lat != 0){
            System.out.println("lat lon");
            url = "https://api.weatherbit.io/v2.0/forecast/daily?lat="+lat+"&lon="+lng+"&key=603e03f3bfb042fca8b3c593c3da5a6b&units=I";
        }
        else {
            System.out.println("zip");
            url = "https://api.weatherbit.io/v2.0/forecast/daily?postal_code=" + zip + "&key=603e03f3bfb042fca8b3c593c3da5a6b&units=I";
        }
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
                        finalresult = String.valueOf(context.getDouble("high_temp"));
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
                System.out.println(error);
            }
        });
        queue.add(jsObjRequest);


    }
    private void setViewPager2Adapter(List<String> dates, List<String> highs, List<String> lows, int descriptor []){
        viewPager2.setAdapter(new ViewPagerAdapter(this, dates, highs,lows, descriptor, viewPager2));
    }
}







