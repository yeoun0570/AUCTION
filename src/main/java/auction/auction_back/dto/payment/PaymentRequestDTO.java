package auction.auction_back.dto.payment;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private int amount; //결제 금액
    private String orderId; //주문 ID
    private String orderName; //주문명
    private String customerName; //구매자 이름
    private String successUrl; //성공시 리다이렉트 URL
    private String failUrl; //실패시 리다이렉트 URL
}
