import ConnectionTest from "@/components/ConnectionTest.vue";
import PaymentSuccess from "@/components/payments/PaymentSuccess.vue";
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
      path: '/success',
      name: 'PaymentSuccess',
      component: PaymentSuccess,
    }

  ],
});

export default router;
