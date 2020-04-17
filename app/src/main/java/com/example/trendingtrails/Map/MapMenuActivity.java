package com.example.trendingtrails.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Database;
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

public class MapMenuActivity extends BaseActivity implements TrailsViewAdapter.OnMapListener {

    private Connection conn;
    private RecyclerView trailCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_map_menu);
        //On click start map activity
        findViewById(R.id.new_trail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(activityIntent);
            }
        });
        findViewById(R.id.nearby_trails).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), TrailsNearMeActivity.class);
                startActivity(activityIntent);
            }
        });

        //Recycler View of cards. Each card reflects item_trail layout
        trailCards = (RecyclerView) findViewById(R.id.recycler_view);

        conn = Database.connect();
        if(conn == null){
            System.out.println("Connection Null in Map Menu");
            //function to output trails not available
            sqlError();
        }
        else{
           new QueryDb().execute();
        }
    }

    private void sqlError(){
        Toast.makeText(getApplicationContext(), "Popular Trails Unavailable", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapClick(int position) {
        String id = ((TextView)trailCards.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_number)).getText().toString();
        int id_num = Integer.parseInt(id);
        System.out.println("Trail id "+id_num+" clicked!");
        Intent intent = new Intent(this, MapExistingTrailActivity.class);
        intent.putExtra("TRAIL_ID", id_num);
        startActivity(intent);
    }

    //Async task so the UI thread not overburdened
    private class QueryDb extends AsyncTask<String,Void, List<Trail>>{
        //Queries db and populates a list of results asynchronously
        @Override
        protected List<Trail> doInBackground(String... query){
            try {
                return Queries.getPopularTrails(conn);
            }
            catch(SQLException e){
                System.out.println("Error in SELECT in MapMenu");
               // sqlError();
                return null;
            }
        }

        //Executes on UI thread after aync query and list generation is done
        @Override
        protected void onPostExecute(List<Trail> trailList){
            if(trailList.isEmpty()){
                System.out.println("null popular trails");
            }
            else {
                super.onPostExecute(trailList);
                TrailsViewAdapter adapter = new TrailsViewAdapter(trailList, MapMenuActivity.this);
                trailCards.setAdapter(adapter);
                trailCards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
    }

}
