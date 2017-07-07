package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
    public static final String TWEET = "TWEET";
    public ArrayList<Tweet> replyTweets;
    TwitterClient client;
    public RecyclerView rvTweets;
    public TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        client = TwitterApp.getRestClient();
        replyTweets = new ArrayList<>();
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra(TWEET));
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        ImageView ivMedia = (ImageView) findViewById(R.id.ivMedia);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        TextView tvNumRetweets = (TextView) findViewById(R.id.tvNumRetweets);
        TextView tvNumFavorites = (TextView) findViewById(R.id.tvNumFavorites);
        ImageView ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
        ImageView ivRetweet = (ImageView) findViewById(R.id.ivRetweet);

        tvName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvDate.setText(tweet.getCreatedAt());
        tvNumFavorites.setText(tweet.getNumFavorites() + " Favorites");
        tvNumRetweets.setText(tweet.getNumRetweets() + " Retweets");
        int profile_radius = 5;
        int profile_margin = 0;
        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(this, profile_radius, profile_margin))
                .into(ivProfileImage);
        if (tweet.getMediaUrl() != null){
            int media_radius = 10;
            int media_margin = 5;
            Glide.with(this)
                    .load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(this, media_radius, media_margin))
                    .into(ivMedia);
        }
        else {
            ivMedia.getLayoutParams().width = 0;
            ivMedia.getLayoutParams().height = 0;
        }
        if (tweet.isFavorited()){
            ivFavorite.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_vector_heart));
            ivFavorite.setColorFilter(ContextCompat.getColor(this,R.color.inline_action_like));
        }
        if (tweet.isRetweeted()){
            ivRetweet.setColorFilter(ContextCompat.getColor(this,R.color.inline_action_like));
        }
        getReplyTweets(tweet);



    }


    public String formatDate (String createdAt){
        SimpleDateFormat formatterMon;
        formatterMon= new SimpleDateFormat("dd MMM yy");
        String resultMon = formatterMon.format(createdAt);

        SimpleDateFormat formatterTime;
        formatterTime= new SimpleDateFormat("h:mm a");
        String resultTime = formatterTime.format(createdAt);

        return resultTime + " " + resultMon;

    }

    public void getReplyTweets(final Tweet tweet){
        client.searchTweet(tweet.user.getScreenName(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray statuses = null;
                try {
                    statuses = response.getJSONArray("statuses");
                    for (int i = 0; i< statuses.length(); i++){
                        JSONObject object= statuses.getJSONObject(i);
                        String inResponseString = object.getString("in_reply_to_status_id");
                        if (inResponseString != null && inResponseString == Long.toString(tweet.uid)) {
                                replyTweets.add(0, Tweet.fromJSON(object));
                                rvTweets.notify();
                        }

                    }
                    setUpRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
    public void setUpRecyclerView(){
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweetAdapter = new TweetAdapter(replyTweets, this, null);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
    }
}
