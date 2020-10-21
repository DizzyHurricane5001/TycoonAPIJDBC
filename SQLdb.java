import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.*;

public class SQLdb {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private static JsonParser parser;
    private static JsonElement jsonTree;
    private static JsonObject jsonObject;
    private static JsonElement rootObject;
    private static JsonElement treeObject;

    public void connectDB() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String password = "";
            String address = "";
            connect = DriverManager
                    .getConnection("jdbc:mysql://"+address+"/sql2371581?&"
                            + "user=sql2371581&password=" +password);

        } catch (Exception e) {
            throw e;
        }
    }


    public void insertData(String ofType, int serverNumber,  String insertData) throws SQLException {

            switch (ofType) {
                case "serverData":
                    if (insertData != null) {
                        parser = new JsonParser();
                        jsonTree = parser.parse(insertData);
                        jsonObject = jsonTree.getAsJsonObject();
                        rootObject = jsonObject.get("server");
                        JsonObject treeObject = rootObject.getAsJsonObject();
                        String uptime = treeObject.get("uptime").toString();
                        String dxp_status = treeObject.get("dxp").toString();
                        String serverRegion = treeObject.get("region").toString();


                        preparedStatement = connect
                                .prepareStatement("insert into serverdata values (default, ?, ?, ?, ?, ? , ?)");
                        preparedStatement.setString(1,  ""+ serverNumber); // server id
                        preparedStatement.setString(2, dxp_status); // dxp status
                        preparedStatement.setString(3, uptime); // uptime
                        preparedStatement.setInt(4, 1); //dxp duration is status = true

                        // will remove
                        preparedStatement.setString(5,  "0"); // weather beta holder (current_weather)
                        preparedStatement.setString(6,  "0"); // weather beta holder (weather_duration)
                        
                        preparedStatement.executeUpdate();
                    }
                    break;
                case "flightData":
                    if (insertData != null && insertData.contains("name")) {
                        Pattern jsonWorkAround[] = {Pattern.compile("[0-9]+(?=\":)")/* first numid */ , Pattern.compile("[a-zA-Z ]+(?=\\\"\\,\\{)"),
                        Pattern.compile("[a-zA-Z ]+(?=\",\")")}; // mid, model, dest
                        Pattern pattern;
                        Matcher matcher;
                        ArrayList Modellist = new ArrayList();
                        ArrayList Midlist = new ArrayList();
                        ArrayList Destlist = new ArrayList();
                        for (int index = 0; index < jsonWorkAround.length; index++) {
                            pattern = jsonWorkAround[index];
                            matcher = pattern.matcher(insertData);
                            while (matcher.find()) {
                                switch(index) {
                                    case 0:
                                        Midlist.add(matcher.group());
                                        break;
                                    case 1:
                                        Modellist.add(matcher.group());
                                        System.out.println(Modellist.get(0));
                                        break;
                                    case 2:
                                        Destlist.add(matcher.group());
                                        break;
                                }
                            }
                        }
                        for (int arrayListIndex = 0; arrayListIndex < Midlist.size(); arrayListIndex ++) {
                            preparedStatement = connect
                                    .prepareStatement("insert into flightdata values (default, ?, ?, ?, ?)");
                            preparedStatement.setString(1, Modellist.get(arrayListIndex).toString());
                            preparedStatement.setString(2, Destlist.get(arrayListIndex).toString());
                            preparedStatement.setString(3, " ");
                            preparedStatement.setString(4, Midlist.get(arrayListIndex).toString());
                            preparedStatement.executeUpdate();
                        }
                     }
                    break;
                case "weatherData":
                    if (insertData != null) {
                        if (insertData.contains("weather")) {
                            Gson gson = new Gson();
                            jsonObject = gson.fromJson(insertData, JsonObject.class);
                            // System.out.println(jsonObject.get("current_weather"));
                            String sqlStatement =
                                    "update serverdata " +
                                            "set currentWeather = ?, " +
                                            "weatherDuration = ?" +
                                            "where serverid = ?";
                            preparedStatement = connect
                                    .prepareStatement(sqlStatement);
                            preparedStatement.setString(1, jsonObject.get("current_weather").toString());
                            preparedStatement.setString(2, jsonObject.get("time_remaining").toString());
                            preparedStatement.setString(3, "" + serverNumber);
                            preparedStatement.executeUpdate();
                        }
                    }
                    break;
            }
        }

    public void resetServerData(String table) throws SQLException {
        statement = connect.createStatement();
        String query = "DELETE from " + table;
        statement.executeUpdate(query);

    }

  public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}
