<template>
  <div class="forgot-password-page">
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      
      <div class="forgot-card card border-0 shadow-lg rounded-4 p-4 p-md-5 text-center">
        
        <div class="icon-circle bg-neon-light text-neon-dark mx-auto mb-4 d-flex align-items-center justify-content-center rounded-circle" style="width: 65px; height: 65px;">
          <i class="bi bi-shield-lock-fill fs-1"></i>
        </div>

        <h3 class="fw-black text-dark mb-3">Khôi phục mật khẩu</h3>
        <p class="text-muted fs-7 mb-4 px-1 px-md-3 lh-base">
          Nhập địa chỉ email liên kết với tài khoản của bạn và chúng tôi sẽ gửi liên kết email để đặt lại mật khẩu.
        </p>

        <form @submit.prevent="handleResetPassword">
          
          <div class="mb-4 text-start">
            <label class="form-label fs-8 fw-bold text-muted">Email đăng kí</label>
            <div class="input-group custom-input-group rounded-3 overflow-hidden">
              <span class="input-group-text border-0 bg-transparent text-muted ps-3">
                <i class="bi bi-envelope-fill"></i>
              </span>
              <input 
                type="email" 
                class="form-control border-0 shadow-none bg-transparent py-2 px-3 fs-7" 
                v-model="email" 
                placeholder="name@example.com"
                required
              >
            </div>
          </div>

          <button type="submit" class="btn btn-neon w-100 fw-bold py-2 rounded-3 mb-4 d-flex justify-content-center align-items-center gap-2">
            GỬI LIÊN KẾT KHÔI PHỤC <i class="bi bi-arrow-right fs-5"></i>
          </button>
        </form>

        <div class="mt-2 fs-8">
          <router-link to="/login" class="text-muted fw-bold text-decoration-none hover-neon d-inline-flex align-items-center gap-2">
            <i class="bi bi-arrow-left fs-6"></i> Quay lại đăng nhập
          </router-link>
        </div>

        <div class="mt-4 pt-3 border-top border-light fs-8 text-muted">
          Gặp sự cố? <a href="#" class="text-dark fw-bold text-decoration-none hover-neon">Liên hệ hỗ trợ</a>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const email = ref('');

// Xử lý gửi yêu cầu khôi phục
const handleResetPassword = () => {
  if (!email.value) {
    alert('Vui lòng nhập địa chỉ email!');
    return;
  }
  console.log('Requesting password reset for:', email.value);
  
  // Bạn sẽ gọi API ở đây, sau khi thành công thì thông báo
  alert(`Liên kết khôi phục mật khẩu đã được gửi đến: ${email.value}\nVui lòng kiểm tra hộp thư của bạn!`);
};
</script>

<style scoped>
/* Reset & Typography */
.forgot-password-page {
  font-family: 'Inter', sans-serif;
  background: radial-gradient(circle at top left, #f1f8f3, #ffffff 40%, #f4f6f8 100%);
  min-height: 100vh;
}

.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }

/* Biến màu thương hiệu */
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }
.text-neon-dark { color: #00cc29 !important; }

/* Thẻ Form (Card) */
.forgot-card {
  width: 100%;
  max-width: 420px;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

/* Custom Input Box */
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

/* Nút Neon */
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

/* Hover text */
.hover-neon {
  transition: color 0.2s;
}
.hover-neon:hover {
  color: #00FF33 !important;
}
</style>