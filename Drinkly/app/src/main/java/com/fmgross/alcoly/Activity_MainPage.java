package com.fmgross.alcoly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.fmgross.alcoly.fragments.Fragment_Main;
import com.fmgross.alcoly.fragments.Fragment_Statistics;
import com.fmgross.alcoly.fragments.Fragment_Timeline;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartActivity();
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment;
                System.out.println(item.getItemId());
            switch (item.getItemId()) {
                case R.id.homeButton:
                    selectedFragment = new Fragment_Main();
                    break;
                case R.id.timeline:
                    selectedFragment = new Fragment_Timeline();
                    break;
                case R.id.statistics:
                    selectedFragment = new Fragment_Statistics();
                    break;
                default:
                    selectedFragment = new Fragment_Main();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
            return true;
        });
        navView.setSelectedItemId(R.id.homeButton);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Main()).commit();
    }

    /**
     * Starts the first Startup activity
     */
    private void showStartActivity() {
        Intent intent = new Intent(this, Activity_FirstStartUp.class);
        startActivity(intent);
    }
}