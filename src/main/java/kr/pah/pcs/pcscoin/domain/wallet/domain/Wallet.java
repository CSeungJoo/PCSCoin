package kr.pah.pcs.pcscoin.domain.wallet.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Wallet {
    @Id
    private int idx;

    @Column
    private String name;

    @Column
    private BigDecimal money;

    @Column
    private boolean isDelete;

    @OneToOne(mappedBy = "wallet")
    private User owner;
}
