package kr.pah.pcs.pcscoin.domain.trade.repository;

import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
