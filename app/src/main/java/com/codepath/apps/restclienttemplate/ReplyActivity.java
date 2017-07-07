package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ReplyActivity extends AppCompatActivity {
    public static final String RESPONSE_TO_TWEET = "responseToTweet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
//        inResponseToTweet = Parcels.unwrap(getArguments().getParcelable(RESPONSE_TO_TWEET));
////        Context context = Parcels.unwrap(getArguments().getParcelable("contextTimeline"));
//        client=TwitterApp.getRestClient();

    }
}
