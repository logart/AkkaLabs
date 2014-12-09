package com.luxoft.akkalabs.day2.sessions.web;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.luxoft.akkalabs.day2.sessions.actors.SessionsHubActor;
import com.luxoft.akkalabs.day2.sessions.websocket.MockSessionProcessor;
import com.luxoft.akkalabs.day2.sessions.websocket.WebSocketLauncher;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Init implements ServletContextListener {

    public static final String ACTOR_SYSTEM_KEY = "echoActorSystem";
    public static final String ACTOR_SYSTEM_NAME = "Echo";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        sce.getServletContext().setAttribute(ACTOR_SYSTEM_KEY, system);

        system.actorOf(Props.create(SessionsHubActor.class, MockSessionProcessor.class), "sessions");

        WebSocketLauncher.launchSessionEndpoint(sce.getServletContext(), "/day2/echo", ACTOR_SYSTEM_KEY);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ActorSystem system = (ActorSystem) sce.getServletContext().getAttribute(ACTOR_SYSTEM_KEY);
        system.shutdown();
    }

    public static ActorSystem getSystem(ServletContext context) {
        return (ActorSystem) context.getAttribute(ACTOR_SYSTEM_KEY);
    }
}
