package ru.fizteh.fivt.students.egiby.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.TwitterException;

import ru.fizteh.fivt.students.egiby.twitterstream.library.*;

/**
 * Created by egiby on 24.09.15.
 */

public class TwitterStream {
    public static void main(String[] args) {
        JCommanderParams jcp = new JCommanderParams();
        JCommander jcm = new JCommander(jcp, args);

        if (jcp.isHelp()) {
            TwitterPrinter.printHelp(jcm);
            System.exit(0);
        }

        if (jcp.isStream()) {
            if (jcp.getNumberTweets() != null) {
                TwitterPrinter.printHelp(jcm);
                System.exit(0);
            }
            TwitterPrinter.getStream(jcp);
        } else {
            try {
                TwitterPrinter.printAllTweets(jcp);
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }
}
