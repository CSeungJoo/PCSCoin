package kr.pah.pcs.pcscoin.global.common;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
public class TokenUtils {

    private static final byte[] secretBytes = Base64.getDecoder().decode("your_secret_key_in_base64");
    private static final SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
    private static final Key SECRET_KEY = new SecretKeySpec(secretBytes, "HmacSHA256");

    private final BCryptPasswordEncoder pwdEncoder;
    private final UserService userService;
    private final RedisTokenService redisTokenService;

    public String generateToken(User user) {
        return pwdEncoder.encode(user.getEmail() + user.getPassword());
    }

    public boolean validateToken(String token) {
        return token.equals(redisTokenService.getToken(SecurityUtils.getLoginUser().getIdx()));
    }
}
