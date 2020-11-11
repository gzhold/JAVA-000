package thread;

import java.util.concurrent.*;

public class ConcurrentToolsDemo {

    public static void main(String[] args) {
        System.out.println("Main thread start...");

        int n = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(n);

        CountDownLatch latch = new CountDownLatch(n);
        Future<Integer> future = executorService.submit(new FiboThread(latch));
        try {
            Integer value = future.get();
            System.out.println("CountDownLatch sum is " + value);
            latch.await(5 * 1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        CyclicBarrier barrier = new CyclicBarrier(n, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 完成最后任务");
            }
        });
        future = executorService.submit(new FiboThread(barrier));
        Integer value = null;
        try {
            value = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("CyclicBarrier sum is " + value);


        Semaphore semaphore = new Semaphore(n);
        future = executorService.submit(new Callable<Integer>() {
            /**
             * Computes a result, or throws an exception if unable to do so.
             *
             * @return computed result
             * @throws Exception if unable to compute a result
             */
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                try {
                    semaphore.acquire();
                    sum = sum();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                return sum;
            }

            private int sum() {
                return fibo(36);
            }

            private int fibo(int a) {
                if ( a < 2) {
                    return 1;
                }
                return fibo(a-1) + fibo(a-2);
            }

        });

        try {
            value = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Semaphore sum is " + value);

        System.out.println("Main thread end...");

    }

    static class FiboThread implements Callable {

        private CountDownLatch countDownLatch;

        private CyclicBarrier cyclicBarrier;

        public FiboThread (CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public FiboThread (CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        public FiboThread(){}

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public Integer call() throws Exception {
            System.out.println("FiboThread start...");
            int total = sum();
            if (null != countDownLatch) {
                countDownLatch.countDown();
            } else if (null != cyclicBarrier) {
                cyclicBarrier.await();
            }
            System.out.println("FiboThread end...");
            return total;
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
    }


}
