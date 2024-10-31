package auction.auction_back.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoRequestDTO {
    //클라이언트가 결제 요청시 보내는 데이터를 담는 객체 DTO

    private String cid;                     // 가맹점 코드
    private String partnerOrderId;          // 가맹점 주문 번호
    private String partnerUserId;           // 가맹점 회원 ID
    private String itemName;                // 상품 이름
    private int quantity;                   // 상품 수량
    private int totalAmount;                // 총 금액
    private int taxFreeAmount;              // 비과세 금액
    private String approvalUrl;             // 결제 성공 시 redirect URL
    private String cancelUrl;               // 결제 취소 시 redirect URL
    private String failUrl;                 // 결제 실패 시 redirect URL
}
