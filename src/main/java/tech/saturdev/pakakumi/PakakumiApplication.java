package tech.saturdev.pakakumi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
@SpringBootApplication(scanBasePackages = "tech.saturdev.pakakumi")
public class PakakumiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PakakumiApplication.class, args);
	}

}
