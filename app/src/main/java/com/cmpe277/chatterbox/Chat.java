package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Chat extends AppCompatActivity {


    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private TabItem mTabStatus;
    private TabItem mTabChat;
    private TabItem mTabFriends;
    private ViewPager mViewPager;

//    String user_id = "John";
    //    String user_id = "Lisbon";

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar =  findViewById(R.id.chat_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ChatterBox");
       // mAuth = FirebaseAuth.getInstance();

        mTabLayout = findViewById(R.id.tabBar);
        mTabStatus = findViewById(R.id.tabStatus);
        mTabChat = findViewById(R.id.tabChat);
        mTabFriends = findViewById(R.id.tabFriends);
        mViewPager = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.logout_btn){

            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(Chat.this, MainActivity.class);
            startActivity(logoutIntent);
        }

        if(item.getItemId() == R.id.settings_btn){

            Intent settingsIntent = new Intent(Chat.this, Settings.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId() == R.id.all_users_btn){

            Intent allUsersIntent = new Intent(Chat.this, Users.class);
            startActivity(allUsersIntent);
        }


//        if (item.getItemId() == R.id.profile_btn){
//            Intent profileIntent = new Intent(Chat.this, Profile.class);
//            profileIntent.putExtra("user_id", user_id);
//           // Toast.makeText(this, "user profile is now visible", Toast.LENGTH_SHORT).show();
//            startActivity(profileIntent);
//
//
//        }

        return true;
    }
}
