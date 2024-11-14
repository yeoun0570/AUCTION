package auction.auction_back.controller;

import auction.auction_back.dto.payment.PaymentConfirmDTO;
import auction.auction_back.dto.payment.PaymentResponseDTO;
import auction.auction_back.dto.payment.PaymentSuccessDTO;
import auction.auction_back.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    //결제 승인 -> 결제하고 나서 승인 처리 -> IN_PROGRESS 삳태를 DONE 으로 만들어주기
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponseDTO> confirmPayment(@RequestBody PaymentConfirmDTO request) {
        return ResponseEntity.ok(paymentService.confirmPayment(request));
    }

    //결제 정보 조회
    @GetMapping("/{paymentKey}")
    public PaymentResponseDTO getPaymentDetails(@PathVariable String paymentKey) {
        return paymentService.getPaymentDetails(paymentKey);
    }

    //구매 확정 처리
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<PaymentResponseDTO> confirmPurchase(@PathVariable String orderId) {
        PaymentResponseDTO response = paymentService.confirmPurchase(orderId);
        return ResponseEntity.ok(response);
    }

    //결제 성공 및 결제 자금 보관(에스크로 홀딩)
    @PostMapping("/success")
    public ResponseEntity<PaymentResponseDTO> processSuccessPayment(
            @RequestBody PaymentSuccessDTO request
    ) {
        PaymentResponseDTO response = paymentService.processPayment(
                request.getPaymentKey(), request.getBuyerId(), request.getSellerId());
        return ResponseEntity.ok(response);
    }
}
