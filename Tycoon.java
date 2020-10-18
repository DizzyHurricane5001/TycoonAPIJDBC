import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
/*
  @AUTHOR: Tom K B (Tom5001/Dizzy Hurricane)
  Notes: Simple Transport Tycoon (GTA V) API calling class. Feel free to use if credit is retained.
  Thanks to Laughlan for some guidance on the API.


 */
public class Tycoon {
    private static final String[] serverList =
            {"http://server.tycoon.community:30120",
            "http://server.tycoon.community:30122",
            "http://server.tycoon.community:30123",
            "http://server.tycoon.community:30124",
            "http://server.tycoon.community:30125",
            "http://na.tycoon.community:30120",
            "http://na.tycoon.community:30122",
            "http://na.tycoon.community:30123",
            "http://na.tycoon.community:30124",
                    "http://na.tycoon.community:30124"};

    private static String TycoonAPIKey = "Tycoon_API_KEY"; // /api key new
    private static URL url;
    private static URLConnection request;

    public static String tycoonAPICall(int num, String directory) throws IOException { //0-8
        try {
                url = new URL( serverList[num] + directory);
                request = url.openConnection();
                request.setConnectTimeout(500);
                request.setRequestProperty("X-Tycoon-Key", TycoonAPIKey);
                request.connect();
                System.out.println("Did server number:" + num);
                return pageReader(url);
        } catch (Exception e) {
            System.out.println("error URL");
        }
        return pageReader(url);
    }


    private static String pageReader(URL connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.openStream()));
        String line;
        while ((line = reader.readLine()) != null)
        {
           // System.out.println(line);
            return line;
        }
        reader.close();
        return "";
    }
}
