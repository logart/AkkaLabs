package com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/day1/wikitopics"})
public class WikipediaStream extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        System.out.println("sucsess");

        AsyncContext context = req.startAsync();
        WikipediaListener wikipediaListener = new WikipediaListenerImpl("test", context);
        ActorSystem actorSystem = (ActorSystem) getServletContext().getAttribute("actorSystem");
        ActorSelection actorSelection = actorSystem.actorSelection("/user/connections");
        actorSelection.tell(new Register(wikipediaListener), null);

        context.setTimeout(240000);
    }

}
