package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by ruppal on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {

    public interface TweetSelectedListener{
        public void onTweetSelected (Tweet tweet);
    }
    public TweetAdapter tweetAdapter;
    public ArrayList<Tweet> tweets;
    public RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        //find the recycler view
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets, getContext(), this);
        //set up the recycler view (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTweets.setAdapter(tweetAdapter);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync();
            }
        });
        //set up swipe refresh
        //todo fix refresh for profile. old tweets show up on refresh instead of new ones
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
//                new ItemClickSupport.OnItemClickListener() {
//                    @Override
//                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                        Tweet tweet = tweets.get(position);
//                        Toast.makeText(getContext(), tweet.body, Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
        return v;
    }

    public void addItems(JSONArray response){
        // Remember to CLEAR OUT old items before appending in the new ones
        tweetAdapter.clear();
        // ...the data has come back, add new items to your adapter...
        for (int i=0; i<response.length(); i++) {
            //for each object, deserialize the JSON
            Tweet currTweet = null;
            try {
                currTweet = Tweet.fromJSON(response.getJSONObject(i));
                //and add it to to the tweets list
                tweets.add(currTweet);
                //notify the adapter that we have added an item
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tweetAdapter.addAll(tweets);
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

    }

    public void fetchTimelineAsync() {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        populateList();
    }

    public void populateList(){

    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
    }

}
