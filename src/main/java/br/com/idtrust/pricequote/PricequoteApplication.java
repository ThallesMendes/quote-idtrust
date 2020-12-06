package br.com.idtrust.pricequote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class PricequoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricequoteApplication.class, args);
	}

}
