package auction.auction_back.service;

import auction.auction_back.domain.Order;
import auction.auction_back.dto.orderWithKakao.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ModelMapper modelMapper;

    //카카오페이 API에서 결제 준비와 승인 요청을 처리하기 위해 제공하는 고유한 URL
    //즉, 카카오페이 API 사용시에 정확한 엔드포인트를 사용하게끔 해준다.
    private static final String KAKAO_PAY_API_URL = "https://kapi.kakao.com/v1/payment/ready";
    private static final String KAKAO_PAY_APPROVE_URL = "https://kapi.kakao.com/v1/payment/approve";

    private String CID = "603BD116EDAB6DD6AD35";

    private String secretKey = "PRD3D8053D3040E7B8E56C3A5F72EF0D4F61A2A4";


    //카카오페이 결제를 시작하는 API 요청 담당
    public KakaoPayReadyResponseDTO kakaoPayReady(OrderDTO orderDTO) {

        //받은 OrderDTO를 Order로 변환
        Order order = modelMapper.map(orderDTO, Order.class);

        //Order 객체 기반으로 생성된 KakaoRequestDTO를 통해 필요한 요청 데이터 구성
        KakaoPayReadyRequestDTO requestDTO = new KakaoPayReadyRequestDTO();
        requestDTO.setCid(CID);
        requestDTO.setPartnerOrderId(order.getOrderId().toString());
        requestDTO.setPartnerUserId(order.getUserId());
        requestDTO.setItemName(order.getItemName());
        requestDTO.setQuantity(order.getQuantity());
        requestDTO.setTotalAmount(order.getTotalAmount());
        requestDTO.setTaxFreeAmount(order.getTaxFreeAmount());
        requestDTO.setApprovalUrl("http://localhost:5173/order/success");
        requestDTO.setCancelUrl("http://localhost:5173/order/cancle");
        requestDTO.setFailUrl("http://localhost:5173/order/fail");

        //HTTP 요청 헤더에 필요한 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        //카카오페이 인증키를 헤더에 추가하여 API 요청시 권한 부여
        httpHeaders.add("Authorization", "KakaoAK" + secretKey);
        //요청 데이터 형식 지정
        httpHeaders.add("Content-Type", "application/x-www-from-urlencoded;charset=UTF-8");

        //HttpEntity에 본문과 헤더를 포함해서 POST 요청에 사용할 준비
        HttpEntity<KakaoPayReadyRequestDTO> requestEntity = new HttpEntity<>(requestDTO, httpHeaders);
        //HTTP 요청에 보낼 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        //카카오페이 고유 결제준비 URL로 POST 요청을 보내고, 그 결과를 KakaoResponseDTO 객체로 받는다.
        KakaoPayReadyResponseDTO responseDTO = restTemplate.postForObject(KAKAO_PAY_API_URL, requestEntity, KakaoPayReadyResponseDTO.class);

        return responseDTO;
    }

    //결제가 성공적으로 이루어졌는지 확인하고, 승인하는 API 요청 담당
    public KakaoPayApproveResponseDTO kakaoPayApprove(KakaoPayApproveRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + secretKey);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<KakaoPayApproveRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(KAKAO_PAY_APPROVE_URL, requestEntity, KakaoPayApproveResponseDTO.class);
    }
}
