package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ruppal on 6/26/17.
 */


@Parcel
public class Tweet {
    public String body; //body of tweet
    public long uid; //database id of the tweet
    public String createdAt;
    public User user;
    public boolean favorited;
    public boolean retweeted;
    public Integer numFavorites;
    public Integer numRetweets;
    public String mediaUrl;
    public Integer mediaWidth;
    public Integer mediaHeight;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject object) throws JSONException{
        Log.i("json", object.toString());
        Tweet tweet = new Tweet();
        tweet.body  = object.getString("text");
        tweet.uid = object.getLong("id");
        tweet.createdAt = object.getString("created_at");
        //pass in the json user object to get a User
        tweet.user = User.fromJSON(object.getJSONObject("user"));
        tweet.favorited = object.getBoolean("favorited");
        tweet.retweeted = object.getBoolean("retweeted");
        tweet.numFavorites = object.getInt("favorite_count");
        tweet.numRetweets = object.getInt("retweet_count");
        if (object.has("entities") && object.getJSONObject("entities").has("media")) {
            JSONArray media = object.getJSONObject("entities").getJSONArray("media");
            JSONObject pic =media.getJSONObject(0).getJSONObject("sizes").getJSONObject("medium");
            tweet.mediaWidth = pic.getInt("w");
            tweet.mediaHeight = pic.getInt("h");
            tweet.mediaUrl = media.getJSONObject(0).getString("media_url");
        }
        else {
            tweet.mediaUrl = null;
        }
        return tweet;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public Integer getNumFavorites() {
        return numFavorites;
    }

    public Integer getNumRetweets() {
        return numRetweets;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public Integer getMediaWidth() {
        return mediaWidth;
    }

    public Integer getMediaHeight() {
        return mediaHeight;
    }
}
