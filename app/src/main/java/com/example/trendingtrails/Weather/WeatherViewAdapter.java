package com.example.trendingtrails.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Models.Location;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.util.List;

public class WeatherViewAdapter extends RecyclerView.Adapter<WeatherViewAdapter.ViewHolder> {

    public OnCardClickListener onCardClickListener;
    public List<Location> locationList;

    public void notifyDataSetChanged(Location location) {
        int pos = 0;
        for (Location l: locationList) {
            if(l == location)
                break;
            pos++;
        }
        notifyItemChanged(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView zip;
        Button deleteZip;
        ImageView weather;
        OnCardClickListener onCardClickListener;

        public ViewHolder(final View itemView, OnCardClickListener onCardClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            zip = itemView.findViewById(R.id.locationName);
            deleteZip = itemView.findViewById(R.id.zipDeleteBtn);
            weather = itemView.findViewById(R.id.locationCurrentWeather);
            this.onCardClickListener = onCardClickListener;
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(locationList.get(getAdapterPosition()).latitude, locationList.get(getAdapterPosition()).longitude);
        }
    }

    public interface OnCardClickListener {
        void onCardClick(double lat, double lon);
    }

    //Constructor
    public WeatherViewAdapter(List<Location> z, OnCardClickListener onCardClickListener) {
        locationList = z;
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public WeatherViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.weather_items, parent, false);
        WeatherViewAdapter.ViewHolder viewHolder = new WeatherViewAdapter.ViewHolder(weatherView, onCardClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.zip.setText("" + locationList.get(position).locationName);

        if(locationList.get(position).currentWeather==0){
            viewHolder.weather.setImageResource(android.R.color.transparent);
        }
        else if(locationList.get(position).currentWeather<522){
            viewHolder.weather.setImageResource(R.drawable.rain);
        }
        else if(locationList.get(position).currentWeather < 623 ){
            viewHolder.weather.setImageResource(R.drawable.snow);
        }
        else if(locationList.get(position).currentWeather < 801){
            viewHolder.weather.setImageResource(R.drawable.sunny);
        }
        else if(locationList.get(position).currentWeather < 803){
            viewHolder.weather.setImageResource(R.drawable.partly_cloudy);
        }
        else{
            viewHolder.weather.setImageResource(R.drawable.cloudy);
        }
        if(locationList.get(position).zipCode == null)
            viewHolder.deleteZip.setVisibility(View.INVISIBLE);
        viewHolder.deleteZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = locationList.get(position).zipCode;
                locationList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, locationList.size());
                Connection conn = Database.connect();
                Queries.deleteZipCode(conn, zip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}


