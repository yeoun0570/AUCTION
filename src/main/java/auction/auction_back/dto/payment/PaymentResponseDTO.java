package auction.auction_back.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String orderId;
    private String orderName;
    private int totalAmount;
    private String status;
    private String paymentKey;
}
