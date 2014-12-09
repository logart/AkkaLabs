package com.luxoft.akkalabs.day1.firstactor;

import akka.actor.UntypedActor;

/**
 * Created by artem on 08.12.14.
 */
public class MyFirstActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println(message);
            return;
        }

        throw new RuntimeException();
    }
}
