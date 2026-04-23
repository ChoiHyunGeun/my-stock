package com.app.my_stock.application.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "market.monitor")
public record MarketMonitoringProperties(List<String> symbols) {
}

