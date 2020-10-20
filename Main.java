public class Main {

    public static SQLdb db;

    public static void main (String[] args) throws Exception {
        db = new SQLdb(); // initialize SQL JDBC
        db.connectDB();
        boolean dbUpdate = true;

        db.insertData("flightData", 0, Tycoon.tycoonAPICall(0, "/status/airline.json"));
      // Tycoon.tycoonAPICall(0, "/status/airline.json");
     while (dbUpdate) {
         db.resetServerData("flightdata");
         db.resetServerData("serverdata");
            for (int x = 0; x < 9; x++) {
               db.insertData("serverData", x, Tycoon.tycoonAPICall(x,"/status/widget/players.json"));
                db.insertData("flightData", x, Tycoon.tycoonAPICall(x, "/status/airline.json"));
                db.insertData("flightData", x, Tycoon.tycoonAPICall(x, "/status/airline.json"));
                //Thread.sleep(100);
            }
            Thread.sleep(120000); // PAGE UPDATE EVERY 10 SECONDS.
        }
        db.close(); // end JDBC Connection
    }
}
