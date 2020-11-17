package test.aop.bytebuddy.cache;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class CacheDecorator {

    // 如果某个方法标注了@MyCache，则返回值能够被自动缓存注解所指定的时长
    @SuppressWarnings("unchecked")
    public static <T> Class<T> decorate(Class<T> clazz) {
        return (Class<T>) new ByteBuddy()
                    .subclass(clazz)
                    .method(ElementMatchers.isAnnotatedWith(MyCache.class))
                    .intercept(MethodDelegation.to(CacheAdvisor.class))
                    .make()
                    .load(clazz.getClassLoader())
                    .getLoaded();
    }

    public static class CacheAdvisor {
        private static ConcurrentHashMap<CacheKey, CacheValue> cache = new ConcurrentHashMap<>();

        @RuntimeType
        public static Object cache(
                    @SuperCall Callable<Object> callable,
                    @Origin Method method, // 原始方法
                    @This Object thisObject, // 当前ByteBuddy动态对象
                    @AllArguments Object[] arguments) throws Exception {
            CacheKey cacheKey = new CacheKey(thisObject, method.getName(), arguments);
            final CacheValue resultExistingInCache = cache.get(cacheKey);

            if (resultExistingInCache != null) {
                if (isCacheExpires(resultExistingInCache, method)) {
                    return invokeMethodAndPutIntoCache(callable, cacheKey);
                } else {
                    return resultExistingInCache.value;
                }
            } else {
                return invokeMethodAndPutIntoCache(callable, cacheKey);
            }
        }

        /**
         * 失效时间
         * @param cacheValue
         * @param method
         * @return
         */
        private static boolean isCacheExpires(CacheValue cacheValue, Method method) {
            long time = cacheValue.time;
            int cacheTime = method.getAnnotation(MyCache.class).cacheTime();
            return System.currentTimeMillis() - time > cacheTime * 1000;
        }

        private static Object invokeMethodAndPutIntoCache(@SuperCall Callable<Object> callable, CacheKey cacheKey) throws Exception {
            Object realMethodInvocationResult = callable.call();
            cache.put(cacheKey, new CacheValue(realMethodInvocationResult, System.currentTimeMillis()));
            return realMethodInvocationResult;
        }
    }

    private static class CacheKey {
        private Object thisObject;
        private String methodName;
        private Object[] arguments;

        CacheKey(Object thisObject, String methodName, Object[] arguments) {
            this.thisObject = thisObject;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(thisObject, cacheKey.thisObject) &&
                        Objects.equals(methodName, cacheKey.methodName) &&
                        Arrays.equals(arguments, cacheKey.arguments);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(thisObject, methodName);
            result = 31 * result + Arrays.hashCode(arguments);
            return result;
        }
    }

    private static class CacheValue {
        private final Object value;
        private final long time;

        CacheValue(Object value, long time) {
            this.value = value;
            this.time = time;
        }
    }

}
