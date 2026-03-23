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
import { computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import ChatWidget from './components/ChatWidget.vue' // Nhớ import ChatWidget vào nhé

const route = useRoute()
const router = useRouter()

// Kiểm tra xem đường dẫn hiện tại có bắt đầu bằng '/admin' không
const isAdminRoute = computed(() => route.path.startsWith('/admin' ) || route.path.startsWith('/shipping'))

// Kiểm tra và tự động đăng xuất khi hết hạn 24h
let checkInterval = null;

const checkTokenExpiration = () => {
  const expiryTime = localStorage.getItem('token_expiry');
  // Nếu có thời gian hết hạn và thời gian hiện tại đã vượt qua nó
  if (expiryTime && new Date().getTime() > parseInt(expiryTime)) {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
    localStorage.removeItem('token_expiry');
    window.dispatchEvent(new Event('auth-change'));
    alert('Phiên đăng nhập đã hết hạn sau 24 giờ. Vui lòng đăng nhập lại!');
    router.push('/login');
  }
};

onMounted(() => {
  checkTokenExpiration();
  checkInterval = setInterval(checkTokenExpiration, 60000); // Tự động quét kiểm tra mỗi phút
});

onUnmounted(() => {
  if (checkInterval) clearInterval(checkInterval);
});
</script>