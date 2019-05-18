package com.example.tourbd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class UserActivityBottomNav extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, PostDetailsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    Fragment fragment = null;
    FrameLayout fragmentContainer;
    Fragment lastFragment = null;
    Boolean isDetails = false;

    NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // Handle navigation view item clicks here.
            int id = menuItem.getItemId();

            if (id == R.id.nav_about_us) {
                // Handle the camera action
            } else if (id == R.id.nav_share_app) {

            } else if (id == R.id.nav_rate_us) {

            } else if (id == R.id.nav_profile) {

            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(UserActivityBottomNav.this, ActivityLogin.class);
                startActivity(I);
                finish();
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            if(!(fragment instanceof PostDetailsFragment)) {
                lastFragment = fragment;
                isDetails = false;
            } else {
                isDetails = true;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("UserActivityBottomNav", "popping backstack");
            fm.popBackStack();
        } else if(lastFragment!=null) {
            Log.e( "TKD", "loading new stuff");

            if(lastFragment instanceof MyEventsFragment && isDetails) {
                Log.e("TKD", "LOADING MY EVENTS");
                loadFragment(new MyEventsFragment());
            } else if(lastFragment instanceof HomeFragment && isDetails){
                loadFragment(new HomeFragment());
            } else if(lastFragment instanceof SearchFragment && isDetails) {
                loadFragment(new SearchFragment());
            } else {
                Log.i("UserActivityBottomNav", "nothing on backstack, calling super");
                super.onBackPressed();
            }
        } else {
            Log.i("UserActivityBottomNav", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bottom_nav_drawer);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fragmentContainer = findViewById(R.id.fragment_container);
        navView.setOnNavigationItemSelectedListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);


        if(lastFragment==null || lastFragment instanceof HomeFragment) {
            //loading the default fragment
            loadFragment(new HomeFragment());
        } else if(lastFragment instanceof MyEventsFragment) {
            loadFragment(new MyEventsFragment());
        } else if(lastFragment instanceof NotificationsFragment) {
            loadFragment(new NotificationsFragment());
        } else if(lastFragment instanceof ProfileFragment) {
            loadFragment(new ProfileFragment());
        } else {
            loadFragment(new HomeFragment());
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_my_events:
                fragment = new MyEventsFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
