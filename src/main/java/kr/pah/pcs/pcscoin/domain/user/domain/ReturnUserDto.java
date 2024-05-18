package kr.pah.pcs.pcscoin.domain.user.domain;

import kr.pah.pcs.pcscoin.domain.model.UserType;
import kr.pah.pcs.pcscoin.domain.wallet.dto.ReturnWalletDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private ReturnWalletDto wallet;

    public ReturnUserDto(User user) {
        this.idx = user.getIdx();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.userType = user.getUserType();
        this.wallet = new ReturnWalletDto(user.getWallet());
    }
}
