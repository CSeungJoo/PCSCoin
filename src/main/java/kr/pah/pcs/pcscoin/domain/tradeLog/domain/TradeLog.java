package kr.pah.pcs.pcscoin.domain.tradeLog.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TradeLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private LocalDateTime tradeDate;

    @Column
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Column(precision = 20, scale = 0)
    private BigDecimal price;

    @Column
    private BigDecimal afterSendWalletMoney;

    @Column
    private BigDecimal afterReceiveWalletMoney;

    @Column
    private boolean isDelete;

    @OneToOne
    @JoinColumn(name = "buy_wallet_id")
    private Wallet sendWallet;

    @OneToOne
    @JoinColumn(name = "sell_wallet_id")
    private Wallet receiveWallet;

}
