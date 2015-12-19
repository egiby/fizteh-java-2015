package ru.fizteh.fivt.students.egiby.moduletests;

import com.beust.jcommander.JCommander;
import ru.fizteh.fivt.students.egiby.moduletests.library.JCommanderParams;
import ru.fizteh.fivt.students.egiby.moduletests.library.TwitterPrinter;
import twitter4j.TwitterException;

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
            TwitterPrinter.getStream(jcp, System.out);
        } else {
            try {
                TwitterPrinter.printAllTweets(jcp, System.out);
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }
}
