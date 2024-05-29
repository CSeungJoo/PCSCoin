package kr.pah.pcs.pcscoin.domain.tradeLog.dto;

import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnTradeLogDto {
    private long tradeLogIdx;
    private LocalDateTime tradeDate;
    private TradeType tradeType;
    private BigDecimal price;
    private BigDecimal afterWalletMoney;
    private String sendWalletName;
    private String receiveWalletName;

    public ReturnTradeLogDto(TradeLog tradeLog, BigDecimal afterWalletMoney, BigDecimal price) {
        this.tradeLogIdx = tradeLog.getIdx();
        this.tradeDate = tradeLog.getTradeDate();
        this.tradeType = tradeLog.getTradeType();
        this.afterWalletMoney = afterWalletMoney;
        this.price = price;
        this.sendWalletName = tradeLog.getSendWallet().getName();
        this.receiveWalletName = tradeLog.getReceiveWallet().getName();
    }
}
