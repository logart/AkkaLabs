package com.luxoft.akkalabs.day1.wikipedia2.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.*;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsActor extends UntypedActor {

    private List<WikipediaListener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Deliver) {
            for (WikipediaListener listener : listeners) {
                listener.deliver(((Deliver) message).getPage());
            }
        } else if (message instanceof Statistic) {
            for (WikipediaListener listener : listeners) {
                listener.statistic((Statistic) message);
            }
        } else if (message instanceof Register) {

            this.listeners.add(((Register) message).getListener());
        }
        if (message instanceof Unregister) {
            //...
        }
    }
}
