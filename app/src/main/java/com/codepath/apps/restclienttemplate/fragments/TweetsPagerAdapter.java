package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ruppal on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    //return total # of fragments
    private Context context;
    private String tabTitles[] = new String[] {"home", "mentions"};

    public TweetsPagerAdapter (FragmentManager fm, Context context){
        super(fm);
        this.context=context;
    }


    @Override
    public int getCount() {
        return 2 ;
    }

    //return frag to use depending on position

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new HomeTimeLineFragment();
        }
        else if (position == 1){
            return new MentionsTimelineFragment();
        }
        else{
            return null;
        }
    }

    //return title

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
