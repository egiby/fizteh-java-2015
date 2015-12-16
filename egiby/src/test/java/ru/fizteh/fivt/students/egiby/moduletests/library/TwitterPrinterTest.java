package ru.fizteh.fivt.students.egiby.moduletests.library;

import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by egiby on 16.12.15.
 */

public class TwitterPrinterTest {
    private static final LocalDateTime CURRENT = LocalDateTime.of(2015, 12, 16, 23, 58, 0);
    private static final String VIR_MESSAGE =
            "I'm the Abrahami Linconi and I save narns!\nPS: Mr Morden will die...\n#Minbar #Shadow_war";

    private static final String ANSWER = "@Londo Mollari: ретвитнул @Vir Cotto: I'm the Abrahami Linconi and " +
            "I save narns!\n" +
            "PS: Mr Morden will die...\n" +
            "#Minbar #Shadow_war";
    @Test
    public void testPrintTweet() throws Exception {
        Status tweet1 = mock(Status.class);
        Status tweet2 = mock(Status.class);

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(user2.getScreenName()).thenReturn("Vir Cotto");
        when(user1.getScreenName()).thenReturn("Londo Mollari");

        when(tweet1.getCreatedAt()).thenReturn(Date.from(CURRENT.atZone(ZoneId.systemDefault()).toInstant()));
        when(tweet1.getUser()).thenReturn(user1);
        when(tweet1.isRetweet()).thenReturn(true);
        when(tweet1.getRetweetedStatus()).thenReturn(tweet2);

        when(tweet2.getUser()).thenReturn(user2);
        when(tweet2.getText()).thenReturn(VIR_MESSAGE);

        assertThat(TwitterPrinter.formatTweet(tweet1, true), is(ANSWER));
    }
}
