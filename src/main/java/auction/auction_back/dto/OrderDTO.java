package auction.auction_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;                   // 주문 ID, 기본 키 역할
    private Long itemId;                 // 주문된 상품의 ID
    private String userId;                  // 주문한 회원의 ID
    private int quantity;                   // 주문 수량
    private int totalAmount;                // 총 결제 금액
    private int charge;                     // 부과된 수수료
    private int taxFreeAmount;              // 비과세 금액
    private String status;                  // 주문 상태 (예: 결제 대기, 완료 등)
    private LocalDateTime createdAt;        // 주문 생성일자
    private String cancelReason;            // 주문 취소 이유
    private String tid;                     // 카카오페이 결제 고유 ID
    private String paymentMethodType;       // 결제 수단 (카드, 계좌이체 등)
    private String itemName;                // 상품 이름
    private LocalDateTime approvedAt;       // 결제 승인 일시
}
