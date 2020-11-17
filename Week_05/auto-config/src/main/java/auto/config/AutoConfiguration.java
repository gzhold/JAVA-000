package auto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ConditionalOnClass(Student.class)
@ConditionalOnProperty(prefix = "autoconfig", name = "enabled", havingValue = "true", matchIfMissing = true)
@PropertySource("classpath:/META-INF/application.properties")
@EnableConfigurationProperties(StudentProperties.class)
public class AutoConfiguration {


    @Autowired
    StudentProperties studentProperties;

    @Bean
    public Student student() {
        Student student = new Student(studentProperties.getId(),studentProperties.getName());
        return student;
    }
}
