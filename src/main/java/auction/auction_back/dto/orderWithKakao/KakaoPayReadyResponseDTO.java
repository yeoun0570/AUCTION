package auction.auction_back.dto.orderWithKakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayReadyResponseDTO {
    //카카오페이 결제 요청 및 승인 응답을 받을 때 사용하는 객체 DTO

    private String tid;                     // 결제 고유 번호
    private String nextRedirectPcUrl;       // PC 웹으로 연결할 URL
    private LocalDateTime createdAt;        // 결제 생성 시간
}
