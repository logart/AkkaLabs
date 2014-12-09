package com.luxoft.akkalabs.day1.wikipedia2.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.Statistic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artem on 08.12.14.
 */
public class StatisticActor extends UntypedActor {
    private Map<String, Integer> statistic = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GarbageStatistic) {
            String term = ((GarbageStatistic) message).getTerm();
            Integer integer = statistic.get(term);
            if (integer == null) {
                statistic.put(term, 1);
            } else {
                statistic.put(term, integer + 1);
            }

            int max = 0;
            String result = null;
            for (Map.Entry<String, Integer> stringIntegerEntry : statistic.entrySet()) {
                Integer value = stringIntegerEntry.getValue();
                if (value > max) {
                    max = value;
                    result = stringIntegerEntry.getKey();
                }
            }

            getContext().system().actorSelection("/user/connections").tell(new Statistic(result, max, ((GarbageStatistic) message).getUrl()), getSelf());

        }
    }
}
