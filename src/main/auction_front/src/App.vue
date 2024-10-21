<script setup>
import { RouterLink, RouterView } from 'vue-router';
</script>

<template>
  <nav>
    <RouterLink to="/test">test</RouterLink>
  </nav>
  
  <RouterView></RouterView>
</template>