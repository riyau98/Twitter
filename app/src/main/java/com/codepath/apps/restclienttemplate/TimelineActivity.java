package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public static final int NEW_TWEET_REQUEST_CODE = 20;



    //steps to create a scrolling thing:
    //recycler view. Set up the recycler view including layout manager and use the adapter created in the itemview.
        //itemView. Create an adapter that connects the itemview.xml to the data passed in. this happens in tweetadapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client=TwitterApp.getRestClient();

        //find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets);
        //set up the recycler view (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
        //populate the array list
        populateTimeline();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    private void populateTimeline(){
         client.getHomeTimeline(new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 //Log.d("TwitterClient", response.toString());
                 //iterate through the response array
                 for (int i=0; i<response.length(); i++) {
                     //for each object, deserialize the JSON
                     Tweet currTweet = null;
                     try {
                         currTweet = Tweet.fromJSON(response.getJSONObject(i));
                         //and add it to to the tweets list
                         tweets.add(currTweet);
                         //notify the adapter that we have added an item
                         tweetAdapter.notifyItemInserted(tweets.size() - 1);
                     }
                     catch (JSONException e){
                         e.printStackTrace();
                     }
                 }
             }

             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 Log.d("TwitterClient", response.toString());
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 Log.d("TwitterClient", responseString);
                 throwable.printStackTrace();
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 Log.d("TwitterClient", errorResponse.toString());
                 throwable.printStackTrace();
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                 Log.d("TwitterClient", errorResponse.toString());
                 throwable.printStackTrace();
             }
         });
    }

    public void composeNewTweet(MenuItem mi){
        Log.i("composeNewTweet", "here");
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, NEW_TWEET_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TWEET_REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet newTweet = Parcels.unwrap(data.getParcelableExtra("newTweet"));
            tweets.add(0, newTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }

    }
}
