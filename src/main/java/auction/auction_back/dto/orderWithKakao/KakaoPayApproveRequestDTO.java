package auction.auction_back.dto.orderWithKakao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 카카오페이 결제 승인 요청에 필요한 데이터를 담는 DTO
public class KakaoPayApproveRequestDTO {
    private String cid;                    // 상점 코드
    private String tid;                    // 결제 고유 번호
    private String partnerOrderId;         // 파트너사 주문번호
    private String partnerUserId;          // 파트너사 회원 ID
    private String pgToken;                // 결제 승인 요청에 필요한 토큰
}
