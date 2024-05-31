package kr.pah.pcs.pcscoin.domain.tradeLog.repository;

import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeLogRepository extends JpaRepository<TradeLog, Long> {
    @Query("select tl from TradeLog tl where tl.receiveWallet = ?1 or tl.sendWallet = ?1")
    Page<TradeLog> getTradeLogsWallet(Wallet wallet, Pageable pageable);

    @Query("select tl from TradeLog tl where (tl.receiveWallet = ?1 or tl.sendWallet = ?1) and tl.tradeDate > ?2 order by tl.tradeDate desc")
    Page<TradeLog> getTradeLogsByWalletAndTradeDate(Wallet wallet, LocalDateTime tradeDate, Pageable pageable);
}
