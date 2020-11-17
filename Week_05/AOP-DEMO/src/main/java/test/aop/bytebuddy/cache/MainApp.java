package test.aop.bytebuddy.cache;

import java.lang.reflect.InvocationTargetException;

public class MainApp {

    public static void main(String[] args) {
        CacheDecorator decorator = new CacheDecorator();

        UserService dataService = null;
        try {
            dataService = decorator.decorate(UserService.class).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert dataService != null;

        // 有缓存的查询：只有第一次执行了真正的查询操作，第二次从缓存中获取
        System.out.println(dataService.queryData(10));
        try {
            Thread.sleep(2 * 1000);
            System.out.println(dataService.queryData(10));
            Thread.sleep(15 * 1000);
            System.out.println(dataService.queryData(10));

            // 无缓存的查询：两次都执行了真正的查询操作
            System.out.println(dataService.queryDataWithoutCache(10));
            Thread.sleep(2 * 1000);
            System.out.println(dataService.queryDataWithoutCache(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
