package org.gz.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class MultiThreadSaveDataDemo {

    private static final ExecutorService executor = Executors.newFixedThreadPool(16);

    private static AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) throws SQLException {
        //通过工具类获取数据库连接对象
        Connection con = JdbcUtil.getConnection();

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                saveData(con);
            });
            log.info("call save method"+i);
        };

        // close connection
    }

    private static void saveData(Connection connection) {
        try {
            StopWatch stopWatch = StopWatch.createStarted();

            String insertSQL = "insert  into t_user_1 (id, name, address) values(?,?,?)";
            PreparedStatement pst = connection.prepareStatement(insertSQL);
            for (int i = 0; i < 10000; i++) {
                pst.setInt(1, count.getAndIncrement());
                pst.setString(2, "test" + i);
                pst.setString(3, "杭州");
                pst.addBatch();
            }
            pst.executeBatch();

            JdbcUtil.close(pst, null);
            stopWatch.stop();
            log.info("thread execution time: {} ms" , stopWatch.getTime(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
