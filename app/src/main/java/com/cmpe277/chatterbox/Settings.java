package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Picture;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase;
    private CircleImageView mProfilePic;
    private TextView mDisplayName;
    private TextView mStatus;
    private Button changePicBtn;
    private Button changeStatusBtn;
    private Toolbar mSettingsBar;
    private StorageReference mPictureStorage;
    private  DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettingsBar = findViewById(R.id.settings_bar);
        setSupportActionBar(mSettingsBar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mProfilePic = findViewById(R.id.profile_pic);
        mDisplayName = findViewById(R.id.display_name);
        mStatus = findViewById(R.id.status_msg);
        changePicBtn = findViewById(R.id.change_image_btn);
        changeStatusBtn = findViewById(R.id.change_status_btn);

        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mPictureStorage = FirebaseStorage.getInstance().getReference();

        String current_uid = mCurrentUser.getUid();
        myRef = mDatabase.getReference().child("Users").child(current_uid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumbnail = dataSnapshot.child("thumbnail").getValue().toString();

                mDisplayName.setText(name);
                mStatus.setText(status);

                Picasso.get().load(image).into(mProfilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mStatus.getText().toString();
                Intent changeStatus = new Intent(Settings.this, Status.class);
                changeStatus.putExtra("status_value", status_value);
                startActivity(changeStatus);

            }
        });


        changePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                picIntent.setType("image/*");
//                startActivityForResult(Intent.createChooser(picIntent, "PROFILE PICTURE"), 1 );

//                 start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Settings.this);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(this);

        }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                String current_uid = mCurrentUser.getUid();

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();

                    StorageReference filepath = mPictureStorage.child("profile_pictures").child(current_uid + ".jpg");

                    filepath.putFile(resultUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();

                                    myRef.child("image").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        Toast.makeText(Settings.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                }
            }



        }
}









































