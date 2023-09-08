package com.example.appcarsharing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private View rootView;
    private ViewGroup container;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
       this.container = container;
       this.inflater = inflater;
       this.savedInstanceState = savedInstanceState;

        ViewPager viewPager = rootView.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        TabPagerAdapter adapter = new TabPagerAdapter(getParentFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewPager viewPager = rootView.findViewById(R.id.viewPager);
        TabPagerAdapter adapter = new TabPagerAdapter(getParentFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        onCreateView(inflater,container,savedInstanceState);
    }

}

