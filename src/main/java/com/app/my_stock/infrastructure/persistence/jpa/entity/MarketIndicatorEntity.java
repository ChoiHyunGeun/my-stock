package com.app.my_stock.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
		name = "market_indicators",
		indexes = {
				@Index(name = "idx_market_indicator_symbol_timestamp", columnList = "symbol,timestamp")
		}
)
public class MarketIndicatorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 32)
	private String symbol;

	@Column(nullable = false, precision = 19, scale = 6)
	private BigDecimal price;

	@Column(nullable = false, precision = 19, scale = 6)
	private BigDecimal change;

	@Column(nullable = false)
	private Instant timestamp;

	protected MarketIndicatorEntity() {
	}

	public MarketIndicatorEntity(String symbol, BigDecimal price, BigDecimal change, Instant timestamp) {
		this.symbol = symbol;
		this.price = price;
		this.change = change;
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getChange() {
		return change;
	}

	public Instant getTimestamp() {
		return timestamp;
	}
}

