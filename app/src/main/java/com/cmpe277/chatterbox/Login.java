package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    EditText emailId, password;
    Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //username = findViewById(R.id.etUserName);
        emailId = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pswd = password.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pswd)){

                    mAuth.signInWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent chatIntent = new Intent(Login.this, Chat.class);
                                        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(chatIntent);
                                        finish();
                                        //FirebaseUser user = mAuth.getCurrentUser();

                                    } else {

                                        Toast.makeText(Login.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });
    }


}
