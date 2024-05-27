package kr.pah.pcs.pcscoin.domain.trade.dto;

import kr.pah.pcs.pcscoin.domain.model.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnTradeDto {
    private Long idx;
    private BigDecimal price;
    private TradeType tradeType;
    private String sendWalletName;
    private String receiveWalletName;
}
