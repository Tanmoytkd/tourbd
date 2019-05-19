package com.example.tourbd;

import android.widget.TextView;

import java.io.Serializable;

public class Notification implements Serializable {
    public String notificationTxt;
    public String key;

    public Notification(String notificationTxt, String key) {
        this.notificationTxt = notificationTxt;
        this.key = key;
    }

    public Notification() {
    }

    public String getNotificationTxt() {
        return notificationTxt;
    }

    public void setNotificationTxt(String notificationTxt) {
        this.notificationTxt = notificationTxt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
