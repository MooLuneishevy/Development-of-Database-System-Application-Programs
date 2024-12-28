package task1;
import java.sql.*;
class task1{
	// 加载JDBC驱动
	static{
		try{
			Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String argv[]){
		try{
			// 连接数据库
			Connection con=null;
			String url="jdbc:db2:sample";
			if(argv.length==0){
				con=DriverManager.getConnection(url);
			}
			else if(argv.length==2){
				String userid=argv[0];
				String passwd=argv[1];
				con=DriverManager.getConnection(url,userid,passwd);
			}
			else{
				throw new Exception("\n Usage: java MyJDBC[,username,password]\n");
			}
			// 建立Statement对象 执行SQL查询 检索salary大于40000的
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT EMPNO, LASTNAME "+" FROM jlu.employee "+" WHERE SALARY > 40000 ");
			while(rs.next()){
	            System.out.println("empno="+rs.getString(1)+"   "+"lastname="+rs.getString(2));
	        }
			// 释放资源
			rs.close();
			stmt.close();
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}