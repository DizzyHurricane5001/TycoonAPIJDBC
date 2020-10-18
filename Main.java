public class Main {

    public static SQLdb db;

    public static void main (String[] args) throws Exception {
        db = new SQLdb(); // initialize SQL JDBC
        db.connectDB();
        db.insertData("serverData", 1, Tycoon.tycoonAPICall(1,"/status/widget/players.json"));
        db.close(); // end JDBC Connection
    }
}
