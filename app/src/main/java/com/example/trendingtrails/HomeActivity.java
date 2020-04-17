package com.example.trendingtrails;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.Map.MapMenuActivity;
import com.example.trendingtrails.Map.TrailsNearMeActivity;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.User;
import com.example.trendingtrails.Profile.ProfileActivity;
import com.example.trendingtrails.Weather.WeatherMenuActivity;
import com.squareup.picasso.Picasso;

import java.sql.Connection;

import static com.example.trendingtrails.Models.Global.AccountInfo;
import static com.example.trendingtrails.Models.Global.User;

public class HomeActivity extends BaseActivity {
    TextView rankTxt;
    TextView welcomeTxt;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.weatherButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        Intent activityIntent = new Intent(getBaseContext(), WeatherMenuActivity.class);
                        startActivity(activityIntent);
            }
        });
        findViewById(R.id.mapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent activityIntent = new Intent(getBaseContext(), MapActivity.class);
                        startActivity(activityIntent);
                }
        });
        findViewById(R.id.trailsNearMeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityIntent = new Intent(getBaseContext(), TrailsNearMeActivity.class);
                startActivity(activityIntent);
            }
        });
        findViewById(R.id.leaderboardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.profileLayout).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent activityIntent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(activityIntent);
            }
        });
        ImageView img = findViewById(R.id.profileImg);
        Picasso.with(getApplicationContext()).load(AccountInfo.personPhoto).transform(new CircleTransform()).into(img);
        welcomeTxt = findViewById(R.id.homeWelcomeTxt);
        rankTxt = findViewById(R.id.homeRankTxt);
    }

    @Override
    protected void onResume(){
        super.onResume();
        new DatabaseTasks().execute();
    }

    public class DatabaseTasks extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String rankStr = "Rank: ";
            switch(User.rank){
                case 1:
                    rankStr += "Beginner";
                    break;
                case 2:
                    rankStr += "Intermediate";
                    break;
                case 3:
                    rankStr += "Advanced";
                    break;
            }
            rankTxt.setText(rankStr);
            welcomeTxt.setText("Welcome, " + User.displayName);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                conn = Database.connect();
                if (conn == null)
                {
                    return null;
                }
                else
                {
                    User = Queries.getUser(conn, AccountInfo.personEmail);
                    if(User == null)
                    {
                        String name = Global.AccountInfo.personGivenName + " " + Global.AccountInfo.personFamilyName;
                        int rank = 0;
                        User = new User(AccountInfo.personEmail, name, rank);
                        Queries.insertUser(conn, User);
                    }
                }
                conn.close();
            }
            catch (Exception ex)
            {
                return null;
            }
            return null;
        }
    }
}
