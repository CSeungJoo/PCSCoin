package kr.pah.pcs.pcscoin.domain.keys.service;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import kr.pah.pcs.pcscoin.domain.keys.repository.KeysRepository;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeysService {
    private final KeysRepository keysRepository;

    public Keys getKeysByIdx(Long keysIdx) {
        return keysRepository.findById(keysIdx).orElseThrow(
                () -> new IllegalStateException("키가 존재하지 않습니다.")
        );
    }

    @Transactional
    public Keys createKeys(User user) {
        Keys keys = Keys.builder()
                .user(user)
                .build();

        return keysRepository.save(keys);
    }


}
