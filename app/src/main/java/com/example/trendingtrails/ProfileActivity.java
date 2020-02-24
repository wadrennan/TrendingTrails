package com.example.trendingtrails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Spinner spinner = findViewById(R.id.experienceSpinner);
        List<String> options = new ArrayList<String>();
        options.add("Beginner");
        options.add("Intermediate");
        options.add("Advanced");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        TextView emailText = findViewById(R.id.emailText);
        EditText nameText = findViewById(R.id.nameText);
        emailText.setText(Account.account.getEmail());
        nameText.setText(Account.account.getDisplayName());
    }
}
