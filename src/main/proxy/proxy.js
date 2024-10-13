const express = require("express");
const cors = require("cors");
const axios = require("axios");

//express 선언
const app = express();

//cors 설정
//app.use : express에서 항상 실행하는 미들웨어(요청/응답 중간처리)
app.use(cors ({
    origin: "http://localhost:3000", //허용할 프론트서버 origin
    methods: ["GET", "POST", "PUT", "DELETE", "OPTIONS"], // 허용할 HTTP 메서드
    maxAge: 3600, //캐시 유효시간(1시간)
    credentials: true, //인증 데이터 응답 가능하도록 설정
}));

app.use(express.json()); // JSON 파싱 미들웨어 추가

//모든 요청을 프록시하는 부분
// "/api"로 들어오는 모든 요청 미들웨어 실행 => "http://localhost:4000/api"로 보내면 해당 함수 실행
app.use("/", async (req,res) => { 

    console.log(`Received request for: ${req.method} ${req.url}`);

    try {
        //1. axios로 백엔드 서버(8080포트)에 데이터 요청하니까 기다려(await)
        const apiResponse = await axios({
            method : req.method, 
            url : `http://localhost:8080${req.url}`,
            data : req.body,
            headers : {...req.body},
        });

        //2. 서버에서 상태 및 데이터 반환
        res.status(apiResponse.status).json(apiResponse.data);
    } catch(err) {
        res.status(err.response ? err.response.status : 500).json({
            errror : "API 요청 실패"
        })
    }
});


//프록시 서버시작
app.listen(4000, () => {
    console.log("프록시 서버(4000포트) 실행 중")
})