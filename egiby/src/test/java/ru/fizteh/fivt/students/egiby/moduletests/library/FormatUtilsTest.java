package ru.fizteh.fivt.students.egiby.moduletests.library;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by egiby on 16.12.15.
 */
public class FormatUtilsTest {

    private static LocalDateTime CURRENT_TIME = LocalDateTime.of(2015, 12, 16, 2, 4, 5);
    @Test
    public void testGetTime() throws Exception {
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2015, 12, 16, 2, 2, 30), CURRENT_TIME),
                is("[только что]"));
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2015, 12, 16, 2, 0, 0), CURRENT_TIME),
                is("[4 минуты назад]"));
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2015, 12, 16, 1, 0, 0), CURRENT_TIME),
                is("[1 час назад]"));
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2015, 12, 15, 22, 0, 0), CURRENT_TIME),
                is("[вчера]"));
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2015, 12, 11, 0, 15, 0), CURRENT_TIME),
                is("[5 дней назад]"));
        assertThat(FormatUtils.getTimeDiffer(LocalDateTime.of(2014, 12, 16, 2, 0, 0), CURRENT_TIME),
                is("[365 дней назад]"));
    }

    private static final String[] FORM = {"кошку", "кошки", "кошек"};

    @Test
    public void testFormatNumber() throws Exception {
        assertThat(FormatUtils.formatNumber(1, FORM), is("1 кошку")); // завести одну кошку ...
        assertThat(FormatUtils.formatNumber(2, FORM), is("2 кошки"));
        assertThat(FormatUtils.formatNumber(30, FORM), is("30 кошек"));
        assertThat(FormatUtils.formatNumber(11, FORM), is("11 кошек"));
        assertThat(FormatUtils.formatNumber(12332451, FORM), is("12332451 кошку"));
    }
}
