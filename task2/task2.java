package task2;
import java.sql.*;
public class task2{
	// 加载JDBC驱动
  	static{
    	try{
      		Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
    	}
    	catch(Exception e){
      		System.out.println("\n  Error loading DB2 Driver...\n");
      		System.out.println(e);
      		System.exit(1);
    	}
  	}
  	public static void main(String args[])throws Exception{
		// 字符串初始化
		String name="";
		String salary=" ";
		String job="";
		String intext="\n NAME     JOB       SALARY\n";
		String indash="--------  --------  --------------\n";
		String blanks="                                                        ";
		System.out.println("Connect statement follows:");
		// 数据库连接
		Connection sample=DriverManager.getConnection("jdbc:db2:sample");
		System.out.println("Connect completed");
		sample.setAutoCommit(false);
		System.out.println(intext);
		System.out.println(indash);
		try{
			// 执行SQL查询语句 获取ID为10的员工
			Statement stmt=sample.createStatement();
			ResultSet rs=stmt.executeQuery("select NAME, JOB, SALARY from jlu.staff Where ID = 10");
			boolean more=rs.next();
			while(more){
				name=rs.getString(1);
				job=rs.getString(2);
				salary=rs.getString(3);
				String outline=(name+blanks.substring(0,10-name.length()))+(job+blanks.substring(0,10-job.length()))+(salary+blanks.substring(0,12-salary.length()));
				System.out.println("\n"+outline);
				more=rs.next();
			}
		}
		catch(SQLException x){
			System.out.println("Error on call "+x.getErrorCode()+" and sqlstate of "+x.getSQLState()+" message "+x.getMessage());
		}
		System.exit(0);
  	}
}