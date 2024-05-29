package kr.pah.pcs.pcscoin.domain.keys.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Keys {
    @Id
    private UUID idx;

    @Column
    private String clientKey;

    @Column
    private String secretKey;

    @OneToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Column
    private LocalDateTime createDate;

    @PrePersist
    private void init() throws NoSuchAlgorithmException {
        idx = UUID.randomUUID();
        clientKey = SecurityUtils.hashing(idx.toString() + LocalDateTime.now());
        secretKey = SecurityUtils.hashing(idx.toString() + LocalDateTime.now() + user.getIdx().toString());
        createDate = LocalDateTime.now();
    }
}
