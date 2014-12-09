package com.luxoft.akkalabs.day1.firstactor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class FirstActorApp {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create();

        ActorRef firstActor = system.actorOf(Props.create(MyFirstActor.class));

        firstActor.tell("lol", null);

        firstActor.tell(new Object(), null);

        Thread.sleep(1000);

        system.shutdown();
    }

}
