<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();
const productId = route.params.productId;
const categories = ref({
  majorCategories: [],
  middleCategories: [],
});

const productInfo = ref(route.state?.productInfo || {});
const selectedCategories = ref({
  majorId: "",
  middleId: "",
});

// 카테고리 데이터 로드
const loadCategories = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/categories/major"
    );
    categories.value.majorCategories = response.data;
    if (productInfo.value.majorId) {
      await loadCategories(productInfo.value.majorId);
    }
  } catch (error) {
    console.error("카테고리 로드 실패:", error);
  }
};

// 대분류 선택시 중분류 로드
const handleMajorChange = async (event) => {
  selectedCategories.value.majorId = event.target.value;
  selectedCategories.value.middleId = "";

  if (selectedCategories.value.majorId) {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/categories/middle/${selectedCategories.value.majorId}`
      );
      categories.value.middleCategories = response.data;
    } catch (error) {
      console.error("중분류 로드 실패:", error);
      categories.value.middleCategories = [];
    }
  } else {
    categories.value.middleCategories = [];
  }
};

// 상품 정보 수정
const updateProduct = async () => {
  try {
    const response = await axios.post(
      "http://localhost:8080/api/my-store/sale-products/modify",
      productInfo.value,
      {
        params: {
          userId: "seller1",
          productId: productId,
        },
      }
    );
    alert("상품 수정이 완료되었습니다.");
    router.push({ name: "UserSellingProduct" }); // 목록 페이지로 이동
  } catch (error) {
    console.log("상품 수정 실패: ", error);
    alert("상품 수정에 실패하였습니다.");
  }
};

onMounted(async () => {
  await loadCategories();
});
</script>

<template>
  <div class="product-form">
    <h2>상품 수정</h2>

    <!-- 카테고리 선택 -->
    <div class="form-group">
      <label>카테고리</label>
      <div class="category-select">
        <select
          v-model="selectedCategories.majorId"
          @change="handleMajorChange"
          required
        >
          <option value="">대분류 선택</option>
          <option
            v-for="major in categories.majorCategories"
            :key="major.majorId"
            :value="major.majorId"
          >
            {{ major.majorName }}
          </option>
        </select>

        <select
          v-model="productInfo.middleId"
          required
          :disabled="!selectedCategories.majorId"
        >
          <option value="">중분류 선택</option>
          <option
            v-for="middle in categories.middleCategories"
            :key="middle.middleId"
            :value="middle.middleId"
          >
            {{ middle.middleName }}
          </option>
        </select>
      </div>
    </div>

    <div class="form-group">
      <label>상품명</label>
      <input v-model="productInfo.productName" required />
    </div>

    <div class="form-group">
      <label>썸네일</label>
      <input v-model="productInfo.thumbnailImage" required />
    </div>

    <div class="form-group">
      <label>이미지1</label>
      <input v-model="productInfo.image1" required />
    </div>

    <div class="form-group">
      <label>가격</label>
      <input
        type="number"
        v-model.number="productInfo.price"
        required
        min="0"
      />
    </div>

    <div class="form-group">
      <label>상품 상태</label>
      <select v-model="productInfo.condition" required>
        <option value="NEW">새상품</option>
        <option value="LIKE_NEW">거의 새것</option>
        <option value="USED">사용감 있음</option>
        <option value="HEAVILY_USED">많이 사용함</option>
      </select>
    </div>

    <div class="form-group">
      <label>배송 상태</label>
      <select v-model="productInfo.status" required>
        <option value="SELLING">판매중</option>
        <option value="RESERVED">예약중</option>
        <option value="SOLD_OUT">판매 완료</option>
      </select>
    </div>

    <div class="form-group">
      <label>직거래 여부</label>
      <div class="radio-group">
        <label class="radio-label">
          <input type="radio" v-model="productInfo.person" :value="true" name="person"/>
          <span>예</span>
        </label>
        <label class="radio-label">
          <input type="radio" v-model="productInfo.person" :value="false" name="person"/>
          <span>아니요</span>
        </label>
      </div>
    </div>

    <div class="form-group" v-if="productInfo.person">
        <label>동행인 여부</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" v-model="productInfo.companion" :value="true" name="companion"/>
            <span>예</span>
          </label>
          <label class="radio-label">
            <input type="radio" v-model="productInfo.companion" :value="false" name="companion"/>
            <span>아니요</span>
          </label>
        </div>
      </div>

      <!-- 동행인이 true일 때만 보이는 성별 선택 -->
      <div class="form-group" v-if="productInfo.companion">
        <label>동행인 성별</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" v-model="productInfo.companionGender" :value="true" name="companionGender"/>
            <span>남자</span>
          </label>
          <label class="radio-label">
            <input type="radio" v-model="productInfo.companionGender" :value="false" name="companionGender"/>
            <span>여자</span>
          </label>
        </div>
      </div>

      <div class="form-group" v-if="productInfo.person">
        <label>대리인 여부</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" v-model="productInfo.deputy" :value="true" name="deputy"/>
            <span>예</span>
          </label>
          <label class="radio-label">
            <input type="radio" v-model="productInfo.deputy" :value="false" name="deputy"/>
            <span>아니요</span>
          </label>
        </div>
      </div>

      <!-- 대리인이 true일 때만 보이는 성별 선택 -->
      <div class="form-group" v-if="productInfo.deputy">
        <label>대리인 성별</label>
        <div class="radio-group">
          <label class="radio-label">
            <input type="radio" v-model="productInfo.deputyGender" :value="true" name="deputyGender"/>
            <span>남자</span>
          </label>
          <label class="radio-label">
            <input type="radio" v-model="productInfo.deputyGender" :value="false" name="deputyGender"/>
            <span>여자</span>
          </label>
        </div>
      </div>

    <div class="form-group">
      <label>상품 설명</label>
      <textarea v-model="productInfo.content" required></textarea>
    </div>

    <div class="button-group">
      <button @click="updateProduct" class="submit-btn">수정 완료</button>
      <button @click="router.back()" class="cancel-btn">취소</button>
    </div>
  </div>
</template>

<style scoped>
@import url("../../assets/userModifyProduct.css");
</style>
