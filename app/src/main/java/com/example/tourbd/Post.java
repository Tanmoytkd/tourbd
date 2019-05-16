package com.example.tourbd;

public class Post {
    String postText;
    String postImageUrl;
    String postDetails;

    public Post() {
    }

    public Post(String postText) {
        this.postText = postText;
    }

    public Post(String postText, String postImageUrl, String postDetails) {
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postDetails = postDetails;
    }

    public String getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(String postDetails) {
        this.postDetails = postDetails;
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
