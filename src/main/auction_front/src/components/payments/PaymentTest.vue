<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const amount = ref(15000);
const orderId = ref("");
const orderName = ref("테스트 상품");
const customerName = ref("test customer");
const paymentResult = ref(null);
const paymentDetails = ref(null);
const tossPayments = ref(null);

// URL에서 결제 정보 가져오기
onMounted(() => {
  // @ts-ignore
  tossPayments.value = window.TossPayments(
    "test_ck_nRQoOaPz8L9JLpKXkEGN3y47BMw6"
  );

  // 성공 경로일 때만 결제 정보 설정
  if (window.location.pathname === "/success") {
    const params = new URLSearchParams(window.location.search);
    const paymentInfo = {
      paymentKey: params.get("paymentKey"),
      orderId: params.get("orderId"),
      amount: params.get("amount"),
    };
    paymentResult.value = paymentInfo; // ref 값 설정
  }
});

// 결제 시작
const startPayment = async () => {
  try {
    orderId.value = `ORDER_${new Date().getTime()}`;

    // v2 버전의 결제 요청 구성
    await tossPayments.value.requestPayment("카드", {
      amount: amount.value,
      orderId: orderId.value,
      orderName: orderName.value,
      customerName: customerName.value,
      successUrl: `${window.location.origin}/success`,
      failUrl: `${window.location.origin}/fail`,
    });
  } catch (error) {
    console.error("결제 시작 실패:", error);
    alert("결제가 취소되었습니다.");
  }
};

// 토스페이먼츠 API에서 직접 결제 상세 정보 조회
const getPaymentDetails = async () => {
      try {
        // 프록시로 설정한 백엔드 엔드포인트로 요청
        console.log(paymentResult.value.paymentKey);
        const response = await axios.get(`http://localhost:8080/api/payments/${paymentResult.value.paymentKey}`);
        paymentDetails.value = response.data;
      } catch (error) {
        console.error("Error fetching data:", error); // 에러 로그 확인
      }
    };
</script>

<template>
  <div>
    <h1>토스페이먼츠 테스트</h1>

    <!-- 결제 전 입력 폼 -->
    <div v-if="!paymentResult" class="payment-info">
      <div>
        <label>상품명:</label>
        <input type="text" v-model="orderName" />
      </div>
      <div>
        <label>금액:</label>
        <input v-model="amount" type="text" />
      </div>
      <div>
        <label>구매자명:</label>
        <input v-model="customerName" type="text" />
      </div>
      <button @click="startPayment">결제 시작</button>
    </div>

    <!-- 결제 완료 후 정보 -->
    <div v-if="paymentResult" class="payment-result">
      <h2>결제 완료</h2>
      <p>주문번호: {{ paymentResult.orderId }}</p>
      <p>결제금액: {{ paymentResult.amount }}원</p>
      <button @click="getPaymentDetails" class="detail-btn">
        상세 내역 보기
      </button>
    </div>

    <!-- 결제 상세 정보 모달 -->
    <div v-if="paymentDetails" class="payment-details-modal">
      <h3>결제 상세 정보</h3>
      <pre>{{ JSON.stringify(paymentDetails, null, 2) }}</pre>
      <button @click="paymentDetails = null">닫기</button>
    </div>
  </div>
</template>

<style scoped>
@import '../assets/paymentTest.css'
</style>