package com.luxoft.akkalabs.day1.futures;

import akka.dispatch.Mapper;
import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by artem on 08.12.14.
 */
public class FinalResultMapper extends Mapper<Result, FinalResult> {
    @Override
    public FinalResult apply(Result r){
        Map<String, Integer> result = new HashMap<>();
        for (TweetObject tweetObject : r.getTweets()) {
            Integer integer = result.get(tweetObject.getLanguage());
            if(integer == null){
                result.put(tweetObject.getLanguage(), 1);
            }else{
                result.put(tweetObject.getLanguage(), integer+1);
            }
        }
        return new FinalResult(r.getKeyword(), result);
    }
}
