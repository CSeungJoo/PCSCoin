package kr.pah.pcs.pcscoin.domain.trade.service;

import kr.pah.pcs.pcscoin.domain.model.TradeType;
import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import kr.pah.pcs.pcscoin.domain.trade.dto.PaymentTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TradeConfirmDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TransferTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.repository.TradeRepository;
import kr.pah.pcs.pcscoin.domain.tradeLog.service.TradeLogService;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.user.service.UserService;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.domain.wallet.repository.WalletRepository;
import kr.pah.pcs.pcscoin.domain.wallet.service.WalletService;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TradeService {

    private final UserService userService;
    private final WalletService walletService;
    private final TradeRepository tradeRepository;
    private final TradeLogService tradeLogService;

    public Trade getTradeByIdx(Long idx) {
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
    public Trade paymentMoney(PaymentTradeDto paymentTradeDto) {
        User sendUser = SecurityUtils.getLoginUser();
        Wallet sendWallet = walletService.getWalletByUser(sendUser);

        User receviceUser = userService.getUser(paymentTradeDto.getSellerIdx());
        Wallet receviceWallet = walletService.getWalletByUser(receviceUser);

        Trade trade = Trade.builder()
                .expiredTime(LocalDateTime.now().plusMinutes(10))
                .tradeType(TradeType.PAYMENT)
                .tradeId(paymentTradeDto.getTradeId())
                .price(paymentTradeDto.getPrice())
                .sendWallet(sendWallet)
                .receiveWallet(receviceWallet)
                .build();

        return tradeRepository.save(trade);
    }

    @Transactional
    public void tradeConfirm(TradeConfirmDto tradeConfirmDto) {
        Trade trade = getTradeByIdx(tradeConfirmDto.getTradeIdx());

        if (!trade.getPrice().equals(tradeConfirmDto.getPrice()) || !trade.getTradeId().equals(tradeConfirmDto.getTradeId()))
            throw new IllegalStateException("결재정보가 달라졌습니다.");

        Wallet sendWallet = trade.getSendWallet();
        Wallet receiveWallet = trade.getReceiveWallet();

        if (transferMoney(sendWallet, receiveWallet, tradeConfirmDto.getPrice())) {
            sendWallet.setMoney(sendWallet.getMoney().subtract(tradeConfirmDto.getPrice()));
            receiveWallet.setMoney(sendWallet.getMoney().add(tradeConfirmDto.getPrice()));
        }

        tradeLogService.createTradeLog(trade);
        deleteTrade(trade);
    }

    public boolean transferMoney(Wallet sendWallet, Wallet receiveWallet, BigDecimal price) {
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