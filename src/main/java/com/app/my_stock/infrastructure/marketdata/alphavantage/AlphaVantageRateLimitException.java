package com.app.my_stock.infrastructure.marketdata.alphavantage;

public class AlphaVantageRateLimitException extends RuntimeException {
	public AlphaVantageRateLimitException(String message) {
		super(message);
	}
}

