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
                    "http://na.tycoon.community:30122",
                    "http://na.tycoon.community:30123",
                    "http://na.tycoon.community:30124",
                    "http://na.tycoon.community:30125"};

    private static String TycoonAPIKey = "TYCOON_API_KEY";
    private static URL url;
    private static URLConnection request;

    public static String tycoonAPICall(int num, String directory) throws IOException {
        try {
            String auth = TycoonAPIKey;
            url = new URL(serverList[num] + directory);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(serverList[num] + directory);
            request.addHeader("X-Tycoon-Key", TycoonAPIKey);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return result;
            }
        } catch (Exception e) {
            System.out.println("Server " + num + "refused the connection!" + e);
        }
        return null;
    }
}
