package ru.fizteh.fivt.students.egiby.twitterstream.library;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by egiby on 15.12.15.
 */
public class TwitterPrinter {
    private static final int DEFAULT_NUMBER_OF_TWEETS = 100;

    public static void printHelp(JCommander jcm) {
        jcm.usage();
    }

    public static void printAllTweets(JCommanderParams jcp) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcp.getKeyword());

        if (jcp.getLocation() != null) {
            LocationUtils.Location location = LocationUtils.getLocationByName(jcp.getLocation());
            query.setGeoCode(location.getCoordinates(), location.getRadius(), Query.KILOMETERS);
        }

        int numberOfTweets = 0;

        int limit = DEFAULT_NUMBER_OF_TWEETS;

        if (jcp.getNumberTweets() != null) {
            limit = jcp.getNumberTweets();
        }

        QueryResult result;
        while (numberOfTweets < limit && query != null) {
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (tweet.isRetweet() && jcp.isHideRetweets()) {
                    continue;
                }

                System.out.println(FormatUtils.formatTweet(tweet, false));

                numberOfTweets++;
                if (numberOfTweets == limit) {
                    return;
                }
            }

            query = result.nextQuery();
        }
    }

    public static void getStream(JCommanderParams jcp) {
        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcp.isHideRetweets() && tweet.isRetweet()) {
                    return;
                }

                System.out.println(FormatUtils.formatTweet(tweet, true));

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        };

        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        FilterQuery filter = new FilterQuery(jcp.getKeyword());

        if (jcp.getLocation() != null) {
            filter.locations(LocationUtils.getLocationByName(jcp.getLocation()).getCoordinateBox());
        }

        twitterStream.filter(filter);
    }
}
