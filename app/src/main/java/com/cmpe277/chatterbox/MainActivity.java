package com.cmpe277.chatterbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login;
    TextView register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.registerText);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

                Intent chatIntent = new Intent(MainActivity.this, Chat.class);
                startActivity(chatIntent);
                finish();

        }

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent loginIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginIntent);
                    finish();
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent registerIntent = new Intent(MainActivity.this, Register.class);
                    startActivity(registerIntent);
                    finish();
                }
            });

        }



    }


