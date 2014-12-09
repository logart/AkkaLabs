package com.luxoft.akkalabs.day1.wikipedia;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.Collection;

public class TweetLinksActor extends UntypedActor {

    private final ActorRef linksActor;

    public TweetLinksActor(ActorRef linksActor) {
        this.linksActor = linksActor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof TweetObject){
            Collection<String> urls = ((TweetObject) message).getUrls();
            for (String url : urls) {
                if(url.length() > 8 && url.contains("wikipedia.org")){
                    linksActor.tell(url, getSelf());
                }
            }

        }else{
            System.out.println(message);
        }
    }
}
