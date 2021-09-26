package com.bank;

import com.bank.atm.AtmService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmApplication extends AtmService {

	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}

}
