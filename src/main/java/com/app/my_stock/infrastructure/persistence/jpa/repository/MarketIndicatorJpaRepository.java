package com.app.my_stock.infrastructure.persistence.jpa.repository;

import com.app.my_stock.infrastructure.persistence.jpa.entity.MarketIndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface MarketIndicatorJpaRepository extends JpaRepository<MarketIndicatorEntity, Long> {
	Optional<MarketIndicatorEntity> findFirstBySymbolOrderByTimestampDesc(String symbol);

	@Query(value = "select avg(price) from market_indicators where symbol = :symbol and timestamp >= :fromInclusive", nativeQuery = true)
	BigDecimal avgPriceSince(@Param("symbol") String symbol, @Param("fromInclusive") Instant fromInclusive);
}

