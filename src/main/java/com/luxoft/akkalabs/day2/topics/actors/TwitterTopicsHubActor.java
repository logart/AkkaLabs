package com.luxoft.akkalabs.day2.topics.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.luxoft.akkalabs.day2.topics.messages.*;
import akka.actor.UntypedActor;

public class TwitterTopicsHubActor extends UntypedActor {

    private final Class<? extends UntypedActor> topicClass;

    public TwitterTopicsHubActor(Class<? extends UntypedActor> topicClass) {
        this.topicClass = topicClass;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SubscribeToTopic) {
            ActorRef child = getContext().getChild(((SubscribeToTopic) message).getKeyword());
            if (child == null) {
                System.out.println("instantiate new child...");
                child = getContext().actorOf(Props.create(
                                topicClass,
                                ((SubscribeToTopic) message).getKeyword()
                        ),
                        ((SubscribeToTopic) message).getKeyword()
                );
            }
            System.out.println("forward subscribe to child " + child);
            child.forward(message, getContext());
        } else if (message instanceof UnsubscribeFromTopic) {
            ActorRef child = getContext().getChild(((UnsubscribeFromTopic) message).getKeyword());
            if (child != null) {
                System.out.println("unsubscribe from " + ((UnsubscribeFromTopic) message).getKeyword());
                child.forward(message, getContext());
            }
        } else if (message instanceof TopicIsEmpty) {
            ActorRef child = getContext().getChild(((TopicIsEmpty) message).getKeyword());
            if (child != null) {
                System.out.println("remove topic " + ((TopicIsEmpty) message).getKeyword());
                child.tell(StopTopic.getInstance(), getSelf());
            }
        }
    }
}
