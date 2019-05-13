package com.example.tourbd;

import android.net.Uri;

public class Post {
    String postText;
    String postImageUrl;

    public Post() {
    }

    public Post(String postText) {
        this.postText = postText;
    }

    public Post(String postText, String postImageUrl) {
        this.postText = postText;
        this.postImageUrl = postImageUrl;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }
}
