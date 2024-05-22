package kr.pah.pcs.pcscoin.domain.user.domain;

import jakarta.persistence.*;
import kr.pah.pcs.pcscoin.domain.model.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudentId {
    @Id
    @GeneratedValue
    private Integer idx;

    @Column
    private Grade grade;

    @Column
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
