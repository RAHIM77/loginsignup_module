package com.example.stjais.carpoolkarle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragmentTransaction.replace(R.id.content,new DashboardFragment()).commit();
                    return true;
                case R.id.navigation_offer_ride:
                    fragmentTransaction.replace(R.id.content,new OfferRideFragment()).commit();
                    return true;
                case R.id.navigation_search_ride:
                    fragmentTransaction.replace(R.id.content,new SearchRideFragment()).commit();
                    return true;
                case R.id.navigation_history:
                    fragmentTransaction.replace(R.id.content,new HistoryFragment()).commit();
                    return true;
                case R.id.navigation_account:
                    fragmentTransaction.replace(R.id.content,new AccountFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,new DashboardFragment()).commit();


    }


}


