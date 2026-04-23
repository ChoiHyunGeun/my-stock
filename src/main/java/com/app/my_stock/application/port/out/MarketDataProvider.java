package com.app.my_stock.application.port.out;

import com.app.my_stock.domain.market.MarketIndicator;

public interface MarketDataProvider {
	MarketIndicator fetchLatest(String symbol);
}

