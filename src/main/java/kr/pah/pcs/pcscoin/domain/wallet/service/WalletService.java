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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    /**
     * @return Wallet
     * 로그인한 유저를 기반으로 지갑 생성
     */
    @Transactional
    public Wallet createWallet() {

        User user = SecurityUtils.getLoginUser();

        Wallet wallet = Wallet.builder()
                .name(user.getName() + "'s wallet")
                .money(new BigDecimal(0))
                .user(user)
                .build();

        Wallet saveWallet = walletRepository.save(wallet);

        user.addWallet(wallet);

        userRepository.save(user);

        return saveWallet;
    }

    /**
     * @param wallet
     * @param newName
     * @return Wallet
     * 지갑 이름 변경
     */
    @Transactional
    public Wallet modifiedWalletName(Wallet wallet, String newName) {
        wallet.setName(newName);
        return walletRepository.save(wallet);
    }

    /**
     * @param user
     * 유저가 가지고 있는 지갑 삭제처리
     */
    @Transactional
    public void deleteWalletByUser(User user) {
        Wallet wallet = getWalletByUser(user);

        wallet.deleteWallet();
        user.addWallet(null);

        walletRepository.save(wallet);
        userRepository.save(user);
    }

    /**
     * @param user
     * @return Wallet
     * 유저를 기반으로 지갑중 삭제되지 않은것을 찾아 조회
     */
    public Wallet getWalletByUser(User user) {
        List<Wallet> wallets = walletRepository.getWalletsByUser(user);
        if(wallets == null)
            throw new IllegalStateException("사용자 소유의 지갑이 없습니다.");

        for (Wallet wallet : wallets) {
            if(isAvailableWallet(wallet))
                return wallet;
        }

        throw new IllegalStateException("사용자 소유의 지갑이 없습니다.");
    }

    /**
     * @param wallet
     * @return true or false
     * 사용가능한 지갑인지 확인
     */
    public boolean isAvailableWallet(Wallet wallet) {
        if (wallet == null || wallet.isDelete())
            return false;

        return true;
    }
}
