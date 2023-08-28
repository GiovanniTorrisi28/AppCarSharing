package com.example.appcarsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    NotificationFragment notificationFragment = new NotificationFragment(this);
    PofFragment pofFragment = new PofFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    TestFragment testFragment = new TestFragment();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notification);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(8);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                    return true;
                }
                else if (item.getItemId() == R.id.notification){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                    return true;
                }
                else if (item.getItemId() == R.id.settings){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                    return true;
                }
                else if (item.getItemId() == R.id.pof){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,pofFragment).commit();
                    return true;
                }
                else if (item.getItemId() == R.id.profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                    return true;
                }
                return false;
            }
        });

        //se l'activity viene aperta da una notifica
        if (getIntent().getBooleanExtra("apriFragment", false)) {
            // Carica il fragment desiderato
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,notificationFragment)
                    .commit();
        }

        //ConnectionCheckThread connectionCheckThread = new ConnectionCheckThread(getApplicationContext(),findViewById(android.R.id.content));
        //connectionCheckThread.start();


    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}