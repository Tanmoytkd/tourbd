package com.example.tourbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    Button btnAddPackage;
    RecyclerView postsList;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        postsList = findViewById(R.id.postsList);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnAddPackage = (Button)findViewById(R.id.btnAddPackage);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser()==null) {
            finish();
        }

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        //db.child("posts").child(uid).child(key).child("postText").setValue(txt);
    }
}
