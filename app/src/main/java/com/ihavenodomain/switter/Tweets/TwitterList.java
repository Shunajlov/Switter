package com.ihavenodomain.switter.Tweets;


import java.util.ArrayList;
import java.util.List;

public class TwitterList {
    List<Tweet> twitterList;

    public List<Tweet> getTwitterList() {
        return twitterList;
    }

    public void add(Tweet tweetItem) {
        if(twitterList == null)
            twitterList = new ArrayList<>();
        twitterList.add(tweetItem);
    }
}
