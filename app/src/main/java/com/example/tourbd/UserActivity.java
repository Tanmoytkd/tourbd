package com.example.tourbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I=new Intent(UserActivity.this,ActivityLogin.class);
                startActivity(I);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser()==null) {
            finish();
        }
    }
}
