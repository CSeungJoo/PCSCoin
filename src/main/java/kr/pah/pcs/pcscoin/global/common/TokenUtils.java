package kr.pah.pcs.pcscoin.global.common;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.security.config.SecurityConfig;
import kr.pah.pcs.pcscoin.global.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TokenUtils {

//    private static final byte[] secretBytes = Base64.getDecoder().decode("your_secret_key_in_base64");
//    private static final Key SECRET_KEY = new SecretKeySpec(secretBytes, "HmacSHA256");

    private final UserService userService;

    public boolean validateToken(String token) {
        return !userService.getUserByToken(token).isDelete();
    }
}
