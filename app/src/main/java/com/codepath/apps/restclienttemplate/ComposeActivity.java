package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class ComposeActivity extends AppCompatActivity {
    EditText etNewTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etNewTweet = (EditText) findViewById(R.id.etNewTweet);
        }
   public void onPostTweet(View v){
       //TODO start here
        postTweet(etNewTweet.getText().toString(),)
   }
}
