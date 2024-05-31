package kr.pah.pcs.pcscoin.domain.tradeLog.controller;

import kr.pah.pcs.pcscoin.domain.trade.dto.ReturnTradeDto;
import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import kr.pah.pcs.pcscoin.domain.tradeLog.dto.ReturnTradeLogDto;
import kr.pah.pcs.pcscoin.domain.tradeLog.service.TradeLogService;
import kr.pah.pcs.pcscoin.domain.user.domain.User;
import kr.pah.pcs.pcscoin.domain.wallet.domain.Wallet;
import kr.pah.pcs.pcscoin.domain.wallet.service.WalletService;
import kr.pah.pcs.pcscoin.global.common.Result;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trade-log")
public class TradeLogController {
    private final TradeLogService tradeLogService;
    private final WalletService walletService;

    @GetMapping("/get")
    public ResponseEntity<?> getMyTradeLog(
            @RequestParam(value = "month", defaultValue = "3") int month,
            @PageableDefault Pageable pageable
            ) {
        try {
            User user = SecurityUtils.getLoginUser();
            Wallet wallet = walletService.getWalletByUser(user);
            LocalDateTime localDateTime = null;

            switch(month) {
                case 3 -> localDateTime = LocalDateTime.now().minusMonths(3);
                case 6 -> localDateTime = LocalDateTime.now().minusMonths(6);
                case 9 -> localDateTime = LocalDateTime.now().minusMonths(9);
                case 12 -> localDateTime = LocalDateTime.now().minusMonths(12);
                case 0 -> localDateTime = LocalDateTime.MIN;
            }

            Page<TradeLog> tradeLogs = tradeLogService.getTradeLogByWalletAndTradeDate(wallet, localDateTime, pageable);

            List<ReturnTradeLogDto> returnTradeLogDtos = tradeLogs.stream()
                    .map(
                            tradeLog ->
                                    tradeLog.getSendWallet() == wallet
                                            ? new ReturnTradeLogDto(tradeLog, tradeLog.getAfterSendWalletMoney(), tradeLog.getPrice().multiply(new BigDecimal(-1)))
                                            : new ReturnTradeLogDto(tradeLog, tradeLog.getAfterReceiveWalletMoney(), tradeLog.getPrice())

                    ).toList();

            return ResponseEntity.ok(new Result<>(returnTradeLogDtos));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다."));
        }
    }

}
