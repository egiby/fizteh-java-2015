package ru.fizteh.fivt.students.egiby.moduletests.library;

import ru.fizteh.fivt.students.egiby.moduletests.TwitterStream;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

/**
 * Created by egiby on 16.12.15.
 */
public class GoogleAPIRequester {
    private static final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";

    private static String getAPIKey() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(TwitterStream.
                class.getResourceAsStream("/GoogleMapsAPI.properties")))) {
            String key = input.readLine();
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getGoogleAPIQuery(String location) {
        String key = getAPIKey();
        String json;

        try {
            json = HttpQuery.getQuery(URL + location + "&key=" + key);
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
