package com.luxoft.akkalabs.clients.wikipedia;

import org.json.JSONObject;

public class WikipediaPage {

    private final String title;
    private final String text;
    private final String url;

    public WikipediaPage(String title, String text, String url) {
        this.title = title;
        this.text = text;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String toJSONString() {
        JSONObject dataObject = new JSONObject();
        dataObject.put("title", title);
        dataObject.put("text", text);
        dataObject.put("url", url);
        dataObject.put("type", "link");
        return dataObject.toString();
    }
}
