
package com.ihavenodomain.switter.Tweets;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Url_ {

    @SerializedName("urls")
    @Expose
    private List<Url__> urls = new ArrayList<Url__>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Url_() {
    }

    /**
     * 
     * @param urls
     */
    public Url_(List<Url__> urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url__> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url__> urls) {
        this.urls = urls;
    }

}
