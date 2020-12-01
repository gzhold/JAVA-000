package org.gz.data;

import java.sql.*;

public class JdbcUtil {

    private static String url;
    private static String username;
    private static String password;

    static {
        url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        username = "root";
        password = "Admin@123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);

    }


    public static void close(Statement stmt, ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
