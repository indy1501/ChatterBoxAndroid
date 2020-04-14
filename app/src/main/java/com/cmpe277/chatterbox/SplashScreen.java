package com.cmpe277.chatterbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Chat.class));
            finish();
        }
    }
}
