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
        if (insertData != null) {
            parser = new JsonParser();
            jsonTree = parser.parse(insertData);
            jsonObject = jsonTree.getAsJsonObject();


            switch (ofType) {
                case "serverData":
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
                    break;
            }
        }
    }
    public void resetServerData() throws SQLException {
        statement = connect.createStatement();
        String query = "DELETE from serverdata";
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
