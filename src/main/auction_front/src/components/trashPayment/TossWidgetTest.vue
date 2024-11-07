<script setup>
import { ref, onMounted } from "vue";
import { loadPaymentWidget } from "@tosspayments/payment-widget-sdk";

const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const customerKey = "CUSTOMER-" + Date.now();
const paymentWidgetLoaded = ref(false);
const isLoading = ref(true);
let paymentWidget = null;

const initPaymentWidget = async () => {
  try {
    isLoading.value = true;
    
    // 결제위젯 SDK 로드 시 옵션 추가
    paymentWidget = await loadPaymentWidget(clientKey, customerKey, {
      type: 'PAYMENT',
      uiMode: 'PC',
      container: {
        width: '100%',
        height: 'auto'
      }
    });

    // 결제위젯 렌더링 시 옵션 추가
    await paymentWidget.renderPaymentMethods(
      "#payment-widget",
      {
        value: 50000,
        currency: "KRW",
        country: "KR",
      },
      {
        // 결제 UI 스타일 지정
        ui: {
          mode: 'HORIZONTAL',
          backgroundColor: '#ffffff',
          padding: '20px',
          borderRadius: '8px',
        },
        // 결제 수단 배치
        layout: {
          columns: 3,
          rows: 'auto',
          spacing: '16px',
        }
      }
    );

    await paymentWidget.renderAgreement("#agreement", {
      ui: {
        theme: 'LIGHT',
        spacing: '16px',
      }
    });

    paymentWidgetLoaded.value = true;
    isLoading.value = false;
    
  } catch (error) {
    console.error("Payment widget initialization failed:", error);
    alert("결제 위젯 로드 중 오류가 발생했습니다.");
    isLoading.value = false;
  }
};

const handlePayment = async () => {
  try {
    if (!paymentWidget) {
      alert("결제 위젯이 준비되지 않았습니다. 페이지를 새로고침해주세요.");
      return;
    }

    await paymentWidget.requestPayment({
      orderId: "tosspayments-" + Date.now(),
      orderName: "테스트 결제",
      customerName: "김토스",
      customerEmail: "customer@email.com",
      successUrl: `${window.location.origin}/success`,
      failUrl: `${window.location.origin}/fail`,
      amount: 50000
    });
    
  } catch (error) {
    if (error.code === "PAY_PROCESS_CANCELED") {
      alert("결제가 취소되었습니다.");
    } else if (error.code === "PAYMENT_METHOD_NOT_SELECTED") {
      alert("결제 수단을 선택해주세요.");
    } else {
      console.error("결제 오류:", error);
      alert("결제 처리 중 오류가 발생했습니다.");
    }
  }
};

onMounted(() => {
  initPaymentWidget();
});
</script>

<template>
  <div class="payment-page">
    <div class="payment-header">
      <h1 class="payment-title">결제하기</h1>
    </div>
    
    <div class="payment-content">
      <div class="payment-amount">
        <h2>결제 금액</h2>
        <p class="amount">50,000원</p>
      </div>

      <div class="payment-widget-section">
        <div v-if="isLoading" class="loading-message">
          결제 위젯을 불러오는 중입니다...
        </div>
        <div id="payment-widget"></div>
        <div id="agreement"></div>
      </div>

      <button
        @click="handlePayment"
        :disabled="isLoading || !paymentWidgetLoaded"
        class="payment-button"
      >
        {{ isLoading ? '로딩중...' : '결제하기' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
@import '../../assets/trashPaymentWidget.css';
</style>