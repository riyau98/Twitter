package com.codepath.apps.restclienttemplate;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by ruppal on 7/7/17.
 */

public interface ReplyListener extends GenericListener {

    void openFragment(Tweet tweet);
}
