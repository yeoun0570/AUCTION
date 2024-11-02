package auction.auction_back.controller;

import auction.auction_back.dto.payment.PaymentResponseDTO;
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

    @PostMapping("/success")
    public ResponseEntity<PaymentResponseDTO> confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount
    ) {
        PaymentResponseDTO response = paymentService.confirmPayment(paymentKey,orderId,amount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentKey}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String paymentKey) {
        PaymentResponseDTO response = paymentService.getPayment(paymentKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentKey}/cancel")
    public ResponseEntity<PaymentResponseDTO> cancelPayment(
            @PathVariable String paymentKey,
            @RequestParam String cancelReason
    ) {
        PaymentResponseDTO response = paymentService.cancelPayment(paymentKey,cancelReason);
        return ResponseEntity.ok(response);
    }
}
