package com.app.my_stock.application.service;

import com.app.my_stock.application.ai.MarketDecisionAgent;
import com.app.my_stock.application.ai.MarketMonitoringProperties;
import com.app.my_stock.application.port.out.NotificationPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class MarketAnalysisService {

	private final MarketDecisionAgent decisionAgent;
	private final MarketMonitoringProperties properties;
	private final NotificationPort notificationPort;

	public MarketAnalysisService(MarketDecisionAgent decisionAgent,
	                            MarketMonitoringProperties properties,
	                            NotificationPort notificationPort) {
		this.decisionAgent = decisionAgent;
		this.properties = properties;
		this.notificationPort = notificationPort;
	}

	public void analyzeAndNotify(Instant now) {
		List<String> symbols = properties.symbols() == null ? List.of() : properties.symbols().stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.distinct()
				.toList();

		if (symbols.isEmpty()) {
			return;
		}

		String decision = decisionAgent.decide(symbols, now);
		if (decision == null || decision.isBlank()) {
			return;
		}
		String trimmed = decision.trim();
		if ("NO_ALERT".equalsIgnoreCase(trimmed)) {
			return;
		}

		notificationPort.sendReport(trimmed);
	}
}

