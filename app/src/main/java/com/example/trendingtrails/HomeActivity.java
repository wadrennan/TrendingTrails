package com.example.trendingtrails;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.weatherButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weatherButton:
                        Intent weatherIntent = new Intent(getBaseContext(), SelectCityActivity.class);
                        startActivity(weatherIntent);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_signout:
                signOut();
                return true;
            case R.id.menuitem_profile:
                Intent profileIntent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(profileIntent);
                return true;
        }
        return false;
    }

    private void signOut() {
        Account.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signoutIntent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(signoutIntent);
                        finishAffinity();
                    }
                });
    }
}
