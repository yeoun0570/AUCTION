import ConnectionTest from "@/components/ConnectionTest.vue";
import PaymentSuccess from "@/components/payments/PaymentSuccess.vue";
import UserInfo from "@/components/user/UserInfo.vue";
import UserInfoModify from "@/components/user/UserInfoModify.vue";
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
      path: "/success",
      name: "PaymentSuccess",
      component: PaymentSuccess,
    },
    { path: "/my-store/info", name: "UserInfo", component: UserInfo },

    {
      path: "/my-store/info/modify",
      name: "UserInfoModify",
      component: UserInfoModify,
    },
  ],
});

export default router;
