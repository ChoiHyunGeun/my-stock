package com.app.my_stock.infrastructure.persistence.jpa.adapter;

import com.app.my_stock.application.port.out.MarketRepository;
import com.app.my_stock.domain.market.MarketIndicator;
import com.app.my_stock.infrastructure.persistence.jpa.entity.MarketIndicatorEntity;
import com.app.my_stock.infrastructure.persistence.jpa.repository.MarketIndicatorJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MarketRepositoryJpaAdapter implements MarketRepository {

	private final MarketIndicatorJpaRepository jpaRepository;

	public MarketRepositoryJpaAdapter(MarketIndicatorJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public MarketIndicator save(MarketIndicator indicator) {
		MarketIndicatorEntity saved = jpaRepository.save(toEntity(indicator));
		return toDomain(saved);
	}

	@Override
	public Optional<MarketIndicator> findLatestBySymbol(String symbol) {
		return jpaRepository.findFirstBySymbolOrderByTimestampDesc(symbol).map(this::toDomain);
	}

	private MarketIndicatorEntity toEntity(MarketIndicator domain) {
		return new MarketIndicatorEntity(
				domain.symbol(),
				domain.price(),
				domain.change(),
				domain.timestamp()
		);
	}

	private MarketIndicator toDomain(MarketIndicatorEntity entity) {
		return new MarketIndicator(
				entity.getSymbol(),
				entity.getPrice(),
				entity.getChange(),
				entity.getTimestamp()
		);
	}
}

