package task9;
import java.sql.*;
public class labaddbatch{
    static{
        try{
            Class.forName ("COM.ibm.db2.jdbc.app.DB2Driver");
        }
        catch (Exception e){
            System.out.println ("\n  Error loading DB2 Driver...\n");
            System.out.println (e);
            System.exit(1);
        }
    }
    public static void main( String args[]) throws Exception{
        try {
            System.out.println("Connect statement follows:");
            Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
            System.out.println("Connect completed");
            sample.setAutoCommit(false);
            System.out.println("\nAutocommit set off");
            Statement stmt = sample.createStatement();
            System.out.println("\n Batch Statements begin ");
            stmt.addBatch("INSERT INTO jlu.DEPARTMENT " + "VALUES ('BT6','BATCH6 NEWYORK','BBBBB1','BTT','NEW YORK CITY6')");
            stmt.addBatch("INSERT INTO jlu.DEPARTMENT " + "VALUES ('BT7','BATCH7 NEWYORK','BBBBB2','BT2','NEW YORK CITY7')");
            stmt.addBatch("INSERT INTO jlu.DEPARTMENT " + "VALUES ('BT8','BATCH8 NEWYORK','BBBBB3','BT3','NEW YORK CITY8')");
            stmt.addBatch("INSERT INTO jlu.DEPARTMENT " + "VALUES ('BT9','BATCH9 NEWYORK','BBBBB4','BT4','NEW YORK CITY9')");
            stmt.addBatch("INSERT INTO jlu.DEPARTMENT " + "VALUES ('BTA','BATCH10 NEWYORK','BBBBB5','BT5','NEW YORK CITY10')");
            System.out.println("\n Batch statements completed executeBatch follows");
            int [] updateCounts = stmt.executeBatch();
            for (int i = 0; i < updateCounts.length; i++) {
                System.out.println("\nUpdate row count " + updateCounts[i] );
            }
            sample.commit();
        }
        catch (SQLException e){
            System.out.println("\n SQLState: " + e.getSQLState() + " SQLCode: " + e.getErrorCode());
            System.out.println("\n Message " + e);
            System.out.println("\n Get Error Message: " + e.getMessage());
        }
    }
}