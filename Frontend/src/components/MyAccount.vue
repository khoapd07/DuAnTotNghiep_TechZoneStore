<template>
  <div class="profile-page bg-light-gray pb-5 pt-4 min-vh-100">
    <div class="container narrow-container">
      
      <div class="row g-4">
        
        <div class="col-lg-3">
          <div class="card border-0 shadow-sm rounded-4 overflow-hidden sticky-lg-top" style="top: 20px;">
            <div class="list-group list-group-flush custom-sidebar">
              
              <a href="#" @click.prevent="currentTab = 'profile'" 
                 class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3 border-0"
                 :class="{ 'active-tab': currentTab === 'profile' }">
                <i class="bi bi-person fs-5"></i>
                <span class="fw-bold fs-7">Thông tin cá nhân</span>
              </a>

              <a href="#" @click.prevent="currentTab = 'reviews'" 
                 class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3 border-0"
                 :class="{ 'active-tab': currentTab === 'reviews' }">
                <i class="bi bi-star fs-5"></i>
                <span class="fw-bold fs-7">Lịch sử đánh giá</span>
              </a>
              
              <a href="#" @click.prevent="handleLogout" class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3 border-0 text-danger mt-2">
                <i class="bi bi-box-arrow-right fs-5"></i>
                <span class="fw-bold fs-7">Đăng xuất</span>
              </a>
              
            </div>
          </div>
        </div>

        <div class="col-lg-9">
          
          <div v-if="currentTab === 'profile'">
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
          </div>

          <div v-if="currentTab === 'reviews'">
            <div class="mb-4">
              <h4 class="fw-black mb-1 text-dark">Lịch sử đánh giá</h4>
              <p class="text-muted fs-7 mb-0">Xem lại các sản phẩm bạn đã trải nghiệm và chia sẻ cảm nhận.</p>
            </div>

            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4 bg-light-green">
              <div class="row align-items-center">
                <div class="col-md-4 border-end-md text-center text-md-start mb-3 mb-md-0">
                  <span class="text-muted fw-bold fs-8 text-uppercase mb-1 d-block">Tổng đánh giá</span>
                  <div class="d-flex align-items-baseline justify-content-center justify-content-md-start gap-2">
                    <h2 class="fw-black text-dark m-0 fs-1">{{ totalReviewsCount }}</h2>
                    <span class="text-neon fw-bold fs-8">bài viết</span>
                  </div>
                </div>
                <div class="col-md-8 ps-md-4">
                  <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="text-muted fs-8 fw-bold">Điểm trung bình</span>
                    <span class="fw-black text-dark fs-5">{{ averageRating }}/5 <i class="bi bi-star-fill text-warning fs-6"></i></span>
                  </div>
                  <div class="progress" style="height: 8px;">
                    <div class="progress-bar bg-neon" :style="{ width: progressWidth }"></div>
                  </div>
                </div>
              </div>
            </div>

            <div class="d-flex flex-wrap align-items-center gap-2 mb-4">
              <button @click="filterRating = 'Tất cả'" class="btn fw-bold fs-8 rounded-pill px-3 py-1 shadow-sm" :class="filterRating === 'Tất cả' ? 'btn-neon text-dark' : 'btn-light text-muted'">Tất cả</button>
              <button @click="filterRating = 5" class="btn fw-bold fs-8 rounded-pill px-3 py-1 d-flex align-items-center gap-1 shadow-sm" :class="filterRating === 5 ? 'btn-neon text-dark' : 'btn-light text-muted'">5 sao <i class="bi bi-star-fill text-warning"></i></button>
              <button @click="filterRating = 4" class="btn fw-bold fs-8 rounded-pill px-3 py-1 d-flex align-items-center gap-1 shadow-sm" :class="filterRating === 4 ? 'btn-neon text-dark' : 'btn-light text-muted'">4 sao <i class="bi bi-star-fill text-warning"></i></button>
              <button @click="filterRating = 'low'" class="btn fw-bold fs-8 rounded-pill px-3 py-1 shadow-sm" :class="filterRating === 'low' ? 'btn-neon text-dark' : 'btn-light text-muted'">Từ 3 sao trở xuống</button>
            </div>

            <div class="d-flex flex-column gap-3">
              <div v-if="loadingReviews" class="text-center py-5">
                 <div class="spinner-border text-neon" role="status"></div>
              </div>

              <div v-for="review in filteredReviews" :key="review.reviewId" class="card border border-light-subtle shadow-sm rounded-4 p-3 review-card transition-all bg-white">
                <div class="d-flex gap-3">
                  <div class="bg-light rounded-3 d-flex align-items-center justify-content-center overflow-hidden border flex-shrink-0" style="width: 80px; height: 80px;">
                    <img v-if="review.productImage" :src="review.productImage" class="img-fluid object-fit-contain p-1" alt="product">
                    <i v-else class="bi bi-box-seam text-secondary opacity-50 fs-2"></i>
                  </div>
                  
                  <div class="flex-grow-1">
                    <div class="d-flex justify-content-between align-items-start mb-1">
                      <h6 class="fw-bold text-dark mb-0 fs-7 line-clamp-1 cursor-pointer hover-primary">{{ review.productName || 'Sản phẩm đã đánh giá' }}</h6>
                      <div class="d-flex text-warning fs-8">
                        <i v-for="n in 5" :key="n" :class="n <= review.rating ? 'bi-star-fill' : 'bi-star text-muted opacity-25'"></i>
                      </div>
                    </div>
                    
                    <span class="text-muted fs-9 fw-medium d-block mb-2">
                      <i class="bi bi-calendar3"></i> {{ formatDate(review.reviewDate) }}
                    </span>

                    <div class="bg-light-gray p-2 rounded-3 fs-8 text-dark fst-italic mb-2 border-start border-3 border-neon">
                      "{{ review.comment || 'Không có bình luận' }}"
                    </div>
                    
                    <div class="text-end mt-2">
                      <router-link :to="`/product/${review.productId}`" class="text-neon text-decoration-none fs-8 fw-bold hover-underline">
                        Xem lại sản phẩm <i class="bi bi-arrow-right"></i>
                      </router-link>
                    </div>
                  </div>
                </div>
              </div>
              
              <div v-if="!loadingReviews && filteredReviews.length === 0" class="text-center py-5 text-muted bg-white rounded-4 border">
                 <i class="bi bi-chat-square-text fs-1 opacity-25 mb-3 d-block"></i>
                 Chưa có đánh giá nào phù hợp.
              </div>
            </div>

          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

