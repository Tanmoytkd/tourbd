package com.example.tourbd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    public EditText emailId, passwd, name, phone;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    Switch ownerSwitch;
    private FirebaseAuth.AuthStateListener authStateListener;
    boolean owner = false;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(RegisterActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(RegisterActivity.this, UserActivityBottomNav.class);
                    startActivity(I);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

        setContentView(R.layout.activity_register);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.ETname);
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.TVSignIn);
        phone = findViewById(R.id.ETphone);
        ownerSwitch = findViewById(R.id.ownerSwitch);
        ownerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                owner = isChecked;
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                String username = name.getText().toString();
                String phoneNumber = phone.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                User user = new User(username, phoneNumber, owner, false, Objects.requireNonNull(task.getResult()).getUser().getUid(), emailID);
                                db.child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user);
//                                if(owner) {
//                                    db.child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("isOwner").setValue("alkfjasklfj");
//                                }
//                                db.child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("nameTxt").setValue(username);
//                                db.child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("phoneTxt").setValue(phoneNumber);
                                startActivity(new Intent(RegisterActivity.this, UserActivityBottomNav.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(RegisterActivity.this, ActivityLogin.class);
                startActivity(I);
                finish();
            }
        });
    }
}