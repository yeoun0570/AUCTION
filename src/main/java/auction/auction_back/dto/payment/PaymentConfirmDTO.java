package auction.auction_back.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmDTO {
    private String paymentKey;     // 토스페이먼츠 결제 키
    private String orderId;        // 주문 번호
    private Integer amount;        // 결제 금액
    private String buyerId;          // 구매자 ID
    private String sellerId;         // 판매자 ID
}
