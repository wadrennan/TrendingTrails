package com.example.trendingtrails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<String> dates;
    private List<String> maxTemps;
    private List<String> lowTemps;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;


    private int[] colorArray = new int[]{android.R.color.black, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_dark};

    ViewPagerAdapter(Context context, List<String> date, List<String> highs, List<String> lows, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.dates = date;
        this.maxTemps = highs;
        this.lowTemps = lows;
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
        //holder.relativeLayout.setBackgroundResource(colorArray[position]);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myDateView, maxTempView, lowTempView;
        ConstraintLayout constraintLayout;
        //Button button;

        ViewHolder(View itemView) {
            super(itemView);
            myDateView = itemView.findViewById(R.id.date);
            maxTempView = itemView.findViewById(R.id.max_temp_data);
            lowTempView = itemView.findViewById(R.id.low_temp_data);
            constraintLayout = itemView.findViewById(R.id.container);
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