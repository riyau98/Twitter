package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {
    EditText etNewTweet;
    TwitterClient client;
    TextView tvNameCompose;
    TextView tvScreenNameCompose;
    ImageView ivComposePic;
    User currentUser;
    TextView tvCharsRemaining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etNewTweet = (EditText) findViewById(R.id.etReplyTweet);
        client = new TwitterClient(this);
        tvNameCompose = (TextView) findViewById(R.id.tvNameCompose);
        tvScreenNameCompose = (TextView) findViewById(R.id.tvScreenNameCompose);
        ivComposePic = (ImageView) findViewById(R.id.ivComposePic);
        tvCharsRemaining = (TextView) findViewById(R.id.tvCharsRemaining);
        //character count
        etNewTweet.addTextChangedListener(mTextEditorWatcher);
        //set the user's name, text, and pro pic on compose page
        renderInformation();

    }
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharsRemaining.setText(String.valueOf(140 - s.length()) + " characters remaining");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void renderInformation(){
        client.verifyCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    currentUser = User.fromJSON(response);
                    tvNameCompose.setText(currentUser.getName());
                    tvScreenNameCompose.setText(currentUser.getScreenName());
                    int radius = 8; // corner radius, higher value = more rounded
                    int margin = 3; // crop margin, set to 0 for corners with no crop
                    Glide.with(ComposeActivity.this)
                            .load(currentUser.getProfileImageUrl())
                            .bitmapTransform(new RoundedCornersTransformation(ComposeActivity.this, radius, margin))
                            .into(ivComposePic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("getUserFailure", errorResponse.toString());
            }
        });

    }


   public void onPostTweet(View v){
        client.postTweet(etNewTweet.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    i.putExtra("newTweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", errorResponse.toString());
            }
        });
   }
}
