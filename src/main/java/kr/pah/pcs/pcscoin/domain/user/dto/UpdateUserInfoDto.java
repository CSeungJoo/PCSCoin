package kr.pah.pcs.pcscoin.domain.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfoDto {
    private String nickname;
    private String phone;
    @Email
    private String email;
    private String firstGradeStudentId;
    private String secondGradeStudentId;
    private String thirdGradeStudentId;
}
