package com.example.tourbd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    private String uid;

    class NotificationHolder extends RecyclerView.ViewHolder {
        TextView notificationTxt;
        ImageView btnDelete;
        View v;


        Context context;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            context = itemView.getContext();

            notificationTxt = v.findViewById(R.id.notificationTxt);
            btnDelete = v.findViewById(R.id.btnDelete);
            Glide.with(context).load(R.drawable.ic_icons_cross).into(btnDelete);

            Log.e("TKD", "all components ready");
        }

        void bind(Notification notification) {
            notificationTxt.setText(notification.notificationTxt);
            btnDelete.setOnClickListener((v) -> {
                db.child("notifications").child(uid).child(notification.key).setValue(null);
            });
        }
    }


    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationsAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_notification, viewGroup, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i) {
        notificationHolder.bind(notifications.get(i));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
