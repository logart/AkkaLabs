package com.luxoft.akkalabs.day2.trending.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingBroadcast;
import com.luxoft.akkalabs.day2.trending.messages.CurrentTrending;
import com.luxoft.akkalabs.day2.trending.messages.UpvoteTrending;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrendingCalculatorActor extends UntypedActor {

    private final Object PING = new Object();
    private Map<String, Integer> statistic = new TreeMap<>();

    @Override
    public void preStart() throws Exception {
        FiniteDuration oneSecond = FiniteDuration.create(1, TimeUnit.SECONDS);

        context().system().scheduler().schedule(
                oneSecond,
                oneSecond,
                self(),
                PING,
                context().dispatcher(),
                self()
        );
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }


    @Override
    public void onReceive(Object message) throws Exception {

        if (message == PING) {
            ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(statistic.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            if (statistic.size() > 15) {
                for (Map.Entry<String, Integer> stringIntegerEntry : entries.subList((int) (entries.size() * 0.8), entries.size() - 1)) {
                    statistic.remove(stringIntegerEntry.getKey());
                }
            }
            List<String> result = Lists.newArrayList();
            for (Map.Entry<String, Integer> wordsWithCounter : entries.subList(0, entries.size() > 5 ? 5 : entries.size())) {
                String key = wordsWithCounter.getKey();
                Integer value = wordsWithCounter.getValue();
                result.add(key + " <-> " + value);
            }

            ActorSelection actor = getContext().actorSelection("/user/sessions");
            actor.tell(new OutgoingBroadcast(new CurrentTrending(result)), getSelf());

//            System.out.println(statistic);

        } else if (message instanceof TweetObject) {
            String[] split = ((TweetObject) message).getText().split("[ .,]+");
            for (String s : split) {
                if (s.length() < 6)
                    continue;
                Integer integer = statistic.get(s);
                if (integer == null) {
                    statistic.put(s, 1);
                } else {
                    statistic.put(s, integer + 1);
                }
            }

        } else if (message instanceof UpvoteTrending) {
            System.out.println("upvote ");
            String keyword = ((UpvoteTrending) message).getKeyword();
            int i = keyword.indexOf(" <-> ");
            keyword = keyword.substring(0, i);
            System.out.printf(keyword);
            Integer integer = statistic.get(keyword);
            if (integer == null) {
                statistic.put(keyword, 5);
            } else {
                statistic.put(keyword, integer + 5);
            }
        }
    }
}
