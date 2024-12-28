package task3;
import java.sql.*;
import java.io.*;
public class task3 {
    // 加载JDBC
    static {
        try {
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        }
		catch (Exception e) {
            System.out.println("\n  Error loading DB2 Driver...\n");
            System.out.println(e);
            System.exit(1);
        }
    }
    public static void main(String args[]) throws Exception {
        // 定义所需变量
        String deptno = "";
        String sqlstmtFetch = "SELECT SALARY FROM jlu.staff WHERE DEPT = ?";
        String sqlstmtUpdate = "UPDATE jlu.staff SET SALARY = SALARY * 1.05 WHERE DEPT = ?";
        String s = " ";
        int mydeptno = 0;
        double newSalary;
        double maxSalary = 100000.0;
        // 输入处理
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Connect statement follows:");
        Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        System.out.println("Connect completed");
        sample.setAutoCommit(false);
        System.out.println("This program will update the salaries for a department");
        System.out.println("\n");
        System.out.println("Please enter a department number: \n ");
        s = in.readLine();
        deptno = s.substring(0, 2);
        mydeptno = Integer.parseInt(deptno);
        System.out.println("Statement stmt follows");
        try {
            // 创建准备语句以查询当前薪水
            PreparedStatement pstmtFetch = sample.prepareStatement(sqlstmtFetch);
            pstmtFetch.setInt(1, mydeptno);
            ResultSet rs = pstmtFetch.executeQuery();
            // 检查是否越界
            while (rs.next()) {
                double currentSalary = rs.getDouble("SALARY");
                newSalary = currentSalary * 1.05;
                // 检查新薪水是否超过最大值
                if (newSalary > maxSalary) {
                    System.out.println("员工薪水更新后将超过最大值100,000，当前薪水: " + currentSalary + ", 新薪水: " + newSalary);
                    return;
                }
            }
            // 执行SQL更新语句
            PreparedStatement pstmtUpdate = sample.prepareStatement(sqlstmtUpdate);
            pstmtUpdate.setInt(1, mydeptno);
            int updateCount = pstmtUpdate.executeUpdate();
            sample.commit();
            System.out.println("\nNumber of rows updated: " + updateCount);
        }
		catch (SQLException x) {
			sample.rollback();
            int SQLCode = x.getErrorCode();
            String SQLState = x.getSQLState();
            String Message = x.getMessage();
            System.out.println("\nSQLCODE:  " + SQLCode);
            System.out.println("\nSQLSTATE: " + SQLState);
            System.out.println("\nSQLERRM:  " + Message);
        }
		finally {
            // 确保连接关闭
            sample.close();
        }
        System.exit(0);
    }
}