package kr.pah.pcs.pcscoin.domain.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModifiedWalletNameDto {
    private String newName;
}

