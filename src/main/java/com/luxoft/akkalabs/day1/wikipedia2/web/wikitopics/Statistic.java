package com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics;

import org.json.JSONObject;

/**
 * Created by artem on 08.12.14.
 */
public class Statistic {
    private final String topic;
    private final int count;
    private final String url;

    public Statistic(String topic, int count, String url) {
        this.topic = topic;
        this.count = count;
        this.url = url;
    }

    public String toJSONString() {
        JSONObject dataObject = new JSONObject();
        dataObject.put("topic", topic);
        dataObject.put("count", count);
        dataObject.put("url", url);
        dataObject.put("type", "stat");
        return dataObject.toString();
    }
}
