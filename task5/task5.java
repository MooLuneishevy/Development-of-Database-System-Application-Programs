package task5;
import java.sql.*;
import javax.swing.*;
public class task5 {
    static {
        // 加载JDBC驱动
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
        // 连接DB2数据库
        Connection conn = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        System.out.println("连接成功");
        conn.setAutoCommit(false);
        // 选择操作
        String oper = JOptionPane.showInputDialog(null, "请选择操作: 1. 单行插入 2. 多行插入 3. 子查询插入");
        int operation = Integer.parseInt(oper);
        switch (operation) {
            case 1:
                insertSingleRow(conn);
                break;
            case 2:
                insertMultipleRows(conn);
                break;
            case 3:
                insertWithSubquery(conn);
                break;
            default:
                System.out.println("输入无效");
        }
        conn.close();
        System.exit(0);
    }
    // 插入单行数据
    public static void insertSingleRow(Connection conn) {
        try {
            String empno = JOptionPane.showInputDialog("请输入员工编号:");
            String firstName = JOptionPane.showInputDialog("请输入名字:");
            String midInit = JOptionPane.showInputDialog("请输入中间名首字母:");
            String lastName = JOptionPane.showInputDialog("请输入姓:");
            String workDept = JOptionPane.showInputDialog("请输入部门:");
            String phoneNo = JOptionPane.showInputDialog("请输入电话:");
            String hireDateStr = JOptionPane.showInputDialog("请输入雇佣日期 (yyyy-mm-dd):");
            String job = JOptionPane.showInputDialog("请输入职位:");
            short edLevel = Short.parseShort(JOptionPane.showInputDialog("请输入教育等级:"));
            String sex = JOptionPane.showInputDialog("请输入性别:");
            String birthDateStr = JOptionPane.showInputDialog("请输入出生日期 (yyyy-mm-dd):");
            double salary = Double.parseDouble(JOptionPane.showInputDialog("请输入薪水:"));
            double bonus = Double.parseDouble(JOptionPane.showInputDialog("请输入奖金:"));
            double comm = Double.parseDouble(JOptionPane.showInputDialog("请输入佣金:"));
            // 提示确认信息
            StringBuilder sb = new StringBuilder();
            sb.append("即将插入的员工信息:\n")
              .append("员工编号: ").append(empno).append("\n")
              .append("名字: ").append(firstName).append("\n")
              .append("中间名首字母: ").append(midInit).append("\n")
              .append("姓: ").append(lastName).append("\n")
              .append("部门: ").append(workDept).append("\n")
              .append("电话: ").append(phoneNo).append("\n")
              .append("雇佣日期: ").append(hireDateStr).append("\n")
              .append("职位: ").append(job).append("\n")
              .append("教育等级: ").append(edLevel).append("\n")
              .append("性别: ").append(sex).append("\n")
              .append("出生日期: ").append(birthDateStr).append("\n")
              .append("薪水: ").append(salary).append("\n")
              .append("奖金: ").append(bonus).append("\n")
              .append("佣金: ").append(comm).append("\n");
            int confirmation = JOptionPane.showConfirmDialog(null, sb.toString() + "\n您确认要插入这些信息吗?", "确认", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                String sqlstmt = "INSERT INTO templ (EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT, PHONENO, HIREDATE, JOB, EDLEVEL, SEX, BIRTHDATE, SALARY, BONUS, COMM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sqlstmt);
                pstmt.setString(1, empno);
                pstmt.setString(2, firstName);
                pstmt.setString(3, midInit);
                pstmt.setString(4, lastName);
                pstmt.setString(5, workDept);
                pstmt.setString(6, phoneNo);
                pstmt.setDate(7, java.sql.Date.valueOf(hireDateStr));
                pstmt.setString(8, job);
                pstmt.setShort(9, edLevel);
                pstmt.setString(10, sex);
                pstmt.setDate(11, java.sql.Date.valueOf(birthDateStr));
                pstmt.setDouble(12, salary);
                pstmt.setDouble(13, bonus);
                pstmt.setDouble(14, comm);
                // 执行语句并提交事务
                int rowCount = pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "成功插入 " + rowCount + " 行");
                conn.commit();
            }
            else {
                conn.rollback();
                JOptionPane.showMessageDialog(null, "操作已取消");
            }
        }
        catch (SQLException e) {
            handleSQLException(e);
        }
    }
    // 实现插入多行数据
    public static void insertMultipleRows(Connection conn) {
        try {
            int numRows = Integer.parseInt(JOptionPane.showInputDialog("请输入要插入的行数:"));
            for (int i = 0; i < numRows; i++) {
                int confirmation = JOptionPane.showConfirmDialog(null, "即将插入第 " + (i + 1) + " 行员工信息，您确认开始吗?", "确认", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    insertSingleRow(conn);
                }
                else {
                    JOptionPane.showMessageDialog(null, "操作已取消");
                    break;
                }
            }
            conn.commit();
        }
        catch (SQLException e) {
            handleSQLException(e);
        }
    }
    // 子查询插入函数
    public static void insertWithSubquery(Connection conn) {
        try {
            String sqlstmt = "INSERT INTO templ (EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT, PHONENO, HIREDATE, JOB, EDLEVEL, SEX, BIRTHDATE, SALARY, BONUS, COMM) " +
                             "SELECT EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT, PHONENO, HIREDATE, JOB, EDLEVEL, SEX, BIRTHDATE, SALARY, BONUS, COMM " +
                             "FROM employee WHERE EMPNO NOT IN (SELECT EMPNO FROM templ)";
            PreparedStatement pstmt = conn.prepareStatement(sqlstmt);
            int rowCount = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "成功插入 " + rowCount + " 行");
            conn.commit();
        }
        catch (SQLException e) {
            handleSQLException(e);
        }
    }
    public static void handleSQLException(SQLException e) {
        int SQLCode = e.getErrorCode();
        String SQLState = e.getSQLState();
        String Message = e.getMessage();
        System.out.println("\nSQLCODE: " + SQLCode);
        System.out.println("\nSQLSTATE: " + SQLState);
        System.out.println("\nSQLERRM: " + Message);
    }
}