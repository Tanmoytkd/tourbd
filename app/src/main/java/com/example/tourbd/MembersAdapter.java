package com.example.tourbd;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberHolder> {
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    private String uid;

    class MemberHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, emailTxt, phoneTxt;
        ImageButton btnDelete, btnAccept;
        View v;


        Context context;

        MemberHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            context = itemView.getContext();

            nameTxt = v.findViewById(R.id.name);
            emailTxt = v.findViewById(R.id.email);
            phoneTxt = v.findViewById(R.id.phoneNumber);
            btnAccept = v.findViewById(R.id.btnAccept);
            btnDelete = v.findViewById(R.id.btnDelete);
            Log.e("TKD", "all components ready");
        }

        void bind(Post post, User member) {
            if(!post.getOwnerUid().equals(uid)) {
                btnDelete.setVisibility(View.GONE);
                btnAccept.setVisibility(View.GONE);
            }

            btnAccept.setOnClickListener((view)->{
                db.child("members").child(post.ownerUid).child(post.postKey).child(member.uid).child("paymentStatus").setValue("Confirmed");
            });

            btnDelete.setOnClickListener((view)->{
                db.child("members").child(post.ownerUid).child(post.postKey).child(member.uid).setValue(null);
            });

            db.child("members").child(post.ownerUid).child(post.postKey).child(member.uid).child("paymentStatus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && !dataSnapshot.getValue(String.class).toLowerCase().equals("pending")) {
                        btnDelete.setVisibility(View.GONE);
                        btnAccept.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    
                }
            });

            nameTxt.setText(member.name);
            emailTxt.setText(member.email);
            phoneTxt.setText(member.phone);
        }
    }


    private Context context;
    private ArrayList<User> members;
    private Post post;

    public MembersAdapter(Context context, Post post, ArrayList<User> members) {
        this.context = context;
        this.post = post;
        this.members = members;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_member, viewGroup, false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder memberHolder, int i) {
        memberHolder.bind(post, members.get(i));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
