package com.app.my_stock.infrastructure.cache.redis;

import com.app.my_stock.domain.market.MarketIndicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, MarketIndicator> marketIndicatorRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, MarketIndicator> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		// 1. Instant를 처리할 수 있는 전용 ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		// (선택) 숫자가 아닌 ISO-8601 문자열 표준 형식을 유지하도록 설정
		objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		StringRedisSerializer keySerializer = new StringRedisSerializer();

		// 2. 생성자에 objectMapper를 함께 전달합니다 (Spring Boot 3.x 기준)
		Jackson2JsonRedisSerializer<MarketIndicator> valueSerializer =
				new Jackson2JsonRedisSerializer<>(objectMapper, MarketIndicator.class);

		template.setKeySerializer(keySerializer);
		template.setValueSerializer(valueSerializer);
		template.setHashKeySerializer(keySerializer);
		template.setHashValueSerializer(valueSerializer);
		template.afterPropertiesSet();
		return template;
	}
}

