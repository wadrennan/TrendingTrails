package com.example.trendingtrails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.trendingtrails.Models.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends HomeActivity {
    ArrayAdapter<String> dataAdapter;
    EditText nameText;
    Spinner spinner;
    Connection conn;
    String email, name, newName;
    int exp, newExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameText = findViewById(R.id.nameText);
        spinner = findViewById(R.id.experienceSpinner);
        List<String> options = new ArrayList<String>();
        options.add("");
        options.add("Beginner");
        options.add("Intermediate");
        options.add("Advanced");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        findViewById(R.id.submitProfileChangesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submitProfileChangesBtn:
                        updateProfile();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void updateProfile() {
        try
        {
            conn = connect();        // Connect to database
            if (conn == null)
            {
                return;
            }
            else
            {
                newName = nameText.getText().toString();
                newExp = spinner.getSelectedItemPosition();
                if(name == newName && exp == newExp){
                    conn.close();
                    return;
                }
                String query = "Update [dbo].[Profile] SET name = '" + newName + "', experience = " + newExp + " where email= '" + email + "' ";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                name = newName;
                exp = newExp;
            }
            conn.close();
        }
        catch (Exception ex)
        {
            return;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Profile profile = new Profile();
        profile.execute("");
    }

    public class Profile extends AsyncTask<String,String,String>
    {
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            email = Account.account.getEmail();
            TextView emailText = findViewById(R.id.emailText);
            emailText.setText(email);
        }

        @Override
        protected void onPostExecute(String r)
        {
                nameText.setText(name);
                spinner.setSelection(exp);
        }
        @Override
        protected String doInBackground(String... params)
        {

                try
                {
                    conn = connect();
                    if (conn == null)
                    {
                        return null;
                    }
                    else
                    {
                        String query = "Select * from [dbo].[Profile] where email= '" + Account.account.getEmail() + "' ";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            name = rs.getString("name");
                            exp = rs.getInt("experience");
                        }
                        else
                        {
                            name = Account.account.getDisplayName();
                            exp = 0;
                            query = "INSERT INTO [dbo].[Profile] (email, name, experience) VALUES ('" + email + "', '" + name +"', 0) ";
                            rs = stmt.executeQuery(query);
                        }
                    }
                    conn.close();
                }
                catch (Exception ex)
                {
                    return null;
                }
                return null;
        }
    }

    @SuppressLint("NewApi")
    public Connection connect()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://trendingtrails.database.windows.net:1433;databaseName=TrendingTrails;user=androidUser@trendingtrails;password=TrendingTrails1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
