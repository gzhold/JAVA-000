package org.gz.multi.config;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;



@Aspect
@Order(-1)	//设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中)
@Component
@Log4j2
public class DataSourceAspect {

    //切点
    @Pointcut("execution(* com.gz.multi.service.*.*(..)))")
    public void pointCut() { }


    /**
     * 执行方法前更换数据源
     *
     * @param joinPoint        切点
     * @param myDataSource 动态数据源
     */
    @Before("@annotation(myDataSource)")
    public void doBefore(JoinPoint joinPoint, MyDataSource myDataSource) {
        Object target = joinPoint.getTarget();
        String method = joinPoint.getSignature().getName();
        log.info("Object is {}, execute method is {}", target, method);

        DataSourceType dataSourceType = myDataSource.value();
        if (dataSourceType == DataSourceType.SLAVE_1) {
            log.info("Set dataSource to  {}", DataSourceType.SLAVE_1.getName());
            ContextHolder.putDataSource(DataSourceType.SLAVE_1);
        } else if (dataSourceType == DataSourceType.SLAVE_2) {
            log.info("Set dataSource to  {}", DataSourceType.SLAVE_2.getName());
            ContextHolder.putDataSource(DataSourceType.SLAVE_2);
        }else {
            log.info("Current dataSource is {}",  DataSourceType.MASTER.getName());
            ContextHolder.putDataSource(DataSourceType.MASTER);
        }
    }

    /**
     * 执行方法后清除数据源设置
     *
     * @param joinPoint        切点
     * @param myDataSource 动态数据源
     */
    @After("@annotation(myDataSource)")
    public void doAfter(JoinPoint joinPoint, MyDataSource myDataSource) {
        log.info(String.format("当前数据源  %s  执行清理方法", myDataSource.value().getName()));
        ContextHolder.clear();
    }

    @Before(value = "pointCut()")
    public void doBeforeWithSlave(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取当前切点方法对象
        Method method = methodSignature.getMethod();
        if (method.getDeclaringClass().isInterface()) {//判断是否为接口
            try {
                //获取实际类型的方法对象
                method = joinPoint.getTarget().getClass()
                            .getDeclaredMethod(joinPoint.getSignature().getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error(">>> 发生错误, error is ", e);
            }
        }
        if (null == method.getAnnotation(MyDataSource.class)) {
            ContextHolder.setSlave();
        }
    }
}
