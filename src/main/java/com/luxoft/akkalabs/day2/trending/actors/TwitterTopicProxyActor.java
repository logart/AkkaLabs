package com.luxoft.akkalabs.day2.trending.actors;

import akka.actor.*;
import com.luxoft.akkalabs.day2.sessions.messages.Outgoing;
import com.luxoft.akkalabs.day2.topics.actors.TwitterTopicActor;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;

import java.util.ArrayList;
import java.util.List;

public class TwitterTopicProxyActor extends UntypedActor {

    private final String keyword;
    private ActorRef actor;
    private List<ActorRef> subscribers = new ArrayList<>();

    public TwitterTopicProxyActor(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("twitter proxy started...");
        actor = context().actorOf(Props.create(TwitterTopicActor.class, keyword));

        context().watch(actor);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Terminated) {
            context().stop(self());
        }

        if (!actor.equals(getSender())) {
            if (message instanceof SubscribeToTopic) {
                if (keyword.equals(((SubscribeToTopic) message).getKeyword())) {
                    System.out.println("sender " + getSender() + " added for keyword " + keyword + " in proxy");
                    if (subscribers.isEmpty()) {
                        actor.tell(new SubscribeToTopic(keyword), getSelf());
                    }
                    subscribers.add(getSender());
                } else {
                    System.out.println("keyword " + ((SubscribeToTopic) message).getKeyword() + " does not match expected " + keyword);
                }
            } else if (message instanceof UnsubscribeFromTopic) {
                boolean removed = subscribers.remove(getSender());
                if (removed) {
                    System.out.println("subscriber " + getSender() + " was correctly unsubscribed!");
                }
                if (subscribers.isEmpty()) {
                    actor.tell(new UnsubscribeFromTopic(keyword), getSelf());
                }
            } else {
                actor.forward(message, getContext());
            }

        } else {
            if (message instanceof Outgoing) {
                for (ActorRef subscriber : subscribers) {
                    subscriber.tell(((Outgoing) message).getMessage(), getSender());
                }
                ActorSelection trendCalculator = getContext().actorSelection("/user/trending");
                trendCalculator.tell(((Outgoing) message).getMessage(), getSelf());
            } else {
                getContext().parent().forward(message, getContext());
            }
        }
    }
}
