package com.example.tourbd;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MyEventsFragment extends Fragment {
    FloatingActionButton floatingBtnAddPackage;
    FirebaseAuth firebaseAuth;
    RecyclerView postsList;
    ArrayList<Post> posts = new ArrayList<>();
    Context context;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1234;
    public static final int MY_PERMISSIONS_REQUEST_INTERNET = 5678;
    EditText statusText;
    EditText statusDescription;
    Button btnAddImage;
    Button btnPost;
    DatabaseReference db;
    String uid;
    Uri file = null;
    private StorageReference mStorageRef;
    public static final int PICK_IMAGE = 1111;
    AlertDialog dialog;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
        }

        db.child("posts").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts = new ArrayList<>();
                for (DataSnapshot myPostSnapshot : dataSnapshot.getChildren()) {
                    posts.add(myPostSnapshot.getValue(Post.class));
                    posts.get(posts.size()-1).setOwnerUid(uid);
                    posts.get(posts.size()-1).setPostKey(myPostSnapshot.getKey());
                }
                postsList.setLayoutManager(new LinearLayoutManager(context));
                postsList.setAdapter(new PostAdapter(context, posts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE) {
            file = data.getData();
            Log.e("TKD", file.getLastPathSegment());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, null);

        floatingBtnAddPackage = v.findViewById(R.id.floatingBtnAddPackage);

        ///////////////////////////////////////////
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        postsList = v.findViewById(R.id.postsList);
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

//        floatingBtnAddPackage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, AddPost.class);
//                startActivity(i);
//            }
//        });


        floatingBtnAddPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                View myview = inflater.inflate(R.layout.modal_add_post,null);

                myDialog.setView(myview);
                dialog = myDialog.create();

                ////////////////////////////////////////
                statusText = myview.findViewById(R.id.statusText);
                statusDescription = myview.findViewById(R.id.statusDescription);
                btnAddImage = myview.findViewById(R.id.btnAddImage);
                btnPost = myview.findViewById(R.id.btnPost);

                mStorageRef = FirebaseStorage.getInstance().getReference();

                askForPermission();

                db = FirebaseDatabase.getInstance().getReference();
                uid = FirebaseAuth.getInstance().getCurrentUser() == null ? "" : FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (uid.equals("") && getActivity()!=null) {
                    getActivity().finish();
                }

                btnAddImage.setOnClickListener((v2) -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                });

                btnPost.setOnClickListener((v2) -> {
                    String txt = statusText.getText().toString();
                    String description = statusDescription.getText().toString();

                    if (file != null) {
                        StorageReference riversRef = mStorageRef.child("images/" + file.getLastPathSegment());
                        UploadTask uploadTask = riversRef.putFile(file);

                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("TKD", "Uploaded: " + String.valueOf(taskSnapshot.getBytesTransferred()));
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
                                        Post post = new Post(txt, uri.toString(), description, uid, key);

                                        db.child("posts").child(uid).child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.dismiss();
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
                    } else {
                        String key = db.child("posts").child(uid).push().getKey();
                        Post post = new Post(txt, null, description, uid, key);
                        db.child("posts").child(uid).child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                ////////////////////////////////////////
                dialog.show();
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser() == null ? "" : FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TKD", "UID: "+uid);
                User user = dataSnapshot.getValue(User.class);
                if(!user.isOwner && !user.isAdmin) {
                    floatingBtnAddPackage.hide();
                } else {
                    floatingBtnAddPackage.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        db.child("users").child(uid).child("isOwner").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return v;
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale( (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions( (Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            } else {
                // Permission has already been granted
            }

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.INTERNET)) {
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.INTERNET},
                            MY_PERMISSIONS_REQUEST_INTERNET);
                }
            } else {
                // Permission has already been granted
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
