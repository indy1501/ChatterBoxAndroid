package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private CircleImageView mProfilePic;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mTotalFriends;
    private Button mFriendRequestBtn;
    private Button mRejectBtn;
    private Toolbar mProfileBar;
    private String mCurrent_state;


    //private FirebaseDatabase mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfileBar = findViewById(R.id.profile_bar);
        setSupportActionBar(mProfileBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String user_id = getIntent().getStringExtra("user_id");

        assert user_id != null;
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfilePic = findViewById(R.id.profile_pic);
        mProfileName = findViewById(R.id.profile_name);
        mProfileStatus = findViewById(R.id.profile_status);
        mTotalFriends = findViewById(R.id.total_friends);
        mFriendRequestBtn = findViewById(R.id.request_btn);
        mRejectBtn = findViewById(R.id.reject_btn);
        mCurrent_state = "not_friends";

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String pName = dataSnapshot.child("name").getValue().toString();
                String pStatus = dataSnapshot.child("status").getValue().toString();
                //String pImage = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(pName);
                mProfileStatus.setText(pStatus);

                //Picasso.get().load(pImage).placeholder(R.drawable.profile).into(mProfilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mFriendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCurrent_state.equals("not_friends")){

                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).child("request_type")
                                                .setValue("received")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Profile.this, "friend request sent successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(Profile.this, "friend request was not sent", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });

        mRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
