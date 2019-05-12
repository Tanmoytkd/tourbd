package com.example.tourbd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPost extends AppCompatActivity {
    EditText statusText;
    Button btnAddImage;
    Button btnPost;
    DatabaseReference db;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        statusText = findViewById(R.id.statusText);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnPost = findViewById(R.id.btnPost);

        db = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser()==null ? "": FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid.equals("")) finish();

        btnPost.setOnClickListener((v)->{
            String txt = statusText.getText().toString();
            String key = db.child("posts").child(uid).push().getKey();
            db.child("posts").child(uid).child(key).child("postText").setValue(txt).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            });
        });
    }
}
