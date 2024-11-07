package auction.auction_back.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessDTO {
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private String buyerId;
    private String sellerId;
}
