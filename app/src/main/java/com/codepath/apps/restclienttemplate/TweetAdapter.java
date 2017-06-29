package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by ruppal on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;
    TimelineActivity currentActivity;
    //pass the tweets array into the constructor

    public TweetAdapter(List<Tweet> tweets, TimelineActivity currentActivity){
        mTweets = tweets;
        this.currentActivity = currentActivity;
    }

    //for each row, inflate layout and pass into viewholder class
    //only called when need to create a new row

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView=inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        final Tweet currentTweet=mTweets.get(position);
        //populate the views according to the data from this specific tweet
        holder.tvUsername.setText(currentTweet.user.name);
        holder.tvBody.setText(currentTweet.body);
        //TODO-add a placeholder picture while image is loading
        int radius = 8; // corner radius, higher value = more rounded
        int margin = 3; // crop margin, set to 0 for corners with no crop
        Glide.with(context)
                .load(currentTweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, margin))
                .into(holder.ivProfileImage);
        holder.tvScreenName.setText(String.format("@%s", currentTweet.user.screenName));
        String relativeTimeAgo=getRelativeTimeAgo(currentTweet.createdAt);
        holder.tvRelativeTimestamp.setText(relativeTimeAgo);
        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fragment", "clicked to open fragment");
                currentActivity.openFragment(currentTweet);
            }
        });

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
        public TextView tvScreenName;
        public TextView tvRelativeTimestamp;
        public ImageButton ibReply;

        public ViewHolder(View itemView){
            super(itemView);
            //preform find view by id lookup
            ivProfileImage = (ImageView) itemView.findViewById(R.id. ivProfileImage);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvNameCompose);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = (TextView) itemView.findViewById(R.id.tvRelativeTimestamp);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }




}
