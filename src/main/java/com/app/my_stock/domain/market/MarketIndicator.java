package com.app.my_stock.domain.market;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

public record MarketIndicator(
		String symbol,
		BigDecimal price,
		BigDecimal change,
		Instant timestamp
) {
}

