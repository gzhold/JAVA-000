package test.spring.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.spring.bean.User;


/**
 * 通过实体类的静态方法获取
 */
public class BeanInstantiationByStaticMethodDemo {

    public static void main(String[] args) {
        // bean-instantiation-static-method.xml
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-static-method.xml");

        User user = beanFactory.getBean("user-by-static-method", User.class);

        System.out.println(user);
        System.out.println("id:" + user.getId() + ", name:" + user.getName());
    }
}
