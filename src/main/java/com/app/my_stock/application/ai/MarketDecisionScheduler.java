package com.app.my_stock.application.ai;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import com.app.my_stock.application.service.MarketAnalysisService;

@Component
public class MarketDecisionScheduler {

	private final MarketAnalysisService marketAnalysisService;

	public MarketDecisionScheduler(MarketAnalysisService marketAnalysisService) {
		this.marketAnalysisService = marketAnalysisService;
	}

	@Scheduled(cron = "5 */5 * * * *") // every minute, after data collection
	public void tick() {
		marketAnalysisService.analyzeAndNotify(Instant.now());
	}
}

