package com.coderevisited.twitter;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * User :  Suresh
 * Date :  27/06/15
 * Version : v1
 */
public class SampleStream {

    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String TOKEN = "";
    private static final String SECRET = "";

    public static void main(String[] args) throws InterruptedException {

        BasicConfigurator.configure();

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);
        StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
        endpoint.stallWarnings(true);
        Authentication authentication = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, SECRET);
        BasicClient client = new ClientBuilder().name("Sajjs-Client").hosts(Constants.STREAM_HOST).endpoint(endpoint)
                .authentication(authentication).processor(new StringDelimitedProcessor(queue)).build();
        client.connect();

        for (int i = 0; i < 100 && !client.isDone(); i++) {
            String message = queue.poll(5, TimeUnit.SECONDS);
            if (message != null) {
                System.out.println("Message " + message);
            }
        }

        client.stop();
        System.out.printf("Num of messages read so far " + client.getStatsTracker().getNumMessages());
    }
}
