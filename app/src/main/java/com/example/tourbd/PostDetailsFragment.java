package com.example.tourbd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

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

    ImageView postImage;
    TextView postText;
    TextView postDetails;
    Post post;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_post_details, null);
        postImage = v.findViewById(R.id.postImage);
        postText = v.findViewById(R.id.postText);
        postDetails = v.findViewById(R.id.postDetails);
        post = (Post) Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getSerializable("post");

        Glide.with(context).load(post.postImageUrl).placeholder(R.drawable.placeholder_place).into(postImage);
        postText.setText(post.postText);
        postDetails.setText(post.postDetails);

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
