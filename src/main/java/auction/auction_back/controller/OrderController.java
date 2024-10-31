package auction.auction_back.controller;

import auction.auction_back.dto.orderWithKakao.KakaoPayApproveRequestDTO;
import auction.auction_back.dto.orderWithKakao.KakaoPayApproveResponseDTO;
import auction.auction_back.dto.orderWithKakao.KakaoPayReadyResponseDTO;
import auction.auction_back.dto.orderWithKakao.OrderDTO;
import auction.auction_back.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/order")
public class OrderController {
    //결제 요청과 승인 요청에 대한 REST API 엔드포인트 지정

    private final OrderService orderService;

    @PostMapping("/ready")
    public ResponseEntity<KakaoPayReadyResponseDTO> readyToKakaoPay(@RequestBody OrderDTO orderDTO) {
        KakaoPayReadyResponseDTO responseDTO = orderService.kakaoPayReady(orderDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/approve")
    public ResponseEntity<KakaoPayApproveResponseDTO> approveToKakaoPay(@RequestBody KakaoPayApproveRequestDTO requestDTO) {
        KakaoPayApproveResponseDTO responseDTO = orderService.kakaoPayApprove(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
