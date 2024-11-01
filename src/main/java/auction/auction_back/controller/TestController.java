package auction.auction_back.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
@Log4j2
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String getTest() {
        log.info("제발 보내져라..........................");
        return "백엔드에서 데이터 보내기 테스트";
    }
}
