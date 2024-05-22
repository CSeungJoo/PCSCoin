package kr.pah.pcs.pcscoin.domain.user.service;

import kr.pah.pcs.pcscoin.domain.model.Grade;
import kr.pah.pcs.pcscoin.domain.user.domain.StudentId;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.dto.CreateUserDto;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import kr.pah.pcs.pcscoin.global.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final MailService mailService;

    @Value("${url}")
    private String url;

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
    public void removeUser(User user) {
        if (user != null)
            userRepository.delete(user);
    }

    public boolean alreadyExistsEmail(String email) {
        try {
            User user = getUserByEmail(email);
            return true;
        }catch (IllegalStateException e) {
            return false;
        }


    }

    @Transactional
    public User createUser(CreateUserDto createUserDto) throws NoSuchAlgorithmException {

        if (alreadyExistsEmail(createUserDto.getEmail()))
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");

        User user = User.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .name(createUserDto.getName())
                .nickname(createUserDto.getNickname())
                .phone(createUserDto.getPhone())
                .build();
        User saveUser = userRepository.save(user);

        for (int i = 0; i < 3; i++) {
            saveUser.addStudentId(StudentId.builder()
                            .grade(i == 0 ? Grade.FIRST : i == 1 ? Grade.SECOND : Grade.THIRD)
                            .studentId("")
                            .build());
        }

        mailService.sendEmail(createUserDto.getEmail()
                ,"본인확인용 메일발송"
                ,"본인이라면 "+ url +"/active?active="+ Base64.getEncoder().encodeToString(saveUser.getIdx().toString().getBytes()) +" 클릭해주세요 \n 아니라면 "+ url +"에 접속하여 문의를 해주세요.");

        return saveUser;
    }

    @Transactional
    public void activeUser(String encodeIdx) {
        byte[] decode = Base64.getDecoder().decode(encodeIdx);
        UUID userIdx = UUID.fromString(new String(decode));
        User user = getUser(userIdx);

        user.accountActive();
    }

}
