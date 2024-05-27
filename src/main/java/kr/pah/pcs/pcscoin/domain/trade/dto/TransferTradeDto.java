package kr.pah.pcs.pcscoin.domain.trade.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferTradeDto {
    private String phone;
    private String tradeId;
    private BigDecimal price;
}
