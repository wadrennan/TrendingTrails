package com.example.trendingtrails;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trendingtrails.Location.LocationsMenuActivity;
import com.example.trendingtrails.Map.MapActivity;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Profile;
import com.example.trendingtrails.Profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.trendingtrails.Models.Global.AccountInfo;
import static com.example.trendingtrails.Models.Global.UserProfile;

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
                        Intent activityIntent = new Intent(getBaseContext(), LocationsMenuActivity.class);
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
    protected void onStart(){
        super.onStart();
        UserProfile = new Profile();
        DatabaseTasks profile = new DatabaseTasks();
        profile.execute("");
    }

    @Override
    protected void onResume(){
        super.onResume();
        UserProfile = new Profile();
        DatabaseTasks profile = new DatabaseTasks();
        profile.execute("");
    }

    public class DatabaseTasks extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String rankStr = "Rank: ";
            switch(UserProfile.rank){
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
            welcomeTxt.setText("Welcome, " + UserProfile.displayName);
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
                    String query = "Select * from [dbo].[Profile] where email= '" + Global.AccountInfo.personEmail + "' ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        UserProfile.displayName = rs.getString("name");
                        UserProfile.rank = rs.getInt("experience");
                    }
                    else
                    {
                        UserProfile.displayName = Global.AccountInfo.personGivenName + " " + Global.AccountInfo.personFamilyName;
                        UserProfile.rank = 0;
                        query = "INSERT INTO [dbo].[Profile] (email, name, experience) VALUES ('" + AccountInfo.personEmail + "', '" + UserProfile.displayName +"', 0) ";
                        stmt.execute(query);
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
