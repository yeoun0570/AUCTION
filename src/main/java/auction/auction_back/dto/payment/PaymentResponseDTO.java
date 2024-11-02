package auction.auction_back.dto.payment;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String paymentKey;
    private String orderId;
    private int amount;
    private PaymentStatus status;
    private String method;
    private LocalDateTime approvedAt;
}
