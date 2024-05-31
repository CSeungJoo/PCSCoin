package kr.pah.pcs.pcscoin.domain.trade.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.pah.pcs.pcscoin.domain.trade.domain.Trade;
import kr.pah.pcs.pcscoin.domain.trade.dto.PaymentTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.ReturnTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TradeConfirmDto;
import kr.pah.pcs.pcscoin.domain.trade.dto.TransferTradeDto;
import kr.pah.pcs.pcscoin.domain.trade.service.TradeService;
import kr.pah.pcs.pcscoin.domain.tradeLog.domain.TradeLog;
import kr.pah.pcs.pcscoin.domain.tradeLog.dto.ReturnTradeLogDto;
import kr.pah.pcs.pcscoin.global.common.Result;
import kr.pah.pcs.pcscoin.global.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferTradeDto transferTradeDto) {
        try {
            Trade trade = tradeService.transferMoney(transferTradeDto);

            return ResponseEntity.ok(new Result<>(new ReturnTradeDto(trade)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.",true));
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> paymentMoney(@RequestBody PaymentTradeDto paymentTradeDto, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Location", paymentTradeDto.getFailUrl());
        try {
            Trade trade = tradeService.paymentMoney(paymentTradeDto, request.getHeader("clientKey"));
            response.setHeader("Location", paymentTradeDto.getSuccessUrl());
            return ResponseEntity.ok(new Result<>(new ReturnTradeDto(trade)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> tradeConfirm(@RequestBody TradeConfirmDto tradeConfirmDto, HttpServletRequest request) {
        try {
            TradeLog tradeLog = tradeService.tradeConfirm(tradeConfirmDto, request.getHeader("secretKey"));

            ReturnTradeLogDto tradeLogDto = new ReturnTradeLogDto(tradeLog, tradeLog.getAfterSendWalletMoney(), tradeLog.getPrice().multiply(new BigDecimal(-1)));

            return ResponseEntity.ok(new Result<>(tradeLogDto));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

    @PostMapping("/refund/{idx}")
    public ResponseEntity<?> refundMoney(@PathVariable("idx") Long tradeLogIdx) {
        try {
            Trade trade = tradeService.refundMoney(tradeLogIdx);

            return ResponseEntity.ok(new Result<>(new ReturnTradeDto(trade)));
        }catch (IllegalStateException e) {
            return ResponseEntity.ok(new Result<>(e.getMessage(), true));
        }catch (Exception e) {
            return ResponseEntity.ok(new Result<>("알수 없는 에러가 발생하였습니다.", true));
        }
    }

}
