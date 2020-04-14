package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Users extends AppCompatActivity {

    private Toolbar mUsersBar;
    private RecyclerView mUsersList;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        mUsersBar = findViewById(R.id.users_tool_bar);
        setSupportActionBar(mUsersBar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersList =findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<UsersList> options = new FirebaseRecyclerOptions.Builder<UsersList>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), UsersList.class)
                .build();

        adapter = new UsersAdapter(options);
        mUsersList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

}






















