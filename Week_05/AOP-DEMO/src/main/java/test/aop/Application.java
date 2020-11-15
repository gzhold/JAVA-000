package test.aop;

import test.aop.decorator.LogDecorator;
import test.aop.decorator.UserService;
import test.aop.decorator.UserServiceImpl;
import test.aop.jdkproxy.LogProxy;

import java.lang.reflect.Proxy;

public class Application {

    public static void main(String[] args) {
        // 装饰器模式
        UserService service = new LogDecorator(new UserServiceImpl());
        String name = service.query("1");
        System.out.println("name1 is " + name);

        // jdk proxy
        UserService userService = (UserService) Proxy.newProxyInstance(
                    service.getClass().getClassLoader(),
                    new Class[]{UserService.class},
                    new LogProxy(service));
        name = userService.query("2");
        System.out.println("name2 is " + name);
    }

}
