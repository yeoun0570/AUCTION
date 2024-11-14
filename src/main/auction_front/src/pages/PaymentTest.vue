<script setup>
import { provide, ref, onMounted } from "vue";
import axios from "axios";
import PaymentProductSection from "@/components/payments/PaymentProductSection.vue";
import DeliverySection from "@/components/payments/DeliverySection.vue";
import PaymentButton from "@/components/payments/PaymentButton.vue";
import PaymentAmountSection from "@/components/payments/PaymentAmountSection.vue";

const orderData = ref(null);
const error = ref(null);
const tossPayments = ref(null);
const orderId = ref(null);
const productId = 2;

provide("orderData", orderData);

const loadOrderPage = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/payments/page/${productId}`
    );
    orderData.value = response.data;
  } catch (error) {
    console.log("주문 페이지 로딩 실패: ", error);
  }
};

onMounted(() => {
  // @ts-ignore
  tossPayments.value = window.TossPayments(
    "test_ck_nRQoOaPz8L9JLpKXkEGN3y47BMw6"
  );

  loadOrderPage();
});

const startPayment = async () => {
  try {
    orderId.value = `ORDER_${new Date().getTime()}`;

    const paymentInfo = {
      productId: productId,
      buyerId: "buyer1",
      amount: orderData.value.totalAmount,
      productName: orderData.value.productName,
    };

    console.log("Current origin: ", window.location.origin);

    await tossPayments.value.requestPayment("카드", {
      amount: orderData.value.totalAmount,
      orderId: orderId.value,
      orderName: orderData.value.productName,
      customerName: orderData.value.nickname,
      successUrl: `${window.location.origin}/success`,
      failUrl: `${window.location.origin}/fail`,
    });
  } catch (error) {
    console.error("결제 시작 실패:", error);
    alert("결제가 취소되었습니다.");
  } 
};
</script>

<template>
  <div class="order-page">
    <h1>결제하기</h1>
    <br>
    <hr color="black">
    <br>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-else-if="orderData" class="order-content">
      <PaymentProductSection :productData="orderData" />
      <br><hr><br>
      <DeliverySection :deliveryData="orderData" />
      <br><hr><br>
      <PaymentAmountSection :product-data="orderData"></PaymentAmountSection>
      <br><hr><br>
      <PaymentButton :loading="loading" @startPayment="startPayment" />
    </div>
  </div>
</template>
