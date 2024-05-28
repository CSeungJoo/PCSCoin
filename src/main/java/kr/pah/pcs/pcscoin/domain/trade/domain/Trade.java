package kr.pah.pcs.pcscoin.domain.trade.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Trade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeIdx;

    @Column
    private LocalDateTime expiredTime;

    @Column
    private String tradeName;

    @Column
    private String tradeId;

    @Column
    private BigDecimal price;

    @Column
    private TradeType tradeType;

    @Column
    private String clientKey;

    @ManyToOne
    @JoinColumn(name = "send_wallet_idx")
    private Wallet sendWallet;

    @ManyToOne
    @JoinColumn
    private Wallet receiveWallet;
}
