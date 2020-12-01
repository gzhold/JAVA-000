package org.gz.data;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;



import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
public class MultiThreadSaveDataByHaikari implements CommandLineRunner {

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(MultiThreadSaveDataByHaikari.class);
    }


    private void batchsaveData() throws SQLException {
        StopWatch stopWatch = StopWatch.createStarted();
        for(int i = 0;i<1000;i++){
            jdbcTemplate.execute("insert  into t_user_0 (id, name, address) values(" + (count.getAndIncrement()) + ", 'test', '杭州')");
        }
        stopWatch.stop();
        log.info("foreach execution time: {} ms" , stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("batch insert data start......");
        for (int i = 0 ; i< 1000;i++){
            executor.execute(() ->{
                try {
                    batchsaveData();
                } catch (SQLException e) {
                    log.error("execute failed, error is {}", e);
                }
            });
        };
        executor.shutdown();

    }
}
