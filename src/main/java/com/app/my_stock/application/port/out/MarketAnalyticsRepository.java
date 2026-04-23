package com.app.my_stock.application.port.out;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface MarketAnalyticsRepository {
	Optional<BigDecimal> movingAveragePriceSince(String symbol, Instant fromInclusive);
}

