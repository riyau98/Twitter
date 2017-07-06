package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    String backgroundImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String screenName = getIntent().getStringExtra("screen_name");
        User otherUser = (User) Parcels.unwrap(getIntent().getParcelableExtra("other_user"));
        client = TwitterApp.getRestClient();
        if (otherUser == null) {
            client.verifyCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    User user = null;
                    try {
                        //create user fragment
                        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
                        //display user fragment inside container dynamiclly

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flContainer, userTimelineFragment);
                        ft.commit();
                        user = User.fromJSON(response);
                        getSupportActionBar().setTitle(user.getScreenName());
                        //populate user headline
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            //create user fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            //display user fragment inside container dynamiclly
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
            populateUserHeadline(otherUser);
        }

    }


    public void loadBackgroundImage() {
        ImageView ivBackgroundImage = (ImageView) findViewById(R.id.ivBackgroundImage);
        if (backgroundImageUrl != null) {
            Glide.with(this)
                    .load(backgroundImageUrl)
                    .into(ivBackgroundImage);
        }
    }


    public void getBackground(User user) {
        client.getProfileBanner(user.getUid(), user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    backgroundImageUrl = response.getJSONObject("sizes").getJSONObject("300x100").getString("url");
                    loadBackgroundImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("failed getting background", errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }


        });
    }


    public void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvName.setText(user.getName());
        getBackground(user);
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .into(ivProfileImage);

        tvTagline.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowers() + " Followers");
        tvFollowing.setText(user.getFollowing() + " Following");
        tvScreenName.setText("@" + user.getScreenName());

    }


}


