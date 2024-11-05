import ConnectionTest from "@/components/ConnectionTest.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/test",
      name: "test",
      component: ConnectionTest,
    },

  ],
});

export default router;
