package com.app.my_stock.infrastructure.marketdata.alphavantage;

import com.app.my_stock.application.port.out.MarketDataProvider;
import com.app.my_stock.domain.market.MarketIndicator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class AlphaVantageRestClientAdapter implements MarketDataProvider {

	private final RestClient restClient;
	private final String apiKey;

	public AlphaVantageRestClientAdapter(
			RestClient restClient,
			@Value("${api.finance.key}") String apiKey
	) {
		this.restClient = restClient;
		this.apiKey = apiKey;
	}

	@Override
	public MarketIndicator fetchLatest(String symbol) {
		try {
			if (isFxSymbol(symbol)) {
				return fetchFx(symbol);
			}
			return fetchGlobalQuote(symbol);

		} catch (AlphaVantageRateLimitException e) {
			throw e;
		} catch (RestClientException e) {
			throw new AlphaVantageTransientException("AlphaVantage client error", e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new AlphaVantageTransientException("AlphaVantage unexpected error", e);
		}
	}

	private MarketIndicator fetchGlobalQuote(String symbol) {
		JsonNode root = restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/query")
						.queryParam("function", "GLOBAL_QUOTE")
						.queryParam("symbol", symbol)
						.queryParam("apikey", apiKey)
						.build())
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					throw new AlphaVantageTransientException("AlphaVantage HTTP error: " + res.getStatusCode(), null);
				})
				.body(JsonNode.class);

		if (root == null) {
			throw new AlphaVantageTransientException("AlphaVantage empty body", null);
		}
		guardRateLimitAndErrors(root);

		JsonNode global = root.get("Global Quote");
		if (global == null || global.isNull() || global.isEmpty()) {
			throw new AlphaVantageTransientException("AlphaVantage missing Global Quote for symbol=" + symbol, null);
		}

		String resolvedSymbol = text(global, "01. symbol", symbol);
		BigDecimal price = decimal(global, "05. price");
		BigDecimal change = decimal(global, "09. change");
		return new MarketIndicator(resolvedSymbol, price, change, Instant.now());
	}

	private MarketIndicator fetchFx(String symbol) {
		String[] parts = symbol.split("/");
		if (parts.length != 2) {
			throw new IllegalArgumentException("FX symbol must be like 'USD/KRW'");
		}
		String from = parts[0].trim();
		String to = parts[1].trim();

		JsonNode root = restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/query")
						.queryParam("function", "CURRENCY_EXCHANGE_RATE")
						.queryParam("from_currency", from)
						.queryParam("to_currency", to)
						.queryParam("apikey", apiKey)
						.build())
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					throw new AlphaVantageTransientException("AlphaVantage HTTP error: " + res.getStatusCode(), null);
				})
				.body(JsonNode.class);

		if (root == null) {
			throw new AlphaVantageTransientException("AlphaVantage empty body", null);
		}
		guardRateLimitAndErrors(root);

		JsonNode rateNode = root.get("Realtime Currency Exchange Rate");
		if (rateNode == null || rateNode.isNull() || rateNode.isEmpty()) {
			throw new AlphaVantageTransientException("AlphaVantage missing Realtime Currency Exchange Rate for symbol=" + symbol, null);
		}

		BigDecimal rate = decimal(rateNode, "5. Exchange Rate");
		return new MarketIndicator(symbol, rate, BigDecimal.ZERO, Instant.now());
	}

	private static boolean isFxSymbol(String symbol) {
		return symbol != null && symbol.contains("/") && symbol.indexOf('/') > 0 && symbol.indexOf('/') < symbol.length() - 1;
	}

	private static void guardRateLimitAndErrors(JsonNode root) {
		JsonNode note = root.get("Note");
		if (note != null && !note.isNull() && !note.asText("").isBlank()) {
			throw new AlphaVantageRateLimitException(note.asText());
		}

		JsonNode error = root.get("Error Message");
		if (error != null && !error.isNull() && !error.asText("").isBlank()) {
			throw new AlphaVantageTransientException("AlphaVantage error: " + error.asText(), null);
		}
	}

	private static String text(JsonNode node, String field, String fallback) {
		JsonNode v = node.get(field);
		String s = v == null || v.isNull() ? "" : v.asText("");
		return s.isBlank() ? fallback : s.trim();
	}

	private static BigDecimal decimal(JsonNode node, String field) {
		JsonNode v = node.get(field);
		if (v == null || v.isNull()) {
			throw new AlphaVantageTransientException("Missing numeric field: " + field, null);
		}
		String s = v.asText("").trim();
		if (s.isEmpty()) {
			throw new AlphaVantageTransientException("Empty numeric field: " + field, null);
		}
		return new BigDecimal(s);
	}
}

