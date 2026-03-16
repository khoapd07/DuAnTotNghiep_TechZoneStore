<template>
  <div id="app" class="d-flex flex-column min-vh-100">
    <Header v-if="!isAdminRoute" /> 
    
    <main class="flex-grow-1">
      <router-view /> 
    </main>
    
    <Footer v-if="!isAdminRoute" />

    <ChatWidget v-if="!isAdminRoute" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import ChatWidget from './components/ChatWidget.vue' // Nhớ import ChatWidget vào nhé

const route = useRoute()

// Kiểm tra xem đường dẫn hiện tại có bắt đầu bằng '/admin' không
const isAdminRoute = computed(() => route.path.startsWith('/admin' ) || route.path.startsWith('/shipping'))
</script>