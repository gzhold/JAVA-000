package test.aop.bytebuddy.log;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 编写一个@MyLog注解来自动生成日志
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {

    String value() default "WARNING!";

    int level() default 0;
}
