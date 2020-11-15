package test.spring.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.spring.bean.User;

public class BeanFactoryDemo {


    public static void main(String[] args) {
        // bean factory
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-bean-factory.xml");

        User user = beanFactory.getBean("user-by-instance-method", User.class);

        System.out.println(user);
        System.out.println("id:" + user.getId() + ", name:" + user.getName());

    }

}
