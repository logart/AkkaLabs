package com.luxoft.akkalabs.day2.sessions.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.sessions.messages.Incoming;
import com.luxoft.akkalabs.day2.sessions.messages.Outgoing;

import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class SessionActor extends UntypedActor {

    private final String sessionId;
    private final Session wsSession;
    private final SessionProcessor sessionProcessor;

    public SessionActor(String sessionId, Session wsSession, SessionProcessor sessionProcessor) {
        this.sessionId = sessionId;
        this.wsSession = wsSession;
        this.sessionProcessor = sessionProcessor;
    }

    @Override
    public void preStart() throws Exception {
        javax.websocket.MessageHandler.Whole<String> listener = new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                self().tell(new Incoming(message), self());
            }
        };
        wsSession.addMessageHandler(listener);

        sessionProcessor.started(sessionId, getContext(), wsSession);
    }

    @Override
    public void postStop() throws Exception {
        sessionProcessor.stopped();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Incoming) {
            sessionProcessor.incoming(((Incoming) message).getMessage());
        } else if (message instanceof Outgoing) {
            sessionProcessor.outgoing(((Outgoing) message).getMessage());
        } else {
            sessionProcessor.outgoing(message);
        }
    }
}
