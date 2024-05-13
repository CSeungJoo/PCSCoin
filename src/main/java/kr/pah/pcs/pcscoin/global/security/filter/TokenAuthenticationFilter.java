package kr.pah.pcs.pcscoin.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.common.TokenUtils;
import kr.pah.pcs.pcscoin.global.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final RedisTokenService redisTokenService;
    private final UserService userService;
    private final TokenUtils tokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractUserId(request);

        if (token != null) {

            if (tokenUtils.validateToken(token)) {
                response.addHeader("token", token);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractUserId(HttpServletRequest request) {
        // 요청에서 UUID 추출하는 로직
        // 실제 구현은 프로젝트의 요구 사항에 따라 다를 수 있음
        String token = request.getHeader("token");
        if (token != null) {
            return token;
        }
        return null;
    }

    private Authentication authentication(String token) {
        return (Authentication) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
