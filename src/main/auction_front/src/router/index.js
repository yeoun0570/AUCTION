import ConnectionTest from "@/components/ConnectionTest.vue";
import TestApproveKakaoPay from "@/components/payments/TestApproveKakaoPay.vue";
import TestCancelKakaoPay from "@/components/payments/TestCancelKakaoPay.vue";
import TestFailKakaoPay from "@/components/payments/TestFailKakaoPay.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/test",
      name: "test",
      component: ConnectionTest,
    },

    {
      path: "/order/success",
      name : "testApprove",
      component : TestApproveKakaoPay,
    },

    {
      path: "/order/cancel",
      name : "testCancel",
      component : TestCancelKakaoPay,
    },

    {
      path: "/order/fail",
      name : "testFail",
      component : TestFailKakaoPay,
    },

  ],
});

export default router;
