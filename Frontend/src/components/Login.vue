<template>
  <div class="login-page">
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      
      <div class="login-card card border-0 shadow-lg rounded-4 p-4 p-md-5">
        
        <div class="text-center mb-4">
          <h3 class="fw-black text-dark">Đăng Nhập</h3>
        </div>

        <form @submit.prevent="handleLogin">
          
          <div class="mb-3">
            <label class="form-label fs-8 fw-bold text-muted">Tên tài khoản</label>
            <div class="input-group custom-input-group rounded-3 overflow-hidden">
              <input 
                type="text" 
                class="form-control border-0 shadow-none bg-transparent py-2 px-3 fs-7" 
                v-model="username" 
                placeholder="Nhập tài khoản của bạn"
                required
              >
              <span class="input-group-text border-0 bg-transparent text-muted pe-3">
                <i class="bi bi-person-fill"></i>
              </span>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label fs-8 fw-bold text-muted">Mật khẩu</label>
            <div class="input-group custom-input-group rounded-3 overflow-hidden">
              <input 
                :type="showPassword ? 'text' : 'password'" 
                class="form-control border-0 shadow-none bg-transparent py-2 px-3 fs-7" 
                v-model="password" 
                placeholder="••••••••"
                required
              >
              <span 
                class="input-group-text border-0 bg-transparent text-muted pe-3 cursor-pointer"
                @click="showPassword = !showPassword"
              >
                <i :class="showPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'"></i>
              </span>
            </div>
          </div>

          <div class="d-flex justify-content-between align-items-center mb-4 fs-8">
            <div class="form-check custom-checkbox mb-0">
              <input class="form-check-input shadow-none" type="checkbox" id="rememberMe" v-model="rememberMe">
              <label class="form-check-label text-muted" for="rememberMe">
                Ghi nhớ đăng nhập
              </label>
            </div>
            <a href="#" class="text-neon text-decoration-none fw-bold">Quên mật khẩu?</a>
          </div>

          <button type="submit" class="btn btn-neon w-100 fw-bold py-2 rounded-3 mb-4 d-flex justify-content-center align-items-center gap-2">
            Đăng Nhập <i class="bi bi-box-arrow-in-right fs-5"></i>
          </button>

          <div class="divider d-flex align-items-center mb-4">
            <hr class="flex-grow-1 text-muted opacity-25">
            <span class="mx-3 text-muted fs-8">Hoặc tiếp tục với</span>
            <hr class="flex-grow-1 text-muted opacity-25">
          </div>

          <button type="button" class="btn btn-outline-light w-100 fw-bold py-2 rounded-3 text-dark d-flex justify-content-center align-items-center gap-2 border">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" class="bi bi-google" viewBox="0 0 16 16">
              <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z"/>
            </svg>
            Google
          </button>

        </form>

        <div class="text-center mt-4 pt-2 fs-8">
          <span class="text-muted">Chưa có tài khoản? </span>
          <router-link to="/register" class="text-dark fw-bold text-decoration-none hover-neon">Đăng kí ngay</router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Trạng thái form
const username = ref('');
const password = ref('');
const rememberMe = ref(false);
const showPassword = ref(false);

// Xử lý đăng nhập (Mock logic - Bạn cần thay bằng API thật của Spring Boot)
const handleLogin = () => {
  console.log('Logging in with:', username.value, password.value, rememberMe.value);
  
  // Giả lập thành công, set token và chuyển hướng
  // localStorage.setItem('jwt_token', 'fake_token_123');
  // localStorage.setItem('user_info', username.value);
  // window.dispatchEvent(new Event('auth-change'));
  // router.push('/');
  
  alert('Giao diện đã sẵn sàng! Chờ kết nối API.');
};
</script>

<style scoped>
/* Reset & Typography */
.login-page {
  font-family: 'Inter', sans-serif;
  /* Mô phỏng nền gradient mờ giống bản thiết kế */
  background: radial-gradient(circle at top left, #f1f8f3, #ffffff 40%, #f4f6f8 100%);
  min-height: 100vh;
}

.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }

/* Biến màu thương hiệu */
.text-neon { color: #00FF33 !important; }

/* Thẻ Form (Card) */
.login-card {
  width: 100%;
  max-width: 420px;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

/* Custom Input Box (Nền xám nhạt bao trọn cả input và icon) */
.custom-input-group {
  background-color: #F4F5F7; /* Màu nền xám cực nhạt */
  border: 1px solid transparent;
  transition: all 0.2s;
}
.custom-input-group:focus-within {
  border-color: #00FF33;
  background-color: #FFFFFF;
}
.custom-input-group input::placeholder {
  color: #adb5bd;
  font-weight: 500;
}

/* Custom Checkbox */
.custom-checkbox .form-check-input {
  border-radius: 4px;
  border-color: #CCC;
  cursor: pointer;
  width: 16px;
  height: 16px;
  margin-top: 0.1rem;
}
.custom-checkbox .form-check-input:checked {
  background-color: #00FF33;
  border-color: #00FF33;
}
.custom-checkbox .form-check-input:checked[type=checkbox] {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'%3e%3cpath fill='none' stroke='%23000' stroke-linecap='round' stroke-linejoin='round' stroke-width='3' d='M6 10l3 3l6-6'/%3e%3c/svg%3e");
}

/* Nút Neon Đăng nhập */
.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover {
  background-color: #00cc29;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 255, 51, 0.3);
}

/* Nút Google */
.btn-outline-light.border {
  border-color: #EAEAEA !important;
  transition: all 0.2s;
}
.btn-outline-light.border:hover {
  background-color: #F4F5F7;
  border-color: #CCC !important;
}

/* Hover text */
.hover-neon {
  transition: color 0.2s;
}
.hover-neon:hover {
  color: #00FF33 !important;
}
</style>