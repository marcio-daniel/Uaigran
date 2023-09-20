package com.uaigran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories("com.uaigran.models.repository")
@ComponentScan(basePackages = { "com.uaigran" })
@EntityScan("com.uaigran.models.entities")
public class UaigranApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaigranApplication.class, args);
	}

}
