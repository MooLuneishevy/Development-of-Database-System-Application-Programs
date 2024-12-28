package task11;
import java.sql.*;
public class task11 {
    static {
        try {
            // 加载DB2数据库的JDBC驱动
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        } catch (Exception e) {
            System.out.println("\n Error loading DB2 Driver... \n");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException {
        // 定义员工编号 位置索引 预准备语句
        String empnum = "000130";
        int startper = 0, startper1, startdpt = 0;
        PreparedStatement stmt1, stmt2, stmt3 = null;
        // 定义SQL语句 员工编号与简历格式 简历 结果集
        String sql1, sql2, sql3 = null;
        String empno, resumefmt = null;
        Clob resumelob = null;
        ResultSet rs1, rs2, rs3 = null;
        Connection con = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
        // 查询简历中'Personal'的起始位置
        sql1 = "SELECT POSSTR(RESUME,'Personal') "
                + "FROM jlu.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt1 = con.prepareStatement(sql1);
        stmt1.setString(1, empnum);
        rs1 = stmt1.executeQuery();
        // 遍历结果集 获取位置索引
        while (rs1.next()) {
            startper = rs1.getInt(1);
        }
        // 查询简历中'Department'的起始位置
        sql2 = "SELECT POSSTR(RESUME,'Department') "
                + "FROM jlu.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt2 = con.prepareStatement(sql2);
        stmt2.setString(1, empnum);
        rs2 = stmt2.executeQuery();
        // 遍历结果集 获取位置索引
        while (rs2.next()) {
            startdpt = rs2.getInt(1);
        }
        // 计算'Personal'的结束位置
        startper1 = startper - 1;
        // 查询指定范围的简历内容
        sql3 = "SELECT EMPNO, RESUME_FORMAT, "
                + "SUBSTR(RESUME,1,?)|| SUBSTR(RESUME,?) AS RESUME "
                + "FROM jlu.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        stmt3 = con.prepareStatement(sql3);
        stmt3.setInt(1, startper1);
        stmt3.setInt(2, startdpt);
        stmt3.setString(3, empnum);
        rs3 = stmt3.executeQuery();
        // 处理查询结果 获取员工信息和简历内容
        empno = null;
        String resumeout = null;
        long len = 0;
        int len1 = 0;
        while (rs3.next()) {
            empno = rs3.getString(1);
            resumefmt = rs3.getString(2);
            resumelob = rs3.getClob(3);
            len = resumelob.length();
            len1 = (int)len;
            resumeout = resumelob.getSubString(1, len1);
        }
        System.out.println("员工编号: " + empno + "\n");
        System.out.println("简历格式: " + resumefmt + "\n");
        System.out.println("简历长度: " + len1 + "\n");
        System.out.println("简历内容: " + resumeout + "\n");
    }
}