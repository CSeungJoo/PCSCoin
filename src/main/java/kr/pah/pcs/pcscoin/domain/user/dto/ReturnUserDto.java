package kr.pah.pcs.pcscoin.domain.user.dto;

import kr.pah.pcs.pcscoin.domain.model.UserType;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.wallet.dto.ReturnWalletDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnUserDto {
    private UUID idx;
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private UserType userType;
    private List<ReturnStudentIdDto> studentId = new ArrayList<>();
    private ReturnWalletDto wallet;

    public ReturnUserDto(User user) {
        this.idx = user.getIdx();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.userType = user.getUserType();
        if(user.getStudentsId() != null)
            user.getStudentsId().forEach(student -> this.studentId.add(new ReturnStudentIdDto(student)));
        if(user.getWallets() != null)
            this.wallet = new ReturnWalletDto(user.getWallets().stream().filter(wallet -> !wallet.isDelete()).findFirst().orElseThrow());
    }
}
