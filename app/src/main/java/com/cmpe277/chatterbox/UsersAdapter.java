package com.cmpe277.chatterbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends FirebaseRecyclerAdapter<UsersList, UsersAdapter.usersViewHolder>{


    public UsersAdapter(@NonNull FirebaseRecyclerOptions<UsersList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull usersViewHolder holder, int position, @NonNull UsersList model) {

        holder.setName(model.getName());
        holder.setStatus(model.getStatus());
        //holder.setImage(model.getImage());
    }


    @NonNull
    @Override
    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_layout,parent,false);
        return new usersViewHolder(view);
    }



    class usersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public usersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String name){

            EditText userName = mView.findViewById(R.id.name);
            userName.setText(name);

        }

        public void setStatus(String status){

            EditText userStatus = mView.findViewById(R.id.status);
            userStatus.setText(status);
        }

//        public void setImage(String dp){
//
//            CircleImageView userDp = mView.findViewById(R.id.dp);
//            userDp.setImageURI(dp);
//        }
    }
}










































