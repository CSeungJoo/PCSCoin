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

    /**
     *
     * @param tradeLogIdx
     * @return TradeLog
     * tradeLogIdx를 통한 TradeLog 조회 값이 없을시 IllegalStateException throw
     */
    public TradeLog getTradeLogByIdx(Long tradeLogIdx) {
        return tradeLogRepository.findById(tradeLogIdx).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 거래이력입니다.")
        );
    }


    /**
     *
     * @param wallet
     * @param pageable
     * @return Page&lt;TradeLog&gt;
     * 지갑을 통한 페이징 조회
     */
    public Page<TradeLog> getTradeLogByWallet(Wallet wallet, Pageable pageable) {
        return tradeLogRepository.getTradeLogsWallet(wallet, pageable);
    }

    /**
     *
     * @param wallet
     * @param tradeDate
     * @param pageable
     * @return Page&lt;TradeLog&gt;
     * 지갑과 날짜를 통한 페이징 조회
     */
    public Page<TradeLog> getTradeLogByWalletAndTradeDate(Wallet wallet, LocalDateTime tradeDate, Pageable pageable) {
        return tradeLogRepository.getTradeLogsByWalletAndTradeDate(wallet, tradeDate, pageable);
    }

    /**
     * Trade 오브젝트를 기반으로 TradeLog 생성 및 return, DB에 영구 저장
     */
    @Transactional
    public TradeLog createTradeLog(Trade trade) {
        TradeLog tradeLog = TradeLog.builder()
                .tradeType(trade.getTradeType())
                .tradeDate(LocalDateTime.now())
                .tradeId(trade.getTradeId())
                .price(trade.getPrice())
                .afterSendWalletMoney(trade.getSendWallet().getMoney())
                .afterReceiveWalletMoney(trade.getReceiveWallet().getMoney())
                .sendWallet(trade.getSendWallet())
                .receiveWallet(trade.getReceiveWallet())
                .build();

        return tradeLogRepository.save(tradeLog);
    }


}
