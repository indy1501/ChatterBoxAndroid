package com.cmpe277.chatterbox;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:
                return new ChatsFragment();

            case 1:
                return new StatusFragment();

            case 2:
                return new FriendsFragment();

            case 3:
                return new RequestsFragment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chats";

            case 1:
                return "Status";

            case 2:
                return "Friends";

            case 3:
                return "Requests";

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
