package test.spring.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import test.spring.bean.User;



public class BeanAnnotationDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanAnnotationDemo.class);

        applicationContext.refresh();

        User user = applicationContext.getBean("user2", User.class);

        System.out.println(user);
        System.out.println("id:" + user.getId() + ", name:" + user.getName());

        applicationContext.close();
    }

    // 1、通过bean方式
    @Bean(name = "user2")
    public User getUser() {
        User user = new User();
        user.setId("456");
        user.setName("test02");
        return user;
    }

}
