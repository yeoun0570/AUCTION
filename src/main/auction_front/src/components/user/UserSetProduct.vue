<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const productInfo = ref({
  productName: "",
  price: 0,
  majorId: null,
  middleId: null,
  content: "",
  condition: {
    NEW: false,
    LIKE_NEW: false,
    USED: false,
    HEAVILY_USED: false,
  },
  images: {
    image1: null, // 썸네일로 사용될 이미지
    image2: null,
    image3: null,
    image4: null,
    image5: null,
  },
  thumbnailImage: "",
});

const previewImages = ref({
  image1: null,
  image2: null,
  image3: null,
  image4: null,
  image5: null,
});

// 선택된 카테고리를 위한 별도의 ref
const selectedCategories = ref({
  majorId: "",
  middleId: "",
});

// 카테고리 목록
const categories = ref({
  majorCategories: [],
  middleCategories: [],
});

// 카테고리 데이터 로드
const loadCategories = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/categories/major"
    );
    categories.value.majorCategories = response.data;
  } catch (error) {
    console.error("카테고리 로드 실패:", error);
  }
};

// 대분류 선택시 중분류 로드
const handleMajorChange = async (event) => {
  console.log("선택된 majorId:", event.target.value);
  selectedCategories.value.majorId = event.target.value;
  selectedCategories.value.middleId = ""; // 중분류 선택 초기화

  if (selectedCategories.value.majorId) {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/categories/middle/${selectedCategories.value.majorId}`
      );
      console.log("중분류 응답:", response.data);
      categories.value.middleCategories = response.data;
    } catch (error) {
      console.error("중분류 로드 실패:", error);
      categories.value.middleCategories = [];
    }
  } else {
    categories.value.middleCategories = [];
  }
};

onMounted(() => {
  loadCategories();
});

// 이미지 업로드 처리
// 현재 이미지를 varchar(255)로 받아서 이미지 크기를 담을 수가 없음. 나중에 클라우드 도입시키고 새로 짜야될거 같음
const handleImageUpload = (event, imageNumber) => {
  const file = event.target.files[0];
  if (file) {
    // 이미지 미리보기 생성
    previewImages.value[`image${imageNumber}`] = URL.createObjectURL(file);

    // 이미지 파일을 Base64로 변환
    const reader = new FileReader();
    reader.onload = (e) => {
      productInfo.value.images[`image${imageNumber}`] = e.target.result;
      // image1이 설정되면 썸네일로 지정
      if (imageNumber === 1) {
        productInfo.value.thumbnailImage = e.target.result;
      }
    };
    reader.readAsDataURL(file);
  }
};

const registerProduct = async () => {
  try {
    // 필수값 체크
    if (
      !productInfo.value.productName ||
      !productInfo.value.price ||
      !productInfo.value.content ||
      !selectedCategories.value.middleId ||
      !productInfo.value.condition ||
      !productInfo.value.images.image1 // 최소 한 장의 이미지는 필수
    ) {
      alert(
        "상품명, 가격, 상품설명, 카테고리, 상품상태, 이미지는 필수 입력값입니다."
      );
      return;
    }

    // 전송 데이터 구성
    const submitData = {
      productName: productInfo.value.productName,
      price: productInfo.value.price,
      content: productInfo.value.content,
      middleId: selectedCategories.value.middleId,
      condition: productInfo.value.condition,
      images: productInfo.value.images,
      thumbnailImage: productInfo.value.thumbnailImage,
    };

    console.log("전송할 데이터:", submitData);

    const response = await axios.post(
      "http://localhost:8080/api/product/register",
      submitData,
      {
        params: {
          userId: "seller1",
        },
      }
    );
    alert("상품이 등록되었습니다.");
    clearForm();
  } catch (error) {
    console.error("상품 등록 실패:", error);
    console.error("에러 응답:", error.response?.data);
    alert(error.response?.data?.error || "상품 등록에 실패했습니다.");
  }
};

const clearForm = () => {
  productInfo.value = {
    productName: "",
    price: 0,
    majorId: null,
    middleId: null,
    content: "",
    condition: null,
    images: {
      image1: null,
      image2: null,
      image3: null,
      image4: null,
      image5: null,
    },
    thumbnailImage: "",
  };

  // 미리보기 이미지도 초기화
  Object.keys(previewImages.value).forEach((key) => {
    if (previewImages.value[key]) {
      URL.revokeObjectURL(previewImages.value[key]);
      previewImages.value[key] = null;
    }
  });
};
</script>

<template>
  <div class="product-form">
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
          v-model="selectedCategories.middleId"
          required
          :disabled="
            !selectedCategories.majorId ||
            categories.middleCategories.length === 0
          "
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
      <label>가격</label>
      <input
        type="number"
        v-model.number="productInfo.price"
        required
        min="0"
      />
    </div>

    <div class="form-group">
      <label>상품 설명</label>
      <textarea v-model="productInfo.content" required></textarea>
    </div>

    <!-- 이미지 업로드 영역 -->
    <div class="form-group">
      <label>상품 이미지 (첫 번째 이미지가 썸네일로 사용됩니다)</label>
      <div class="image-upload-container">
        <div v-for="n in 5" :key="n" class="image-upload-box">
          <input
            type="file"
            :id="`image${n}`"
            accept="image/*"
            @change="(e) => handleImageUpload(e, n)"
            style="display: none"
          />
          <label :for="`image${n}`" class="image-upload-label">
            <div v-if="previewImages[`image${n}`]" class="image-preview">
              <img :src="previewImages[`image${n}`]" alt="Preview" />
            </div>
            <div v-else class="upload-placeholder">
              <span>+</span>
              <span>이미지 {{ n }}</span>
            </div>
          </label>
        </div>
      </div>
    </div>

    <div class="form-group">
      <label>상품 상태</label>
      <select v-model="productInfo.condition" required>
        <option value="">상태 선택</option>
        <option value="NEW">새상품</option>
        <option value="LIKE_NEW">거의 새것</option>
        <option value="USED">사용감 있음</option>
        <option value="HEAVILY_USED">많이 사용함</option>
      </select>
    </div>

    <button @click="registerProduct">상품 등록</button>
  </div>
</template>

<style scoped>
@import url("../../assets/userSetProduct.css");
</style>
