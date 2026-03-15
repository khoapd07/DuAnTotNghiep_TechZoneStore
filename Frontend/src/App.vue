<template>
  <Header v-if="!isAdminRoute" /> 
  
  <router-view /> 
  
  <Footer v-if="!isAdminRoute" />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

const route = useRoute()

// Kiểm tra xem đường dẫn hiện tại có bắt đầu bằng '/admin' không
const isAdminRoute = computed(() => route.path.startsWith('/admin' ) || route.path.startsWith('/shipping'))
</script>