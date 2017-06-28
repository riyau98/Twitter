package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReplyTweetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReplyTweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReplyTweetFragment extends DialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    EditText etReplyTweet;


    public ReplyTweetFragment() {
        // Required empty public constructor
    }

    public static ReplyTweetFragment newInstance() {
        ReplyTweetFragment fragment = new ReplyTweetFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment", "in on create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reply_tweet, container, false);
        Button btReply= (Button) view.findViewById(R.id.btReply);
        btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fragment", "clicked reply");
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etReplyTweet = (EditText) view.findViewById(R.id.etReplyTweet);
        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle("Reply");
        // Show soft keyboard automatically and request focus to field
        etReplyTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }



    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


}
