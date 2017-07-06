package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ruppal on 6/26/17.
 */

@Parcel
public class User {
    //list all attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String tagLine;
    public int following;
    public int followers;

    //deserialize JSON
    public static User fromJSON(JSONObject object) throws JSONException{
        User user=new User();
        //extract and fill out the values
        user.name=object.getString("name");
        long userId=object.getLong("id");
        user.uid=userId;
        String sName=object.getString("screen_name");
        user.screenName=sName;
        user.profileImageUrl=object.getString("profile_image_url");
        user.tagLine = object.getString("description");
        user.followers = object.getInt("followers_count");
        user.following = object.getInt("friends_count");
        return user;
    }

    public String getTagLine() {
        return tagLine;
    }

    public Integer getFollowing() {
        return following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }



}
