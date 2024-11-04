package auction.auction_back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String privateKey;        //결제키
    private String orderId;           //주문ID
    private String orderName;         //주문명
    private int amount;               //결제금액
    private String status;            //결제상태
    private LocalDateTime createdAt;  //결제 생성 시간
    private LocalDateTime approvedAt; //결제 승인 시간
    private String method;            //결제 수단
    private String customerName;      //구매자 이름
}
