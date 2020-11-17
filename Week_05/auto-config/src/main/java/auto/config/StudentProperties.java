package auto.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "autoconfig.student")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentProperties {

    private String id;

    private String name;

}
