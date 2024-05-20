package kr.pah.pcs.pcscoin.global.common;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    public static User getLoginUser() {
        try {
            PrincipalDetails user = (PrincipalDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            return  user.getUser();
        }catch (Exception e) {
            throw new IllegalStateException("로그인한 상태가 아닙니다.");
        }
    }

    private static final String ALGORITHM = "SHA-256";
    private static final String salt = "https://github.com/CSeungJoo";

    public static String hashing(String valueToEnc) throws NoSuchAlgorithmException {
        String saltedVal = valueToEnc + salt;
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hash = digest.digest(saltedVal.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

}
