package com.luxoft.akkalabs.day1.wikipedia;

import akka.actor.UntypedActor;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artem on 08.12.14.
 */
public class StatisticActor extends UntypedActor {
    private Map<String, Integer> statistic = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if ("get".equals(message)) {
            int max = 0;
            String result = null;
            for (Map.Entry<String, Integer> stringIntegerEntry : statistic.entrySet()) {
                Integer value = stringIntegerEntry.getValue();
                if (value > max) {
                    max = value;
                    result = stringIntegerEntry.getKey();
                }
            }

            getSender().tell(result, getSelf());

        }else if (message instanceof String) {
            String url = (String) message;
            Integer integer = statistic.get(url);
            if (integer == null) {
                statistic.put(url, 1);
            } else {
                statistic.put(url, integer + 1);
            }
        }
    }
}
