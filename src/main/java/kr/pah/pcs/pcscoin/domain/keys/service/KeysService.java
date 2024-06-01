package kr.pah.pcs.pcscoin.domain.keys.service;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import kr.pah.pcs.pcscoin.domain.keys.repository.KeysRepository;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeysService {
    private final KeysRepository keysRepository;


    /**
     *
     * @param keysIdx
     * @return Keys
     */
    public Keys getKeysByIdx(UUID keysIdx) {
        return keysRepository.findById(keysIdx).orElseThrow(
                () -> new IllegalStateException("키가 존재하지 않습니다.")
        );
    }

    /**
     *
     * @param clientKey
     * @return Keys
     */
    public Keys getKeysByClientKey(String clientKey) {
        return keysRepository.getByClientKey(clientKey).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 클라이언트 키 입니다.")
        );
    }

    public Keys getKeysByUser(User user) {
        return keysRepository.getByUserIdx(user.getIdx()).orElseThrow(
                () -> new IllegalStateException("키가 존재하지 않습니다.")
        );
    }

    /**
     *
     * @param clientKey
     * @return true or false
     */
    public boolean validClient(String clientKey) {
        try {
            Keys keys = getKeysByClientKey(clientKey);

            return true;
        }catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * @param user
     * @return Keys
     */
    @Transactional
    public Keys createKeys(User user) {
        Keys keys = Keys.builder()
                .user(user)
                .build();

        return keysRepository.save(keys);
    }


}
