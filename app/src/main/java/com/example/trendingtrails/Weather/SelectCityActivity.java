package com.example.trendingtrails.Weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Models.Input;
import com.example.trendingtrails.R;

import java.sql.Connection;

public class SelectCityActivity extends BaseActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_select_city);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.zipcode);
                String s = editText.getText().toString();

                if (Input.isValidZipCode(s)) {
                    Connection conn = Database.connect();
                    Intent intent = new Intent(SelectCityActivity.this, WeatherActivity.class);
                    intent.putExtra("zip", s);
                    intent.putExtra("isAdded", true);
                    startActivity(intent);
                    finish();
                } else {
                    findViewById(R.id.ziperror).setVisibility(View.VISIBLE);
                }
            }

        });
    }
}
