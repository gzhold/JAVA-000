package test.aop.jdkproxy;

import test.aop.decorator.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LogProxy implements InvocationHandler {

    private UserService delegate;

    public LogProxy(UserService delegate) {
        this.delegate = delegate;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass().getSimpleName() + "-" + method.getName() + " is invoked: " + Arrays.toString(args));
        Object result = method.invoke(delegate, args);
        System.out.println(method.getName() + " is finished: " + result);
        return result;
    }
}
