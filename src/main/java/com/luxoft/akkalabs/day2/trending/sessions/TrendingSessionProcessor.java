package com.luxoft.akkalabs.day2.trending.sessions;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;
import com.luxoft.akkalabs.day2.trending.messages.CurrentTrending;
import com.luxoft.akkalabs.day2.trending.messages.UpvoteTrending;

import java.io.IOException;
import javax.websocket.Session;

public class TrendingSessionProcessor implements SessionProcessor {

    private String sessionId;
    private ActorContext context;
    private Session session;

    @Override
    public void started(String sessionId, ActorContext context, Session session) {
        System.out.println("session " + sessionId + " started;");

        this.sessionId = sessionId;
        this.context = context;
        this.session = session;
    }

    @Override
    public void stopped() {
        System.out.println("session " + sessionId + " stopped;");

        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void incoming(String message) {
        System.out.println("incoming " + message);
        String[] tokens = message.split(" ", 2);
        ActorRef topics;
        if (tokens.length == 2) {
            switch (tokens[0]) {
                case "subscribe":
                    topics = context.actorFor("/user/topics");
                    topics.tell(new SubscribeToTopic(tokens[1]), context.self());
                    break;
                case "unsubscribe":
                    topics = context.actorFor("/user/topics");
                    topics.tell(new UnsubscribeFromTopic(tokens[1]), context.self());
                    break;
                case "upvote":
                    topics = context.actorFor("/user/trending");
                    topics.tell(new UpvoteTrending(tokens[1]), context.self());
                    break;
            }

        }
    }

    @Override
    public void outgoing(Object message) throws IOException {
        if (message instanceof TweetObject) {
            session.getBasicRemote().sendText("tweet " + ((TweetObject) message).getText());
        } else if (message instanceof CurrentTrending) {
            session.getBasicRemote().sendText("trend " + ((CurrentTrending) message).toJSON());
        } else {
            System.out.println("unrecognized out " + message);
            session.getBasicRemote().sendText("unrecognized message " + message);
        }
    }
}
