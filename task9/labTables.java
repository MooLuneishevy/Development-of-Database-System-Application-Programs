package task9;
import java.sql.*;
public class labTables {
    static {
        try {
            // 加载 DB2 JDBC 驱动
            Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
        }
        catch (Exception e) {
            System.exit(1);
        }
    }
    public static void main(String args[]) throws Exception {
        try {
            // 建立与 DB2 数据库的连接
            Connection sample = DriverManager.getConnection("jdbc:db2:sample", "db2admin", "db2admin");
            DatabaseMetaData dbmd = sample.getMetaData();
            String[] tableTypes = {"TABLE", "VIEW"};
            ResultSet rs = dbmd.getTables("sample", "JLU", "%", tableTypes);
            // 遍历结果集
            while (rs.next()) {
                String s = rs.getString(1);
                System.out.println("\nCatalog Name: " + s + " Schema Name: " + rs.getString(2) + " Table Name: " + rs.getString(3));
            }
        } catch (Exception e) {
            System.out.println("\n Error MetaData Call");
            System.out.println("\n " + e);
            System.exit(1);
        }
    }
}