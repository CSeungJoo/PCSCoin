package kr.pah.pcs.pcscoin.domain.user.dto;

import kr.pah.pcs.pcscoin.domain.model.Grade;
import kr.pah.pcs.pcscoin.domain.user.domain.StudentId;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnStudentIdDto {
    private Grade grade;
    private String studentId;

    public ReturnStudentIdDto(StudentId studentId) {
        this.grade = studentId.getGrade();
        this.studentId = studentId.getStudentId();
    }
}
