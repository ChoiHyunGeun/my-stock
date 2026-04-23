package com.app.my_stock.infrastructure.marketdata.alphavantage;

public class AlphaVantageTransientException extends RuntimeException {
	public AlphaVantageTransientException(String message, Throwable cause) {
		super(message, cause);
	}
}

