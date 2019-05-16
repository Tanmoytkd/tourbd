package com.example.tourbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivityNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton floatingBtnAddPackage;
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    Button btnAddPackage;
    RecyclerView postsList;
    ArrayList<Post> posts=new ArrayList<>();
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        floatingBtnAddPackage = findViewById(R.id.floatingBtnAddPackage);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ///////////////////////////////////////////
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        postsList = findViewById(R.id.postsList);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnAddPackage = findViewById(R.id.btnAddPackage);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I=new Intent(UserActivityNew.this,ActivityLogin.class);
                startActivity(I);
                finish();
            }
        });

        View.OnClickListener addPackageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivityNew.this, AddPost.class);
                startActivity(i);
            }
        };
        btnAddPackage.setOnClickListener(addPackageListener);
        floatingBtnAddPackage.setOnClickListener(addPackageListener);

        String uid = FirebaseAuth.getInstance().getCurrentUser()==null ? "": FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.child("users").child(uid).child("isOwner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) btnAddPackage.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ///////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser()==null) {
            finish();
        }

        db.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts = new ArrayList<>();
                for(DataSnapshot userBasedPosts: dataSnapshot.getChildren()) {
                    for(DataSnapshot postDataSnapshot: userBasedPosts.getChildren()) {
                        posts.add(postDataSnapshot.getValue(Post.class));
                    }
                }
                postsList.setLayoutManager(new LinearLayoutManager(UserActivityNew.this));
                postsList.setAdapter(new PostAdapter(UserActivityNew.this, posts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_activity_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_us) {
            // Handle the camera action
        } else if (id == R.id.nav_share_app) {

        } else if (id == R.id.nav_rate_us) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent I=new Intent(UserActivityNew.this,ActivityLogin.class);
            startActivity(I);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
