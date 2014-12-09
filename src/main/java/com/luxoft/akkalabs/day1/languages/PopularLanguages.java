package com.luxoft.akkalabs.day1.languages;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PopularLanguages {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create();

        ActorRef twitterActor = system.actorOf(Props.create(TwitterActor.class));

        List<String> keywords =
                Arrays.asList("Google", "Apple", "Android", "iPhone", "Lady Gaga");

        for(int i =0;i < 3; ++i) {
            for (String keyword : keywords) {
                akka.pattern.Patterns.pipe(
                        Futures.future(
                                new TweetFetcher(system, keyword, 1),
                                system.dispatcher()
                        )
                                .map(
                                        new FinalResultMapper(),
                                        system.dispatcher()
                                ),
                        system.dispatcher()
                ).to(twitterActor);
            }
        }

        System.out.println(1);


        Timeout timeout = Timeout.apply(1, TimeUnit.SECONDS);
        for(int i =0;i < 300; ++i){
            Thread.sleep(3000);

            System.out.println(2);
            Future<Object> future = Patterns.ask(twitterActor, "get", timeout);

            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object success) throws Throwable {

                    System.out.println(success);
                }
            }, system.dispatcher());
        }

        system.shutdown();
    }
}
