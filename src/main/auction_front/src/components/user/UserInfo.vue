<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const userInfo = ref({});
const loading = ref(true);
const error = ref(null);
const userId = "buyer1"; //테스트를 위한 사용자 아이디 임의로 설정

const goToModify = () => {
  router.push({
    name: "UserInfoModify",
    params: { currentUserInfo: userInfo.value },
  });
};

//사용자 정보 페이지 로딩할 메서드 작성
const loadUserInfo = async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await axios.get(
      `http://localhost:8080/api/my-store/info`,
      {
        params: {
          userId: userId,
        },
      }
    );
    console.log("받아온 데이터:", response.data);  // 데이터 확인
    userInfo.value = response.data;
  } catch (err) {
    error.value = "사용자 정보를 불러오는데 실패했습니다.";
    console.error("Error loading user info:", err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadUserInfo();
});
</script>

<template>
  <div v-if="userInfo" class="user-info">
    <h2>내 정보</h2>
    <div class="info-container">
      <div class="info-item">
        <label>프로필 사진:</label>
        <img :src="userInfo.profile" :alt="userInfo.nickname" />
      </div>
      <div class="info-item">
        <label>아이디:</label>
        <span>{{ userInfo.userId }}</span>
      </div>
      <div class="info-item">
        <label>이름:</label>
        <span>{{ userInfo.nickname}}</span>
      </div>
      <div class="info-item">
        <label>이메일:</label>
        <span>{{ userInfo.email }}</span>
      </div>
      <div class="info-item">
        <label>전화번호:</label>
        <span>{{ userInfo.mobile }}</span>
      </div>
      <div class="info-item">
        <label>생년월일:</label>
        <span>{{ userInfo.userBirth }}</span>
      </div>
      <div class="info-item">
        <label>주소:</label>
        <span>{{ userInfo.addrMain }}</span>
      </div>
      <div class="info-item">
        <label>상세주소:</label>
        <span>{{ userInfo.addrDetail }}</span>
      </div>
      <div class="info-item">
        <label>우편번호:</label>
        <span>{{ userInfo.addrZipcode }}</span>
      </div>

      <div v-if="userInfo.kids && userInfo.kids.length > 0" class="kids-info">
        <h3>자녀 정보</h3>
        <div v-for="kid in userInfo.kids" :key="kid.kidId" class="kid-item">
          <p>생년월일: {{ kid.kidBirth }}</p>
          <p>성별: {{ kid.kidGender ? "남자" : "여자" }}</p>
        </div>
      </div>
    </div>

    <button class="modify-btn" @click="goToModify">정보 수정</button>
  </div>
  <div v-else>로딩중...</div>
</template>

<style scoped>
@import url("../../assets/userInfo.css");
</style>
