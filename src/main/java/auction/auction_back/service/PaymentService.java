package auction.auction_back.service;

import auction.auction_back.config.RestTemplateConfig;
import auction.auction_back.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Base64;


@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentService {
    private final RestTemplateConfig restTemplate;

    private String secretKey = "test_sk_DnyRpQWGrN5Xzapz6XA0VKwv1M9E";

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
}
