<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import PaymentCancelButton from "../payments/PaymentCancelButton.vue";
import PaymentConfirmButton from "../payments/PaymentConfirmButton.vue";

const userOrders = ref([]);
const userId = "buyer1"; //추후에 로그인 한 유저로 변경
const currentStatus = ref("ESCROW_HOLDING"); //상태 기본값 설정
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);

//상품 상태 변경 메서드
const changeStatus = async (status) => {
  currentStatus.value = status;
  await loadUserOrders();
};

const loadUserOrders = async (page = 0) => {
  try {
    //params 객체를 사용하면 Axios가 파라미터들을 자동으로 인코딩해줌
    const response = await axios.get(
      `http://localhost:8080/api/my-store/buy-products`,
      {
        params: {
          userId: userId,
          status: currentStatus.value,
          page: page,
          size: pageSize.value,
        },
      }
    );
    userOrders.value = response.data.content; //백의 PageResponseDTO의 실제 데이터를 가리키는 content
    totalPages.value = response.data.totalPages; //백의 PageResponseDTO의 전체 페이지수 = totalPages
    currentPage.value = response.data.currentPage; //백의 PageResponseDTO의 현재 페이지 = currentPage
  } catch (error) {
    console.error("로딩중 에러: ", error);
  }
};

const changePage = async (page) => {
  await loadUserOrders(page);
  // 페이지 변경 후 스크롤 최상단으로 이동
  window.scrollTo(0, 0);
};

// 결제 취소 처리
const cancelOrder = async () => {
  if (!paymentResult.value?.orderId) return;

  try {
    loading.value = true;
    error.value = null;

    // 취소 전 확인
    if (!confirm("결제를 취소하시겠습니까?")) {
      return;
    }

    console.log("결제 취소 시작 - orderId:", paymentResult.value.orderId);

    const response = await axios.post(
      `http://localhost:8080/api/payments/${paymentResult.value.orderId}/cancel`
    );

    console.log("결제 취소 완료:", response.data);
    paymentDetails.value = response.data;
    alert("결제가 취소되었습니다. 전체 금액이 환불됩니다.");

    // 취소 후 메인 페이지나 상품 페이지로 이동
    // router.push("/"); // 또는 상품 상세 페이지로 이동
  } catch (error) {
    console.error("결제 취소 중 오류:", error);
    error.value =
      error.response?.data?.message || "결제 취소 중 오류가 발생했습니다.";
    alert(error.value);
  } finally {
    loading.value = false;
  }
};

// 구매 확정 처리
const confirmPurchase = async () => {
  if (!paymentResult.value?.orderId) return;

  try {
    loading.value = true;
    error.value = null;

    console.log("구매확정 요청 시작 - orderId:", paymentResult.value.orderId);

    const response = await axios.post(
      `http://localhost:8080/api/payments/${paymentResult.value.orderId}/confirm`
    );

    console.log("구매확정 응답:", response.data);
    paymentDetails.value = response.data;

    if (response.data.status === "ESCROW_RELEASED") {
      alert("구매가 확정되었습니다.");
    } else {
      throw new Error(`잘못된 상태: ${response.data.status}`);
    }
  } catch (error) {
    console.error("구매 확정 중 오류:", error);
    error.value =
      error.response?.data?.message || "구매 확정 중 오류가 발생했습니다.";
    alert(error.value);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadUserOrders();
});
</script>

<template>
  <h1>내 구매 상품</h1>
  <div class="status-filter">
    <button
      @click="changeStatus('ESCROW_HOLDING')"
      :class="{ active: currentStatus === 'ESCROW_HOLDING' }"
    >
      구매중
    </button>

    <button
      @click="changeStatus('ESCROW_RELEASED')"
      :class="{ active: currentStatus === 'ESCROW_RELEASED' }"
    >
      구매완료
    </button>
    <PaymentCancelButton :productData="userOrders" @cancelOrder="cancelOrder"></PaymentCancelButton>
    <PaymentConfirmButton ></PaymentConfirmButton>
  </div>

  <div v-if="userOrders.length > 0" class="orders-grid">
    <div v-for="order in userOrders" :key="order.orderId" class="order-card">
      <img
        :src="order.thumbnailImage"
        :alt="order.productName"
        class="product-image"
      />
      <h3>{{ order.productName }}</h3>
      <p class="price">{{ order.price.toLocaleString() }}원</p>
      <p class="status" :class="order.status.toLowerCase()">
        {{ order.status === "ESCROW_HOLDING" ? "예약중" : "판매완료" }}
      </p>
    </div>
  </div>
  <div v-else>구매한 상품이 없습니다.</div>

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
@import url("../../assets/userOrderProduct.css");
</style>
