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
                        //String dxp_status = treeObject.get("dxp").toString();

                        //JsonArray jarray = jsonObject.getAsJsonArray("server");
                        String uptime = treeObject.get("uptime").toString();
                        String dxp_status = treeObject.get("dxp").toString();
                        String serverRegion = treeObject.get("region").toString();


                        preparedStatement = connect
                                .prepareStatement("insert into serverdata values (default, ?, ?, ?, ?,default,?)");
                        preparedStatement.setString(1, serverRegion + "" + serverNumber); // server id
                        preparedStatement.setString(2, dxp_status); // dxp status
                        preparedStatement.setString(3, uptime); // uptime
                        preparedStatement.setInt(4, 1); //dxp duration is status = true
                        preparedStatement.setInt(5, 0); //dxp expired true false
                        //    preparedStatement.setTimestamp(5, );
                        preparedStatement.executeUpdate(); //insert
                    }
                    break;
                case "flightData":
                    if (insertData != null) {
                        Pattern p = Pattern.compile("[0-9]+(?=])");//. represents single character
                        Matcher m;
                        String airRoutes[] = insertData.split("\"");
                        String allowedAircraft[] = {"Nimbus", "Vestra", "Shamal", "Luxor", "Boeing 737-200", "Dash-8" , "Velum", "Miljet", "Boeing",
                        "Airbus"};
                        String allowedDestinations[] = {"Chianski", "LSIA", "Pacific", "SSIA", "Mount", "Post", "POST", "Paleto", "Zancudo", "Chumash", "Aircraft", "Sandy",
                                "Francis", "Mckenzie"};
                        int newLength = 0;
                        String formattedArray[][] = new String[100][3];
                        for (int x = 0; x < airRoutes.length; x++) { //m = p.matcher(airRoutes[x]);
                            m = p.matcher(airRoutes[x]);
                            if (m.find()) {
                             formattedArray[newLength][2] = m.group(0);
                                System.out.println("Java regex response: " + formattedArray[newLength][2]);
                            }

                            for (int y = 0; y < allowedAircraft.length; y++) {
                                if (airRoutes[x].contains(allowedAircraft[y])) {
                                    formattedArray[newLength][0] = airRoutes[x];
                                    //System.out.println(formattedArray[newLength][0]);
                                    break;
                                }
                            }

                            for (int z = 0; z < allowedDestinations.length; z++) {
                                if (airRoutes[x].contains(allowedDestinations[z])) {
                                    formattedArray[newLength][1] = airRoutes[x];
                                    //System.out.println(formattedArray[newLength][1]);
                                    newLength += 1;
                                    break;
                                }
                            }


                        } // new array formatted

                        for (int q = 0; q < formattedArray.length; q++) {
                            if (formattedArray[q][0] != null && formattedArray[q][1] != null && formattedArray[q][2] != null) {
                                System.out.println("Model:" + formattedArray[q][0] + " To: " + formattedArray[q][1] + " Player ID: " + formattedArray[q][2]);
                                preparedStatement = connect
                                        .prepareStatement("insert into flightdata values (default, ?, ?, ?, ?)");
                                preparedStatement.setString(1, formattedArray[q][0]);
                                preparedStatement.setString(2, formattedArray[q][1]);
                                preparedStatement.setString(3, "PlaceHolder");
                                preparedStatement.setString(4, formattedArray[q][2]);
                                preparedStatement.executeUpdate();
                            } else {
                               // System.out.println("Something was null with DB input");
                            }

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
