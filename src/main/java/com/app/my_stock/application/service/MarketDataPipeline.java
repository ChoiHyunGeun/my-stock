package com.app.my_stock.application.service;

import com.app.my_stock.domain.market.MarketIndicator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MarketDataPipeline {

	private final MarketDataFetcher marketDataFetcher;
	private final ExecutorService ioPool = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));

	public MarketDataPipeline(MarketDataFetcher marketDataFetcher) {
		this.marketDataFetcher = marketDataFetcher;
	}

	public List<MarketIndicator> fetchAndStoreAll(List<String> symbols) {
		Objects.requireNonNull(symbols, "symbols");
		List<CompletableFuture<MarketIndicator>> futures = symbols.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.distinct()
				.map(s -> CompletableFuture.supplyAsync(() -> marketDataFetcher.fetchAndStore(s), ioPool))
				.toList();

		return futures.stream().map(CompletableFuture::join).toList();
	}
}

