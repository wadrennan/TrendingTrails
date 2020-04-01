package com.example.trendingtrails.Weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Database;
import com.example.trendingtrails.Models.Input;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.Statement;

import static com.example.trendingtrails.Models.Global.AccountInfo;

public class SelectCityActivity extends BaseActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_select_city);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.zipcode);
                String s = editText.getText().toString();

                if(Input.isValidZipCode(s)){
                    addLocation(s);
                    Intent intent = new Intent(SelectCityActivity.this, WeatherActivity.class);
                    intent.putExtra("zip",s);
                    startActivity(intent);
                    finish();
                }
                else{
                    findViewById(R.id.ziperror).setVisibility(View.VISIBLE);
                }
            }

        });
    }

    private void addLocation(String code) {
        Connection conn = Database.connect();
        String query = " INSERT INTO ZipCodes (code, email) " +
                " Values ('" + code + "','" + AccountInfo.personEmail + "')";

        System.out.println(query);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (Exception e) {
            System.out.println("Error in adding Location");
            System.out.println(e);
        }
    }
}
