package com.example.trendingtrails.Weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Info.TrailInfoActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Map.TrailsNearMeActivity;
import com.example.trendingtrails.Map.TrailsViewAdapter;
import com.example.trendingtrails.Models.AccountInfo;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WeatherMenuActivity extends BaseActivity implements WeatherViewAdapter.OnCardClickListener {
    private double lat;
    private double lon;
    private Connection conn;
    private LocationTrack lt;
    private RecyclerView weatherCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_locations_updated);
    }

    @Override protected void onResume() {
        super.onResume();
        checkLocationPermissions();
        lt = new LocationTrack(this);
        if(lt.canGetLocation()){
            lat = lt.getLatitude();
            lon = lt.getLongitude();
        }
        else{
            lat = 0;
            lon = 0;
        }
        weatherCards = findViewById(R.id.weather_cards);
        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null in WeatherMenu");
            //function to output trails not available
            //sqlError();
        }
        else{
            String query = "SELECT code FROM ZipCodes WHERE email='"+ Global.AccountInfo.personEmail+"';";
            System.out.println(query);
            new WeatherMenuActivity.QueryDb().execute(query);
        }
        findViewById(R.id.add_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), SelectCityActivity.class);
                startActivity(activityIntent);
            }
        });
        findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), WeatherActivity.class);
                activityIntent.putExtra("lat",lat);
                activityIntent.putExtra("lng",lon);
                startActivity(activityIntent);
            }
        });
    }

    private class QueryDb extends AsyncTask<String,Void, List<Integer>> {
        //Queries db and populates a list of results asynchronously
        @Override
        protected List<Integer> doInBackground(String... query){
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query[0]);
                List<Integer> zipList = new ArrayList<>();
                rs.next();
                while(!rs.isAfterLast()){
                    int z = rs.getInt("code");
                    zipList.add(z);
                    rs.next();
                }
                return zipList;
            }
            catch(SQLException e){
                System.out.println("Error in SELECT in WeatherMenu");
                //sqlError();
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(List<Integer> zipList){
            if(zipList == null){
                Toast.makeText(getApplicationContext(), "Error Has Occured", Toast.LENGTH_LONG).show();
            }
            else {
                super.onPostExecute(zipList);
                WeatherViewAdapter adapter = new WeatherViewAdapter(zipList, WeatherMenuActivity.this);
                weatherCards.setAdapter(adapter);
                weatherCards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
    }
    @Override
    public void onCardClick(int position) {
        //Intent intent = new Intent(this, MapExistingTrailActivity.class);
        String code = ((TextView)weatherCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.zip_value)).getText().toString();
        System.out.println("Trail id "+code+" clicked!");
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("zip", code);
        startActivity(intent);
        // String name = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.trail_name)).getText().toString();
        //System.out.println("Trail "+name+" clicked! id = "+id);

    }
}
