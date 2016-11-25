
package com.ihavenodomain.switter.Tweets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entities_ {

    @SerializedName("url")
    @Expose
    private Url_ url;
    @SerializedName("description")
    @Expose
    private Description description;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Entities_() {
    }

    /**
     * 
     * @param description
     * @param url
     */
    public Entities_(Url_ url, Description description) {
        this.url = url;
        this.description = description;
    }

    /**
     * 
     * @return
     *     The url
     */
    public Url_ getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(Url_ url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Description description) {
        this.description = description;
    }

}
