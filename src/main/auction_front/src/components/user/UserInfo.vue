<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

const userInfo = ref(null);
const loading = ref(true);
const error = ref(null);
const userId = "buyer1"; //테스트를 위한 사용자 아이디 임의로 설정

//사용자 정보 페이지 로딩할 메서드 작성
const loadUserInfo = async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await axios.get(
      `http://localhost:8080/api/my-store/info?userId=${userId}`
    );
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
  <h1>유저 정보 테스트</h1>

  <div v-if="loading">로딩 중...</div>

  <div v-else-if="error" class="error">
    {{ error }}
  </div>

  <div v-if="userInfo" class="user-info">
    <img :src="userInfo.profile" alt="사용자 프로필 사진" />
    <p>아이디: {{ userInfo.userId }}</p>
    <p>이름: {{ userInfo.nickname }}</p>
    <p>비밀번호: {{ userInfo.password }}</p>
    <p>생일: {{ userInfo.userBirth }}</p>
    <p>전화번호: {{ userInfo.mobile }}</p>
    <p>이메일: {{ userInfo.email }}</p>
    <p>성별: {{ userInfo.gender ? "남자" : "여자" }}</p>
    <p>주소: {{ userInfo.addrMain }}</p>
    <p>상세주소: {{ userInfo.addrDetail }}</p>
    <p>우편번호: {{ userInfo.addrZipcode }}</p>
    <p>신뢰도 점수: {{ userInfo.score }}</p>

    <!-- Kid 정보 추가 -->
    <h2>자녀 정보</h2>
    <div v-if="userInfo.kids && userInfo.kids.length > 0">
      <div v-for="kid in userInfo.kids" :key="kid.kidId" class="kid-info">
        <p>자녀 ID: {{ kid.kidId }}</p>
        <p>생년월일: {{ kid.kidBirth }}</p>
        <p>성별: {{ kid.kidGender ? "남자" : "여자" }}</p>
        <hr />
      </div>
    </div>
    <div v-else>등록된 자녀 정보가 없습니다.</div>
  </div>

  <div class="kid-info"></div>
</template>
