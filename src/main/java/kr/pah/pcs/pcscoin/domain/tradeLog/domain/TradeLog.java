package kr.pah.pcs.pcscoin.domain.tradeLog.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class TradeLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column
    private LocalDate tradeDate;

    @Column
    private TradeType tradeType;

    @Column
    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "buy_wallet_id")
    private Wallet buyWallet;

    @OneToOne
    @JoinColumn(name = "sell_wallet_id")
    private Wallet sellWallet;

}
