<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const userProducts = ref([]);
const userId = "seller1"; //추후에 로그인 한 유저로 변경
const currentStatus = ref("SELLING"); //상태 기본값 설정
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);

//상품 상태 변경 메서드
const changeStatus = async (status) => {
  currentStatus.value = status;
  await loadUserProducts();
};

const loadUserProducts = async (page = 0) => {
  try {
    //params 객체를 사용하면 Axios가 파라미터들을 자동으로 인코딩해줌
    const response = await axios.get(
      `http://localhost:8080/api/my-store/sale-products`,
      {
        params: {
          userId: userId,
          status: currentStatus.value, // .value 사용
          page: page,
          size: pageSize.value,
        },
      }
    );
    userProducts.value = response.data.content; //백의 PageResponseDTO의 실제 데이터를 가리키는 content
    totalPages.value = response.data.totalPages; //백의 PageResponseDTO의 전체 페이지수 = totalPages
    currentPage.value = response.data.currentPage; //백의 PageResponseDTO의 현재 페이지 = currentPage
  } catch (error) {
    console.error("로딩중 에러: ", error);
  }
};

const changePage = async (page) => {
  await loadUserProducts(page);
  // 페이지 변경 후 스크롤 최상단으로 이동
  window.scrollTo(0, 0);
};

onMounted(() => {
  loadUserProducts();
});
</script>

<template>
  <h1>내 판매 상품</h1>
  <div class="status-filter">
    <button
      @click="changeStatus('SELLING')"
      :class="{ active: currentStatus === 'SELLING' }"
    >
      판매중
    </button>

    <button
      @click="changeStatus('RESERVED')"
      :class="{ active: currentStatus === 'RESERVED' }"
    >
      예약중
    </button>

    <button
      @click="changeStatus('SOLD_OUT')"
      :class="{ active: currentStatus === 'SOLD_OUT' }"
    >
      판매완료
    </button>
  </div>

  <div v-if="userProducts.length > 0" class="products-grid">
    <div
      v-for="product in userProducts"
      :key="product.productName"
      class="product-card"
    >
      <img
        :src="product.thumbnailImage"
        :alt="product.productName"
        class="product-image"
      />
      <h3>{{ product.productName }}</h3>
      <p class="price">{{ product.price.toLocaleString() }}원</p>
      <p class="status" :class="product.status.toLowerCase()">
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
  <div v-else>등록된 상품이 없습니다.</div>

  <!-- 페이지네이션 -->
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
@import url('../../assets/userSellingProduct.css');
</style>
