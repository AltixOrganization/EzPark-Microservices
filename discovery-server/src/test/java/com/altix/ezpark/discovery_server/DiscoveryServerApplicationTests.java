package com.altix.ezpark.discovery_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.cloud.config.enabled=false" })
class DiscoveryServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
