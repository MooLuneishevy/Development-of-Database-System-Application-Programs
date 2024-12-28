package task4;
import java.sql.*;
import javax.swing.*;
public class task4 {
    // 加载JDBC驱动
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
        String deptno = "";
        int mydeptno = 0;
        // SQL语句
        String sqlstmtUpdate = "UPDATE jlu.staff SET SALARY = SALARY * 1.05 WHERE DEPT = ?";
        String sqlstmtFetch = "SELECT NAME, SALARY FROM jlu.staff WHERE DEPT = ?";   
        System.out.println("Connect statement follows:");
        // 连接数据库
        Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        System.out.println("Connect completed");
        sample.setAutoCommit(false);
        System.out.println("This program will update the salaries for a department");
        System.out.println("\n");
        String s = JOptionPane.showInputDialog(null, "请输入要修改的部门编号:");
        deptno = s.substring(0, 2);
        mydeptno = Integer.parseInt(deptno);
        PreparedStatement pstmtFetch = sample.prepareStatement(sqlstmtFetch);
        pstmtFetch.setInt(1, mydeptno);
        // 首先执行查询 获得结果集 再次确认是否修改
        ResultSet rs = pstmtFetch.executeQuery();
        StringBuilder sb = new StringBuilder();
        sb.append("即将修改的员工薪水:\n");
        boolean exist = false;
        while (rs.next()) {
        	exist = true;
            String name = rs.getString("NAME");
            double salary = rs.getDouble("SALARY");
            sb.append("员工: ").append(name).append(", 当前薪水: ").append(salary).append("\n");
        }
        if (!exist) {
            JOptionPane.showMessageDialog(null, "没有找到该部门的员工.");
            sample.rollback();
            sample.close();
            System.exit(0);
        }
        int confirmation = JOptionPane.showConfirmDialog(null, sb.toString() + "\n您确认要修改这些员工的薪水吗?", "确认", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            // 执行更新语句
            PreparedStatement pstmtUpdate = sample.prepareStatement(sqlstmtUpdate);
            pstmtUpdate.setInt(1, mydeptno);
            int updateCount = pstmtUpdate.executeUpdate();
            sample.commit();
            JOptionPane.showMessageDialog(null, "成功修改了:" + updateCount + "行");
        }
        else {
            // 如果用户取消 回滚事务
            sample.rollback();
            JOptionPane.showMessageDialog(null, "操作已取消.");
        }
        sample.close();
        System.exit(0);
    }
}