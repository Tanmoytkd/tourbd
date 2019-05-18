package com.example.tourbd;

import java.io.Serializable;

public class User implements Serializable {
    String name, phone;
    boolean isOwner, isAdmin;
    String uid, email;

    public User() {
    }

    public User(String name, String phone, boolean isOwner, boolean isAdmin, String uid, String email) {
        this.name = name;
        this.phone = phone;
        this.isOwner = isOwner;
        this.isAdmin = isAdmin;
        this.uid = uid;
        this.email = email;
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
