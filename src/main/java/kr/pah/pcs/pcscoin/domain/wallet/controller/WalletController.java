package kr.pah.pcs.pcscoin.domain.wallet.controller;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.domain.wallet.dto.ModifiedWalletNameDto;
import kr.pah.pcs.pcscoin.domain.wallet.dto.ReturnWalletDto;
import kr.pah.pcs.pcscoin.domain.wallet.service.WalletService;
import kr.pah.pcs.pcscoin.global.common.Result;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    @GetMapping("/get")
    public ResponseEntity<?> getMyWallet() {
        try {

            User user = SecurityUtils.getLoginUser();
            Wallet wallet = walletService.getWalletByUser(user);

            return ResponseEntity.ok(new Result<>(new ReturnWalletDto(wallet)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWallet() {
        try {
            User user = SecurityUtils.getLoginUser();

            try {
                walletService.getWalletByUser(user);
            }catch (IllegalStateException e) {
                Wallet wallet = walletService.createWallet();
                return ResponseEntity.ok(new Result<>(new ReturnWalletDto(wallet)));
            }

            throw new IllegalStateException("이미 지갑을 가지고 있습니다.");
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다."));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteWallet() {
        try {
            User user = SecurityUtils.getLoginUser();
            Wallet wallet = walletService.getWalletByUser(user);

            walletService.deleteWalletByUser(user);

            return ResponseEntity.ok(new Result<>("정상적으로 삭제되었습니다."));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

    @PutMapping("/rename")
    public ResponseEntity<?> modifiedWalletName(@RequestBody ModifiedWalletNameDto modifiedWalletNameDto) {
        try {
            User user = SecurityUtils.getLoginUser();
            Wallet getWallet = walletService.getWalletByUser(user);

            Wallet wallet = walletService.modifiedWalletName(getWallet, modifiedWalletNameDto.getNewName());

            return ResponseEntity.ok(new Result<>(new ReturnWalletDto(wallet)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }
}
