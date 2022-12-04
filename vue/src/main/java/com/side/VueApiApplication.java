package com.side;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan(basePackages = {"com.side.domain.mapper"})
public class VueApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueApiApplication.class, args);
	}
	

}
