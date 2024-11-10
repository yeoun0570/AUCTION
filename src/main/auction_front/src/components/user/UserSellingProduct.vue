<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const userProducts = ref([]);
const userId = "seller1"; //추후에 로그인 한 유저로 변경
const currentStatus = ref("SELLING"); //상태 기본값 설정

//상품 상태 변경 메서드
const changeStatus = async (status) => {
  currentStatus.value = status;
  await loadUserProducts();
};

const loadUserProducts = async () => {
  try {
    //params 객체를 사용하면 Axios가 파라미터들을 자동으로 인코딩해줌
    const response = await axios.get(
      `http://localhost:8080/api/my-store/sale-products`,
      {
        params: {
          userId: userId,
          status: currentStatus.value, // .value 사용
        },
      }
    );
    userProducts.value = response.data;
  } catch (error) {
    console.error("로딩중 에러: ", error);
  }
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
</template>

<style scoped>
.status-filters {
  margin-bottom: 20px;
}

.status-filters button {
  margin-right: 10px;
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.status-filters button.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding: 20px;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
}

.product-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.price {
  font-weight: bold;
  color: #2c3e50;
  margin: 10px 0;
}

.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9em;
}

.status.selling {
  background-color: #28a745;
  color: white;
}

.status.sold {
  background-color: #dc3545;
  color: white;
}

.status.reserved {
  background-color: #ffc107;
  color: black;
}
</style>
