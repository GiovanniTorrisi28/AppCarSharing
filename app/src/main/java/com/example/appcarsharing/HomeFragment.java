package com.example.appcarsharing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private Button logout;
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

        //pulsante di logout
       /* logout = rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //logut utente con ritorno alla schermata login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
*/

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
    }
}

