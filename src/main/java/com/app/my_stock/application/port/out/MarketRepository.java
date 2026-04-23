package com.app.my_stock.application.port.out;

import com.app.my_stock.domain.market.MarketIndicator;

import java.util.Optional;

public interface MarketRepository {
	MarketIndicator save(MarketIndicator indicator);

	Optional<MarketIndicator> findLatestBySymbol(String symbol);
}

