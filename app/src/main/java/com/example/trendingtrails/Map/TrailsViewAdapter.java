package com.example.trendingtrails.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.util.List;

public class TrailsViewAdapter extends RecyclerView.Adapter<TrailsViewAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView distance;
        public Button mapTrail;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.trail_name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            mapTrail = (Button) itemView.findViewById(R.id.map_trail);
        }
    }
    //Members
    private List<Trail> mTrails;

    //Constructor
    public TrailsViewAdapter(List<Trail> t){
        mTrails = t;
    }

    @Override
    public TrailsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trailView = inflater.inflate(R.layout.item_trail, parent, false);
        ViewHolder viewHolder = new ViewHolder(trailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailsViewAdapter.ViewHolder viewHolder, int position){
        Trail t = mTrails.get(position);
        TextView name = viewHolder.name;
        name.setText(t.name);
        TextView distance = viewHolder.distance;
        distance.setText(""+t.distance+" miles");

    }

    @Override
    public int getItemCount(){
        return mTrails.size();
    }
}
