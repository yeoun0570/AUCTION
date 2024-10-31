package auction.auction_back.dto.orderWithKakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
// 카카오페이 결제 승인 응답 데이터를 담는 DTO
public class KakaoPayApproveResponseDTO {
    private String aid;                    // 요청 고유 번호
    private String tid;                    // 결제 고유 번호
    private String cid;                    // 상점 코드
    private String partnerOrderId;         // 파트너사 주문번호
    private String partnerUserId;          // 파트너사 회원 ID
    private String paymentMethodType;      // 결제 수단
    private Amount amount;                 // 결제 금액 정보
    private String itemName;               // 상품명
    private int quantity;                  // 상품 수량
    private LocalDateTime approvedAt;               // 결제 승인 시각

    // Amount 클래스는 결제 금액 정보를 포함하는 별도 클래스로 정의 가능
    public static class Amount {
        private int total;                 // 총 결제 금액
        private int taxFree;               // 비과세 금액
        private int vat;                   // 부가세
        private int point;                 // 포인트 결제 금액
        private int discount;              // 할인 금액

        // 각 필드에 대한 getter, setter 추가
    }

}

