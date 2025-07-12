package com.best_store.right_bite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RightBiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RightBiteApplication.class, args);
	}

}
