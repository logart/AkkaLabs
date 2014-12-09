package com.luxoft.akkalabs.day1.languages;

import java.util.Map;

/**
 * Created by artem on 08.12.14.
 */
public class FinalResult {
    private final String keyword;
    private final Map<String, Integer> tweetsStatistic;

    public FinalResult(String keyword, Map<String, Integer> tweets) {
        this.keyword = keyword;
        this.tweetsStatistic = tweets;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<String, Integer> getTweetsStatistic() {
        return tweetsStatistic;
    }

}
