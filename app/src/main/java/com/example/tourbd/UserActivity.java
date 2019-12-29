package com.example.tourbd;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    Button btnAddPackage;
    RecyclerView postsList;
    ArrayList<Post> posts=new ArrayList<>();
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(UserActivity.this, UserActivityNew.class));
        finish();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        postsList = findViewById(R.id.postsList);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnAddPackage = findViewById(R.id.btnAddPackage);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I=new Intent(UserActivity.this,ActivityLogin.class);
                startActivity(I);
                finish();
            }
        });

        btnAddPackage.setOnClickListener((v)->{
            Intent i = new Intent(UserActivity.this, AddPost.class);
            startActivity(i);
        });

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
                postsList.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                postsList.setAdapter(new PostAdapter(UserActivity.this, posts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
