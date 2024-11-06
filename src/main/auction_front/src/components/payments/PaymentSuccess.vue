<!-- PaymentSuccess.vue -->
<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

onMounted(() => {
  // URL 파라미터에서 결제 정보 가져오기
  const paymentInfo = {
    paymentKey: route.query.paymentKey,
    orderId: route.query.orderId,
    amount: route.query.amount
  };
  
  // localStorage에 결제 정보 저장
  localStorage.setItem('lastPayment', JSON.stringify(paymentInfo));
  
  // 메인 페이지로 리다이렉트
  router.push('/');
});
</script>

<template>
  <div>결제 완료 처리 중...</div>
</template>