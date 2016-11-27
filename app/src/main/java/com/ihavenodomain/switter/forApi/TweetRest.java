package com.ihavenodomain.switter.forApi;

import com.ihavenodomain.switter.Tweets.Tweet;

import java.util.LinkedList;

import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;
// https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2

public interface TweetRest {
    @GET("1.1/statuses/user_timeline.json?")
    Observable<LinkedList<Tweet>> getTweet(@Query("screen_name") String screenName, @Query("count") String count);

    @GET("1.1/statuses/user_timeline.json?")
    Observable<LinkedList<Tweet>> getTweet(@Query("screen_name") String screenName, @Query("count") String count, @Query("max_id") String maxId);
}
