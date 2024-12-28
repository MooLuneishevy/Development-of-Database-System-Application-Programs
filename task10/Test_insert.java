package task10;
import java.sql.*;
import java.io.*;
public class Test_insert {
	public static void main(String[] args) throws Exception {
		Test_insert test = new Test_insert();
		Connection conn = test.createConnection();
		// Blob对象的插入的方法:
		try {
			// 创建插入语句.
			java.sql.PreparedStatement preparedStatement = conn.prepareStatement("insert into jlu.emp_photo values('000130','jpeg',?)");
			//创建文件对象:
			File file=new File("c:/backa_a.jpg");
			// 创建流对象:
			java.io.BufferedInputStream imageInput = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
			//参数赋值:
			preparedStatement.setBinaryStream(1, imageInput,(int) file.length());
			//执行语句
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		catch(java.io.FileNotFoundException ex) {
			ex.printStackTrace();
		}
		finally{
			try {
				conn.close();
			}
			catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}
	private Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
			conn = java.sql.DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
		}
		catch (SQLException ex1) {
			ex1.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return conn;
	}
}