package com.example.appcarsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    NotificationFragment notificationFragment = new NotificationFragment(this);
    PofFragment pofFragment = new PofFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    TestFragment testFragment = new TestFragment();

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

    }


}