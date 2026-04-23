package com.app.my_stock.application.port.out;

import com.app.my_stock.domain.market.MarketIndicator;

import java.time.Duration;
import java.util.Optional;

public interface MarketCache {
	void putLatest(MarketIndicator indicator, Duration ttl);

	Optional<MarketIndicator> getLatest(String symbol);
}

