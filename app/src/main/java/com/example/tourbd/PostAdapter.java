package com.example.tourbd;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    class PostHolder extends RecyclerView.ViewHolder {
        TextView postText;
        ImageView postImage;
        Button btnJoinOrLeave;
        View v;

        Context context;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            context = itemView.getContext();
            postText = itemView.findViewById(R.id.postText);
            postImage = itemView.findViewById(R.id.postImage);
            btnJoinOrLeave = itemView.findViewById(R.id.btnJoinOrLeave);
        }

        void bind(Post post) {
            postText.setText(post.postText);
            if (post.postImageUrl != null && !post.postImageUrl.equals("")) {
                Glide.with(context).load(post.postImageUrl).into(postImage);
            } else {
                Glide.with(context)
                        .load("https://k6u8v6y8.stackpathcdn.com/blog/wp-content/uploads/2016/03/Ravangla.jpg")
                        .placeholder(R.drawable.placeholder_place)
                        .into(postImage);
            }
        }
    }


    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
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
