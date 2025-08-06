package com.hanati.cop.tuneWizard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hanati.cop.tuneWizard.mapper")
public class TuneWizardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TuneWizardApplication.class, args);
	}

}
