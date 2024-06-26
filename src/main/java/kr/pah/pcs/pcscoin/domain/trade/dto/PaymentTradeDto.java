package kr.pah.pcs.pcscoin.domain.trade.dto;

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
public class PaymentTradeDto {
    private BigDecimal price;
    private String tradeId;
    private String tradeName;
    private String successUrl;
    private String failUrl;
}
