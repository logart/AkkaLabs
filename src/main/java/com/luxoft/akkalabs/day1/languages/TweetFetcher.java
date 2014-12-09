package com.luxoft.akkalabs.day1.languages;

import akka.actor.ActorSystem;
import com.luxoft.akkalabs.clients.twitter.QueueTwitterClient;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.clients.twitter.TwitterClients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by artem on 08.12.14.
 */
public class TweetFetcher implements Callable<Result> {
    private final ActorSystem system;
    private final String keyword;
    private final int count;

    public TweetFetcher(ActorSystem system, String keyword, int i) {
        this.system = system;
        this.keyword = keyword;
        this.count = i;
    }


    @Override
    public Result call() throws Exception {
        Collection<TweetObject> tweets = new ArrayList<>();
        try (QueueTwitterClient client = TwitterClients.start(system, keyword)) {
            for(int i = 0; i < count; ++i) {
                TweetObject nextTweet = client.next();
                tweets.add(nextTweet);
            }
        }
        return new Result(keyword, tweets);
    }

}
