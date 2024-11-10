<script setup>
import TossWidgetTest from "./components/trashPayment/TossWidgetTest.vue";
import PaymentTest from "@/components/payments/PaymentTest.vue";
import PaymentWidget from "./components/trashPayment/PaymentWidget.vue";
import UserInfo from "./components/user/UserInfo.vue";
import UserSellingProduct from "./components/user/UserSellingProduct.vue";
import UserOrderProduct from "./components/user/UserOrderProduct.vue";
import UserWishlist from "./components/user/UserWishlist.vue";
</script>

<template>
  <!-- <TossWidgetTest></TossWidgetTest> -->
  <!-- <PaymentWidget></PaymentWidget> -->
  <!-- <TossTest></TossTest> -->
  <!-- <PaymentTest></PaymentTest> -->
  <UserInfo></UserInfo>
  <!-- <UserSellingProduct></UserSellingProduct> -->
  <!-- <UserOrderProduct></UserOrderProduct> -->
  <!-- <UserWishlist></UserWishlist> -->
  <RouterView></RouterView>
</template>
