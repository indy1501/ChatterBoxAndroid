package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfilePic;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mTotalFriends;
    private Button mSendFriendRequestBtn, mCancelBtn;
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
        mSendFriendRequestBtn = findViewById(R.id.request_btn);
        mCancelBtn = findViewById(R.id.cancel_req_btn);
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
//                        mSendFriendRequestBtn.setText("Send Request");
//                        mCancelBtn.setVisibility(View.INVISIBLE);
//                        mRejectBtn.setEnabled(false);

                    }
                    else if(type_of_request.equals("received")){
                        mCurrentState = "request_received";
                        mSendFriendRequestBtn.setText("Accept Request");
                        mCancelBtn.setVisibility(View.VISIBLE);
                        mCancelBtn.setEnabled(true);
//                        mCancelBtn.setText(R.string.rejectRequest);
                        mCancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelRequest();
                            }
                        });


                    }
                }
                else{
                    mFriendsDatabase.child(senderUserId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(receiverUserId)) {
                                        mCurrentState= "friends";
                                        mSendFriendRequestBtn.setText("Remove this Contact");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!senderUserId.equals(receiverUserId)) {
            mSendFriendRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSendFriendRequestBtn.setEnabled(false);

                    if (mCurrentState.equals("not_friends")) {
                        mSendFriendRequestBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                        sendChatRequest();
                    }
                    if (mCurrentState.equals("request_received")) {
                        acceptChatRequest();
                    }
                    if (mCurrentState.equals("request_sent")) {
                        cancelRequest();
                    }
                }
            });

        } else {
            mSendFriendRequestBtn.setVisibility(View.INVISIBLE);
            mCancelBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void cancelRequest() {
        mFriendReqDatabase.child(senderUserId).child(receiverUserId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mFriendReqDatabase.child(receiverUserId).child(senderUserId)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mSendFriendRequestBtn.setEnabled(true);
                                        mCurrentState = "not_friends";
                                        mSendFriendRequestBtn.setText(R.string.sendRequest);
                                        mCancelBtn.setVisibility(View.INVISIBLE);
                                        mCancelBtn.setEnabled(false);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void acceptChatRequest() {
        mFriendsDatabase.child(senderUserId).child(receiverUserId)
                .child("Friends").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mFriendsDatabase.child(receiverUserId).child(senderUserId)
                                    .child("Friends").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mFriendReqDatabase.child(senderUserId).child(receiverUserId)
                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        mFriendReqDatabase.child(receiverUserId).child(senderUserId)
                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                mSendFriendRequestBtn.setEnabled(true);
                                                                mCurrentState = "friends";
                                                                mSendFriendRequestBtn.setText(R.string.requestSentMessage);

                                                                mCancelBtn.setVisibility(View.INVISIBLE);
                                                                mCancelBtn.setEnabled(false);
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendChatRequest() {
        mFriendReqDatabase.child(senderUserId)
                .child(receiverUserId)
                .child("type_of_request")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mFriendReqDatabase.child(receiverUserId).child(senderUserId)
                                    .child("type_of_request").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mSendFriendRequestBtn.setText(R.string.requestSentMessage);
                                                mSendFriendRequestBtn.setEnabled(false);
                                                mCurrentState = "request_sent";
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
