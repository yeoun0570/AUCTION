package auction.auction_back.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ModelMapper modelMapper;

    private static final String KAKAO_PAY_API_URL = "https://kapi.kakao.com/v1/payment/ready";
    private static final String KAKAO_PAY_APPROVE_URL = "https://kapi.kakao.com/v1/payment/approve";




    //카카오페이 결제를 시작하는 API 요청 담당

    //결제가 성공적으로 이루어졌는지 확인하고, 승인하는 API 요청 담당
}
