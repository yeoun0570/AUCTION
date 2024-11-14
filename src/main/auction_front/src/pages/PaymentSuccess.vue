<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const paymentResult = ref(null);
const paymentDetails = ref(null);
const loading = ref(false);
const error = ref(null);

onMounted(async () => {
  try {
    const params = new URLSearchParams(window.location.search);
    const paymentInfo = {
      paymentKey: params.get("paymentKey"),
      orderId: params.get("orderId"),
      amount: params.get("amount"),
      productId: 4,
    };
    console.log("결제 정보: ", paymentInfo);
    paymentResult.value = paymentInfo;

    //1. 결제 상태 확인
    const details = await getPaymentDetails();

    //2. DONE 상태일 때만 에스크로 처리 진행
    if (details?.status === "DONE") {
      await processSuccessPayment(paymentInfo);
    } else {
      await confirmPayment(paymentInfo);
    }
  } catch (error) {
    console.error("결제 승인 중 오류:", error);
    error.value = "결제 승인 중 오류가 발생했습니다.";
  }
});

// 결제 승인 요청 (백엔드에서 처리)
const confirmPayment = async (paymentInfo) => {
  try {
    console.log("결제 승인 시작");
    const response = await axios.post(
      "http://localhost:8080/api/payments/confirm", // 백엔드의 결제 승인 엔드포인트
      {
        paymentKey: paymentInfo.paymentKey,
        orderId: paymentInfo.orderId,
        amount: parseInt(paymentInfo.amount),
        buyerId: "seller", // 실제로는 로그인한 사용자 ID
        productId: paymentInfo.productId, // URL 쿼리 파라미터에서 가져옴
      }
    );

    console.log("결제 승인 완료:", response.data);
    paymentDetails.value = response.data;

    if (response.data.status === "DONE") {
      await processSuccessPayment(paymentInfo);
    }
  } catch (error) {
    console.error("결제 승인 실패:", error);
    error.value = "결제 승인 중 오류가 발생했습니다.";
  }
};

// 백엔드에 결제 정보 저장
const processSuccessPayment = async (paymentInfo) => {
  try {
    console.log("에스크로 전환 시작");
    const response = await axios.post(
      "http://localhost:8080/api/payments/success",
      {
        paymentKey: paymentInfo.paymentKey,
        orderId: paymentInfo.orderId,
        amount: parseInt(paymentInfo.amount),
        buyerId: "seller", // 실제로는 로그인한 사용자 ID
        productId: paymentInfo.productId, // URL 쿼리 파라미터에서 가져옴
      }
    );

    console.log("에스크로로 전환 완료:", response.data);
    paymentDetails.value = response.data;
  } catch (error) {
    console.error("에스크로 전환 중 오류:", error);
    throw error;
  } finally {
    loading.value = false;
  }
};

// 결제 상세 정보 조회
const getPaymentDetails = async () => {
  if (!paymentResult.value?.paymentKey) return null;

  try {
    const response = await axios.get(
      `http://localhost:8080/api/payments/${paymentResult.value.orderId}/detail`
    );
    console.log("상세 정보 조회: ", response.data);
    paymentDetails.value = response.data;
  } catch (error) {
    console.error("상세 정보 조회 실패:", error);
    error.value = "상세 정보 조회 중 오류가 발생했습니다.";
    return null;
  }
};
</script>

<template>
  <div class="payment-success">
    <h2>결제 완료</h2>

    <div v-if="loading" class="loading">처리 중...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="paymentResult" class="payment-result">
      <p>주문번호: {{ paymentResult.orderId }}</p>
      <p>결제금액: {{ paymentResult.amount }}원</p>

      <div class="button-group">
        <button
          @click="getPaymentDetails"
          :disabled="loading"
          class="detail-btn"
        >
          상세 내역 보기
        </button>
        <!-- paymentDetails가 있고 상태가 ESCROW_HOLDING일 때 구매확정 버튼 표시 -->
      </div>
    </div>

    <div v-if="paymentDetails" class="payment-details">
      <h3>결제 상세 정보</h3>
      <pre>{{ JSON.stringify(paymentDetails, null, 2) }}</pre>
    </div>
  </div>
</template>

<style scoped>
@import "../assets/paymentSuccess.css";
</style>
