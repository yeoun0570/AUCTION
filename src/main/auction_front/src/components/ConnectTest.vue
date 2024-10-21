<script>
import axios from "axios";
import { ref, onMounted } from "vue";

export default {
  setup() {
    const data = ref("로딩 중..."); //데이터 받기 전 기본메시지

    onMounted(async () => {
      try {
        const response = await axios.get("http://localhost:4000/api/test");
        data.value = response.data;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    });

    return { data };
  }
};
</script>

<template>
  <div id="app">
    <h1>백엔드에서 받은 데이터:</h1>
    <pre>{{ data }}</pre>
  </div>
</template>