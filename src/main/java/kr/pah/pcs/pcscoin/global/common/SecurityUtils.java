package kr.pah.pcs.pcscoin.global.common;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
}
