package com.cmpe277.chatterbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsFragment extends Fragment {

    private RecyclerView mRequestsList;
    private View requestsFragmentView;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private DatabaseReference mUsersDatabase, mFriendReqDatabase, mFriendsDatabase;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestsFragmentView = inflater.inflate(R.layout.fragment_requests, container, false);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        mRequestsList = requestsFragmentView.findViewById(R.id.friend_requests_list);
        mRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return requestsFragmentView;


    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(mFriendReqDatabase.child(mCurrentUserId), Friends.class)
                .build();

        FirebaseRecyclerAdapter<Friends, FriendRequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<Friends, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendRequestViewHolder holder, int position, @NonNull Friends model) {
                final View mFriendReqBtn = holder.itemView.findViewById(R.id.accept_btn);
                final View mRejectBtn = holder.itemView.findViewById(R.id.reject_btn);

                mFriendReqBtn.setVisibility(View.VISIBLE);
                mRejectBtn.setVisibility(View.VISIBLE);

                final String listUserId = getRef(position).getKey();

                final DatabaseReference mRequestType = getRef(position).child("type_of_request").getRef();

                mRequestType.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            String requestType = dataSnapshot.getValue().toString();

                            if(requestType.equals("received")){
                                mUsersDatabase.child(listUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if(dataSnapshot.hasChild("image")){
//                                            String requestProfileImage = dataSnapshot.child("image").getValue().toString();
//                                           // Picasso.get().load(requestProfileImage).into(holder.pImage);
//                                        }

                                        final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                        final String requestUserStatus = dataSnapshot.child("status").getValue().toString();

                                        holder.name.setText(requestUserName);
                                        holder.status.setText(R.string.requestComment);

                                        mFriendReqBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mFriendsDatabase.child(mCurrentUserId)
                                                        .child(listUserId).child("Friends")
                                                        .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            mFriendReqDatabase.child(mCurrentUserId)
                                                                    .child(listUserId).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    mFriendReqDatabase.child(listUserId)
                                                                            .child(mCurrentUserId).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(getContext(), "New Friend Saved", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                        mRejectBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mFriendReqDatabase.child(mCurrentUserId)
                                                        .child(listUserId).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    mFriendReqDatabase.child(listUserId)
                                                                            .child(mCurrentUserId).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        });
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_list_layout, viewGroup, false);
                return new FriendRequestViewHolder(view);
            }
        };
    mRequestsList.setAdapter(adapter);
    adapter.startListening();

    }

    private class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView name,status;
        CircleImageView dp;
        Button acceptBtn, cancelBtn;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            dp = itemView.findViewById(R.id.dp);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            cancelBtn = itemView.findViewById(R.id.reject_btn);
        }
    }
}
