package com.example.trendingtrails.Models;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AccountInfo {
    public String personName;
    public String personGivenName;
    public String personFamilyName;
    public String personEmail;
    public String personId;
    public Uri personPhoto;

    public AccountInfo(GoogleSignInAccount account){
         personName = account.getDisplayName();
         personGivenName = account.getGivenName();
         personFamilyName = account.getFamilyName();
         personEmail = account.getEmail();
         personId = account.getId();
         personPhoto = account.getPhotoUrl();
    }
}
