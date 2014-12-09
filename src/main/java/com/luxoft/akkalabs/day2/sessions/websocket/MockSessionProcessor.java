package com.luxoft.akkalabs.day2.sessions.websocket;

import akka.actor.ActorContext;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by artem on 09.12.14.
 */
public class MockSessionProcessor implements SessionProcessor {
    private Session session;

    @Override
    public void started(String sessionId, ActorContext context, Session session) throws IOException {
        this.session = session;
        System.out.println("started session "+sessionId);
    }

    @Override
    public void stopped() throws IOException {
        session.close();
        System.out.println("stopped session ");
    }

    @Override
    public void incoming(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void outgoing(Object message) throws IOException {
        System.out.println("out");
    }
}
