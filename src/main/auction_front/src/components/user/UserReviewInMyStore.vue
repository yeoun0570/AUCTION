<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const reviewInfo = ref([]);
const userId = "seller1";
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);
const reviewRate = ref(0);  // 상점 평점을 위한 ref 추가

const loadReviews = async (page = 0) => {
    console.log(reviewInfo.value);
  try {
    const response = await axios.get(
      "http://localhost:8080/api/my-store/reviews",
      {
        params: {
          userId: userId,
          page: page,
          size: pageSize.value,
        },
      }
    );
    reviewInfo.value = response.data.content;
    totalPages.value = response.data.totalPages;
    currentPage.value = response.data.currentPage;
    // 첫 번째 리뷰의 reviewRate를 사용 (모든 리뷰가 같은 reviewRate를 가지고 있음)
    reviewRate.value = reviewInfo.value[0]?.reviewRate || 0;
  } catch (error) {
    console.log("로딩 중 에러: ", error);
  }
};

const changePage = async (page) => {
  await loadReviews(page);
  window.scrollTo(0, 0);
};

onMounted(() => {
  loadReviews();
});
</script>

<template>
  <div class="reviews-container">
    <h1 class="main-title">내가 받은 후기 모음</h1>
    <div v-if="reviewInfo.length > 0">
      <div class="store-score">
        <h2>
          상점 점수: <span>{{ reviewRate }}점</span>
        </h2>
      </div>

      <div class="reviews-list">
        <div
          v-for="review in reviewInfo"
          :key="review.reviewId"
          class="review-card"
        >
          <div class="review-header">
            <div class="profile-section">
              <img
                :src="review.profile || '/default-profile.png'"
                alt="프로필 사진"
                class="profile-image"
              />
              <h3 class="author-name">{{ review.authorName }}</h3>
            </div>
            <div class="rating">
              <span class="rate">{{ review.rate }}점</span>
            </div>
          </div>

          <div class="product-info">
            <p class="product-name">{{ review.productName }}</p>
          </div>

          <p class="review-content">{{ review.review }}</p>
        </div>
      </div>

      <div v-if="totalPages > 1" class="pagination">
        <button
          :disabled="currentPage === 0"
          @click="changePage(currentPage - 1)"
          class="page-btn"
        >
          이전
        </button>

        <button
          v-for="page in totalPages"
          :key="page"
          :class="['page-btn', { active: currentPage === page - 1 }]"
          @click="changePage(page - 1)"
        >
          {{ page }}
        </button>

        <button
          :disabled="currentPage === totalPages - 1"
          @click="changePage(currentPage + 1)"
          class="page-btn"
        >
          다음
        </button>
      </div>
    </div>
    <div v-else class="no-reviews">아직 받은 후기가 없습니다.</div>
  </div>
</template>
<style scoped>
@import url("../../assets/UserReviewInMyStore.css");
</style>
