package thread;


import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class PrintThreadReturn {

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(8, 8, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(48),
                new BasicThreadFactory.Builder().namingPattern("thread-%d").build(),
                (r, executor) -> System.out.println("The default thread pool is full!!"));

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        int result = 0;

        // 工作中常用的2种方式
        // 第一种： CompletableFuture.supplyAsync
//        try {
//            result = CompletableFuture.supplyAsync(PrintThreadReturn::sum).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }

        // 第二种线程池
        Future future = pool.submit(new ThreadSum());
        try {
            result = (int) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }



    static class ThreadSum implements Callable {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public Integer call() throws Exception {
            return sum();
        }
    }

}
