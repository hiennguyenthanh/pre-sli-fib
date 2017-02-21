package com.example.hiennguyen.firebaseexample;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by Civics on 2/21/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
