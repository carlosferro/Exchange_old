package com.ferro.exchange.security.jwt;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtProperties {

	private String secretKey = "secret";

	private long validityInMs = 3600000; // 1h
}
