package kr.pah.pcs.pcscoin.domain.wallet.service;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.repository.UserRepository;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.domain.wallet.repository.WalletRepository;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    @Transactional
    public Wallet createWallet() {

        User user = SecurityUtils.getLoginUser();

        Wallet wallet = Wallet.builder()
                .name(user.getName() + "'s wallet")
                .money(new BigDecimal(0))
                .owner(user)
                .build();

        Wallet saveWallet = walletRepository.save(wallet);

        user.setWallet(wallet);

        userRepository.save(user);

        return saveWallet;
    }

    @Transactional
    public Wallet modifiedWalletName(Wallet wallet, String newName) {
        wallet.setName(newName);
        return walletRepository.save(wallet);
    }

    @Transactional
    public void deleteWallet(User user) {
        Wallet wallet = user.getWallet();

        wallet.deleteWallet();
        user.setWallet(null);

        walletRepository.save(wallet);
        userRepository.save(user);
    }

    public Wallet getWalletByUser(User user) {
        if(user.getWallet() == null)
            throw new IllegalStateException("사용자 소유의 지갑이 없습니다.");

        return user.getWallet();
    }
}
