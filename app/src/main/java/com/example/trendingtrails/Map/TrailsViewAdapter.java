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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView distance;
        public Button mapTrail;
        OnMapListener onMapListener;
        public TextView id;

        public ViewHolder(View itemView, OnMapListener onMapListener){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.trail_name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            mapTrail = (Button) itemView.findViewById(R.id.map_trail);
            id = itemView.findViewById(R.id.id_number);
            this.onMapListener = onMapListener;
            mapTrail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMapListener.onMapClick(getAdapterPosition());
        }
    }

    public interface OnMapListener{
        void onMapClick(int position);
    }
    //Members
    private List<Trail> mTrails;
    private OnMapListener monMapListener;

    //Constructor
    public TrailsViewAdapter(List<Trail> t, OnMapListener onMapListener){

        mTrails = t;
        this.monMapListener = onMapListener;
    }

    @Override
    public TrailsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trailView = inflater.inflate(R.layout.item_trail, parent, false);
        ViewHolder viewHolder = new ViewHolder(trailView, monMapListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailsViewAdapter.ViewHolder viewHolder, int position){
        Trail t = mTrails.get(position);
        TextView name = viewHolder.name;
        name.setText(t.name);
        TextView distance = viewHolder.distance;
        distance.setText(""+t.distance+" miles");
        TextView id = viewHolder.id;
        id.setText(""+t.id);
        id.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount(){
        return mTrails.size();
    }
}
