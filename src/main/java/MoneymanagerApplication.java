import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"Controller", "Service", "Config", "Security", "util", "dto"})
@EntityScan(basePackages = "Entity")
@EnableJpaRepositories(basePackages = "Repository")
public class MoneymanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneymanagerApplication.class, args);
    }

}
