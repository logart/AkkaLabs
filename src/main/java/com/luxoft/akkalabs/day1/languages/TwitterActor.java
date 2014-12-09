package com.luxoft.akkalabs.day1.languages;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artem on 08.12.14.
 */
public class TwitterActor extends UntypedActor {

    private final Map<String, Integer> languages = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Exception {
//        System.out.println(message);

        if("get".equals(message)){
            getSender().tell(languages, getSelf());
        }

        if (message instanceof FinalResult) {
            Map<String, Integer> tweetsStatistic = ((FinalResult) message).getTweetsStatistic();
            for (Map.Entry<String, Integer> stringIntegerEntry : tweetsStatistic.entrySet()) {
                Integer integer = languages.get(stringIntegerEntry.getKey());
                if (integer == null) {
                    languages.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                } else {
                    languages.put(stringIntegerEntry.getKey(), integer + stringIntegerEntry.getValue());
                }
            }
        }
    }
}
