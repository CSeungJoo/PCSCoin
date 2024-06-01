package kr.pah.pcs.pcscoin.domain.keys.dto;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnKeysDto {
    private String clientKey;
    private String secretKey;

    public ReturnKeysDto(Keys keys) {
        this.clientKey = keys.getClientKey();
        this.secretKey = keys.getSecretKey();
    }
}
