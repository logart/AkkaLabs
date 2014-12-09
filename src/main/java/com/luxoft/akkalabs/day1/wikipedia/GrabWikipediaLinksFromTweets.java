package com.luxoft.akkalabs.day1.wikipedia;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.luxoft.akkalabs.clients.twitter.TwitterClient;
import com.luxoft.akkalabs.clients.twitter.TwitterClients;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

public class GrabWikipediaLinksFromTweets {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("GrabWikipediaLinksFromTweets");

        ActorRef statisticActor = system.actorOf(Props.create(StatisticActor.class));
        ActorRef linksActor = system.actorOf(Props.create(WikipediaActor.class, statisticActor));
        ActorRef tweetsActor = system.actorOf(Props.create(TweetLinksActor.class, linksActor));

        TwitterClient c = TwitterClients.start(system, tweetsActor, "wikipedia");

        Timeout timeout = Timeout.apply(60, TimeUnit.SECONDS);
        for(int i =0;i < 300; ++i){

            Thread.sleep(10000);

            Future<Object> future = Patterns.ask(statisticActor, "get", timeout);

            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object success) throws Throwable {

                    System.out.println(success);
                }
            }, system.dispatcher());
        }
    }
}
