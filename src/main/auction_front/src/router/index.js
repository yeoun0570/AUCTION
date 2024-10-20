import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/test',
    name: 'ConnectionTest',
    component: () => import('../components/ConnectionTest.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
