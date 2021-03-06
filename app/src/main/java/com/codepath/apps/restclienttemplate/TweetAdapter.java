package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
import com.codepath.apps.restclienttemplate.fragments.HomeTimeLineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by ruppal on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private static final int REPLY_TO_TWEET_REQUEST_CODE = 100;
    private List<Tweet> mTweets;
    private TweetAdapterListener mListener;
    Context context;
    //TimelineActivity currentActivity;
    TwitterClient client;
    HomeTimeLineFragment homeTimeLineFragment;
    ReplyListener replyListener;
    //pass the tweets array into the constructor


    //define an interface
    public interface TweetAdapterListener extends  GenericListener{
        public void onItemSelected(View view, int position);
    }

    public TweetAdapter(List<Tweet> tweets, Context currentActivity, GenericListener listener){
        mTweets = tweets;
        //this.currentActivity = (TimelineActivity) currentActivity;
        client = TwitterApp.getRestClient();
        mListener= (TweetAdapterListener) listener;
        replyListener = (ReplyListener) listener;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get the data according to position
        final Tweet currentTweet=mTweets.get(position);
        //populate the views according to the data from this specific tweet
        holder.tvUsername.setText(currentTweet.user.name);
        holder.tvBody.setText(currentTweet.body);
        //TODO-add a placeholder picture while image is loading
        int image_radius = 15; // corner radius, higher value = more rounded
        int image_margin = 3; // crop margin, set to 0 for corners with no crop
        int profile_radius = 5; // corner radius, higher value = more rounded
        int profile_margin = 0;
        Glide.with(context)
                .load(currentTweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, profile_radius, profile_margin))
                .into(holder.ivProfileImage);
        if (currentTweet.mediaUrl!=null){
            holder.ivMedia.getLayoutParams().height = currentTweet.mediaHeight;
            holder.ivMedia.getLayoutParams().width = currentTweet.mediaWidth;
            Glide.with(context)
                    .load(currentTweet.mediaUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, image_radius, image_margin))
                    .into(holder.ivMedia);
        }
        else{
            holder.ivMedia.getLayoutParams().height = 0;
            holder.ivMedia.getLayoutParams().width = 0;
        }
        holder.tvScreenName.setText(String.format("@%s", currentTweet.user.screenName));
        String relativeTimeAgo=getRelativeTimeAgo(currentTweet.createdAt);
        holder.tvRelativeTimestamp.setText(relativeTimeAgo);
        holder.tvNumFavorites.setText(currentTweet.numFavorites.toString());
        holder.tvNumRetweets.setText(currentTweet.numRetweets.toString());
        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fragment", currentTweet.toString());
                replyListener.openFragment(currentTweet);
            }
        });
        if (currentTweet.favorited){
            holder.ibFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_heart));
            holder.ibFavorite.setColorFilter(ContextCompat.getColor(context,R.color.inline_action_like));
        }
        if (currentTweet.retweeted){
            holder.ibRetweet.setColorFilter(ContextCompat.getColor(context,R.color.inline_action_like));
        }

        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentTweet.retweeted) {
                    client.retweet(currentTweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                Tweet updatedTweet = Tweet.fromJSON(response);
                                mTweets.set(position, updatedTweet);
                                holder.ibRetweet.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_like));
                                holder.tvNumRetweets.setText(String.valueOf(updatedTweet.numRetweets));
                                //add reply to timeline. doesnt work on refresh tho cant find it in the api.
