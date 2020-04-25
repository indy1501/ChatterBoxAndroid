package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
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
    private String mCurrentState, receiverUserId, senderUserId;


    private DatabaseReference mFriendReqDatabase, mUsersDatabase, mFriendsDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfileBar = findViewById(R.id.profile_bar);
        setSupportActionBar(mProfileBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        receiverUserId = getIntent().getStringExtra("user_id");
        mCurrentUser = mAuth.getCurrentUser();
        senderUserId = mCurrentUser.getUid();

        mProfilePic = findViewById(R.id.profile_pic);
        mProfileName = findViewById(R.id.profile_name);
        mProfileStatus = findViewById(R.id.profile_status);
        mTotalFriends = findViewById(R.id.total_friends);
        mFriendRequestBtn = findViewById(R.id.request_btn);
        mRejectBtn = findViewById(R.id.reject_btn);
        mCurrentState = "not_friends";

        retrieveUserData();



    }

    private void retrieveUserData() {
        mUsersDatabase.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName = (String) dataSnapshot.child("name").getValue();
                    String pStatus = (String) dataSnapshot.child("status").getValue();

                    mProfileName.setText(pName);
                    mProfileStatus.setText(pStatus);

                    processRequest();
                }else{
                    String pName = (String) dataSnapshot.child("name").getValue();
                    String pStatus = (String) dataSnapshot.child("status").getValue();
                    //String pImage = dataSnapshot.child("image").getValue().toString();

                    mProfileName.setText(pName);
                    mProfileStatus.setText(pStatus);

                    processRequest();
                }

                //Picasso.get().load(pImage).placeholder(R.drawable.profile).into(mProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void processRequest() {
        mFriendReqDatabase.child(senderUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    String type_of_request = dataSnapshot.child(receiverUserId).child("type_of_request").getValue().toString();

                    if(type_of_request.equals("sent")){
                        mCurrentState = "request_sent";
                        mFriendRequestBtn.setText("Send Request");
                        mRejectBtn.setVisibility(View.INVISIBLE);
//                        mRejectBtn.setEnabled(false);

                    }
                    else if(type_of_request.equals("received")){
                        mCurrentState = "request_received";
                        mFriendRequestBtn.setText("Accept Request");
                        mRejectBtn.setText(R.string.rejectRequest);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!senderUserId.equals(receiverUserId)) {
            mFriendRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFriendRequestBtn.setEnabled(false);

                    if (mCurrentState.equals("not_friends")) {
                        mFriendRequestBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        mFriendRequestBtn.setText(R.string.requestSentMessage);
                        mFriendRequestBtn.setEnabled(false);
                        sendChatRequest();
                    }
                    if (mCurrentState.equals("request_received")) {
                        acceptChatRequest();
                    }
                }
            });

        } else {
            mFriendRequestBtn.setVisibility(View.INVISIBLE);
            mRejectBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void acceptChatRequest() {
    }

    private void sendChatRequest() {
    }
}
