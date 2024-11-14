<script setup>
import TossWidgetTest from "./components/trashPayment/TossWidgetTest.vue";
import PaymentWidget from "./components/trashPayment/PaymentWidget.vue";
import UserInfo from "./components/user/UserInfo.vue";
import UserSellingProduct from "./components/user/UserSellingProduct.vue";
import UserOrderProduct from "./components/user/UserOrderProduct.vue";
import UserWishlist from "./components/user/UserWishlist.vue";
import UserSetProduct from "./components/user/UserRegisterProduct.vue";
import UserWriteReview from "./components/user/UserWriteReview.vue";
import UserReviewInMyStore from "./components/user/UserReviewInMyStore.vue";
import UserRemove from "./components/user/UserRemove.vue";
import PaymentTest from "./pages/PaymentTest.vue";
import PaymentSuccess from "./pages/PaymentSuccess.vue";
</script>

<template>
  <!-- <TossWidgetTest></TossWidgetTest> -->
  <!-- <PaymentWidget></PaymentWidget> -->
  <!-- <TossTest></TossTest> -->
  <!-- <PaymentTest></PaymentTest> -->
  <!-- <UserInfo></UserInfo> -->
  <!-- <UserSellingProduct></UserSellingProduct> -->
  <!-- <UserOrderProduct></UserOrderProduct> -->
  <!-- <UserWishlist></UserWishlist> -->
  <!-- <UserSetProduct></UserSetProduct> -->
  <!-- <UserWriteReview></UserWriteReview> -->
  <!-- <UserReviewInMyStore></UserReviewInMyStore> -->
  <!-- <UserRemove></UserRemove> -->
  <RouterView></RouterView>
</template>
