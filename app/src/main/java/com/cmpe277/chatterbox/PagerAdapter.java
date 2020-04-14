package com.cmpe277.chatterbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;


    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {

        super(fm, behavior);
        this.numOfTabs = behavior;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new ChatsFragment();
                
            case 1:
                return new StatusFragment();

            case 2:
                return new FriendsFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
