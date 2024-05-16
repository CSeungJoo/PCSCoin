package kr.pah.pcs.pcscoin.global.security.auth;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 유저이름입니다.")
        );

        return new PrincipalDetails(user);
    }
}