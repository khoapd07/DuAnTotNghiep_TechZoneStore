<template>
  <div class="register-page">
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      
      <div class="register-card card border-0 shadow-lg rounded-4 p-4 p-md-5">
        
        <div class="text-center mb-4">
          <h3 class="fw-black text-dark">Đăng Ký Tài Khoản</h3> 
        </div>

        <div v-if="errorMessage" class="alert alert-danger py-2 fs-8 text-center" role="alert">
          {{ errorMessage }}
        </div>
        <div v-if="successMessage" class="alert alert-success py-2 fs-8 text-center" role="alert">
          {{ successMessage }}
        </div>

        <form @submit.prevent="handleRegister">
          
          <div class="mb-3">
            <label class="form-label fs-8 fw-bold text-muted">Tên tài khoản</label> 
            <div class="input-group custom-input-group rounded-3 overflow-hidden">
              <input 
                type="text" 
                class="form-control border-0 shadow-none bg-transparent py-2 px-3 fs-7" 
                v-model="username" 
                placeholder="Nhập tài khoản của bạn" 
                required
                :disabled="isLoading"
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
                :disabled="isLoading"
              >
              <span 
                class="input-group-text border-0 bg-transparent text-muted pe-3 cursor-pointer"
                @click="showPassword = !showPassword"
              >
                <i :class="showPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'"></i>
              </span>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label fs-8 fw-bold text-muted">Xác nhận mật khẩu</label> 
            <div class="input-group custom-input-group rounded-3 overflow-hidden">
              <input 
                :type="showConfirmPassword ? 'text' : 'password'" 
                class="form-control border-0 shadow-none bg-transparent py-2 px-3 fs-7" 
                v-model="confirmPassword" 
                placeholder="••••••••"
                required
                :disabled="isLoading"
              >
              <span 
                class="input-group-text border-0 bg-transparent text-muted pe-3 cursor-pointer"
                @click="showConfirmPassword = !showConfirmPassword"
              >
                <i :class="showConfirmPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'"></i>
              </span>
            </div>
          </div>

          <button 
            type="submit" 
            class="btn btn-neon w-100 fw-bold py-2 rounded-3 mb-4 d-flex justify-content-center align-items-center gap-2"
            :disabled="isLoading"
          >
            <span v-if="isLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <span v-else>Đăng ký <i class="bi bi-person-plus-fill fs-5"></i></span> 
          </button>

          <div class="divider d-flex align-items-center mb-4">
            <hr class="flex-grow-1 text-muted opacity-25">
            <span class="mx-3 text-muted fs-8">Hoặc tiếp tục với</span> 
            <hr class="flex-grow-1 text-muted opacity-25">
          </div>

          <div class="d-flex justify-content-center">
            <GoogleLogin :callback="handleGoogleLogin" />
          </div>

        </form>

        <div class="text-center mt-4 pt-2 fs-8">
          <span class="text-muted">Đã có tài khoản? </span>
          <router-link to="/login" class="text-dark fw-bold text-decoration-none hover-neon">Đăng nhập ngay</router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
// Xóa import axios từ thư viện gốc, thay bằng Instance (thực thể) api của bạn
import api from '../utils/axios';

const router = useRouter();

// Trạng thái form
const username = ref('');
const password = ref('');
const confirmPassword = ref('');

const showPassword = ref(false);
const showConfirmPassword = ref(false);

const isLoading = ref(false);// Trạng thái thông báo
const errorMessage = ref('');
const successMessage = ref('');

// Xử lý gọi API Đăng ký
const handleRegister = async () => {
  // Validate form
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'Mật khẩu xác nhận không khớp. Vui lòng kiểm tra lại!';
    return;
  }

  if (password.value.length < 6) {
    errorMessage.value = 'Mật khẩu phải có ít nhất 6 ký tự!';
    return;
  }
  
  errorMessage.value = '';
  successMessage.value = '';
  isLoading.value = true;

  try {
    // Thay đổi fetch thành api.post
    await api.post('/auth/register', {
      username: username.value,
      password: password.value
    });

    // Thành công
    successMessage.value = 'Đăng ký thành công! Đang chuyển hướng sang trang đăng nhập...';
    
    // Đợi 2 giây rồi chuyển hướng sang trang Đăng Nhập
    setTimeout(() => {
      router.push('/login');
    }, 2000);

  } catch (error) {
    // Xử lý lỗi với axios
    if (error.response && error.response.data) {
       errorMessage.value = typeof error.response.data === 'string' 
                            ? error.response.data 
                            : 'Đăng ký thất bại. Vui lòng thử lại!';
    } else {
       errorMessage.value = error.message;
    }
  } finally {
    isLoading.value = false;
  }
};

// Xử lý đăng nhập Google
const handleGoogleLogin = async (response) => {
  isLoading.value = true;
  errorMessage.value = '';
  successMessage.value = '';

  try {
    // Thay đổi axios.post thành api.post
    const res = await api.post('/auth/google', {
      token: response.credential
    });

    const data = res.data;

    // Lưu token và thông tin người dùng
    localStorage.setItem('jwt_token', data.token);
    const expiryTime = new Date().getTime() + 86400000;
    localStorage.setItem('token_expiry', expiryTime.toString());

    const userInfo = {
      userId: data.userId,
      username: data.username,
      role: data.role
    };
    localStorage.setItem('user_info', JSON.stringify(userInfo));

    // Gộp giỏ hàng khách vãng lai
    const guestCart = JSON.parse(localStorage.getItem('guest_cart') || '[]');
    if (guestCart.length > 0) {
        const mergeData = guestCart.map(item => ({
            productId: item.productId,
            variantId: item.variantId,
            quantity: item.quantity
        }));
        // Thay đổi axios.post thành api.post
        await api.post(`/cart/${userInfo.userId}/merge`, mergeData);
        localStorage.removeItem('guest_cart'); 
    }

    // Kích hoạt cập nhật Header
    window.dispatchEvent(new Event('auth-change'));

    // Báo thành công và chuyển hướng ngay lập tức (không cần đợi 2s như form thường)
    successMessage.value = 'Xác thực Google thành công!';
    if (data.role === 'Admin' || data.role === 'Staff') {
      router.push('/admin'); 
    } else {
      router.push('/'); 
    }

  } catch (error) {
    if (error.response && error.response.data) {
       errorMessage.value = error.response.data;
    } else {
       errorMessage.value = "Xác thực Google thất bại!";
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
/* Code CSS bạn giữ nguyên y hệt như cũ */
.register-page {
  font-family: 'Inter', sans-serif;
  background: radial-gradient(circle at top left, #f1f8f3, #ffffff 40%, #f4f6f8 100%);
  min-height: 100vh;
}

.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }

.text-neon { color: #00FF33 !important; }

.register-card {
  width: 100%;
  max-width: 420px;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.custom-input-group {
  background-color: #F4F5F7; 
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
.custom-input-group input:disabled {
  background-color: #e9ecef !important;
}

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

.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover:not(:disabled) {
  background-color: #00cc29;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 255, 51, 0.3);
}
.btn-neon:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.btn-outline-light.border {
  border-color: #EAEAEA !important;
  transition: all 0.2s;
}
.btn-outline-light.border:hover {
  background-color: #F4F5F7;
  border-color: #CCC !important;
}

.hover-neon {
  transition: color 0.2s;
}
.hover-neon:hover {
  color: #00FF33 !important;
}
</style>