package com.app.my_stock.infrastructure.cache.redis;

import com.app.my_stock.application.port.out.MarketCache;
import com.app.my_stock.domain.market.MarketIndicator;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisMarketCacheAdapter implements MarketCache {

	private final RedisTemplate<String, MarketIndicator> redisTemplate;

	public RedisMarketCacheAdapter(RedisTemplate<String, MarketIndicator> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void putLatest(MarketIndicator indicator, Duration ttl) {
		redisTemplate.opsForValue().set(key(indicator.symbol()), indicator, ttl);
	}

	@Override
	public Optional<MarketIndicator> getLatest(String symbol) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(key(symbol)));
	}

	private static String key(String symbol) {
		return "market:latest:" + symbol;
	}
}

