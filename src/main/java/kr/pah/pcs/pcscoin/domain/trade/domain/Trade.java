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
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"trade_id", "receive_wallet_idx"})})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Trade {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Idx;

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
    @JoinColumn(name = "receive_wallet_idx")
    private Wallet receiveWallet;
}
