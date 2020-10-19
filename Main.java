public class Main {

    public static SQLdb db;

    public static void main (String[] args) throws Exception {
        db = new SQLdb(); // initialize SQL JDBC
        db.connectDB();
        db.resetServerData();
        boolean dbUpdate = true;
        while (dbUpdate) {
            for (int x = 0; x < 10; x++) {
                db.insertData("serverData", x, Tycoon.tycoonAPICall(x,"/status/widget/players.json"));
            }
            Thread.sleep(10000); // PAGE/DB UPDATE EVERY 10 SECONDS. 
        }
        db.close(); // end JDBC Connection
    }
}
