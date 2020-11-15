package test.spring.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.spring.bean.User;

public class FactoryBeanDemo {

    public static void main(String[] args) {
        // factory bean
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-factory-bean.xml");

        User user = beanFactory.getBean("userFactoryBean", User.class);

        System.out.println(user);

        System.out.println("id:" + user.getId() + ", name:" + user.getName());
    }

}
