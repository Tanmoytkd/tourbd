package com.example.tourbd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Post implements Serializable {
    String postText;
    String postImageUrl;
    String postDetails;
    String ownerUid;
    String postKey;

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

    public Post(String postText, String postImageUrl, String postDetails, String ownerUid, String postKey) {
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postDetails = postDetails;
        this.ownerUid = ownerUid;
        this.postKey = postKey;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
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
