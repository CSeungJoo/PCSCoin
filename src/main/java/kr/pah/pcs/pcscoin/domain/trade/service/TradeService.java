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

    /**
     * @param tradeIdx
     * @return Trade
     * tradeIdx를 통한 Trade 조회
     * @exception IllegalStateException "조회 값이 없을시"
     */
    public Trade getTradeByIdx(UUID tradeIdx) {
        return tradeRepository.findById(tradeIdx).orElseThrow(
                () -> new IllegalStateException("존재하지 않은 거래입니다.")
        );
    }

    /**
     * @param transferTradeDto
     * @return Trade
     * 로그인된 유저와 dto를 통해 받아온 핸드폰 번호로 유저를 조회하여 Trade 생성 후 저장 및 return
     */
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

    /**
     * @param paymentTradeDto
     * @param clientKey
     * @return Trade
     * 클라이언트 키가 올바른 키인지 검증후 Trade 생성후 저장 및 return
     * @exception IllegalStateException "잘못된 클라이언트 키인 경우"
     */
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

    /**
     * @param tradeConfirmDto
     * @param secretKey
     * @return TradeLog
     * 거래 정보가 동일한지 검증후 거래타입이 결재 라면 secretKey검증 후 잔액 조회후 결재 진행 및 거래 완료처리
     */
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
        walletService.save(sendWallet);
        walletService.save(receiveWallet);

        TradeLog tradeLog = tradeLogService.createTradeLog(trade);
        deleteTrade(trade);

        return tradeLog;
    }

    /**
     * @param sendWallet
     * @param price
     * @return true or false
     * 돈을 보내려는 지갑의 잔액이 부족한지 확인
     */
    public boolean transferMoneyCheck(Wallet sendWallet, BigDecimal price) {
        switch(sendWallet.getMoney().subtract(price).compareTo(BigDecimal.ZERO)) {
            case -1 -> throw new IllegalStateException("계좌에 잔액의 부족합니다");
            case 0, 1 -> {
                return true;
            }
        }
        return false;
    }

    /**
     * @param trade
     * 거래 삭제
     */
    @Transactional
    public void deleteTrade(Trade trade) {
        tradeRepository.delete(trade);
    }

    /**
     * @param tradeLogIdx
     * @return Trade
     * tradeLogIdx로 TradeLog를 불러와 환불처리
     */
    @Transactional
    public Trade refundMoney(Long tradeLogIdx, String secretKey) {
        TradeLog tradeLog = tradeLogService.getTradeLogByIdx(tradeLogIdx);

        if (!tradeLog.getTradeType().equals(TradeType.PAYMENT))
            throw new IllegalStateException("환불은 결제유형:결제 만 가능합니다.");

        if (!tradeLog.getReceiveWallet().getUser().getKeys().getSecretKey().equals(secretKey))
            throw new IllegalStateException("잘못된 시크릿키 입니다.");

        Trade trade = Trade.builder()
                .sendWallet(tradeLog.getReceiveWallet())
                .receiveWallet(tradeLog.getSendWallet())
                .tradeName("refund")
                .price(tradeLog.getPrice())
                .tradeId(tradeLog.getTradeId() + "-refund")
                .expiredTime(LocalDateTime.now().plusMinutes(5))
                .tradeType(TradeType.REFUND)
                .build();

        return tradeRepository.save(trade);
    }

    /**
     * @param tradeIdx
     * 거래 삭제
     */
    public void cancelTrade(UUID tradeIdx) {
        User user = SecurityUtils.getLoginUser();

        Trade trade = getTradeByIdx(tradeIdx);

        if (!user.getIdx().equals(trade.getReceiveWallet().getUser().getIdx()) && !user.getIdx().equals(trade.getSendWallet().getUser().getIdx()))
            throw new IllegalStateException("사용자가 관련된 거래가 아닙니다.");
        deleteTrade(trade);
    }
}
