package com.cmpe277.chatterbox;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private View friendsView;
    private RecyclerView mFriendList;
    private DatabaseReference mFriendsDatabase, mUsersDatabase;

    private FirebaseAuth mAuth;
    private String currentUserId;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendsView = inflater.inflate(R.layout.fragment_friends, container, false);
        mFriendList = friendsView.findViewById(R.id.friend_list);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        return friendsView;
    };

}
