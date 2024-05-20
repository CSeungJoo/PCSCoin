package kr.pah.pcs.pcscoin.domain.user.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.Role;
import kr.pah.pcs.pcscoin.domain.model.UserType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.*;

import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
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

    @Column
    private boolean isActive;

    @Column
    private String token;

    @OneToOne
    @JoinColumn(name = "wallet_idx")
    private Wallet wallet;

    @PrePersist
    public void init() throws Exception {
        idx = UUID.randomUUID();
        isDelete = false;
        isActive = false;
        if(userType == null)
            userType = UserType.USER;
        if (role == null)
            role = Role.USER;
        token = SecurityUtils.hashing(Base64.getEncoder().encodeToString((idx.toString() + email + password).getBytes()));
    }

    public void accountActive() {
        isActive = true;
    }
}
