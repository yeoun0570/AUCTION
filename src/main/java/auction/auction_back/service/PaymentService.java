package auction.auction_back.service;

import auction.auction_back.dto.payment.PaymentRequestDTO;
import auction.auction_back.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
    private final String TOSS_SECRET_KEY = "test_sk_DnyRpQWGrN5Xzapz6XA0VKwv1M9E";
    private final String TOSS_API_URL = "https://api.tosspayments.com/v1";

    //결제 승인
    public PaymentResponseDTO confirmPayment(String paymentKey, String orderId, int amount) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            //
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));
            // HTTP 헤더의 Content-Type 을 application/json 으로 설정한다
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 바디 설정
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", orderId);
            params.put("amount", amount);

            //
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(
                    TOSS_API_URL + "/payment/" + paymentKey + "/confirm",
                    requestEntity,
                    PaymentResponseDTO.class
            );

            log.info("결제 승인 성공: {} ", response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            log.info("결제 승인 실패: {} ", e.getMessage());
            throw new RestClientException("결제 승인 실패:", e);
        }
    }

    //결제 조회
    public PaymentResponseDTO getPayment(String paymentKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));

            //?
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PaymentResponseDTO> response = restTemplate.exchange(
                    TOSS_API_URL + "/payment/" + paymentKey,
                    HttpMethod.GET,
                    requestEntity,
                    PaymentResponseDTO.class
            );

            log.info("결제 조회 성공: {} ", response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            log.error("결제 조회 실패: {} ", e.getMessage());
            throw new RuntimeException("결제 조회 실패: ", e);
        }
    }

    //결제 취소
    public PaymentResponseDTO cancelPayment(String paymentKey, String cancelReason) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> params = new HashMap<>();
            params.put("cancelReason", cancelReason);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(
                    TOSS_API_URL + "/payment/" + paymentKey + "/cancel",
                    requestEntity,
                    PaymentResponseDTO.class
            );

            log.info("결제 취소 성공: {}", response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            log.info("결체 취소 실패: {}", e.getMessage());
            throw new RestClientException("결제 취소 실패: ", e);
        }
    }
}
