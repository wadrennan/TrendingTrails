package com.example.trendingtrails.Profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Info.TrailInfoActivity;
import com.example.trendingtrails.Models.AddedTrail;
import com.example.trendingtrails.R;

import java.sql.Connection;
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
public class AddedTrailsFragment extends Fragment {
    Connection conn;
    List<AddedTrail> trails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_added_trails, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new AddedTrailsTasks().execute();
    }

    public class AddedTrailsTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LinearLayout lm = getView().findViewById(R.id.atLayout);
            if (lm == null)
                return;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 50, 0, 0);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(10, 0, 0, 0);
            for (final AddedTrail trail : trails) {
                LinearLayout ll = new LinearLayout(getView().getContext());
                ll.setLayoutParams(params);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackground(getResources().getDrawable(R.drawable.border));
                ll.setPadding(25, 25, 25, 25);
                TextView name = new TextView(getView().getContext());
                name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                name.setLayoutParams(textParams);
                name.setText(trail.name);
                TextView distance = new TextView(getView().getContext());
                distance.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                distance.setLayoutParams(textParams);
                distance.setText("Distance: " + String.format("%.2f", trail.distance) + " miles");
                TextView intensity = new TextView(getView().getContext());
                intensity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                intensity.setLayoutParams(textParams);
                if (trail.intensity < 4) {
                    intensity.setText("Easy");
                } else if (trail.intensity < 8) {
                    intensity.setText("Medium");
                } else {
                    intensity.setText("Hard");
                }
                TextView rating = new TextView(getView().getContext());
                rating.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                rating.setLayoutParams(textParams);
                rating.setText("Rating: " + Integer.toString(trail.rating));
                ll.addView(name);
                ll.addView(distance);
                ll.addView(intensity);
                ll.addView(rating);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), TrailInfoActivity.class);
                        intent.putExtra("TRAIL_ID", trail.id);
                        startActivity(intent);
                    }
                });
                lm.addView(ll);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                conn = Database.connect();
                if (conn == null) {
                    return null;
                } else {
                    trails = Queries.getAddedTrails(conn);
                }
                conn.close();
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }

}
