package auction.auction_back.service;

import auction.auction_back.config.RestTemplateConfig;
import auction.auction_back.domain.Payment;
import auction.auction_back.domain.PaymentStatus;
import auction.auction_back.dto.payment.PaymentResponseDTO;
import auction.auction_back.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Base64;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestTemplateConfig restTemplate;
    private final ModelMapper modelMapper;
    private String secretKey = "test_sk_DnyRpQWGrN5Xzapz6XA0VKwv1M9E";

    //결제 처리 메서드 -> 결제 정보 저장하고 에스크로로 홀딩 처리
    @Transactional
    public PaymentResponseDTO processPayment(String paymentKey, Long buyerId, Long sellerId) {
        log.info("결제 처리 시작 - paymentKey: {}, buyerId: {}, sellerId: {}",
                paymentKey, buyerId, sellerId);
        // 토스 페이먼츠 API에서 결제 정보 조회
        PaymentResponseDTO tossResponse = getPaymentDetails(paymentKey);

        log.info("토스페이먼츠 응답 - status: {}, orderId: {}, amount: {}",
                tossResponse.getStatus(), tossResponse.getOrderId(), tossResponse.getTotalAmount());

        // 새로운 결제 엔티티 생성
        Payment payment = new Payment();
        // 해당 엔티티 기본 설정
        payment.setPaymentKey(paymentKey);
        payment.setOrderId(tossResponse.getOrderId());
        payment.setOrderName(tossResponse.getOrderName());
        payment.setTotalAmount(tossResponse.getTotalAmount());
        payment.setBuyerId(buyerId);
        payment.setSellerId(sellerId);
        payment.setCreatedAt(LocalDateTime.now());

        //결제 완료 후에 에스크로 상태로 전환
        if ("DONE".equals(tossResponse.getStatus())) {
            payment.setStatus(PaymentStatus.ESCROW_HOLDING); // 결제 상태 에스크로로 변경
            payment.setPaidAt(LocalDateTime.now()); //결제 완료 시점 기록
            log.info("에스크로 전환 완료 - orderId: {}, amount: {}",
                    payment.getOrderId(), payment.getTotalAmount());
        } else {
            //그 외의 경우 토스 페이먼츠에서 제공하는 상태값 그대로 사용
            payment.setStatus(PaymentStatus.valueOf(tossResponse.getStatus()));
            log.warn("결제 미완료 상태 - status: {}, orderId: {}",
                    tossResponse.getStatus(), payment.getOrderId());
        }

        //결제 정보를 DB에 저장하고 DTO로 변환해서 반환
        Payment savedPayment = paymentRepository.save(payment);

        log.info("결제 정보 저장 완료 - id: {}, status: {}",
                savedPayment.getId(), savedPayment.getStatus());

        return modelMapper.map(savedPayment, PaymentResponseDTO.class);
    }

    //구매 확정 메서드
    @Transactional
    public PaymentResponseDTO confirmPurchase(String orderId) {
        //주문번호로 결제 정보 조회(주문번호가 없으면 예외 상황 발생)
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("결제 기록이 없습니다."));

        //현재 결제 상태가 에스크로 상태인지 확인
        if (payment.getStatus() != PaymentStatus.ESCROW_HOLDING) {
            throw new IllegalArgumentException("결제가 에스크로 상태가 아닙니다.");
        }

        //토스 페이먼츠 API를 호출하여 판매자에게 돈을 이체
        releaseEscrowPayment(payment.getPaymentKey());

        //상태 업데이트
        payment.setStatus(PaymentStatus.ESCROW_RELEASED); //구매 완료했으니 구매확정 상태로 변경
        payment.setEscrowReleasedAt(LocalDateTime.now()); //구매 확정 시점 기록

        //변경된 사항을 DTO로 변환하여 반환
        return modelMapper.map(payment, PaymentResponseDTO.class);
    }


    public PaymentResponseDTO getPaymentDetails(String paymentKey) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey;

        HttpHeaders headers = new HttpHeaders();
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);


        //외부 API 호출
        try {
            ResponseEntity<PaymentResponseDTO> response = restTemplate.restTemplate().exchange(
                    url, HttpMethod.GET, entity, PaymentResponseDTO.class
            );
            log.info("API Response: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch payment details, please try again.");
        } catch (HttpServerErrorException e) {
            log.error("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Server Error, please try again later.");
        } catch (Exception e) {
            log.error("Unexpected error occurred: ", e);
            throw new RuntimeException("Unexpected error, please try again.");
        }
    }

    //에스크로 해제하여 판매자에게 돈을 이체하는 메서드
    private void releaseEscrowPayment(String paymentKey) {
        //해당 토스 페이먼츠 엔드포인트를 호출하면 에스크로 해제하고 판매 정산
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/release";

        HttpHeaders headers = new HttpHeaders();
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.restTemplate().exchange(
                url, HttpMethod.GET, entity, String.class
        );
    }
}
