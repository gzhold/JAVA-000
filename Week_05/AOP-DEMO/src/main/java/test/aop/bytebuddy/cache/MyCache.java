package test.aop.bytebuddy.cache;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyCache {

    // 缓存时常，默认60s
    int cacheTime() default 60;

}
