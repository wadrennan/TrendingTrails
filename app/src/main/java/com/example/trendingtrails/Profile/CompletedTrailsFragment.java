package com.example.trendingtrails.Profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout;
import androidx.fragment.app.Fragment;

import com.example.trendingtrails.Database;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedTrailsFragment extends Fragment {
    View view;
    Connection conn;
    List<Trail> trails = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CompletedTrailTasks db = new CompletedTrailTasks();
        db.execute("");
        view = inflater.inflate(R.layout.fragment_completed_trails, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public class CompletedTrailTasks extends AsyncTask<String,String,String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LinearLayout lm = getView().findViewById(R.id.ctLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 50);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(10, 0, 0, 0);
            for (Trail trail: trails) {
                LinearLayout ll = new LinearLayout(view.getContext());
                ll.setLayoutParams(params);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackground(getResources().getDrawable(R.drawable.border));
                ll.setPadding(25, 25, 25, 25);
                LinearLayout l1 = new LinearLayout(view.getContext());
                TextView name = new TextView(view.getContext());
                name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                name.setLayoutParams(textParams);
                name.setText(trail.name);
                TextView distance = new TextView(view.getContext());
                distance.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                distance.setLayoutParams(textParams);
                distance.setText("Distance: " + Double.toString(trail.distance));
                TextView intensity = new TextView(view.getContext());
                intensity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                intensity.setLayoutParams(textParams);
                switch(trail.intensity){
                    case 1:
                        intensity.setText("Easy");
                        break;
                    case 2:
                        intensity.setText("Medium");
                        break;
                    case 3:
                        intensity.setText("Hard");
                        break;
                }

                TextView rating = new TextView(view.getContext());
                rating.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                rating.setLayoutParams(textParams);
                rating.setText("Rating: " + Integer.toString(trail.rating));
                ll.addView(name);
                ll.addView(distance);
                ll.addView(intensity);
                ll.addView(rating);
                lm.addView(ll);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                conn = Database.connect();
                if (conn == null) {
                    return null;
                }
                else {
                    String query = "SELECT Name, Intensity, Rating, Distance FROM Trails t " +
                            " JOIN CompletedTrails ct " +
                            " ON ct.TrailId = t.TrailId " +
                            " where email= '" + Global.AccountInfo.personEmail + "' ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String name = rs.getString("Name");
                        int intensity = rs.getInt("Intensity");
                        int rating = rs.getInt("Rating");
                        double distance = rs.getDouble("Distance");
                        trails.add(new Trail(name, intensity, rating, distance));
                    }
                }
                conn.close();
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }
}