//                                Tweet newRetweetedTweet = Tweet.fromJSON(response);
//                                currentActivity.addNewTweet(newRetweetedTweet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("favorite", errorResponse.toString());
                        }
                    });
                }

                else {
                    client.unRetweet(currentTweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            currentTweet.retweeted=false;
                            currentTweet.numRetweets = currentTweet.numRetweets -1 ;
                            holder.ibRetweet.setColorFilter(ContextCompat.getColor(context, R.color.black));
                            holder.tvNumRetweets.setText(String.valueOf(currentTweet.getNumRetweets()));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("favorite", errorResponse.toString());
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



            }
        });

        holder.ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentTweet.favorited) {
                    client.favoriteTweet(currentTweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                Tweet updatedTweet = Tweet.fromJSON(response);
                                mTweets.set(position, updatedTweet);
                                holder.ibFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_heart));
                                holder.ibFavorite.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_like));
                                holder.tvNumFavorites.setText(String.valueOf(updatedTweet.numFavorites));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.e("favorite", errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable , errorResponse );
                        }
                    });
                }

                else {
                    client.unfavoriteTweet(currentTweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                Tweet updatedTweet = Tweet.fromJSON(response);
                                mTweets.set(position, updatedTweet);
                                holder.ibFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_heart_stroke));
                                holder.ibFavorite.setColorFilter(ContextCompat.getColor(context, R.color.black));
                                holder.tvNumFavorites.setText(String.valueOf(updatedTweet.numFavorites));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("favorite", errorResponse.toString());
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



            }
        });
    }


//    public View.OnClickListener onRetweetClick (final ViewHolder holder){
//        View.OnClickListener retweetClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                client.retweet(currentTweet, new JsonHttpResponseHandler(){
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            holder.tvNumRetweets.setText(String.valueOf(Tweet.fromJSON(response).numRetweets));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        Log.e("fail retweet", errorResponse.toString());
//                    }
//                });
//            }
//        };
//        return retweetClick;
//
//    }
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create Viewholder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvBody;
        public TextView tvUsername;
        public TextView tvScreenName;
        public TextView tvRelativeTimestamp;
        public ImageButton ibReply;
        public ImageButton ibFavorite;
        public ImageButton ibRetweet;
        public TextView tvNumFavorites;
        public TextView tvNumRetweets;
        public ImageView ivMedia;

        public ViewHolder(View itemView){
            super(itemView);
            //preform find view by id lookup
            ivProfileImage = (ImageView) itemView.findViewById(R.id. ivProfileImage);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvNameCompose);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = (TextView) itemView.findViewById(R.id.tvRelativeTimestamp);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
            ibFavorite = (ImageButton) itemView.findViewById(R.id.ibFavorite);
            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            tvNumFavorites = (TextView) itemView.findViewById(R.id.tvNumFavorites);
            tvNumRetweets = (TextView) itemView.findViewById(R.id.tvNumRetweets);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
            //handle on click events
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);
                    User user = tweet.getUser();
                    Intent i = new Intent (context, ProfileActivity.class);
                    i.putExtra("other_user", Parcels.wrap(user));
                    i.putExtra("screen_name", user.getScreenName());
                    context.startActivity(i);
                }
            });

//            ibReply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Tweet tweet = mTweets.get(position);
//                    Intent i = new Intent(context, ReplyActivity.class);
//                    i.putExtra(ReplyActivity.RESPONSE_TO_TWEET, Parcels.wrap(tweet));
//                    ((TimelineActivity) context).startActivityforResult(i, REPLY_TO_TWEET_REQUEST_CODE );
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        //get the position of the row element
                        int position = getAdapterPosition();
                        //fire listener is not null
                        mListener.onItemSelected(v, position);
                    }
                }
            });


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

    public View.OnClickListener onFavoriteClick(final ViewHolder holder,final Tweet currentTweet) {
        View.OnClickListener favoriteListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    client.favoriteTweet(currentTweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            //                                currentTweet = Tweet.fromJSON(response);
                            holder.ibFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_heart));
                            holder.ibFavorite.setColorFilter(ContextCompat.getColor(context,R.color.inline_action_like));
                            holder.tvNumFavorites.setText(String.valueOf(currentTweet.numFavorites + 1));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("favorite", errorResponse.toString());
                        }
                    });


//                else {
//                    client.unfavoriteTweet(currentTweet, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            try {
//                                currentTweet = Tweet.fromJSON(response);
//                                holder.ibFavorite.setBackgroundColor(Color.TRANSPARENT);
//                                holder.tvNumFavorites.setText(String.valueOf(currentTweet.numFavorites));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.e("favorite", errorResponse.toString());
//                        }
//                    });
//                }
            }
        };
        return favoriteListener;
    }



}
