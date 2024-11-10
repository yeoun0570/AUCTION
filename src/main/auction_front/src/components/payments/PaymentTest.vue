<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from 'vue-router';

const route = useRoute();
const orderData = ref(null);
const loading = ref(false);
const error = ref(null);
const tossPayments = ref(null);
const orderId = ref(null);
const productId = 1;


//주문페이지 데이터 로드
const loadOrderPage = async () => {
  try {
    loading.value = true;
    //const productId = 1; //추후에 실제 받은 값으로 변경
    const resposne = await axios.get(`http://localhost:8080/api/payments/page/${productId}`)
    orderData.value = resposne.data;
  } catch(error) {
    console.log('주문 페이지 로딩 실패: ', error);
  } finally {
    loading.value = false;
  }
}

// 토스페이먼츠 초기화
onMounted(() => {
  // @ts-ignore
  tossPayments.value = window.TossPayments(
    "test_ck_nRQoOaPz8L9JLpKXkEGN3y47BMw6"
  );

  loadOrderPage();
});

// 결제 시작
const startPayment = async () => {
  try {
    loading.value = true;
    orderId.value = `ORDER_${new Date().getTime()}`;

    //현재 도메인 확인 로그
    console.log('Current origin: ', window.location.origin);

    // 토스 페이먼츠 결제창 호출
    await tossPayments.value.requestPayment("카드", {
      amount: orderData.value.totalAmount,
      orderId: orderId.value,
      orderName: orderData.value.productName,
      customerName: orderData.value.nickname,
      successUrl: `${window.location.origin}/success?productId=${productId}`,
      failUrl: `${window.location.origin}/fail`,
    });
  } catch (error) {
    console.error("결제 시작 실패:", error);
    alert("결제가 취소되었습니다.");
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="order-page">
    <div v-if="loading" class="loading">로딩 중...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="orderData" class="order-content">
      <!-- 상품 정보 섹션 -->
      <div class="product-section">
        <img :src="orderData.thumbnailImage" alt="상품 이미지" class="thumbnail">
        <div class="product-info">
          <h2>{{ orderData.productName }}</h2>
          <p class="price">상품가격: {{ orderData.price.toLocaleString() }}원</p>
          <p class="charge">수수료 (3.5%): {{ orderData.chargeAmount.toLocaleString() }}원</p>
          <p class="total">총 결제금액: {{ orderData.totalAmount.toLocaleString() }}원</p>
        </div>
      </div>

      <!-- 배송 정보 섹션 -->
      <div class="delivery-section">
        <h3>배송 정보</h3>
        <div class="delivery-info">
          <p>받는 사람: {{ orderData.nickname }}</p>
          <p>연락처: {{ orderData.mobile }}</p>
          <p>우편번호: {{ orderData.addrZipcode }}</p>
          <p>주소: {{ orderData.addrMain }}</p>
          <p>상세주소: {{ orderData.addrDetail }}</p>
        </div>
      </div>

      <!-- 결제 버튼 -->
      <button 
        @click="startPayment" 
        :disabled="loading" 
        class="payment-button"
      >
        결제하기
      </button>
    </div>
  </div>
</template>

<style scoped>
@import "../../assets/paymentTest.css";
</style>
