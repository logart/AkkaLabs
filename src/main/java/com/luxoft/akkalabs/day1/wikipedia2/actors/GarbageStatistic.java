package com.luxoft.akkalabs.day1.wikipedia2.actors;

/**
 * Created by artem on 09.12.14.
 */
public class GarbageStatistic {

    private final String term;
    private final String url;

    public GarbageStatistic(String url, String term) {
        this.url = url;
        this.term = term;
    }

    public String getUrl() {
        return url;
    }

    public String getTerm() {
        return term;
    }
}
