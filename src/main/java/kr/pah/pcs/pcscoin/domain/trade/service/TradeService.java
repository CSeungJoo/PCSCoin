package kr.pah.pcs.pcscoin.domain.trade.service;

import kr.pah.pcs.pcscoin.domain.keys.domain.Keys;
import kr.pah.pcs.pcscoin.domain.keys.service.KeysService;
import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import kr.pah.pcs.pcscoin.domain.trade.dto.PaymentTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TradeConfirmDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TransferTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.repository.TradeRepository;
import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import kr.pah.pcs.pcscoin.domain.tradeLog.service.TradeLogService;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.domain.wallet.service.WalletService;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TradeService {

    private final UserService userService;
    private final WalletService walletService;
    private final TradeRepository tradeRepository;
    private final TradeLogService tradeLogService;
    private final KeysService keysService;

    public Trade getTradeByIdx(UUID idx) {
        return tradeRepository.findById(idx).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 거래입니다.")
        );
    }

    @Transactional
    public Trade transferMoney(TransferTradeDto transferTradeDto) {

        User sendUser = SecurityUtils.getLoginUser();
        Wallet sendWallet = walletService.getWalletByUser(sendUser);

        User receiveUser = userService.getUserByPhone(transferTradeDto.getPhone());
        Wallet receviceWallet = walletService.getWalletByUser(receiveUser);

        Trade trade = Trade.builder()
                .expiredTime(LocalDateTime.now().plusMinutes(5))
                .tradeType(TradeType.TRANSFER)
                .tradeId(transferTradeDto.getTradeId())
                .price(transferTradeDto.getPrice())
                .sendWallet(sendWallet)
                .receiveWallet(receviceWallet)
                .build();

        return tradeRepository.save(trade);
    }

    @Transactional
    public Trade paymentMoney(PaymentTradeDto paymentTradeDto, String clientKey) {

        User receviceUser = keysService.getKeysByClientKey(clientKey).getUser();
        Wallet receviceWallet = walletService.getWalletByUser(receviceUser);

        if (!keysService.validClient(clientKey) || !receviceUser.getKeys().getClientKey().equals(clientKey))
            throw new IllegalStateException("잘못된 클라이언트키 입니다.");

        User sendUser = SecurityUtils.getLoginUser();
        Wallet sendWallet = walletService.getWalletByUser(sendUser);


        Trade trade = Trade.builder()
                .expiredTime(LocalDateTime.now().plusMinutes(10))
                .tradeType(TradeType.PAYMENT)
                .tradeId(paymentTradeDto.getTradeId())
                .price(paymentTradeDto.getPrice())
                .sendWallet(sendWallet)
                .clientKey(clientKey)
                .receiveWallet(receviceWallet)
                .build();

        return tradeRepository.save(trade);
    }

    @Transactional
    public TradeLog tradeConfirm(TradeConfirmDto tradeConfirmDto, String secretKey) {
        Trade trade = getTradeByIdx(tradeConfirmDto.getTradeIdx());

        if (trade.getPrice().compareTo(tradeConfirmDto.getPrice()) != 0 || !trade.getTradeId().equals(tradeConfirmDto.getTradeId())) {
            throw new IllegalStateException("결재정보가 달라졌습니다.");
        }

        Wallet receiveWallet = trade.getReceiveWallet();
        Wallet sendWallet = trade.getSendWallet();

        if (trade.getTradeType().equals(TradeType.PAYMENT)) {
            Keys keys = receiveWallet.getUser().getKeys();
            if(secretKey == null ||!secretKey.equals(keys.getSecretKey()))
                throw new IllegalStateException("잘못된 시크릿키 입니다.");
        }


        if (transferMoneyCheck(sendWallet, tradeConfirmDto.getPrice())) {
            sendWallet.setMoney(sendWallet.getMoney().subtract(tradeConfirmDto.getPrice()));
            receiveWallet.setMoney(sendWallet.getMoney().add(tradeConfirmDto.getPrice()));
        }

        TradeLog tradeLog = tradeLogService.createTradeLog(trade);
        deleteTrade(trade);

        return tradeLog;
    }

    public boolean transferMoneyCheck(Wallet sendWallet, BigDecimal price) {
        switch(sendWallet.getMoney().subtract(price).compareTo(BigDecimal.ZERO)) {
            case -1 -> throw new IllegalStateException("계좌에 잔액의 부족합니다");
            case 0, 1 -> {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteTrade(Trade trade) {
        tradeRepository.delete(trade);
    }

}
