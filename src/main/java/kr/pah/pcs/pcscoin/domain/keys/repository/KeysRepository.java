package kr.pah.pcs.pcscoin.domain.keys.repository;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeysRepository extends JpaRepository<Keys, Long> {
}
