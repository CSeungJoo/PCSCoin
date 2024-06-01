package kr.pah.pcs.pcscoin.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Order(1)
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            User user = userService.getUserByToken(token);
            if (user.isActive() && !user.isDelete())
                saveAuthentication(user);
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
