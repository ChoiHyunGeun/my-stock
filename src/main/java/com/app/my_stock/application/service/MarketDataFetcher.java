package com.app.my_stock.application.service;

import com.app.my_stock.application.port.out.MarketCache;
import com.app.my_stock.application.port.out.MarketDataProvider;
import com.app.my_stock.application.port.out.MarketRepository;
import com.app.my_stock.domain.market.MarketIndicator;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class MarketDataFetcher {

	private static final Duration LATEST_TTL = Duration.ofMinutes(2);

	private final MarketDataProvider marketDataProvider;
	private final MarketCache marketCache;
	private final MarketRepository marketRepository;

	public MarketDataFetcher(MarketDataProvider marketDataProvider, MarketCache marketCache, MarketRepository marketRepository) {
		this.marketDataProvider = marketDataProvider;
		this.marketCache = marketCache;
		this.marketRepository = marketRepository;
	}

	/**
	 * 외부 API 호출 → 즉시 Redis 캐싱 → PostgreSQL 영속화.
	 * RateLimit/Retry는 외부 API 호출부에만 적용.
	 */
	@Retry(name = "alphaVantage")
	@RateLimiter(name = "alphaVantage")
	@Transactional
	public MarketIndicator fetchAndStore(String symbol) {
		MarketIndicator indicator = marketDataProvider.fetchLatest(symbol);

		// 실시간 조회 성능을 위해 먼저 캐싱
		marketCache.putLatest(indicator, LATEST_TTL);

		// 과거 분석을 위해 영속화 (인덱스는 entity에 적용)
		return marketRepository.save(indicator);
	}
}

