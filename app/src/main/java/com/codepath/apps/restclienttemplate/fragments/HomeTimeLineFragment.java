package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.ReplyTweetFragment;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ruppal on 7/3/17.
 */

public class HomeTimeLineFragment extends TweetsListFragment implements ReplyTweetFragment.OnItemSelectedListener{
    private TwitterClient client;
    private ReplyTweetFragment replyTweetFragment;
    public static final int NEW_TWEET_REQUEST_CODE = 20;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client= TwitterApp.getRestClient();
        //populate the array list
        populateTimeline();
    }


    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.i("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void populateList(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("failed to refresh", throwable.toString());
            }
        });
    }

    public void openFragment(Tweet tweet) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
        replyTweetFragment = ReplyTweetFragment.newInstance(tweet);

//        replyTweetFragment.show(fm, "fragment_edit_name");

        replyTweetFragment.setTargetFragment(this, NEW_TWEET_REQUEST_CODE);
        replyTweetFragment.show( getActivity().getSupportFragmentManager(), "reply");
    }

    public void composeNewTweet(MenuItem mi){
        Log.i("composeNewTweet", "here");
        Intent i = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(i, NEW_TWEET_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TWEET_REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet newTweet = Parcels.unwrap(data.getParcelableExtra("newTweet"));
            addNewTweet(newTweet);
        }
    }

    @Override
    public void repliedToTweet(Tweet newTweet) {
        if (replyTweetFragment != null) {
            addNewTweet(newTweet);
        }
    }

    public void addNewTweet(Tweet newTweet){
        tweets.add(0, newTweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }



}

