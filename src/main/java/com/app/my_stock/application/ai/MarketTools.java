package com.app.my_stock.application.ai;

import com.app.my_stock.application.port.out.MarketAnalyticsRepository;
import com.app.my_stock.application.port.out.MarketCache;
import com.app.my_stock.domain.market.MarketIndicator;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class MarketTools {

	private final MarketCache marketCache;
	private final MarketAnalyticsRepository marketAnalyticsRepository;

	public MarketTools(MarketCache marketCache, MarketAnalyticsRepository marketAnalyticsRepository) {
		this.marketCache = marketCache;
		this.marketAnalyticsRepository = marketAnalyticsRepository;
	}

	@Tool(description = "Redis에서 심볼의 최신가를 조회한다. 캐시에 없으면 null을 반환한다.")
	public CurrentPrice getCurrentPrice(String symbol) {
		return marketCache.getLatest(symbol)
				.map(i -> new CurrentPrice(i.symbol(), i.price(), i.change(), i.timestamp()))
				.orElse(null);
	}

	@Tool(description = "DB에서 N일 이동평균선(종가 평균)을 계산한다. 데이터가 없으면 null을 반환한다.")
	public MovingAverage getHistoricalMovingAverage(String symbol, int days) {
		if (days <= 0) {
			throw new IllegalArgumentException("days must be > 0");
		}
		Instant from = Instant.now().minus(days, ChronoUnit.DAYS);
		BigDecimal avg = marketAnalyticsRepository.movingAveragePriceSince(symbol, from).orElse(null);
		return avg == null ? null : new MovingAverage(symbol, days, avg, from);
	}

	public record CurrentPrice(String symbol, BigDecimal price, BigDecimal change, Instant timestamp) {
	}

	public record MovingAverage(String symbol, int days, BigDecimal averagePrice, Instant fromInclusive) {
	}
}

