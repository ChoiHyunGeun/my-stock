package com.app.my_stock.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketDataFetchScheduler {

	private static final Logger log = LoggerFactory.getLogger(MarketDataFetchScheduler.class);

	private static final List<String> SYMBOLS = List.of("USD/KRW", "NVDA");

	private final MarketDataFetcher marketDataFetcher;

	public MarketDataFetchScheduler(MarketDataFetcher marketDataFetcher) {
		this.marketDataFetcher = marketDataFetcher;
	}

	/**
	 * 10분마다 데이터 수집.
	 * AI 의사결정 스케줄러가 뒤에서 돌도록(몇 초 후) 분리해서 충돌을 피한다.
	 */
	//@Scheduled(cron = "0 */5 * * * *")
	public void fetch() {
		for (String symbol : SYMBOLS) {
			try {
				marketDataFetcher.fetchAndStore(symbol);
			} catch (Exception e) {
				log.warn("Market fetch failed. symbol={}", symbol, e);
			}
		}
	}
}

