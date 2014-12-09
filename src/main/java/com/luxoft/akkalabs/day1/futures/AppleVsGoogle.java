package com.luxoft.akkalabs.day1.futures;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.OnSuccess;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Map;

public class AppleVsGoogle {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create();


        Future<FinalResult> appleFuture = Futures.future(new TweetFetcher(system, "apple", 10), system.dispatcher()).map(
                new FinalResultMapper(), system.dispatcher()
        );

        appleFuture.onSuccess(new OnSuccess<FinalResult>() {
            @Override
            public void onSuccess(FinalResult result) throws Throwable {
                System.out.println(result.getKeyword());
                for (Map.Entry<String, Integer> stringIntegerEntry : result.getTweetsStatistic().entrySet()) {
                    System.out.println(stringIntegerEntry.getKey() + " : " + stringIntegerEntry.getValue());
                }

                system.shutdown();
            }
        }, system.dispatcher());

    }
}
