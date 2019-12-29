package com.example.tourbd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PostDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    ImageButton btnDelete;
    ImageView postImage;
    TextView postText;
    TextView postDetails;
    RecyclerView memberList;
    Post post;
    Context context;
    Button btnGoing, btnNotGoing;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uid = firebaseAuth.getUid();
    ArrayList<User> members;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_details, null);
        postImage = v.findViewById(R.id.postImage);
        postText = v.findViewById(R.id.postText);
        postDetails = v.findViewById(R.id.postDetails);
        post = (Post) Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getSerializable("post");
        btnGoing = v.findViewById(R.id.btnGoing);
        btnNotGoing = v.findViewById(R.id.btnNotGoing);
        btnDelete = v.findViewById(R.id.btnDelete);
        memberList = v.findViewById(R.id.membersList);
        memberList.setLayoutManager(new LinearLayoutManager(context));

        if(!post.ownerUid.equals(uid)) {
            btnDelete.setVisibility(View.GONE);
        }

        btnDelete.setOnClickListener((view)->{
            db.child("posts").child(post.ownerUid).child(post.postKey).setValue(null);
            ((UserActivityBottomNav) context).onBackPressed();
        });

        db.child("members").child(post.ownerUid).child(post.postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                members = new ArrayList<>();
                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    User user = userData.getValue(User.class);
                    members.add(user);
                }
                Log.e("TKD", String.valueOf(members.size()));
                memberList.setAdapter(new MembersAdapter(context, post, members));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Glide.with(context).load(post.postImageUrl).placeholder(R.drawable.placeholder_place).into(postImage);
        postText.setText(post.postText);
        postDetails.setText(post.postDetails);

        btnGoing.setOnClickListener((view) -> {
            db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    db.child("members").child(post.ownerUid).child(post.postKey).child(uid).setValue(user);
                    db.child("members").child(post.ownerUid).child(post.postKey).child(uid).child("paymentStatus").setValue("Pending");

                    assert user != null;
                    String key = post.postKey+"~"+user.uid;
                    Notification notification = new Notification(user.name + " wants to join the event '" + post.postText + "' ", key);
                    db.child("notifications").child(post.ownerUid).child(key).setValue(notification);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });

        btnNotGoing.setOnClickListener((view) -> {
            db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    db.child("members").child(post.ownerUid).child(post.postKey).child(uid).setValue(null);
                    assert user != null;
                    String key = post.postKey+"~"+user.uid;
                    db.child("notifications").child(post.ownerUid).child(key).setValue(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