// --- STATE ĐIỀU HƯỚNG TAB ---
const currentTab = ref('profile'); // Mặc định là tab 'profile'

// ==========================================
// LOGIC TAB 1: THÔNG TIN CÁ NHÂN
// ==========================================
const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

const profileForm = ref({ fullName: '', phoneNumber: '', email: '', address: '' });
const pwdForm = ref({ currentPassword: '', newPassword: '', confirmPassword: '' });

const getAuthConfig = () => {
  const token = localStorage.getItem('jwt_token'); 
  return { headers: { Authorization: `Bearer ${token}` } };
};

const getUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try { return JSON.parse(userInfoString).userId; } catch (e) {}
  }
  return null;
};

const fetchProfile = async () => {
  const userId = getUserId();
  if (!userId) {
    alert("Vui lòng đăng nhập!"); router.push('/login'); return;
  }
  try {
    const response = await axios.get(`http://localhost:8080/api/profile/${userId}`, getAuthConfig());
    profileForm.value = response.data;
  } catch (error) {
    if (error.response?.status === 401) {
      alert("Phiên đăng nhập hết hạn!"); localStorage.clear(); router.push('/login');
    }
  }
};

const saveProfile = async () => {
  const userId = getUserId();
  if (!userId) return;
  try {
    const response = await axios.put(`http://localhost:8080/api/profile/${userId}`, profileForm.value, getAuthConfig());
    alert(response.data);
    // Cập nhật lại localStorage để header cập nhật tên
    const userInfo = JSON.parse(localStorage.getItem('user_info'));
    userInfo.fullName = profileForm.value.fullName;
    localStorage.setItem('user_info', JSON.stringify(userInfo));
    window.dispatchEvent(new Event('auth-change'));
  } catch (error) {
    alert(error.response?.data || 'Đã xảy ra lỗi!');
  }
};

