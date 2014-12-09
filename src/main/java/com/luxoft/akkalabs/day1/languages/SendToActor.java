package com.luxoft.akkalabs.day1.languages;

import akka.actor.ActorRef;
import akka.dispatch.OnSuccess;
import scala.PartialFunction;

/**
 * Created by artem on 08.12.14.
 */
public class SendToActor extends OnSuccess<FinalResult> {
    private final ActorRef actor;

    public SendToActor(ActorRef actor) {
        this.actor = actor;
    }

    @Override
    public void onSuccess(FinalResult success) throws Throwable {
        actor.tell(success, null);

    }
}
