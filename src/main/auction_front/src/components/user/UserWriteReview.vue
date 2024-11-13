<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const reviewInfo = ref({
  authorId: "",
  productId: null,
  review: "",
  rate: null,
});

const writeReview = async () => {
  try {
    const response = await axios.post(
      "http://localhost:8080/api/review/write",
      reviewInfo.value
    );
    alert("리뷰가 작성되었습니다.");
  } catch (error) {
    console.log("리뷰 작성 실패: ", error);
  }
};
</script>

<template>
  <div>
    <div>
      <label>후기 작성자</label>
      <input v-model="reviewInfo.authorId" type="text" required />
    </div>

    <div>
      <label>상품 아이디</label>
      <input v-model="reviewInfo.productId" type="number" />
    </div>

    <div>
      <label>리뷰 내용</label>
      <input v-model="reviewInfo.review" type="text" />
    </div>

    <div>
      <label>평점</label>
      <input v-model="reviewInfo.rate" type="number" />
    </div>
    <button
    @click="writeReview">작성하기</button>
  </div>
</template>
