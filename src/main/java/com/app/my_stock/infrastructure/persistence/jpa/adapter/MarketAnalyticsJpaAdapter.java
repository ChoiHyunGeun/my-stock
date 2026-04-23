package com.app.my_stock.infrastructure.persistence.jpa.adapter;

import com.app.my_stock.application.port.out.MarketAnalyticsRepository;
import com.app.my_stock.infrastructure.persistence.jpa.repository.MarketIndicatorJpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Repository
public class MarketAnalyticsJpaAdapter implements MarketAnalyticsRepository {

	private final MarketIndicatorJpaRepository marketIndicatorJpaRepository;

	public MarketAnalyticsJpaAdapter(MarketIndicatorJpaRepository marketIndicatorJpaRepository) {
		this.marketIndicatorJpaRepository = marketIndicatorJpaRepository;
	}

	@Override
	public Optional<BigDecimal> movingAveragePriceSince(String symbol, Instant fromInclusive) {
		BigDecimal avg = marketIndicatorJpaRepository.avgPriceSince(symbol, fromInclusive);
		return Optional.ofNullable(avg);
	}
}

