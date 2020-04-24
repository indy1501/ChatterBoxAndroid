package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Users extends AppCompatActivity {

    private Toolbar mUsersBar;
    private RecyclerView mUsersList;
//    private UsersAdapter adapter;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList =findViewById(R.id.users_list);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        mUsersBar = findViewById(R.id.users_tool_bar);
        setSupportActionBar(mUsersBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Users");




//        mUsersList.setHasFixedSize(true);



//        adapter = new UsersAdapter(options);
//        mUsersList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UsersList> options = new FirebaseRecyclerOptions.Builder<UsersList>()
                .setQuery(usersRef, UsersList.class)
                .build();

        FirebaseRecyclerAdapter<UsersList, UsersListViewHolder> adapter =
                new FirebaseRecyclerAdapter<UsersList, UsersListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UsersListViewHolder holder, final int position, @NonNull UsersList model) {
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        //holder.setImage(model.getImage());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(Users.this, Profile.class);
                                profileIntent.putExtra("user_id", user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UsersListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_list_layout, viewGroup, false);
                        return new UsersListViewHolder(view);
                    }
                };
        mUsersList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class UsersListViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircleImageView profileImage;

        public UsersListViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            userStatus = itemView.findViewById(R.id.status);
            //profileImage = itemView.findViewById(R.id.dp);
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

}






















