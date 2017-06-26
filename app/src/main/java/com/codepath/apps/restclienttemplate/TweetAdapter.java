package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

/**
 * Created by ruppal on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    //pass the tweets array into the constructor

    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    //for each row, inflate layout and pass into viewholder class
    //only called when need to create a new row

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView=inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet currentTweet=mTweets.get(position);
        //populate the views according to the data from this specific tweet
        holder.tvUsername.setText(currentTweet.user.name);
        holder.tvBody.setText(currentTweet.body);
        //TODO-load the image using glide
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create Viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvBody;
        public TextView tvUsername;

        public ViewHolder(View itemView){
            super(itemView);
            //preform find view by id lookup
            ivProfileImage = (ImageView) itemView.findViewById(R.id. ivProfileImage);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);

        }
    }
}
