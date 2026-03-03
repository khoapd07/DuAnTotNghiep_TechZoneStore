<template>
  <div class="profile-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">
      
      <div class="row g-4">
        
        <div class="col-lg-3">
          <div class="card border-0 shadow-sm rounded-4 overflow-hidden sticky-lg-top" style="top: 20px;">
            <div class="list-group list-group-flush custom-sidebar">
              
              <a href="#" class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3 border-0 active-tab">
                <i class="bi bi-person fs-5"></i>
                <span class="fw-bold fs-7">Thông tin cá nhân</span>
              </a>
              
              <a href="#" class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3 border-0 text-danger mt-2">
                <i class="bi bi-box-arrow-right fs-5"></i>
                <span class="fw-bold fs-7">Đăng xuất</span>
              </a>
              
            </div>
          </div>
        </div>

        <div class="col-lg-9">
          
          <div class="card border-0 shadow-sm rounded-4 mb-4">
            <div class="card-body p-4 p-md-5">
              <div class="mb-4">
                <h4 class="fw-black mb-1 text-dark">Hồ Sơ Cá Nhân</h4>
                <p class="text-muted fs-7 mb-0">Cập nhật thông tin tài khoản của bạn để trải nghiệm tốt hơn.</p>
              </div>

              <form @submit.prevent="saveProfile">
                <div class="row g-4 mb-4">
                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Họ và tên</label>
                    <input type="text" class="form-control custom-input" v-model="profileForm.fullName" placeholder="Nhập họ và tên" required>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Số điện thoại</label>
                    <input type="text" class="form-control custom-input" v-model="profileForm.phoneNumber" placeholder="Nhập số điện thoại" required>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Email</label>
                    <input type="email" class="form-control custom-input" v-model="profileForm.email" placeholder="Nhập email" required>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Địa chỉ</label>
                    <input type="text" class="form-control custom-input" v-model="profileForm.address" placeholder="Nhập địa chỉ" required>
                  </div>
                </div>
                <button type="submit" class="btn btn-neon fw-bold px-4 py-2 fs-7 rounded-2 text-dark">
                  LƯU THAY ĐỔI
                </button>
              </form>
            </div>
          </div>

          <div class="card border-0 shadow-sm rounded-4 mb-4">
            <div class="card-body p-4 p-md-5">
              <div class="d-flex justify-content-between align-items-start mb-4">
                <div>
                  <h4 class="fw-black mb-1 text-dark">Bảo mật & Mật khẩu</h4>
                  <p class="text-muted fs-7 mb-0">Đổi mật khẩu định kỳ để bảo vệ tài khoản của bạn.</p>
                </div>
                <i class="bi bi-shield-check fs-3 text-neon"></i>
              </div>

              <form @submit.prevent="updatePassword">
                <div class="row g-4 mb-4">
                  <div class="col-12">
                    <label class="form-label fw-bold fs-8 text-dark">Mật khẩu hiện tại</label>
                    <div class="input-group">
                      <input :type="showCurrentPassword ? 'text' : 'password'" class="form-control custom-input border-end-0" v-model="pwdForm.currentPassword" placeholder="••••••••" required>
                      <span class="input-group-text custom-input bg-white border-start-0 cursor-pointer" @click="showCurrentPassword = !showCurrentPassword">
                        <i :class="showCurrentPassword ? 'bi bi-eye-slash text-muted' : 'bi bi-eye text-neon'"></i>
                      </span>
                    </div>
                  </div>
                  
                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Mật khẩu mới</label>
                    <div class="input-group">
                      <input :type="showNewPassword ? 'text' : 'password'" class="form-control custom-input border-end-0" v-model="pwdForm.newPassword" placeholder="Mật khẩu ít nhất 8 ký tự" required>
                      <span class="input-group-text custom-input bg-white border-start-0 cursor-pointer" @click="showNewPassword = !showNewPassword">
                        <i :class="showNewPassword ? 'bi bi-eye-slash text-muted' : 'bi bi-eye text-neon'"></i>
                      </span>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label fw-bold fs-8 text-dark">Xác nhận mật khẩu mới</label>
                    <div class="input-group">
                      <input :type="showConfirmPassword ? 'text' : 'password'" class="form-control custom-input border-end-0" v-model="pwdForm.confirmPassword" placeholder="Nhập lại mật khẩu mới" required>
                      <span class="input-group-text custom-input bg-white border-start-0 cursor-pointer" @click="showConfirmPassword = !showConfirmPassword">
                        <i :class="showConfirmPassword ? 'bi bi-eye-slash text-muted' : 'bi bi-eye text-neon'"></i>
                      </span>
                    </div>
                  </div>
                </div>

                <div class="d-flex align-items-center gap-4">
                  <button type="submit" class="btn btn-outline-neon fw-bold px-4 py-2 fs-7 rounded-2">
                    CẬP NHẬT MẬT KHẨU
                  </button>
                  <a href="#" class="text-neon fw-bold text-decoration-none fs-7">QUÊN MẬT KHẨU?</a>
                </div>
              </form>
            </div>
          </div>

          <div class="d-flex flex-column flex-md-row gap-4 justify-content-center mt-2">
            
            <div class="card border-0 shadow-sm rounded-4 flex-grow-1" style="max-width: 300px;">
              <div class="card-body p-4 d-flex align-items-center gap-3">
                <div class="icon-box bg-neon-light text-neon rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
                  <i class="bi bi-cart3 fs-4"></i>
                </div>
                <div>
                  <small class="text-muted fw-bold fs-8 text-uppercase">ĐƠN HÀNG</small>
                  <h5 class="fw-black mb-0 text-dark">12 Đơn</h5>
                </div>
              </div>
            </div>

            <div class="card border-0 shadow-sm rounded-4 flex-grow-1" style="max-width: 300px;">
              <div class="card-body p-4 d-flex align-items-center gap-3">
                <div class="icon-box bg-neon-light text-neon rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
                  <i class="bi bi-calendar3 fs-4"></i>
                </div>
                <div>
                  <small class="text-muted fw-bold fs-8 text-uppercase">NGÀY THAM GIA</small>
                  <h5 class="fw-black mb-0 text-dark">15/05/2023</h5>
                </div>
              </div>
            </div>

          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

