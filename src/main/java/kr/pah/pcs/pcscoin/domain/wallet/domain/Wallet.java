package kr.pah.pcs.pcscoin.domain.wallet.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Wallet {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idx;

    @Column
    private String name;

    @Column
    private BigDecimal money;

    @Column
    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_idx", unique = false)
    private User user;

    @PrePersist
    public void init() {
        isDelete = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void deleteWallet() {
        isDelete = true;
    }
}
