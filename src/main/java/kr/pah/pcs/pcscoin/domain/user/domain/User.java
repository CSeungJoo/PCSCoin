package kr.pah.pcs.pcscoin.domain.user.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.Role;
import kr.pah.pcs.pcscoin.domain.model.UserType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class User {
    @Id @GeneratedValue
    private UUID idx;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean isDelete;

    @OneToOne
    @JoinColumn(name = "wallet_idx")
    private Wallet wallet;

    @PrePersist
    public void init() {
        isDelete = false;
        if (role == null)
            role = Role.USER;
    }
}
