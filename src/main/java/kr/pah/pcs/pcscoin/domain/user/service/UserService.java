package kr.pah.pcs.pcscoin.domain.user.service;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.dto.CreateUserDto;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import kr.pah.pcs.pcscoin.global.security.config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwdEncoder;

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 유저입니다.")
        );
    }
    public User getUserByToken(String token) {
        return userRepository.getUserByToken(token).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 유저입니다.")
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 유저입니다.")
        );
    }

    @Transactional
    public User createUser(CreateUserDto createUserDto) {


        System.out.println(createUserDto.getPassword());
        User user = User.builder()
                .email(createUserDto.getEmail())
                .password(pwdEncoder.encode(createUserDto.getPassword()))
                .name(createUserDto.getName())
                .nickname(createUserDto.getNickname())
                .phone(createUserDto.getPhone())
                .build();

        return userRepository.save(user);
    }
}
