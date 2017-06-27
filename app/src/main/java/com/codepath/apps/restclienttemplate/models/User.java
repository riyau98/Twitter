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

    //deserialize JSON
    public static User fromJSON(JSONObject object) throws JSONException{
        User user=new User();
        //extract and fill out the values
        user.name=object.getString("name");
        user.uid=object.getLong("id");
        user.screenName=object.getString("screen_name");
        user.profileImageUrl=object.getString("profile_image_url");

        return user;


    }

}
