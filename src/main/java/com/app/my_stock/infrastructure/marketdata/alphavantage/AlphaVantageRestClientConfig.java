package com.app.my_stock.infrastructure.marketdata.alphavantage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AlphaVantageRestClientConfig {

	@Bean
	public RestClient alphaVantageRestClient(@Value("${api.finance.base-url}") String baseUrl) {
		return RestClient.builder()
				.baseUrl(baseUrl)
				.build();
	}
}

