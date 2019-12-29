package com.example.tourbd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    RecyclerView postsList;
    SearchView searchView;
    ArrayList<Post> posts = new ArrayList<>();
    Context context;
    String uid;
    String searchQuery="";
    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchQuery = s;
                postsList.setLayoutManager(new LinearLayoutManager(context));
                postsList.setAdapter(new PostAdapter(context, posts, searchQuery));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchQuery = s;
                postsList.setLayoutManager(new LinearLayoutManager(context));
                postsList.setAdapter(new PostAdapter(context, posts, searchQuery));
                return true;
            }
        });

        db.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts = new ArrayList<>();
                for(DataSnapshot userBasedPosts: dataSnapshot.getChildren()) {
                    String userUid = userBasedPosts.getKey();
                    for(DataSnapshot postDataSnapshot: userBasedPosts.getChildren()) {
                        String key = postDataSnapshot.getKey();
                        posts.add(postDataSnapshot.getValue(Post.class));
                    }
                }
                postsList.setLayoutManager(new LinearLayoutManager(context));
                postsList.setAdapter(new PostAdapter(context, posts, searchQuery));
//                Log.e("TKD", "QUERY: " + searchQuery);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, null);


        ///////////////////////////////////////////
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        postsList = v.findViewById(R.id.postsList);
        searchView = v.findViewById(R.id.searchView);
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


        String uid = FirebaseAuth.getInstance().getCurrentUser() == null ? "" : FirebaseAuth.getInstance().getCurrentUser().getUid();
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