// State ẩn/hiện mật khẩu
const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

// State data từ form
const profileForm = ref({
  fullName: '',
  phoneNumber: '',
  email: '',
  address: ''
});

const pwdForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// Hàm hỗ trợ lấy Headers
const getAuthConfig = () => {
  const token = localStorage.getItem('jwt_token'); 
  return {
    headers: { Authorization: `Bearer ${token}` }
  };
};

// Hàm lấy userId từ localStorage (Giống Header.vue)
const getUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      const parsedUser = JSON.parse(userInfoString);
      return parsedUser.userId;
    } catch (e) {
      console.error('Lỗi parse user_info');
    }
  }
  return null;
};

// Fetch dữ liệu hồ sơ cá nhân
const fetchProfile = async () => {
  const userId = getUserId();
  if (!userId) {
    alert("Vui lòng đăng nhập để xem thông tin!");
    router.push('/login');
    return;
  }

  try {
    const response = await axios.get(`http://localhost:8080/api/profile/${userId}`, getAuthConfig());
    profileForm.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải thông tin cá nhân:', error);
    if (error.response && error.response.status === 401) {
      alert("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!");
      localStorage.clear();
      router.push('/login');
    }
  }
};

// Lưu Profile
const saveProfile = async () => {
  const userId = getUserId();
  if (!userId) return;

  try {
    const response = await axios.put(`http://localhost:8080/api/profile/${userId}`, profileForm.value, getAuthConfig());
    alert(response.data); // Hoặc dùng toast
  } catch (error) {
    console.error('Lưu profile thất bại:', error);
    alert(error.response?.data || 'Đã xảy ra lỗi hệ thống!');
  }
};

// Đổi Mật Khẩu
const updatePassword = async () => {
  if (pwdForm.value.newPassword !== pwdForm.value.confirmPassword) {
    alert("Mật khẩu xác nhận không khớp!");
    return;
  }

  const userId = getUserId();
  if (!userId) return;

  try {
    const response = await axios.put(`http://localhost:8080/api/profile/${userId}/password`, pwdForm.value, getAuthConfig());
    alert(response.data);
    pwdForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' };
  } catch (error) {
    console.error('Đổi mật khẩu thất bại:', error);
    alert(error.response?.data || 'Mật khẩu hiện tại không đúng hoặc lỗi hệ thống!');
  }
};

// Xử lý Đăng xuất (Dùng chung icon đăng xuất bên Sidebar)
const handleLogout = () => {
  localStorage.removeItem('jwt_token');
  localStorage.removeItem('user_role');
  localStorage.removeItem('user_info');
  
  window.dispatchEvent(new Event('auth-change'));
  router.push('/login');
};

onMounted(() => {
  fetchProfile();
});
</script>

<style scoped>
/* Typography & Utilities */
.profile-page {
  font-family: 'Inter', sans-serif;
}
.bg-light-gray { background-color: #F8F9FA; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }

/* Biến màu chủ đạo TechZone */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }

/* Custom Container */
.narrow-container {
  max-width: 1000px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* ------ SIDEBAR STYLE ------ */
.custom-sidebar .list-group-item {
  transition: all 0.2s ease;
}
.custom-sidebar .list-group-item:hover:not(.active-tab) {
  background-color: #f1f1f1;
}
.active-tab {
  background-color: rgba(0, 255, 51, 0.1) !important;
  color: #00FF33 !important;
  border-left: 4px solid #00FF33 !important;
}
.active-tab i {
  color: #00FF33 !important;
}

/* ------ FORM INPUTS STYLE ------ */
.custom-input {
  background-color: #F9FAFB;
  border: 1px solid #EAEAEA;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  font-size: 0.85rem;
}
.custom-input:focus,
.input-group:focus-within .custom-input {
  background-color: #FFFFFF;
  border-color: #00FF33;
  box-shadow: none;
  outline: 0;
}
/* Sửa màu viền khi focus cho input-group chứa icon mắt */
.input-group:focus-within .input-group-text,
.input-group:focus-within input {
  border-color: #00FF33 !important;
}

/* ------ BUTTONS STYLE ------ */
.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover {
  background-color: #00cc29;
}

.btn-outline-neon {
  background-color: transparent;
  color: #00FF33;
  border: 1px solid #00FF33;
  transition: all 0.2s ease;
}
.btn-outline-neon:hover {
  background-color: rgba(0, 255, 51, 0.1);
  color: #00FF33;
}
</style>