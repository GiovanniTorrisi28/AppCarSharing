package com.example.appcarsharing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.viewPager);

        TabPagerAdapter adapter = new TabPagerAdapter(getParentFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //fragment aperto dalla mappa
       /* Bundle args = getArguments();
        if (args != null)
            tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tabLayout.getTabAt(args.getInt("selectedPage")).select(); // L'indice 1 rappresenta il secondo elemento
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
*/
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewPager viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setCurrentItem(2);
        TabPagerAdapter adapter = new TabPagerAdapter(getParentFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

