package com.example.trendingtrails.Weather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.Map.TrailsViewAdapter;
import com.example.trendingtrails.Models.Trail;
import com.example.trendingtrails.R;

import java.util.List;

public class WeatherViewAdapter extends RecyclerView.Adapter<WeatherViewAdapter.ViewHolder> {

    public OnCardClickListener onCardClickListener;
    public List<Integer> zipCodeList;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView zip;
        OnCardClickListener onCardClickListener;
        public ViewHolder(View itemView, OnCardClickListener onCardClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            zip = itemView.findViewById(R.id.zip_value);
            this.onCardClickListener = onCardClickListener;
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition());


        }
    }

    public interface OnCardClickListener{
        void onCardClick(int position);
    }

    //Constructor
    public WeatherViewAdapter(List<Integer> z, OnCardClickListener onCardClickListener){
        zipCodeList = z;
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public WeatherViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.weather_items, parent, false);
        WeatherViewAdapter.ViewHolder viewHolder = new WeatherViewAdapter.ViewHolder(weatherView, onCardClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewAdapter.ViewHolder viewHolder, int position){
        viewHolder.zip.setText(""+zipCodeList.get(position));

    }

    @Override
    public int getItemCount(){
        return zipCodeList.size();
    }
}


