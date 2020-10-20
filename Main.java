public class Main {

    public static SQLdb db;

    public static void main (String[] args) throws Exception {
        db = new SQLdb(); 
        db.connectDB();
        final int sleepTime = 120000;
        boolean dbUpdate = true;
     while (dbUpdate) {
         db.resetServerData("flightdata");
         db.resetServerData("serverdata");
            for (int x = 0; x < 9; x++) {
               db.insertData("serverData", x, Tycoon.tycoonAPICall(x,"/status/widget/players.json"));
                db.insertData("flightData", x, Tycoon.tycoonAPICall(x, "/status/airline.json"));
            }
            Thread.sleep(sleepTime); 
        }
        db.close(); 
    }
}
