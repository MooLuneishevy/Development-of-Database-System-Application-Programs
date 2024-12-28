package task9;
import java.sql.*;
public class labposition{
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
    	String  salary = "";
    	String job = "";
        String intext = "\n NAME     JOB       SALARY\n";
        String indash = "--------  --------  --------------\n";
        String blanks = "                                                        ";
        String outline = "";
        System.out.println("Connect statement follows:");
        Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        System.out.println("Connect completed");
        sample.setAutoCommit(false);
        System.out.println("Statement stmt follows");
        try{
            ResultSet rs = null;
            String sql = "select NAME, JOB, SALARY from jlu.staff ";
            PreparedStatement stmt = sample.prepareStatement(sql, rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
            System.out.println( intext );
            System.out.println( indash );
            rs = stmt.executeQuery( );
            rs.next();
            name = rs.getString(1);
            job = rs.getString(2);
            salary = rs.getString(3);
            outline = (name + blanks.substring(0, 10 - name.length())) + (job + blanks.substring(0, 10 - job.length())) + (salary + blanks.substring(0, 12 - salary.length()));
            System.out.println("\n" + outline);
            rs.last();
            name = rs.getString(1);
            job = rs.getString(2);
            salary = rs.getString(3);
            outline = (name + blanks.substring(0, 10 - name.length())) + (job + blanks.substring(0, 10 - job.length())) + (salary + blanks.substring(0, 12 - salary.length()));
            System.out.println("\n" + outline);
            rs.previous();
            name = rs.getString(1);
            job = rs.getString(2);
            salary = rs.getString(3);
            outline = (name + blanks.substring(0, 10 - name.length())) + (job + blanks.substring(0, 10 - job.length())) + (salary + blanks.substring(0, 12 - salary.length()));
            System.out.println("\n" + outline);
            rs.first();
            name = rs.getString(1);
            job = rs.getString(2);
            salary = rs.getString(3);
            outline = (name + blanks.substring(0, 10 - name.length())) + (job + blanks.substring(0, 10 - job.length())) + (salary + blanks.substring(0, 12 - salary.length()));
            System.out.println("\n" + outline);
        }
        catch ( SQLException x ){
            System.out.println("Error on call " + x.getErrorCode() + " and sqlstate of " + x.getSQLState()  + " message " + x.getMessage() );
        }
        System.exit(0);
    }
}