package com.example.trendingtrails.Weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trendingtrails.HomeActivity;
import com.example.trendingtrails.R;

public class SelectCityActivity extends HomeActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_select_city);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.zipcode);
                String s = editText.getText().toString();

                if(s.length() != 5){
                    findViewById(R.id.ziperror).setVisibility(View.VISIBLE);
                }
                else{
                    Intent intent = new Intent(SelectCityActivity.this, WeatherActivity.class);
                    intent.putExtra("zip",s);
                    startActivity(intent);

                }
            }

        });
    }

}
