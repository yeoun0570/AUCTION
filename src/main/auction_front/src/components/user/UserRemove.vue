<script setup>
import axios from "axios";
import { ref } from "vue";

const userId = "buyer3";
const userInfo =  ref({ canceled: false });  // 초기값 설정

const removeUser = async () => {
  if (!confirm("정말 탈퇴하시겠습니까?")) {
    return;
  }

  try {
    const response = await axios.post(
      "http://localhost:8080/api/my-store/remove",
      null,
      {
        params: {
          userId: userId,
        },
      }
    );
    userInfo.value = response.data;

    console.log("Response:", response.data);
    console.log("userInfo: ", userInfo.value);
    if (response.data.isCanceled) {
      alert("회원 탈퇴가 완료되었습니다.");
      // 로그아웃 처리나 메인 페이지로 이동 등 추가 작업
      // router.push('/');
    }
  } catch (error) {
    console.log("탈퇴 실패: ", error);
    if (error.response?.data) {
      alert(error.response.data); // 서버에서 보낸 에러 메시지 표시
    } else {
      alert("회원 탈퇴 처리 중 오류가 발생했습니다.");
    }
  }
};
</script>

<template>
  <button @click="removeUser" class="remove-btn">회원 탈퇴</button>
  <p>{{ userInfo.canceled }}</p>
</template>

<style scoped>
.remove-btn {
  background-color: #dc3545;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.remove-btn:hover {
  background-color: #c82333;
}
</style>
