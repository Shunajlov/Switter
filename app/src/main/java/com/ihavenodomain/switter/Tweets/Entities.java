
package com.ihavenodomain.switter.Tweets;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entities {

    @SerializedName("urls")
    @Expose
    private List<Url> urls = new ArrayList<Url>();
    @SerializedName("hashtags")
    @Expose
    private List<Object> hashtags = new ArrayList<Object>();
    @SerializedName("user_mentions")
    @Expose
    private List<Object> userMentions = new ArrayList<Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Entities() {
    }

    /**
     * 
     * @param urls
     * @param hashtags
     * @param userMentions
     */
    public Entities(List<Url> urls, List<Object> hashtags, List<Object> userMentions) {
        this.urls = urls;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The hashtags
     */
    public List<Object> getHashtags() {
        return hashtags;
    }

    /**
     * 
     * @param hashtags
     *     The hashtags
     */
    public void setHashtags(List<Object> hashtags) {
        this.hashtags = hashtags;
    }

    /**
     * 
     * @return
     *     The userMentions
     */
    public List<Object> getUserMentions() {
        return userMentions;
    }

    /**
     * 
     * @param userMentions
     *     The user_mentions
     */
    public void setUserMentions(List<Object> userMentions) {
        this.userMentions = userMentions;
    }

}
