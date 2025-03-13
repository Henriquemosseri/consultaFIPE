package com.github.henriquemosseri.ConsultaFIPE;

import com.github.henriquemosseri.ConsultaFIPE.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsultaFipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConsultaFipeApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		new Principal().principal();
	}
}

