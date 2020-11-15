package test.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class LoginAuther {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/user")
    public String getUser(HttpServletRequest request){
        StringBuilder builder = new StringBuilder();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT id, name, balance FROM account";
            ResultSet rs = queryByStatement(connection, sql);

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                BigDecimal balance =  rs.getBigDecimal("balance");
//                System.out.println("id = " + id + ", name = " + name + ", balance = " + balance);
                builder.append("id = ");
                builder.append(id);
                builder.append(", name = ");
                builder.append(name);
                builder.append(", balance = ");
                builder.append(balance);
                builder.append("\r\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try{
                if(connection!=null) {
                    connection.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return builder.toString();
    }

    private ResultSet queryByStatement(Connection connection, String sql) throws SQLException {
        assert connection != null;
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

}
