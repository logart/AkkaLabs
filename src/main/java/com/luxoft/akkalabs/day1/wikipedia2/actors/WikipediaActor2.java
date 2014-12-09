package com.luxoft.akkalabs.day1.wikipedia2.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import com.luxoft.akkalabs.clients.wikipedia.WikipediaClient;
import com.luxoft.akkalabs.clients.wikipedia.WikipediaPage;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.Deliver;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikipediaActor2 extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            final String url = (String) message;
            if (url.length() > 8 && url.contains("wikipedia.org")) {
                Futures.future(new Callable<WikipediaPage>() {

                    public WikipediaPage call() {
                        Pattern re = Pattern.compile("https?://(\\w{2}).wikipedia.org/wiki/(.*)");

                        Matcher matcher = re.matcher(url);
                        if (!matcher.matches()) {
                            System.out.println(url + " does not match");
                        }

                        String lang = matcher.group(1);
                        String term = matcher.group(2);

                        WikipediaPage page = WikipediaClient.getPage(lang, term, url);
                        if (page != null) {
                            getContext().system().actorSelection("/user/connections").tell(new Deliver(page), getSelf());
                            getContext().system().actorSelection("/user/statistic").tell(new GarbageStatistic(url, term), getSelf());
                        }

                        return page;
                    }
                }, getContext().system().dispatcher());
            }
        }
    }
}
