package task10;
import java.sql.*;
import java.io.*;
public class Test {
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		Connection conn = test.createConnection();
		try {
			java.sql.Statement st=conn.createStatement();
			java.sql.ResultSet rs=st.executeQuery("select picture from jlu.emp_photo where empno='000150'");
			while(rs.next()){
				//读取Blob对象
				Blob blob= (Blob) rs.getBlob(1);
				//Blob对象转化为InputStream流
				java.io.InputStream inputStream =blob.getBinaryStream();
				//要写入的文件
				File fileOutput = new File("c:/backa.jpg");
				//文件的写入流的定义
				FileOutputStream fo = new FileOutputStream(fileOutput);
				int c;
				//读取流并写入到文件中
				while ((c = inputStream.read()) != -1)
					fo.write(c);
				//流的关闭:
				fo.close();
			}
		} 
		catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		catch(java.io.FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch(java.io.IOException ex){
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