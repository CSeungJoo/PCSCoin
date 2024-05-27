package kr.pah.pcs.pcscoin.domain.tradeLog.service;

import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import kr.pah.pcs.pcscoin.domain.tradeLog.repository.TradeLogRepository;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeLogService {

    private final TradeLogRepository tradeLogRepository;

    public Page<TradeLog> getTradeLogByWallet(Wallet wallet, Pageable pageable) {
        return tradeLogRepository.getTradeLogsWallet(wallet, pageable);
    }

    public List<TradeLog> getTradeLogByWalletAndTradeDate(Wallet wallet, LocalDateTime tradeDate) {
        return tradeLogRepository.getTradeLogsByWalletAndTradeDate(wallet, tradeDate);
    }

    @Transactional
    public TradeLog createTradeLog(Trade trade) {
        TradeLog tradeLog = TradeLog.builder()
                .tradeType(trade.getTradeType())
                .tradeDate(LocalDateTime.now())
                .price(trade.getPrice())
                .afterSendWalletMoney(trade.getSendWallet().getMoney())
                .afterReceiveWalletMoney(trade.getReceiveWallet().getMoney())
                .sendWallet(trade.getSendWallet())
                .receiveWallet(trade.getReceiveWallet())
                .build();

        return tradeLogRepository.save(tradeLog);
    }

}
