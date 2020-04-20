package com.example.trendingtrails.Weather;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.trendingtrails.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<String> dates;
    private List<String> maxTemps;
    private List<String> lowTemps;
    private int descriptors[];
    private List<String> rainChance;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;


    private int[] colorArray = new int[]{android.R.color.black, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_dark};

    ViewPagerAdapter(Context context, List<String> date, List<String> highs, List<String> lows, int descriptor[], List<String> rainChance, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.dates = date;
        this.maxTemps = highs;
        this.lowTemps = lows;
        this.descriptors = descriptor;
        this.rainChance = rainChance;
        this.viewPager2 = viewPager2;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String date = dates.get(position);
        holder.myDateView.setText(date);
        String highTemp = maxTemps.get(position);
        holder.maxTempView.setText(highTemp);
        String lowTemp = lowTemps.get(position);
        holder.lowTempView.setText(lowTemp);
        String rainProp = rainChance.get(position);
        holder.rain.setText(rainProp + "%");
        if(descriptors[position]<522){
            holder.iconView.setImageResource(R.drawable.rain);
            holder.description.setText("Rainy");
            holder.constraintLayout.setBackgroundResource(R.drawable.cloudybackground);
            holder.constraintLayout.getBackground().setAlpha(100);
        }
        else if(descriptors[position] < 623 ){
            holder.iconView.setImageResource(R.drawable.snow);
            holder.description.setText("Snowy");
            holder.constraintLayout.setBackgroundResource(R.drawable.snowybackground);
            holder.constraintLayout.getBackground().setAlpha(100);
        }
        else if(descriptors[position] >751 && descriptors[position] < 801){
            holder.iconView.setImageResource(R.drawable.sunny);
            holder.description.setText("Sunny");
            holder.constraintLayout.setBackgroundResource(R.drawable.sunnybackground);
            holder.constraintLayout.getBackground().setAlpha(100);

        }
        else if(descriptors[position]  < 803){
            holder.iconView.setImageResource(R.drawable.partly_cloudy);
            holder.description.setText("Partly Cloudy");
            holder.constraintLayout.setBackgroundResource(R.drawable.partlycloudybackground);
            holder.constraintLayout.getBackground().setAlpha(100);
        }
        else{
            holder.iconView.setImageResource(R.drawable.cloudy);
            holder.description.setText("Cloudy");
            holder.constraintLayout.setBackgroundResource(R.drawable.cloudybackground);
            holder.constraintLayout.getBackground().setAlpha(100);
        }

        //holder.relativeLayout.setBackgroundResource(colorArray[position]);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myDateView, maxTempView, lowTempView, description, rain;
        ImageView iconView;
        ConstraintLayout constraintLayout;
        //Button button;

        ViewHolder(View itemView) {
            super(itemView);
            myDateView = itemView.findViewById(R.id.date);
            maxTempView = itemView.findViewById(R.id.max_temp_data);
            lowTempView = itemView.findViewById(R.id.low_temp_data);
            iconView = itemView.findViewById(R.id.icon);
            constraintLayout = itemView.findViewById(R.id.container);
            description = itemView.findViewById(R.id.dayWeatherDescription);
            rain = itemView.findViewById(R.id.rainChance);
            /*button = itemView.findViewById(R.id.btnToggle);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL)
                        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    else{
                        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                    }
                }
            });*/
        }
    }

}