package kr.pah.pcs.pcscoin.domain.wallet.dto;

import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnWalletDto {
    private int idx;
    private String name;
    private BigDecimal money;

    public ReturnWalletDto(Wallet wallet) {
        this.idx = wallet.getIdx();
        this.name = wallet.getName();
        this.money = wallet.getMoney();
    }
}
