package ru.fizteh.fivt.students.egiby.moduletests.library;

import org.apache.commons.io.IOUtils;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by egiby on 04.10.15.
 */
public class HttpQuery {
    public static String getQuery(String url) throws MalformedURLException {
        URL answer = new URL(url);

        try (InputStreamReader input = new InputStreamReader(answer.openStream())) {
            return IOUtils.toString(input);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
