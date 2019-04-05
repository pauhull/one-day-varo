import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class PlaytimeCounter {

    public static void main(String[] args) throws IOException, ParseException {

        File file = new File("E:\\candycane stats");
        if (!file.isDirectory()) return;

        JSONParser jsonParser = new JSONParser();

        long playtimeMinutes = 0;

        for (File json : file.listFiles()) {

            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(json));

            playtimeMinutes += (Long) jsonObject.get("stat.playOneMinute");
        }

        playtimeMinutes /= 20;
        playtimeMinutes /= 60;
        playtimeMinutes /= 60;

        System.out.println(playtimeMinutes);
    }
}
