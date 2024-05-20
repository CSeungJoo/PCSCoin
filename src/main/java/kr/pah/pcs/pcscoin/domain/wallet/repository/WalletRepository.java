package kr.pah.pcs.pcscoin.domain.wallet.repository;

import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
