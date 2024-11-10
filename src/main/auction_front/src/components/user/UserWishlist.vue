<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const userWishlist = ref([]);
const userId = "buyer2"; //추후에 로그인한 유저로 변경
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);

//찜 목록 조회 로딩 메서드
const loadUserWishlist = async (page = 0) => {
  try {
    //params 객체를 사용하면 Axios가 파라미터들을 자동으로 인코딩해줌
    const response = await axios.get(
      `http://localhost:8080/api/my-store/wishlist`,
      {
        params: {
          userId: userId,
          page: page,
          size: pageSize.value,
        },
      }
    );
    userWishlist.value = response.data.content;
    totalPages.value = response.data.totalPages;
    currentPage.value = response.data.currentPage;
  } catch (error) {
    console.log("로딩 중 에러 : ", error);
  }
};

const changePage = async (page) => {
  await loadUserWishlist(page);
  // 페이지 변경 후 스크롤 최상단으로 이동
  window.scrollTo(0, 0);
};

onMounted(() => {
  loadUserWishlist();
});
</script>

<template>
  <h1>나의 위시리스트 목록</h1>
  <div v-if="userWishlist.length > 0" class="products-grid">
    <div
      v-for="product in userWishlist"
      :key="product.productName"
      class="product-card"
    >
      <img
        :src="product.thumbnamilImage"
        :alt="product.productName"
        class="product-image"
      />
      <h3>{{ product.productName }}</h3>
      <p class="price">{{ product.price }} 원</p>
      <p class="status" :class="product.status">
        {{
          product.status === "SELLING"
            ? "판매중"
            : product.status === "RESERVED"
            ? "예약중"
            : "판매완료"
        }}
      </p>
    </div>
  </div>
  <div v-else>찜한 상품이 없습니다.</div>

  <div v-if="totalPages > 1" class="pagination">
    <button :disabled="currentPage === 0" @click="changePage(currentPage - 1)">
      이전
    </button>

    <button
      v-for="page in totalPages"
      :key="page"
      :class="{ active: currentPage === page - 1 }"
      @click="changePage(page - 1)"
    >
      {{ page }}
    </button>

    <button
      :disabled="currentPage === totalPages - 1"
      @click="changePage(currentPage + 1)"
    >
      다음
    </button>
  </div>
</template>

<style scoped>
@import url("../../assets/userSellingProduct.css");
</style>
