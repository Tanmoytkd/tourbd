package com.example.tourbd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyEventsFragment extends Fragment {
    FloatingActionButton floatingBtnAddPackage;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    RecyclerView postsList;
    ArrayList<Post> posts = new ArrayList<>();
    Context context;
    String uid;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, null);

        floatingBtnAddPackage = v.findViewById(R.id.floatingBtnAddPackage);

        ///////////////////////////////////////////
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        postsList = v.findViewById(R.id.postsList);
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        floatingBtnAddPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddPost.class);
                startActivity(i);
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser() == null ? "" : FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.isOwner) {
                    floatingBtnAddPackage.hide();
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
