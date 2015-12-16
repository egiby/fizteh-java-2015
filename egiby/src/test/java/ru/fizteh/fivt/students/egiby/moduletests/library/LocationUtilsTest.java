package ru.fizteh.fivt.students.egiby.moduletests.library;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.fizteh.fivt.students.egiby.moduletests.TwitterStream;
import org.apache.commons.io.IOUtils;
import twitter4j.GeoLocation;
import twitter4j.JSONObject;

import java.io.*;

import static org.mockito.Matchers.any;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by egiby on 16.12.15.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocationUtils.class, GoogleAPIRequester.class})
public class LocationUtilsTest {
    private static JSONObject json;

    private static final double LONDON_LAT = 51.5073509;
    private static final double LONDON_LNG = -0.1277583;

    private static final double [] LONDON_SOUTHWEST = {51.38494009999999, -0.3514683};
    private static final double [] LONDON_NORTHEAST = {51.6723432, 0.148271};

    @BeforeClass
    public static void getJSON() throws Exception {
        try(InputStreamReader input = new InputStreamReader(TwitterStream.class.
                getResourceAsStream("/GoogleAPISampleAnswer.json"))) {
            json = new JSONObject(IOUtils.toString(input));
        }
    }

    @Test
    public void testGetLocationByName() throws Exception {
        PowerMockito.mockStatic(GoogleAPIRequester.class);
        PowerMockito.when(GoogleAPIRequester.getGoogleAPIQuery(any(String.class))).thenReturn(json);

        LocationUtils.Location location = LocationUtils.getLocationByName("London");
        assertThat(location.getCoordinates(), is(new GeoLocation(LONDON_LAT, LONDON_LNG)));
        assertThat(location.getCoordinateBox()[0], is(LONDON_SOUTHWEST));
        assertThat(location.getCoordinateBox()[1], is(LONDON_NORTHEAST));
    }
}
