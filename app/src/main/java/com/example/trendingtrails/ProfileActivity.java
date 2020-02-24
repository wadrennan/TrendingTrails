package com.example.trendingtrails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onStart(){
        super.onStart();
        EditText emailText = findViewById(R.id.emailText);
        emailText.setText(Constants.account.getEmail());
    }
}
