
package com.ihavenodomain.switter.Tweets;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Url__ {

    @SerializedName("expanded_url")
    @Expose
    private Object expandedUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("indices")
    @Expose
    private List<Long> indices = new ArrayList<Long>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Url__() {
    }

    /**
     * 
     * @param indices
     * @param expandedUrl
     * @param url
     */
    public Url__(Object expandedUrl, String url, List<Long> indices) {
        this.expandedUrl = expandedUrl;
        this.url = url;
        this.indices = indices;
    }

    /**
     * 
     * @return
     *     The expandedUrl
     */
    public Object getExpandedUrl() {
        return expandedUrl;
    }

    /**
     * 
     * @param expandedUrl
     *     The expanded_url
     */
    public void setExpandedUrl(Object expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The indices
     */
    public List<Long> getIndices() {
        return indices;
    }

    /**
     * 
     * @param indices
     *     The indices
     */
    public void setIndices(List<Long> indices) {
        this.indices = indices;
    }

}
