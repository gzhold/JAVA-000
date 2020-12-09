package org.gz.xa;


import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class MainAppDemo {

    private static String configFile = "/META-INF/application.yaml";

    public static void main(String[] args) throws IOException, SQLException {
        DataSource dataSource = getDatasource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        process(jdbcTemplate);
    }


    private static void process(final JdbcTemplate jdbcTemplate) {
        TransactionTypeHolder.set(TransactionType.XA);
        System.out.println("############### start commit transaction ################");
        jdbcTemplate.execute((ConnectionCallback<Object>) connection -> {
            connection.setAutoCommit(false);
            int result;
            try {
                result = doInsert(connection);
                printData(jdbcTemplate, "----------------- query all before commit ------------------");
                connection.commit();
                printData(jdbcTemplate, "----------------- query all after  commit ------------------");
            } catch (final SQLException ex) {
                connection.rollback();
                throw ex;
            }
            return result;
        });
        System.out.println("############### start rollback transaction ################");
        TransactionTypeHolder.set(TransactionType.XA);
        jdbcTemplate.execute((ConnectionCallback<Object>) connection -> {
            connection.setAutoCommit(false);
            doInsert(connection);
            connection.rollback();
            printData(jdbcTemplate, "----------------- query all after rollback ------------------");
            return null;
        });
    }

    private static int doInsert(final Connection connection) throws SQLException {
        int updateCount = 0;
        String sql = "INSERT INTO t_order (user_id, status) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 10; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "2");
                updateCount += preparedStatement.executeUpdate();
            }
            return updateCount;
        }
    }

    private static void printData(final JdbcTemplate jdbcTemplate, final String title) {
        System.out.println(title);
        List<Map<String, Object>> data = jdbcTemplate.queryForList("SELECT * FROM t_order");
        if (data.isEmpty()) {
            System.out.println("t_order is empty");
            return;
        }
        for (Map<String, Object> each : data) {
            System.out.println(each.toString());
        }
    }


    private static DataSource getDatasource() throws IOException, SQLException {
        return YamlShardingSphereDataSourceFactory.createDataSource(getFile(configFile));
    }

    private static File getFile(final String fileName) {
        return new File(Thread.currentThread().getClass().getResource(fileName).getFile());
    }


}
