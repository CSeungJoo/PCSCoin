package kr.pah.pcs.pcscoin.domain.keys.repository;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeysRepository extends JpaRepository<Keys, Long> {
     Optional<Keys> getByClientKey(String clientKey);
}
