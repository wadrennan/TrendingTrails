package com.example.trendingtrails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_button:
                        signIn();
                        break;
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        Account.mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Account.account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(Account.account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            Account.account = task.getResult(ApiException.class);
            updateUI(Account.account);
        } catch (ApiException e){
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null) {
            Intent successfulIntent = new Intent(getBaseContext(), HomeActivity.class);
            startActivity(successfulIntent);
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = Account.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }
}
