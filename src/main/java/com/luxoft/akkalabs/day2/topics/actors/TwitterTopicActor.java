package com.luxoft.akkalabs.day2.topics.actors;

import akka.actor.ActorRef;
import com.luxoft.akkalabs.clients.twitter.TwitterClient;
import com.luxoft.akkalabs.clients.twitter.TwitterClients;
import com.luxoft.akkalabs.day2.sessions.messages.Outgoing;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingBroadcast;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingToSession;
import com.luxoft.akkalabs.day2.topics.messages.TopicIsEmpty;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;
import com.luxoft.akkalabs.day2.topics.messages.StopTopic;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.ArrayList;
import java.util.List;

public class TwitterTopicActor extends UntypedActor {

    private final String keyword;
    private TwitterClient client;
    private final List<ActorRef> subscribers = new ArrayList<>();

    public TwitterTopicActor(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void preStart() throws Exception {
        client = TwitterClients.start(getContext().system(), getSelf(), keyword);
    }

    @Override
    public void postStop() throws Exception {
        client.stop();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TweetObject) {
            for (ActorRef subscriber : subscribers) {
                subscriber.tell(new OutgoingBroadcast(message), getSelf());
            }
        } else if (message instanceof SubscribeToTopic) {
            if (keyword.equals(((SubscribeToTopic) message).getKeyword())) {
                System.out.println("sender " + getSender() + " added for keyword " + keyword);
                subscribers.add(getSender());
            } else {
                System.out.println("keyword " + ((SubscribeToTopic) message).getKeyword() + " does not match expected " + keyword);
            }
        } else if (message instanceof UnsubscribeFromTopic) {
            boolean removed = subscribers.remove(getSender());
            if (removed) {
                System.out.println("client successfully unsubscribed!");
            } else {
                throw new RuntimeException("error unsubscription!");
            }
            if (subscribers.isEmpty()) {
                getContext().parent().tell(new TopicIsEmpty(keyword), getSelf());
            }
        } else if (message instanceof StopTopic) {
            System.out.println("topic " + keyword + " has " + subscribers.size() + " subscribers!");
            if (!subscribers.isEmpty()) {
                for (ActorRef subscriber : subscribers) {
                    context().parent().tell(new SubscribeToTopic(keyword), subscriber);
                }
            }
            context().stop(self());
            System.out.println("topic " + keyword + " removed!");
        }
    }
}
