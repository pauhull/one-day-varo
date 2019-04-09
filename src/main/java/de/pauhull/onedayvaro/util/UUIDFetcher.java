package de.pauhull.onedayvaro.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {

    private static final String API_URL = "https://api.minetools.eu/uuid/%s";

    public String getNameSync(UUID uuid) {

        try {

            URL url = new URL(String.format(API_URL, uuid.toString().replace("-", "")));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JSONObject object = (JSONObject) new JSONParser().parse(reader);
            String name = object.get("name").toString();

            reader.close();
            connection.disconnect();

            return name;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

}
