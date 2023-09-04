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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private View rootView;
    private ViewPager viewPager;
    private  TabPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewGroup container;
    LayoutInflater inflater;
    Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
       this.container = container;
       this.inflater = inflater;
       this.savedInstanceState = savedInstanceState;

        viewPager = rootView.findViewById(R.id.viewPager);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        adapter = new TabPagerAdapter(getParentFragmentManager(), 0);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //fragment aperto dalla mappa
        Bundle args = getArguments();
        if (args != null)
            tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    adapter.setDestination(args.getInt("selectedDestination"));
                    viewPager.setAdapter(adapter);

                    tabLayout.getTabAt(args.getInt("selectedPage")).select();
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewPager = rootView.findViewById(R.id.viewPager);
        adapter = new TabPagerAdapter(getParentFragmentManager(), 0);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        onCreateView(inflater,container,savedInstanceState);
    }

}

