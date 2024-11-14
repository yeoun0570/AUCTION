import ConnectionTest from "@/components/ConnectionTest.vue";
import PaymentSuccess from "@/pages/PaymentSuccess.vue";
import UserInfo from "@/components/user/UserInfo.vue";
import UserInfoModify from "@/components/user/UserInfoModify.vue";
import UserModifyProduct from "@/components/user/UserModifyProduct.vue";
import UserSellingProduct from "@/components/user/UserSellingProduct.vue";
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

    {
      path: "/my-store/sale-products/modify/:productId",
      name: "UserModifyProduct",
      component: UserModifyProduct,
    },

    {
      path: "/my-store/sale-products",
      name: "UserSellingProduct",
      component: UserSellingProduct,
    },
  ],
});

export default router;
