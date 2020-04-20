package com.example.trendingtrails.Leaderboard;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Models.Leader;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyLeaderboardFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_leaderboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        new DailyTasks().execute();
    }

    protected class DailyTasks extends AsyncTask<Void, Void, List<Leader>>{
        @Override
        protected List<Leader> doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date date = calendar.getTime();
            Object start = new java.sql.Timestamp(date.getTime());
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            Object end = new java.sql.Timestamp(date.getTime());
            List<Leader> leaders = new ArrayList<>();
            try {
                Connection conn = Database.connect();
                if (conn != null) {
                    leaders = Queries.getLeaders(conn, start, end);
                }
                conn.close();
            } catch (Exception ex) {

            }
            return leaders;
        }

        @Override
        protected void onPostExecute(List<Leader> leaders) {
            super.onPostExecute(leaders);
            LinearLayout lm = getView().findViewById(R.id.dailyLayout);
            if (lm == null)
                return;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 30, 0, 0);
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nameParams.setMarginStart(40);
            LinearLayout.LayoutParams distanceParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
            LinearLayout.LayoutParams placeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            placeParams.setMargins(45, 0, 45, 0);
            int i = 1;
            for (final Leader leader : leaders) {
                LinearLayout ll = new LinearLayout(getView().getContext());
                ll.setLayoutParams(params);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(25, 25, 25, 25);
                ImageView place = new ImageView(getView().getContext());
                TextView standardPlace = new TextView(getView().getContext());
                TextView name = new TextView(getView().getContext());
                TextView distance = new TextView(getView().getContext());
                if(i == 1)
                    place.setImageResource(R.drawable.firsticon);
                else if(i == 2)
                    place.setImageResource(R.drawable.secondicon);
                else if(i == 3)
                    place.setImageResource(R.drawable.thirdicon);
                else{
                    standardPlace.setLayoutParams(placeParams);
                    standardPlace.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                    standardPlace.setText(Integer.toString(i));
                }
                distance.setLayoutParams(distanceParams);
                distance.setGravity(Gravity.RIGHT);
                distance.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                distance.setText(String.format("%.2f", leader.distance));
                name.setLayoutParams(nameParams);
                name.setGravity(Gravity.BOTTOM);
                name.setMaxWidth(425);
                name.setPadding(0, 20,0, 0);
                name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                name.setText(leader.name);
                if(i <= 3)
                    ll.addView(place);
                else
                    ll.addView(standardPlace);
                ll.addView(name);
                ll.addView(distance);
                lm.addView(ll);
                i++;
            }
        }
    }
}
