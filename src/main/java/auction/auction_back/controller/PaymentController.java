package auction.auction_back.controller;

import auction.auction_back.dto.payment.PaymentResponseDTO;
import auction.auction_back.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{paymentKey}")
    public PaymentResponseDTO getPaymentDetails(@PathVariable String paymentKey) {
        return paymentService.getPaymentDetails(paymentKey);
    }

}
