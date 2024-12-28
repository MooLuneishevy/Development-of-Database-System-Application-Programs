package task8;
import java.sql.*;
public class labstaff1 {
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
        String name = "";
        String salary = " ";
        String job = "";
        String  intext = "\n NAME     JOB       SALARY\n";
        String  indash = "--------  --------  --------------\n";
        String blanks = "                                                        ";
        System.out.println("Connect statement follows:");
        Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        System.out.println("Connect completed");
        sample.setAutoCommit(false);
        System.out.println( intext );
        System.out.println( indash );
        try{
            Statement stmt = sample.createStatement();
            ResultSet rs = stmt.executeQuery( "select NAME, JOB, SALARY from jlu.staff Where ID = 10");
            boolean more = rs.next();
            while ( more ) {
                name = rs.getString(1);
                job = rs.getString(2);
                salary = rs.getString(3);
                String outline = (name + blanks.substring(0, 10 - name.length())) + (job + blanks.substring(0, 10 - job.length())) + (salary + blanks.substring(0, 12 - salary.length()));
                System.out.println("\n" + outline);
                more = rs.next();
            }
        }
        catch ( SQLException x ){
            System.out.println("Error on call " + x.getErrorCode() + " and sqlstate of " + x.getSQLState()  + " message " + x.getMessage() );
        }
        System.exit(0);
    }
}