const updatePassword = async () => {
  if (pwdForm.value.newPassword !== pwdForm.value.confirmPassword) {
    alert("Mật khẩu xác nhận không khớp!"); return;
  }
  try {
    const response = await axios.put(`http://localhost:8080/api/profile/${getUserId()}/password`, pwdForm.value, getAuthConfig());
    alert(response.data);
    pwdForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' };
  } catch (error) {
    alert(error.response?.data || 'Lỗi đổi mật khẩu!');
  }
};

const handleLogout = () => {
  localStorage.clear();
  window.dispatchEvent(new Event('auth-change'));
  router.push('/login');
};

// ==========================================
// LOGIC TAB 2: LỊCH SỬ ĐÁNH GIÁ
// ==========================================
const API_REVIEW_URL = 'http://localhost:8080/api/reviews';
const reviews = ref([]);
const loadingReviews = ref(false);
const filterRating = ref('Tất cả');

const fetchUserReviews = async () => {
  const userId = getUserId();
  if (!userId) return;
  loadingReviews.value = true;
  try {
    const response = await axios.get(`${API_REVIEW_URL}/user/${userId}`);
    reviews.value = response.data;
  } catch (error) {
    console.error("Lỗi tải đánh giá:", error);
  } finally {
    loadingReviews.value = false;
  }
};

const filteredReviews = computed(() => {
  if (filterRating.value === 'Tất cả') return reviews.value;
  if (filterRating.value === 'low') return reviews.value.filter(r => r.rating <= 3);
  return reviews.value.filter(r => r.rating === filterRating.value);
});

const totalReviewsCount = computed(() => reviews.value.length);
const averageRating = computed(() => {
  if (reviews.value.length === 0) return 0;
  const sum = reviews.value.reduce((acc, r) => acc + r.rating, 0);
  return (sum / reviews.value.length).toFixed(1);
});
const progressWidth = computed(() => `${(averageRating.value / 5) * 100}%`);

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
};

// --- CHẠY KHI COMPONENT LOAD ---
onMounted(() => {
  fetchProfile();
  fetchUserReviews();
});
</script>

<style scoped>
/* Typography & Utilities */
.profile-page { font-family: 'Inter', sans-serif; }
.bg-light-gray { background-color: #F8F9FA; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.cursor-pointer { cursor: pointer; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }
.bg-light-green { background-color: #f6f8f5; }
.border-neon { border-color: #00FF33 !important; }

/* Custom Container */
.narrow-container { max-width: 1000px !important; margin: 0 auto; }

/* ------ SIDEBAR STYLE ------ */
.custom-sidebar .list-group-item { transition: all 0.2s ease; }
.custom-sidebar .list-group-item:hover:not(.active-tab) { background-color: #f1f1f1; }
.active-tab {
  background-color: rgba(0, 255, 51, 0.1) !important;
  color: #00FF33 !important;
  border-left: 4px solid #00FF33 !important;
}
.active-tab i { color: #00FF33 !important; }

/* ------ FORM INPUTS STYLE ------ */
.custom-input { background-color: #F9FAFB; border: 1px solid #EAEAEA; padding: 0.6rem 1rem; border-radius: 8px; font-size: 0.85rem; }
.custom-input:focus, .input-group:focus-within .custom-input {
  background-color: #FFFFFF; border-color: #00FF33; box-shadow: none; outline: 0;
}
.input-group:focus-within .input-group-text, .input-group:focus-within input { border-color: #00FF33 !important; }

/* ------ BUTTONS STYLE ------ */
.btn-neon { background-color: #00FF33; color: #000; border: none; transition: all 0.2s ease; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(0,255,51,0.3); }
.btn-outline-neon { background-color: transparent; color: #00FF33; border: 1px solid #00FF33; transition: all 0.2s ease; }
.btn-outline-neon:hover { background-color: rgba(0, 255, 51, 0.1); color: #00FF33; }
.hover-primary:hover { color: #00FF33 !important; }
.hover-underline:hover { text-decoration: underline !important; }

/* Hiệu ứng Card Review */
.review-card { transition: 0.2s ease; }
.review-card:hover { border-color: #00FF33 !important; box-shadow: 0 5px 15px rgba(0,0,0,0.05) !important; }
</style>