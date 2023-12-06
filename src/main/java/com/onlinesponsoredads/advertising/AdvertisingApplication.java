package com.onlinesponsoredads.advertising;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
public class AdvertisingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertisingApplication.class, args);
	}

}
