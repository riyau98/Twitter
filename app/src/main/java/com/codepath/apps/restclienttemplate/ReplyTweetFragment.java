package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;




public class ReplyTweetFragment extends DialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Tweet inResponseToTweet;
    EditText etReplyTweet;
    TwitterClient client;
    Context context;
    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private OnItemSelectedListener listener;


    public ReplyTweetFragment() {
        // Required empty public constructor
    }

    public static ReplyTweetFragment newInstance(Tweet tweet) {
        ReplyTweetFragment fragment = new ReplyTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable("responseToTweet", Parcels.wrap(tweet));
//        args.putParcelable("contextTimeline", Parcels.wrap(context));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inResponseToTweet = Parcels.unwrap(getArguments().getParcelable("responseToTweet"));
//        Context context = Parcels.unwrap(getArguments().getParcelable("contextTimeline"));
        context = getActivity();
        client=new TwitterClient(context);
        Log.i("fragment", "in on create. in response to tweet" + inResponseToTweet.toString());

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
                client.replyToTweet(etReplyTweet.getText().toString(), inResponseToTweet.uid, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = null;
                        try {
                            tweet = Tweet.fromJSON(response);
                            // Now we can fire the event when the user selects something in the fragment
                            listener.repliedToTweet(tweet);
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("fragment", errorResponse.toString());
                    }
                });

            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etReplyTweet = (EditText) view.findViewById(R.id.etReplyTweet);
        etReplyTweet.setText("@"+ inResponseToTweet.user.getScreenName().toString() + " ");
        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle("Reply");
        // Show soft keyboard automatically and request focus to field
        etReplyTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void repliedToTweet(Tweet tweet);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }
    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

}
