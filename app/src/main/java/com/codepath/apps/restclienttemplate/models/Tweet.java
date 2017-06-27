package com.codepath.apps.restclienttemplate.models;

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

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject object) throws JSONException{
        Tweet tweet = new Tweet();
        tweet.body  = object.getString("text");
        tweet.uid = object.getLong("id");
        tweet.createdAt = object.getString("created_at");
        //pass in the json user object to get a User
        tweet.user = User.fromJSON(object.getJSONObject("user"));
        return tweet;
    }


}
