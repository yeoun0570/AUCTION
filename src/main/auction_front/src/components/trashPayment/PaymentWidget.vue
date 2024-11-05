<script setup>
import { ref, onMounted } from "vue";
import { loadTossPayments, ANONYMOUS } from "@tosspayments/tosspayments-sdk";

const widgets = ref(null);
const amount = {
  currency: "KRW",
  value: 50000,
};

// 비동기 초기화 함수
const initializePayment = async () => {
  const tossPayments = await loadTossPayments(
    "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"
  );
  widgets.value = tossPayments.widgets({ customerKey: ANONYMOUS });

  await widgets.value.setAmount(amount);

  await Promise.all([
    widgets.value.renderPaymentMethods({
      selector: "#payment-method",
    }),
    widgets.value.renderAgreement({
      selector: "#agreement",
    }),
  ]);
};

const handlePayment = async () => {
  try {
    if (!widgets.value) return;

    await widgets.value.requestPayment({
      orderId: generateRandomString(),
      orderName: "테스트 상품",
      successUrl: `${window.location.origin}/success`,
      failUrl: `${window.location.origin}/fail`,
    });
  } catch (error) {
    console.log(error);
  }
};

const generateRandomString = () => {
  return window.btoa(Math.random()).slice(0, 20);
};

// onMounted에서 초기화 실행
onMounted(() => {
  initializePayment();
});
</script>

<template>
  <div class="wrapper w-100">
    <div class="max-w-540 w-100">
      <div id="payment-method" class="w-100"></div>
      <div id="agreement" class="w-100"></div>
      <div class="btn-wrapper w-100">
        <button
          id="payment-request-button"
          class="btn primary w-100"
          @click="handlePayment"
        >
          결제하기
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  overflow: auto;
}

.w-100 {
  width: 100%;
}

.max-w-540 {
  max-width: 540px;
}

.btn-wrapper {
  padding: 0 24px;
}

.btn {
  padding: 11px 22px;
  border: none;
  border-radius: 8px;
  background-color: #f2f4f6;
  color: #4e5968;
  font-weight: 600;
  font-size: 17px;
  cursor: pointer;
}

.btn.primary {
  background-color: #3282f6;
  color: #f9fcff;
}
</style>
