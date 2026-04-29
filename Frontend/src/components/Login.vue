<template>
  <div class="login-page">
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      
      <div class="login-card card border-0 shadow-lg rounded-4 p-4 p-md-5">
        
        <div class="text-center mb-4">
          <h3 class="fw-black text-dark">Đăng Nhập</h3>
        </div>

        <div v-if="errorMessage" class="alert alert-danger py-2 fs-8 text-center" role="alert">
          {{ errorMessage }}
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

          <div class="d-flex justify-content-between align-items-center mb-4 fs-8">
            <div class="form-check custom-checkbox mb-0">
              <input class="form-check-input shadow-none" type="checkbox" id="rememberMe" v-model="rememberMe" :disabled="isLoading">
              <label class="form-check-label text-muted" for="rememberMe">
                Ghi nhớ đăng nhập
              </label>
            </div>
            <router-link class="text-neon text-decoration-none fw-bold" to="/ForgotPassword">Quên mật khẩu?</router-link>
          </div>

          <button 
            type="submit" 
            class="btn btn-neon w-100 fw-bold py-2 rounded-3 mb-4 d-flex justify-content-center align-items-center gap-2"
            :disabled="isLoading"
          >
            <span v-if="isLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <span v-else>Đăng Nhập <i class="bi bi-box-arrow-in-right fs-5"></i></span>
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
// Xóa import axios từ thư viện gốc, thay bằng Instance (thực thể) api của bạn
import api from '../utils/axios'; 

const router = useRouter();

// Trạng thái form
const username = ref('');
const password = ref('');
const rememberMe = ref(false);
const showPassword = ref(false);

// Trạng thái xử lý API
const isLoading = ref(false);
const errorMessage = ref('');

// Xử lý đăng nhập gọi API thật
const handleLogin = async () => {
  // Reset thông báo lỗi và bật trạng thái loading
  errorMessage.value = '';
  isLoading.value = true;

  try {
    // Thay fetch bằng api.post, Axios sẽ tự động nối Base URL (địa chỉ URL cơ sở)
    const response = await api.post('/auth/login', {
      username: username.value,
      password: password.value
    });

    // Bỏ qua bước kiểm tra !response.ok vì Axios tự động ném ra Exception (ngoại lệ) nếu lỗi
    // Axios tự động Parse (phân tích) JSON, nên lấy trực tiếp từ response.data
    const data = response.data;

    // 1. Lưu JWT token vào localStorage
    localStorage.setItem('jwt_token', data.token);
    
    // Lưu thời gian hết hạn của token (24 giờ = 86400000 ms)
    const expiryTime = new Date().getTime() + 86400000;
    localStorage.setItem('token_expiry', expiryTime.toString());

    // 2. Lưu thông tin người dùng (Lưu ý: Không lưu mật khẩu)
    const userInfo = {
      userId: data.userId,
      username: data.username,
      role: data.role
    };
    localStorage.setItem('user_info', JSON.stringify(userInfo));

    // Gọi sau khi lưu user_info và token thành công
    const guestCart = JSON.parse(localStorage.getItem('guest_cart') || '[]');
    if (guestCart.length > 0) {
        const mergeData = guestCart.map(item => ({
            productId: item.productId,
            variantId: item.variantId,
            quantity: item.quantity
        }));
        
        // Thay axios.post bằng api.post
        await api.post(`/cart/${userInfo.userId}/merge`, mergeData);
        // Xóa giỏ hàng local đi sau khi đã gộp thành công
        localStorage.removeItem('guest_cart'); 
    }

    // 3. (Tùy chọn) Kích hoạt event để các component khác (như Header) biết trạng thái đăng nhập thay đổi
    window.dispatchEvent(new Event('auth-change'));

    // 4. Chuyển hướng người dùng dựa theo Role
    if (data.role === 'Admin' || data.role === 'Staff') {
      router.push('/admin/orders'); // Hoặc trang quản trị tương ứng

    }
    else if(data.role === 'Shipper') {
      router.push('/shipping'); // Về trang chủ mua hàng
    }
    
    else {
      router.push('/'); // Về trang chủ mua hàng
    }

  } catch (error) {
    // Xử lý lỗi (Error Handling) riêng biệt của Axios
    if (error.response && error.response.data) {
       // Xử lý trường hợp Backend trả về chuỗi text hoặc Object
       errorMessage.value = typeof error.response.data === 'string' 
                            ? error.response.data 
                            : (error.response.data.message || 'Sai tài khoản hoặc mật khẩu. Vui lòng thử lại!');
    } else {
       errorMessage.value = error.message;
    }
  } finally {
    // Tắt trạng thái loading
    isLoading.value = false;
  }
};

//đăng nhập bằng google
const handleGoogleLogin = async (response) => {
  isLoading.value = true;
  errorMessage.value = '';

  try {
    // Thay axios.post bằng api.post
    const res = await api.post('/auth/google', {
      token: response.credential
    });

    const data = res.data;

    // Các bước xử lý y hệt như đăng nhập thường
    localStorage.setItem('jwt_token', data.token);
    const expiryTime = new Date().getTime() + 86400000;
    localStorage.setItem('token_expiry', expiryTime.toString());

    const userInfo = {
      userId: data.userId,
      username: data.username, // Đây là email từ Google
      role: data.role
    };
    localStorage.setItem('user_info', JSON.stringify(userInfo));

    // Merge (gộp) giỏ hàng nếu có
    const guestCart = JSON.parse(localStorage.getItem('guest_cart') || '[]');
    if (guestCart.length > 0) {
        const mergeData = guestCart.map(item => ({
            productId: item.productId,
            variantId: item.variantId,
            quantity: item.quantity
        }));
        
        // Thay axios.post bằng api.post
        await api.post(`/cart/${userInfo.userId}/merge`, mergeData);
        localStorage.removeItem('guest_cart'); 
    }

    window.dispatchEvent(new Event('auth-change'));

    // Route (chuyển hướng)
    if (data.role === 'Admin' || data.role === 'Staff') {
      router.push('/admin'); 
    } else {
      router.push('/'); 
    }

  } catch (error) {
    if (error.response && error.response.data) {
       errorMessage.value = error.response.data;
    } else {
       errorMessage.value = "Đăng nhập Google thất bại!";
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
/* TOÀN BỘ PHẦN STYLE BẠN GIỮ NGUYÊN */
.login-page {
  font-family: 'Inter', sans-serif;
  background: radial-gradient(circle at top left, #f1f8f3, #ffffff 40%, #f4f6f8 100%);
  min-height: 100vh;
}

.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }

.text-neon { color: #00FF33 !important; }

.login-card {
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