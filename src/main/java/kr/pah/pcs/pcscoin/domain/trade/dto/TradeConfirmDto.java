package kr.pah.pcs.pcscoin.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeConfirmDto {
    private Long tradeIdx;
    private BigDecimal price;
    private String tradeId;
}
