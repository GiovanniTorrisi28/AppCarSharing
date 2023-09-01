package com.example.appcarsharing;

import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_TABS = 2;
    private List<CharSequence> titles = Arrays.asList("Cerca Passaggio","Offri Passaggio");
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AskRideFragment();
            case 1:
                return new OfferRideFragment();
            default:
                return new SettingsFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
