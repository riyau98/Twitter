package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String screenName = getIntent().getStringExtra("screen_name");
        User otherUser = (User) Parcels.unwrap(getIntent().getParcelableExtra("other_user"));
        client=TwitterApp.getRestClient();
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
        }
        else{
            //create user fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            //display user fragment inside container dynamiclly

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
            populateUserHeadline(otherUser);
        }

    }

    public void populateUserHeadline(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .into(ivProfileImage);
        tvTagline.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowers() + " Followers");
        tvFollowing.setText(user.getFollowing() + " Following");

    }
}
