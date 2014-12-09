package com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics;

import com.luxoft.akkalabs.clients.wikipedia.WikipediaPage;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by artem on 08.12.14.
 */
public class WikipediaListenerImpl implements WikipediaListener {

    private AsyncContext context;
    private PrintWriter out;

    public WikipediaListenerImpl(String streamId, AsyncContext context){
        this.context = context;
    }

    @Override
    public void deliver(WikipediaPage page) throws NotDeliveredException {
        try {
            out = context.getResponse().getWriter();

            String data = page.toJSONString();

            String eventId = Long.toString(System.currentTimeMillis());
            String[] lines = data.split("\n");
            out.append("id: ").append(eventId).append('\n');
            for (String line : lines) {
                out.append("data: ").append(line).append('\n');
            }

            out.append("\n\n");

            context.getResponse().flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void statistic(Statistic statistic) throws NotDeliveredException {
        try {
            out = context.getResponse().getWriter();

            String data = statistic.toJSONString();

            String eventId = Long.toString(System.currentTimeMillis());
            String[] lines = data.split("\n");
            out.append("id: ").append(eventId).append('\n');
            for (String line : lines) {
                out.append("data: ").append(line).append('\n');
            }

            out.append("\n\n");

            context.getResponse().flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getStreamId() {
        return null;
    }

    @Override
    public void close() {
        out.close();
    }
}
