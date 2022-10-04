package Eco_Log.Eco_Log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EcoLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoLogApplication.class, args);
	}

}
