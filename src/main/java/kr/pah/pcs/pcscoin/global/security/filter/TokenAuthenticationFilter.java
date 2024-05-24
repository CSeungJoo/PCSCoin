package kr.pah.pcs.pcscoin.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.common.TokenUtils;
import kr.pah.pcs.pcscoin.global.security.auth.PrincipalDetails;
import kr.pah.pcs.pcscoin.global.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
@Order(1)
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final TokenUtils tokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {

            if (tokenUtils.validateToken(token)) {
                saveAuthentication(userService.getUserByToken(token));
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getHeader("token") == null || request.getHeader("token").isBlank()) {
            return null;
        }
        String token = request.getHeader("token");
        if (token != null) {
            return token;
        }
        return null;
    }

    public void saveAuthentication(User myUser) {
        String password = myUser.getPassword();

        PrincipalDetails userDetailsUser = new PrincipalDetails(myUser);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, password, userDetailsUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
