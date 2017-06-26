package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 
@SpringBootApplication

// We need the following in order to detect the filter
// from the security package
@ServletComponentScan(value="security")
@ComponentScan({"services", "controller", "persistence"})
@EnableJpaRepositories(basePackages={"repository"})
public class Application {
 
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
