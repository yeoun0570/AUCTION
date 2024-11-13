<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();
const userInfo = ref({});
const loading = ref(null);
const error = ref(null);
const userId = "buyer1"; // 임시로 설정, 나중에 로그인한 사용자 ID로 변경

onMounted(() => {
  //이전 페이지에서 전달받은 현재 사용자 정보로 초기화
  if (route.params.currentUserInfo) {
    userInfo.value = { ...route.params.currentUserInfo };
  } else {
    loadUserInfo();
  }
});

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
    userInfo.value = response.data;
  } catch (err) {
    error.value = "사용자 정보를 불러오는데 실패했습니다.";
    console.error("Error loading user info:", err);
  } finally {
    loading.value = false;
  }
};

const updateUserInfo = async () => {
  try {
    loading.value = true;
    error.value = null;

    // 필요한 데이터만 추출
    const updateData = {
      userId: userId,
      nickname: userInfo.value.nickname,
      mobile: userInfo.value.mobile,
      email: userInfo.value.email,
      userBirth: userInfo.value.userBirth,
      addrMain: userInfo.value.addrMain,
      addrDetail: userInfo.value.addrDetail,
      addrZipcode: userInfo.value.addrZipcode,
      profile: userInfo.value.profile,
      kids: userInfo.value.kids?.map((kid) => ({
        kidId: kid.kidId,
        kidBirth: kid.kidBirth,
        kidGender: kid.kidGender,
      })),
    };

    console.log("전송할 데이터: ", updateData);

    await axios.post(
      `http://localhost:8080/api/my-store/info/modify`,
      updateData
    );
    alert("정보가 성공적으로 수정되었습니다.");
    router.push({ name: "UserInfo" });
  } catch (err) {
    error.value = "정보 수정 중 오류가 발생했습니다.";
    console.log("수정 실패: ", err.response?.data); // 에러 응답 데이터 확인
  } finally {
    loading.value = false;
  }
};

const addKid = () => {
  if (!userInfo.value.kids) userInfo.value.kids = [];
  userInfo.value.kids.push({
    userId: userId,
    kidId: null,
    kidBirth: null,
    kidGender: true,
  });
};

const cancel = () => {
  router.push({ name: "UserInfo" });
};
</script>

<template>
  <div v-if="userInfo" class="user-info-modify">
    <h2>내 정보 수정</h2>
    <form @submit.prevent="updateUserInfo" class="modify-form">
      <div class="form-group">
        <label>프로필 사진</label>
        <input v-model="userInfo.profile" type="image" />
      </div>

      <div class="form-group">
        <label>이름</label>
        <input v-model="userInfo.nickname" type="text" />
      </div>

      <div class="form-group">
        <label>이메일</label>
        <input v-model="userInfo.email" type="email" />
      </div>

      <div class="form-group">
        <label>전화번호</label>
        <input v-model="userInfo.mobile" type="tel" />
      </div>

      <div class="form-group">
        <label>생년월일</label>
        <input v-model="userInfo.userBirth" type="date" />
      </div>

      <div class="form-group">
        <label>주소</label>
        <input v-model="userInfo.addrMain" type="text" />
      </div>

      <div class="form-group">
        <label>상세주소</label>
        <input v-model="userInfo.addrDetail" type="text" />
      </div>

      <div class="form-group">
        <label>우편번호</label>
        <input v-model="userInfo.addrZipcode" type="text" />
      </div>

      <div class="kids-section">
        <h3>자녀 정보</h3>
        <button type="button" @click="addKid">자녀 추가</button>

        <div
          v-for="(kid, index) in userInfo.kids"
          :key="index"
          class="kid-info"
        >
          <input v-model="kid.kidBirth" type="date" />
          <select v-model="kid.kidGender">
            <option :value="true">남자</option>
            <option :value="false">여자</option>
          </select>
        </div>
      </div>

      <div class="error-message" v-if="error">{{ error }}</div>

      <div class="button-group">
        <button type="submit" :disabled="loading">
          {{ loading ? "수정 중..." : "수정 완료" }}
        </button>
        <button type="button" @click="cancel">취소</button>
      </div>
    </form>
  </div>
  <div v-else>로딩중...</div>
</template>

<style scoped>
@import url("../../assets/userInfo.css");
</style>
