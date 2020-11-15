package test.spring.service;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;
import test.spring.bean.User;

import java.util.Map;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * 从小马哥专栏学到的用法
 */
public class RegistryBeanDefinitionDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册BeanDefinition -- user
        registryBeanDefinition(applicationContext, "user1");
        // 启动
        applicationContext.refresh();

        User user = applicationContext.getBean("user1", User.class);
        System.out.println(user);
        System.out.println("id:" + user.getId() + ", name:" + user.getName());

        Map<String, User> userMap = applicationContext.getBeansOfType(User.class);
        System.out.println(userMap);

        applicationContext.close();
    }

    public static void registryBeanDefinition (BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(User.class);
        beanDefinitionBuilder.addPropertyValue("id", "101")
                    .addPropertyValue("name", "java14");

        if (StringUtils.hasText(beanName)) {
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        } else {
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
        }
    }

}
