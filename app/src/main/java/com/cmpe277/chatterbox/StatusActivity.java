package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private EditText mStatus;
    private Button mUpdateBtn;
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private Toolbar mStatusBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mStatus = findViewById(R.id.status_et);
        mUpdateBtn = findViewById(R.id.status_update_btn);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mStatusBar = findViewById(R.id.status_bar);
        setSupportActionBar(mStatusBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");



        mStatus.setHint(status_value);

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

               String status = mStatus.getText().toString();
                mStatusDatabase.child("status").setValue(status);

            }
       });
    }
}
