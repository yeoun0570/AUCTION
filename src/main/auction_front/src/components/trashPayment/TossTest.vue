<script>
export default {
  data() {
    return {
      amount: {
        currency: "KRW",
        value: 50000,
      },
      selectedPaymentMethod: null,
      paymentMethods: [
        "CARD",
        "TRANSFER",
        "VIRTUAL_ACCOUNT",
        "MOBILE_PHONE",
        "CULTURE_GIFT_CERTIFICATE",
        "FOREIGN_EASY_PAY",
      ],
      methodNames: {
        CARD: "카드",
        TRANSFER: "계좌이체",
        VIRTUAL_ACCOUNT: "가상계좌",
        MOBILE_PHONE: "휴대폰",
        CULTURE_GIFT_CERTIFICATE: "문화상품권",
        FOREIGN_EASY_PAY: "해외간편결제",
      },
      clientKey: "test_ck_nRQoOaPz8L9JLpKXkEGN3y47BMw6",
      customerKey: this.generateRandomString(),
      tossPayments: null,
    };
  },
  mounted() {
    this.tossPayments = TossPayments(this.clientKey);
  },
  methods: {
    selectPaymentMethod(method) {
      if (this.selectedPaymentMethod) {
        this.$refs[this.selectedPaymentMethod].style.backgroundColor =
          "#ffffff";
      }

      this.selectedPaymentMethod = method;
      this.$refs[method].style.backgroundColor = "rgb(229, 239, 255)";
    },
    async requestPayment() {
      const payment = this.tossPayments.payment({
        customerKey: this.customerKey,
      });

      if (!this.selectedPaymentMethod) {
        alert("결제 방법을 선택해 주세요.");
        return;
      }

      const commonParams = {
        amount: this.amount,
        orderId: this.generateRandomString(),
        orderName: "토스 티셔츠 외 2건",
        successUrl: `${window.location.origin}/payment/success.html`,
        failUrl: `${window.location.origin}/fail.html`,
        customerEmail: "customer123@gmail.com",
        customerName: "김토스",
        customerMobilePhone: "01012341234",
      };

      switch (this.selectedPaymentMethod) {
        case "CARD":
          await payment.requestPayment({
            method: "CARD",
            ...commonParams,
            card: {
              useEscrow: false,
              flowMode: "DEFAULT",
              useCardPoint: false,
              useAppCardOnly: false,
            },
          });
          break;
        case "TRANSFER":
          await payment.requestPayment({
            method: "TRANSFER",
            ...commonParams,
            transfer: {
              cashReceipt: { type: "소득공제" },
              useEscrow: false,
            },
          });
          break;
        case "VIRTUAL_ACCOUNT":
          await payment.requestPayment({
            method: "VIRTUAL_ACCOUNT",
            ...commonParams,
            virtualAccount: {
              cashReceipt: { type: "소득공제" },
              useEscrow: false,
              validHours: 24,
            },
          });
          break;
        case "MOBILE_PHONE":
          await payment.requestPayment({
            method: "MOBILE_PHONE",
            ...commonParams,
          });
          break;
        case "CULTURE_GIFT_CERTIFICATE":
          await payment.requestPayment({
            method: "CULTURE_GIFT_CERTIFICATE",
            ...commonParams,
          });
          break;
        case "FOREIGN_EASY_PAY":
          await payment.requestPayment({
            method: "FOREIGN_EASY_PAY",
            amount: {
              value: 100,
              currency: "USD",
            },
            orderId: this.generateRandomString(),
            orderName: "토스 티셔츠 외 2건",
            successUrl: `${window.location.origin}/payment/success.html`,
            failUrl: `${window.location.origin}/fail.html`,
            customerEmail: "customer123@gmail.com",
            customerName: "김토스",
            customerMobilePhone: "01012341234",
            foreignEasyPay: {
              provider: "PAYPAL",
              country: "KR",
            },
          });
          break;
      }
    },
    async requestBillingAuth() {
      const payment = this.tossPayments.payment({
        customerKey: this.customerKey,
      });
      await payment.requestBillingAuth({
        method: "CARD",
        successUrl: `${window.location.origin}/payment/billing.html`,
        failUrl: `${window.location.origin}/fail.html`,
        customerEmail: "customer123@gmail.com",
        customerName: "김토스",
      });
    },
    generateRandomString() {
      return window.btoa(Math.random()).slice(0, 20);
    },
  },
};
</script>

<template>
  <div class="wrapper">
    <div
      class="box_section"
      style="
        padding: 40px 30px 50px 30px;
        margin-top: 30px;
        margin-bottom: 50px;
      "
    >
      <h1>일반 결제</h1>
      <!-- 결제 UI -->
      <div id="payment-method" style="display: flex">
        <button
          v-for="method in paymentMethods"
          :key="method"
          :id="method"
          class="button2"
          @click="selectPaymentMethod(method)"
        >
          {{ methodNames[method] }}
        </button>
      </div>
      <!-- 결제하기 버튼 -->
      <button class="button" style="margin-top: 30px" @click="requestPayment">
        결제하기
      </button>
    </div>
    <div
      class="box_section"
      style="
        padding: 40px 30px 50px 30px;
        margin-top: 30px;
        margin-bottom: 50px;
      "
    >
      <h1>정기 결제</h1>
      <!-- 빌링 버튼 -->
      <button
        class="button"
        style="margin-top: 30px"
        @click="requestBillingAuth"
      >
        빌링키 발급하기
      </button>
    </div>
  </div>
</template>


<style scoped>
/* 여기에 필요한 스타일을 추가하세요 */
.button2 {
  /* 기본 버튼 스타일 */
}
.button {
  /* 기본 버튼 스타일 */
}
.wrapper {
  /* 기본 래퍼 스타일 */
}
.box_section {
  /* 기본 박스 섹션 스타일 */
}
</style>
