package br.com.losystem.chefdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChefdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChefdeskApplication.class, args);
	}

}
