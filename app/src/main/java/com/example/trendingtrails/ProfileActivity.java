package com.example.trendingtrails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.trendingtrails.Models.Global.AccountInfo;
import static com.example.trendingtrails.Models.Global.UserProfile;

public class ProfileActivity extends BaseActivity {
    ArrayAdapter<String> dataAdapter;
    EditText nameText;
    Spinner spinner;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameText = findViewById(R.id.nameText);
        nameText.setText(UserProfile.displayName);
        TextView emailTxt = findViewById(R.id.emailText);
        emailTxt.setText(AccountInfo.personEmail);
        spinner = findViewById(R.id.experienceSpinner);
        List<String> options = new ArrayList<String>();
        options.add("");
        options.add("Beginner");
        options.add("Intermediate");
        options.add("Advanced");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(UserProfile.rank);
        findViewById(R.id.submitProfileChangesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        updateProfile();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
            }
        });
    }

    private void updateProfile() {
        try
        {
            conn = Database.connect();        // Connect to database
            if (conn == null)
            {
                return;
            }
            else
            {
                String newName = nameText.getText().toString();
                int newExp = spinner.getSelectedItemPosition();
                if(UserProfile.displayName == newName && UserProfile.rank == newExp){
                    conn.close();
                    return;
                }
                String query = "Update [dbo].[Profile] SET name = '" + newName + "', experience = " + newExp + " where email= '" + AccountInfo.personEmail + "' ";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
                UserProfile.displayName = newName;
                UserProfile.rank = newExp;
            }
            conn.close();
        }
        catch (Exception ex)
        {
            return;
        }
    }
}
