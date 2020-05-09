package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, emailId, password;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.etUserName);
        emailId = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        register = findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString().trim();
                String pswd = password.getText().toString().trim();
                final String displayName = username.getText().toString().trim();

                if(email.isEmpty()){

                    emailId.setError("Please enter an email address");
                    emailId.requestFocus();

                } else if(pswd.isEmpty()) {

                    password.setError("Please enter the password");
                    password.requestFocus();

                }else if(pswd.length() < 6) {

                    password.setError("The password must be more than 6 characters");
                    password.requestFocus();

                //register the user
                } else if(!(email.isEmpty() && pswd.isEmpty())){

                    progressBar.setVisibility(View.VISIBLE);

                    register_user(displayName, email, pswd);

                } else {
                    Toast.makeText(RegisterActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void register_user(final String displayName, String email, String pswd) {

        mAuth.createUserWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String uid = currentUser.getUid();
                                        DatabaseReference myRef = mDatabase.getReference().child("Users").child(uid);

                                        HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("name", displayName);
                                        userMap.put("status", "Available on ChatterBox");
                                        userMap.put("image", "default");
                                        userMap.put("thumbnail", "default");

                                        myRef.setValue(userMap);


                                        Toast.makeText(RegisterActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        Intent chatPageIntent = new Intent(RegisterActivity.this, PostLoginActivity.class);
                                        chatPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(chatPageIntent);
                                        finish();

                                    } else {

                                        Toast.makeText(RegisterActivity.this, "Authentication failed. Please check the credentials and try again" + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


    }
}
