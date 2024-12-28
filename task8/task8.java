package task8;
import java.sql.*;
import javax.swing.*;
public class task8 {
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
        String oper = JOptionPane.showInputDialog(null, "请选择操作: 1. 单行插入 2. 多行插入 3. 子查询插入 4. 修改属性");
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
            case 4:
                updateEmployee(conn);
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
    public static void updateEmployee(Connection conn) {
        try {
            // 查询员工信息
            String selectSql = "SELECT * FROM templ";
            PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
            ResultSet rs = selectPstmt.executeQuery();
            int index = 0;
            while (rs.next())
                index++;
            if (index == 0) {
                JOptionPane.showMessageDialog(null, "没有找到员工记录");
                return;
            }
            // 提示用户输入需要修改的行数
            String rowInput = JOptionPane.showInputDialog("请输入想要修改的行数 (1-" + (index - 1) + "):");
            // 验证输入的行数是否合法
            int rowNumber = Integer.parseInt(rowInput);
            if (rowNumber < 1 || rowNumber >= index) {
                JOptionPane.showMessageDialog(null, "输入的行数无效！");
                return;
            }
            // 使用游标再次查询以获得选定行的信息
            rs = selectPstmt.executeQuery();
            for (int i = 0; i < rowNumber; i++)
                rs.next();
            String empno = rs.getString("EMPNO");
            String currentFirstName = rs.getString("FIRSTNME");
            String currentLastName = rs.getString("LASTNAME");
            String currentMidInit = rs.getString("MIDINIT");
            String currentWorkDept = rs.getString("WORKDEPT");
            String currentPhoneNo = rs.getString("PHONENO");
            String currentHireDate = rs.getString("HIREDATE");
            String currentJob = rs.getString("JOB");
            String currentEdLevel = rs.getString("EDLEVEL");
            String currentSex = rs.getString("SEX");
            String currentBirthDate = rs.getString("BIRTHDATE");
            String currentSalary = rs.getString("SALARY");
            String currentBonus = rs.getString("BONUS");
            String currentComm = rs.getString("COMM");
            // 提供属性选择
            String property = JOptionPane.showInputDialog("当前信息:\n" +
                    "名字: " + currentFirstName + "\n" +
                    "中间名缩写: " + currentMidInit + "\n" +
                    "姓: " + currentLastName + "\n" +
                    "工作部门: " + currentWorkDept + "\n" +
                    "电话号码: " + currentPhoneNo + "\n" +
                    "雇佣日期: " + currentHireDate + "\n" +
                    "职位: " + currentJob + "\n" +
                    "教育水平: " + currentEdLevel + "\n" +
                    "性别: " + currentSex + "\n" +
                    "出生日期: " + currentBirthDate + "\n" +
                    "工资: " + currentSalary + "\n" +
                    "奖金: " + currentBonus + "\n" +
                    "佣金: " + currentComm + "\n" +
                    "您想修改的属性:");
            // 根据用户输入的属性查询新值
            String newValue = "";
            if ("FIRSTNME".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的名字:", currentFirstName);
            else if ("MIDINIT".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的中间名缩写:", currentMidInit);
            else if ("LASTNAME".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的姓:", currentLastName);
            else if ("WORKDEPT".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的工作部门:", currentWorkDept);
            else if ("PHONENO".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的电话号码:", currentPhoneNo);
            else if ("HIREDATE".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的雇佣日期:", currentHireDate);
            else if ("JOB".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的职位:", currentJob);
            else if ("EDLEVEL".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的教育水平:", currentEdLevel);
            else if ("SEX".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的性别:", currentSex);
            else if ("BIRTHDATE".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的出生日期:", currentBirthDate);
            else if ("SALARY".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的工资:", currentSalary);
            else if ("BONUS".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的奖金:", currentBonus);
            else if ("COMM".equalsIgnoreCase(property))
                newValue = JOptionPane.showInputDialog("请输入新的佣金:", currentComm);
            else {
                JOptionPane.showMessageDialog(null, "输入的属性无效！");
                return;
            }
            // 更新SQL语句
            String updateSql = "UPDATE templ SET " + property + " = ? WHERE EMPNO = ?";
            PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
            updatePstmt.setString(1, newValue);
            updatePstmt.setString(2, empno);
            // 执行更新并提交事务
            int rowAffected = updatePstmt.executeUpdate();
            conn.commit();
            JOptionPane.showMessageDialog(null, "成功更新 " + rowAffected + " 行");
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