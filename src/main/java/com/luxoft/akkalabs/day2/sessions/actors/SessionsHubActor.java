package com.luxoft.akkalabs.day2.sessions.actors;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingBroadcast;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingToSession;
import com.luxoft.akkalabs.day2.sessions.messages.RegisterSession;
import com.luxoft.akkalabs.day2.sessions.messages.UnregisterSession;

public class SessionsHubActor extends UntypedActor {

    private final Class<? extends SessionProcessor> processorClass;

    public SessionsHubActor(Class<? extends SessionProcessor> processor) {
        this.processorClass = processor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RegisterSession) {

            getContext().actorOf(Props.create(
                    SessionActor.class,

                    ((RegisterSession) message).getSessionId(),
                    ((RegisterSession) message).getSession(),
                    processorClass.newInstance()
            ),
                    ((RegisterSession) message).getSessionId()
            );
        } else if (message instanceof UnregisterSession) {
            getContext().getChild(((UnregisterSession) message).getSessionId()).tell(PoisonPill.getInstance(), getSelf());
        } else if (message instanceof OutgoingToSession) {
            ActorRef child = getContext().getChild(((OutgoingToSession) message).getSessionId());
            child.forward(message, getContext());
        } else if (message instanceof OutgoingBroadcast) {
            for (ActorRef child : getContext().getChildren()) {
                child.forward(message, getContext());
            }

        }
    }
}
