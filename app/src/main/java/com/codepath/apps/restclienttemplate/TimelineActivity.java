package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;


public class TimelineActivity extends AppCompatActivity {





    //steps to create a scrolling thing:
    //recycler view. Set up the recycler view including layout manager and use the adapter created in the itemview.
        //itemView. Create an adapter that connects the itemview.xml to the data passed in. this happens in tweetadapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
      //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        //set up tab layout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //todo make sure you pass in screen name
        //launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

//    public void openFragment(Tweet tweet) {
//        FragmentManager fm = getSupportFragmentManager();
//        replyTweetFragment = ReplyTweetFragment.newInstance(tweet);
//        replyTweetFragment.show(fm, "fragment_edit_name");
//    }
//
//    public void composeNewTweet(MenuItem mi){
//        Log.i("composeNewTweet", "here");
//        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//        startActivityForResult(i, NEW_TWEET_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == NEW_TWEET_REQUEST_CODE && resultCode == RESULT_OK) {
//            Tweet newTweet = Parcels.unwrap(data.getParcelableExtra("newTweet"));
//            addNewTweet(newTweet);
//        }
//    }
//    @Override
//    public void repliedToTweet(Tweet newTweet) {
//        if (replyTweetFragment != null) {
//            addNewTweet(newTweet);
//        }
//    }
//
//    public void addNewTweet(Tweet newTweet){
//        fragmentTweetsList.tweets.add(0, newTweet);
//        fragmentTweetsList.tweetAdapter.notifyItemInserted(0);
//        fragmentTweetsList.rvTweets.scrollToPosition(0);
//    }
//


}


