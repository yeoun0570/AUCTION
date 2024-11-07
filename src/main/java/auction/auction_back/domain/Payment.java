package auction.auction_back.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey; //토스페이먼츠에서 제공하는 결제 고유키
    private String orderId;
    private String orderName;
    private int totalAmount;

    @Enumerated(EnumType.STRING) // 결제 상태 enum으로 저장
    @Column(length = 20)
    private PaymentStatus status; // 현재 결제 상태

    private LocalDateTime createdAt;        // 결제 생성 시점
    private LocalDateTime expiryDateTime;   // 결제 만료 예정 시점 (생성 시점 + 30분)
    private LocalDateTime paidAt;           //결제 완료 시점 -> 구매자가 돈이 빠져나가는 시점
    private LocalDateTime escrowReleasedAt; //구매 확정 시점 -> 판매자에게 돈이 들어가는 시점
    private LocalDateTime escrowDeadline;   // 에스크로 자동 확정 기한

    private String buyerId;
    private String sellerId;
}
