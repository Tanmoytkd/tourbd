package com.example.tourbd;

import java.io.Serializable;

public class User implements Serializable {
    String name, phone;
    boolean isOwner, isAdmin;
    String uid, email, propic;

    public User() {
        propic = "https://firebasestorage.googleapis.com/v0/b/tourbd-7c475.appspot.com/o/ProfilePlaceholder.png?alt=media&token=297c0995-ca58-414e-a95a-97d649155cbb";
    }

    public User(String name, String phone, boolean isOwner, boolean isAdmin, String uid, String email) {
        this.name = name;
        this.phone = phone;
        this.isOwner = isOwner;
        this.isAdmin = isAdmin;
        this.uid = uid;
        this.email = email;
    }

    public User(String name, String phone, boolean isOwner, boolean isAdmin, String uid, String email, String propic) {
        this.name = name;
        this.phone = phone;
        this.isOwner = isOwner;
        this.isAdmin = isAdmin;
        this.uid = uid;
        this.email = email;
        this.propic = propic;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
