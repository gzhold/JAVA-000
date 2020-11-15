package test.spring.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.spring.bean.User;


/**
 * xml定义好bean，通过ApplicationContext获取bean
 */
public class BeanInstantiationDemo {

    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation.xml");

        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);

        System.out.println("id:" + user.getId() + ", name:" + user.getName());
    }

}
