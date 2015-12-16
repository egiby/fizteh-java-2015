package ru.fizteh.fivt.students.egiby.moduletests.library;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by egiby on 30.09.15.
 */
public class FormatUtils {
    private static final long FIVE = 5L;
    private static final long ONE = 1L;
    private static final long TEN = 10L;
    private static final long ELEVEN = 11L;
    private static final long HUNDRED = 100L;
    private static final long TWENTY = 20L;
    private static final String[] DAYS = {"день", "дня", "дней"};
    private static final String[] HOURS = {"час", "часа", "часов"};
    private static final String[] MINUTES = {"минуту", "минуты", "минут"};

    public static String formatNumber(long number, String[] form) {
        if (number % TEN == ONE && number % HUNDRED != ELEVEN) {
            return number + " " + form[0];
        }

        if (number % TEN > ONE && number % TEN < FIVE && (number % HUNDRED < ELEVEN || number % HUNDRED > TWENTY)) {
            return number + " " + form[1];
        }

        return number + " " + form[2];
    }

    public static String getTimeDiffer(LocalDateTime firstTime, LocalDateTime secondTime) {
        String timeString;

        if (ChronoUnit.MINUTES.between(firstTime, secondTime) < 2) {
            timeString = "только что";
        } else if (ChronoUnit.HOURS.between(firstTime, secondTime) < 1) {
            timeString = formatNumber(ChronoUnit.MINUTES.between(firstTime, secondTime), MINUTES) + " назад";
        } else if (ChronoUnit.HOURS.between(firstTime, secondTime) < secondTime.getHour()) {
            timeString = formatNumber(ChronoUnit.HOURS.between(firstTime, secondTime), HOURS) + " назад";
        } else if (ChronoUnit.DAYS.between(firstTime, secondTime) <= 1) {
            timeString = "вчера";
        } else {
            timeString = formatNumber(ChronoUnit.DAYS.between(firstTime, secondTime), DAYS) + " назад";
        }

        return "[" + timeString + "]";
    }
}
