package com.example.tourbd;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AddPost extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    EditText statusText;
    EditText statusDescription;
    Button btnAddImage;
    Button btnPost;
    DatabaseReference db;
    String uid;
    Uri file=null;
    private StorageReference mStorageRef;
    public static final int PICK_IMAGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        statusText = findViewById(R.id.statusText);
        statusDescription = findViewById(R.id.statusDescription);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnPost = findViewById(R.id.btnPost);

        mStorageRef = FirebaseStorage.getInstance().getReference();



        db = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser()==null ? "": FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid.equals("")) finish();

        btnAddImage.setOnClickListener((v)->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        btnPost.setOnClickListener((v)->{
            String txt = statusText.getText().toString();
            String description = statusDescription.getText().toString();

            if(file!=null) {
                StorageReference riversRef = mStorageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("TKD", String.valueOf(taskSnapshot.getBytesTransferred()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TKD", e.toString());
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.e("TKD", "Upload Complete");
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("TKD", uri.toString());

                                String key = db.child("posts").child(uid).push().getKey();
                                assert key != null;
                                db.child("posts").child(uid).child(key).child("postImageUrl").setValue(uri.toString());
                                db.child("posts").child(uid).child(key).child("postDetails").setValue(description);
                                db.child("posts").child(uid).child(key).child("postText").setValue(txt).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TKD", e.toString());
                            }
                        });
                    }
                });



                // Register observers to listen for when the add_icon is done or if it fails
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        Log.e("TKD", exception.toString());
//                        exception.printStackTrace();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                        // ...
//                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
////                                String key = db.child("posts").child(uid).push().getKey();
////                                db.child("posts").child(uid).child(key).child("postImageUrl").setValue(uri);
////                                db.child("posts").child(uid).child(key).child("postText").setValue(txt).addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
////                                        finish();
////                                    }
////                                });
//
//                                Log.e("TKD", uri.toString());
//                            }
//                        });
//                    }
//                });


            } else {
                String key = db.child("posts").child(uid).push().getKey();
                db.child("posts").child(uid).child(key).child("postText").setValue(txt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            file = data.getData();
        }
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
            Intent I=new Intent(AddPost.this,ActivityLogin.class);
            startActivity(I);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
