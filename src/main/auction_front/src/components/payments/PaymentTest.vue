<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from 'vue-router';

const router = useRouter();
const amount = ref(15000);
const orderId = ref("");
const orderName = ref("테스트 상품");
const customerName = ref("test customer");
const tossPayments = ref(null);
const loading = ref(false);

// 토스페이먼츠 초기화
onMounted(() => {
  // @ts-ignore
  tossPayments.value = window.TossPayments(
    "test_ck_nRQoOaPz8L9JLpKXkEGN3y47BMw6"
  );
});

// 결제 시작
const startPayment = async () => {
  try {
    loading.value = true;
    orderId.value = `ORDER_${new Date().getTime()}`;

    //현재 도메인 확인 로그
    console.log('Current origin: ', window.location.origin);

    // successUrl
    const successUrl = new URL(router.resolve({ name: 'PaymentSuccess' }).href, window.location.origin).href;

    console.log('Success URL: ', successUrl); //디버깅용

    // 토스 페이먼츠 결제창 호출
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
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div>
    <h1>토스페이먼츠 테스트</h1>

    <!-- 로딩 표시 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">처리 중...</div>
    </div>

    <!-- 결제 입력 폼 -->
    <div class="payment-info">
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
      <button @click="startPayment" :disabled="loading">결제 시작</button>
    </div>
  </div>
</template>

<style scoped>
@import "../../assets/paymentTest.css";
</style>
