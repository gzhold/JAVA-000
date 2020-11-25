package org.api.demo.agent;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.apache.commons.lang3.time.StopWatch;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MethodCostTime {

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            // 原有函数执行
            return callable.call();
        } finally {
            stopWatch.stop();
            System.out.println(method + " 方法耗时： " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms");
        }
    }

}
