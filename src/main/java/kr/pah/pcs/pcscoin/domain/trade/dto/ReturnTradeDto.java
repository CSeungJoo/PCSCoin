package kr.pah.pcs.pcscoin.domain.trade.dto;

import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnTradeDto {
    private UUID tradeIdx;
    private BigDecimal price;
    private String tradeId;
    private TradeType tradeType;
    private String sendWalletName;
    private String receiveWalletName;

    public ReturnTradeDto(Trade trade) {
        this.tradeIdx = trade.getIdx();
        this.price = trade.getPrice();
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.sendWalletName = trade.getSendWallet().getName();
        this.receiveWalletName = trade.getReceiveWallet().getName();
    }
}
