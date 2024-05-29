package kr.pah.pcs.pcscoin.domain.trade.repository;

import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID> {
}
