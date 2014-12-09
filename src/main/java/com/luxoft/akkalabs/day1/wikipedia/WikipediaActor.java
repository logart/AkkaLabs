package com.luxoft.akkalabs.day1.wikipedia;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class WikipediaActor extends UntypedActor {

    private ActorRef popularActor;

    public WikipediaActor(ActorRef popularActor){
        this.popularActor = popularActor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            String url = (String) message;
            if (url.length() > 8 && url.contains("wikipedia.org")) {
//                System.out.println(url);
                popularActor.tell(url, getSelf());
            }
        }
    }
}
