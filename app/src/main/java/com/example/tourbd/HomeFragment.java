package com.example.tourbd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    RecyclerView postsList;
    SearchView searchView;
    ArrayList<Post> posts = new ArrayList<>();
    Context context;
    String uid;
    String searchQuery="";
    View v;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        searchView.clearFocus();

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

//        hideKeyboard(getActivity());
        hideKeyboardFrom(context, v);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, null);


        ///////////////////////////////////////////
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        postsList = v.findViewById(R.id.postsList);
        searchView = v.findViewById(R.id.searchView);
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        searchView.setFocusableInTouchMode(true);


        String uid = FirebaseAuth.getInstance().getCurrentUser() == null ? "" : FirebaseAuth.getInstance().getCurrentUser().getUid();
        hideKeyboardFrom(context, v);
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
