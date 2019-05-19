package com.example.tourbd;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    private String uid;

    class PostHolder extends RecyclerView.ViewHolder {
        TextView postText;
        ImageView postImage;
        Button btnDetails;
        ImageButton btnDelete;

        View v;


        Context context;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            context = itemView.getContext();
            postText = itemView.findViewById(R.id.postDetails);
            postImage = itemView.findViewById(R.id.postImage);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Post post) {
            postText.setText(post.postText);
            if (post.postImageUrl != null && !post.postImageUrl.equals("")) {
                Glide.with(context).load(post.postImageUrl).placeholder(R.drawable.placeholder_place).into(postImage);
            } else {
                Glide.with(context)
                        .load("https://k6u8v6y8.stackpathcdn.com/blog/wp-content/uploads/2016/03/Ravangla.jpg")
                        .placeholder(R.drawable.placeholder_place)
                        .into(postImage);
            }

            db.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if(user.isAdmin) {
                        Log.e("TKD", "You are an admin");
                        btnDelete.setVisibility(View.VISIBLE);
                        btnDelete.setOnClickListener((v)->{
                            db.child("posts").child(post.ownerUid).child(post.postKey).setValue(null);
                        });
                    } else if(!uid.equals(post.ownerUid)) {
                        btnDelete.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(uid.equals(post.ownerUid)) {
                Log.e("TKD", "you are the owner");
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener((v)->{
                    db.child("posts").child(post.ownerUid).child(post.postKey).setValue(null);
                });
            } else {
                btnDelete.setVisibility(View.GONE);
            }

            btnDetails.setOnClickListener((v)->{
                UserActivityBottomNav activity = (UserActivityBottomNav) context;
                activity.getIntent().putExtra("post", post);
                activity.loadFragment(new PostDetailsFragment());
            });
        }
    }


    private Context context;
    private ArrayList<Post> posts;
    String searchQuery="";

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getUid();
    }

    public PostAdapter(Context context, ArrayList<Post> posts, String searchQuery) {
        this.context = context;
        this.searchQuery = searchQuery;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getUid();

        ArrayList<Post> tempPosts = new ArrayList<>();
        for(Post post: posts) {
            if(post.postText.toLowerCase().contains(searchQuery.toLowerCase()) /* || post.postDetails.toLowerCase().contains(searchQuery.toLowerCase()) */ ) {
                Log.e("TKD", "QUERY: " + searchQuery);
                Log.e("TKD", "EVENT TITLE: " + post.postText);
                Log.e("TKD", "Event Details: " + post.postDetails);
                tempPosts.add(post);
            }
        }
        this.posts = tempPosts;

        Log.e("TKD", "QUERY: " + searchQuery);
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_post, viewGroup, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder postHolder, int i) {
        postHolder.bind(posts.get(i));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
