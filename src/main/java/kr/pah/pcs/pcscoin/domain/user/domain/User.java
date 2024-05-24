package kr.pah.pcs.pcscoin.domain.user.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.Role;
import kr.pah.pcs.pcscoin.domain.model.UserType;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<StudentId> studentsId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Wallet> wallets;

    @PrePersist
    public void init() throws NoSuchAlgorithmException {
        idx = UUID.randomUUID();
        isDelete = false;
        isActive = false;
        if(userType == null)
            userType = UserType.USER;
        if (role == null)
            role = Role.USER;
        token = SecurityUtils.hashing(Base64.getEncoder().encodeToString((idx.toString() + email + password).getBytes()));
    }

    @PreUpdate
    public void update() throws NoSuchAlgorithmException {
        token = SecurityUtils.hashing(Base64.getEncoder().encodeToString((idx.toString() + email + password).getBytes()));
    }

    public void deleteUser() {
        isDelete = true;
    }

    public void accountActive() {
        isActive = true;
    }

    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
    }

    public void addStudentId(StudentId studentId) {
        this.studentsId.add(studentId);
    }

    public void initStudentsId() {
        this.studentsId = new ArrayList<>();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
