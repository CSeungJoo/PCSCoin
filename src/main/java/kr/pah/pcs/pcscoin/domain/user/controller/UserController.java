package kr.pah.pcs.pcscoin.domain.user.controller;

import kr.pah.pcs.pcscoin.domain.user.dto.ReturnUserDto;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.dto.CreateUserDto;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder pwdEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto) {
        createUserDto.setPassword(pwdEncoder.encode(createUserDto.getPassword()));
        try {
            User user = userService.createUser(createUserDto);
            return ResponseEntity.ok(new Result<>(new ReturnUserDto(user)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> activeUser(@RequestParam("active") String active) {
        try {
            userService.activeUser(active);

            return ResponseEntity.ok(new Result<>("정상적으로 활성화 되었습니다."));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e){
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }
}
