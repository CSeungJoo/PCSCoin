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

    public Keys getKeysByClientKey(String clientKey) {
        return keysRepository.getByClientKey(clientKey).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 클라이언트 키 입니다.")
        );
    }

    public boolean validClient(String clientKey) {
        try {
            Keys keys = getKeysByClientKey(clientKey);

            return true;
        }catch (IllegalStateException e) {
            return false;
        }
    }

    @Transactional
    public Keys createKeys(User user) {
        Keys keys = Keys.builder()
                .user(user)
                .build();

        return keysRepository.save(keys);
    }


}
