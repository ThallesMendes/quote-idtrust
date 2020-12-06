package br.com.idtrust.pricequote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PricequoteApplicationTests {

	@Test
	@DisplayName("Should be test load application beans with success")
	void contextLoads() {
		PricequoteApplication.main(new String[]{"--spring.config.location=classpath:application-test.yml"});
	}

}
