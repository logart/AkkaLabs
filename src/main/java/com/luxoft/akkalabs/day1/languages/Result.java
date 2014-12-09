package com.luxoft.akkalabs.day1.languages;

import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.Collection;

/**
 * Created by artem on 08.12.14.
 */
public class Result {
    private final String keyword;
    private final Collection<TweetObject> tweets;

    public Result(String keyword, Collection<TweetObject> tweets) {
        this.keyword = keyword;
        this.tweets = tweets;
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<TweetObject> getTweets() {
        return tweets;
    }
}
