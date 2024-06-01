package kr.pah.pcs.pcscoin.domain.keys.controller;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import kr.pah.pcs.pcscoin.domain.keys.dto.ReturnKeysDto;
import kr.pah.pcs.pcscoin.domain.keys.service.KeysService;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.global.common.Result;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keys")
public class KeysController {

    private final KeysService keysService;

    @GetMapping("/get")
    public ResponseEntity<?> getKeysByUser() {
        try {
            User user = SecurityUtils.getLoginUser();

            Keys keys = keysService.getKeysByUser(user);

            return ResponseEntity.ok(new Result<>(new ReturnKeysDto(keys)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다."));
        }
    }
}
