package auction.auction_back.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//결제 후 응답받는 데이터 목록 중 우리 서버에 저장해서 사용할 데이터 목록
public class PaymentResponseDTO {
    private String orderId;
    private String orderName;
    private int totalAmount;
    private String status;
    private String paymentKey;
}
