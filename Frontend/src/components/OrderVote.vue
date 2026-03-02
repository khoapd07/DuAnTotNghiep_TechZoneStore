<template>
  <main class="review-page py-5 bg-white min-vh-100">
    <div class="container" style="max-width: 1000px;">
      
      <div class="mb-5">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-2 fs-7 fw-medium">
            <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none hover-primary">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none hover-primary">Tài khoản</a></li>
            <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Lịch sử đánh giá</li>
          </ol>
        </nav>
        <h1 class="fw-black text-uppercase text-dark mb-2" style="font-size: 2.2rem; letter-spacing: -0.5px;">
          Lịch sử đánh giá <span class="text-neon">của tôi</span>
        </h1>
        <p class="text-muted fs-7 max-w-75">
          Xem lại toàn bộ lịch sử các sản phẩm công nghệ bạn đã trải nghiệm và chia sẻ cảm nhận với cộng đồng TechZone.
        </p>
      </div>

      <div class="row g-4 mb-5">
        <div class="col-lg-3">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 bg-light-green">
            <span class="text-muted fw-bold fs-8 text-uppercase mb-1">Tổng đánh giá</span>
            <div class="d-flex align-items-baseline gap-2 mb-3">
              <h2 class="fw-black text-dark m-0 fs-1">{{ totalReviewsCount }}</h2>
              <span class="text-neon fw-bold fs-8">bài viết</span>
            </div>
            <div class="border-top pt-3 mt-auto">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted fs-8 fw-bold">Trung bình</span>
                <span class="fw-black text-dark fs-7">{{ averageRating }}/5</span>
              </div>
              <div class="progress" style="height: 6px;">
                <div class="progress-bar bg-neon" :style="{ width: progressWidth }"></div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-lg-9 d-flex flex-column justify-content-end">
          <div class="d-flex flex-wrap align-items-center gap-2">
            <button @click="filterRating = 'Tất cả'" class="btn fw-bold fs-7 rounded-pill px-4 py-2 shadow-sm" :class="filterRating === 'Tất cả' ? 'btn-neon' : 'btn-light'">
              Tất cả
            </button>
            <button @click="filterRating = 5" class="btn btn-light border-0 fw-bold fs-7 rounded-pill px-4 py-2 d-flex align-items-center gap-1 hover-gray">
              5 sao <i class="bi bi-star-fill text-warning fs-8"></i>
            </button>
            <button @click="filterRating = 4" class="btn btn-light border-0 fw-bold fs-7 rounded-pill px-4 py-2 d-flex align-items-center gap-1 hover-gray">
              4 sao <i class="bi bi-star-fill text-warning fs-8"></i>
            </button>
            <button @click="filterRating = 'low'" class="btn btn-light border-0 fw-bold fs-7 rounded-pill px-4 py-2 hover-gray">
              3 sao trở xuống
            </button>
            <div class="ms-auto d-none d-md-block">
              <button class="btn btn-link text-muted text-decoration-none fw-bold fs-7 d-flex align-items-center gap-2 hover-primary shadow-none">
                <i class="bi bi-filter-left fs-5"></i> Mới nhất trước
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex flex-column gap-4">
        <div v-if="loading" class="text-center py-5">
           <div class="spinner-border text-neon" role="status"></div>
        </div>

        <div v-for="review in filteredReviews" :key="review.reviewId" class="card border border-light-subtle shadow-sm rounded-4 p-4 review-card transition-all">
          <div class="row g-4">
            <div class="col-sm-auto">
              <div class="bg-light rounded-4 d-flex align-items-center justify-content-center overflow-hidden border img-wrapper" style="width: 120px; height: 120px;">
                <i :class="getProductIcon(review.categoryName)" class="text-secondary opacity-50" style="font-size: 3rem;"></i>
              </div>
            </div>
            
            <div class="col-sm d-flex flex-column">
              <div class="d-flex flex-wrap justify-content-between align-items-start gap-2 mb-3">
                <div>
                  <h5 class="fw-bold text-dark mb-1 review-title cursor-pointer">{{ review.productName }}</h5>
                  <span class="text-muted fs-8 fw-medium d-flex align-items-center gap-1">
                    <i class="bi bi-calendar3"></i> Đánh giá ngày: {{ formatDate(review.reviewDate) }}
                  </span>
                </div>
                <div class="badge rounded-pill d-flex align-items-center gap-1 px-3 py-2 border" :class="review.rating === 5 ? 'border-neon bg-neon-light text-neon' : 'bg-light text-dark'">
                  <span class="fw-bold fs-7">{{ review.rating }}/5</span>
                  <i class="bi bi-star-fill" :class="review.rating === 5 ? 'text-neon' : 'text-warning'"></i>
                </div>
              </div>

              <div class="border-start border-3 ps-3 mb-3" :class="review.rating === 5 ? 'border-neon' : 'border-secondary'">
                <p class="text-dark fs-7 m-0 fst-italic lh-lg">"{{ review.comment }}"</p>
              </div>

              <div class="d-flex align-items-center gap-3 mt-auto border-top pt-3">
                <button class="btn btn-link text-muted p-0 text-decoration-none fs-8 fw-bold d-flex align-items-center gap-1 hover-primary shadow-none">
                  <i class="bi bi-pencil-square"></i> Sửa đánh giá
                </button>
                <span class="text-muted">|</span>
                <a href="#" class="text-neon text-decoration-none fs-8 fw-bold hover-underline">Xem sản phẩm</a>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="!loading && filteredReviews.length === 0" class="text-center py-5 text-muted">
           Chưa có đánh giá nào phù hợp.
        </div>
      </div>

      <div class="d-flex justify-content-center mt-5 gap-2">
        <button class="btn btn-light border px-3 text-muted rounded-3"><i class="bi bi-chevron-left"></i></button>
        <button class="btn btn-neon fw-bold px-3 rounded-3 text-dark">1</button>
        <button class="btn btn-light border fw-bold px-3 rounded-3 text-dark hover-gray">2</button>
        <button class="btn btn-light border fw-bold px-3 rounded-3 text-dark hover-gray">3</button>
        <button class="btn btn-light border px-3 text-muted rounded-3 hover-gray"><i class="bi bi-chevron-right"></i></button>
      </div>

    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// --- 1. CONFIG & STATE ---
const CURRENT_USER_ID = 1; 
const API_URL = 'http://localhost:8080/api/reviews';
const reviews = ref([]);
const loading = ref(true);
const filterRating = ref('Tất cả');

// --- 2. FETCH DATA FROM BACKEND ---
const fetchUserReviews = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${API_URL}/user/${CURRENT_USER_ID}`);
    reviews.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải lịch sử đánh giá:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchUserReviews();
});

// --- 3. FILTER LOGIC ---
const filteredReviews = computed(() => {
  if (filterRating.value === 'Tất cả') return reviews.value;
  if (filterRating.value === 'low') return reviews.value.filter(r => r.rating <= 3);
  return reviews.value.filter(r => r.rating === filterRating.value);
});

// --- 4. STATS CALCULATIONS ---
const totalReviewsCount = computed(() => reviews.value.length);

const averageRating = computed(() => {
  if (reviews.value.length === 0) return 0;
  const sum = reviews.value.reduce((acc, r) => acc + r.rating, 0);
  return (sum / reviews.value.length).toFixed(1);
});

const progressWidth = computed(() => {
  const percentage = (averageRating.value / 5) * 100;
  return `${percentage}%`;
});

// --- 5. HELPERS ---
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  });
};

const getProductIcon = (categoryName) => {
  if (!categoryName) return 'bi-box-seam';
  const name = categoryName.toLowerCase();
  if (name.includes('laptop')) return 'bi-laptop';
  if (name.includes('phím') || name.includes('keyboard')) return 'bi-keyboard';
  if (name.includes('chuột') || name.includes('mouse')) return 'bi-mouse3';
  if (name.includes('tai nghe') || name.includes('headset')) return 'bi-headset';
  return 'bi-cpu';
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&family=Space+Grotesk:wght@700&display=swap');

.review-page {
  font-family: 'Inter', system-ui, sans-serif;
}

/* Các class Utilities */
.fw-black { font-weight: 900; font-family: 'Space Grotesk', sans-serif; }
.fs-7 { font-size: 0.9rem; }
.fs-8 { font-size: 0.8rem; }
.max-w-75 { max-width: 75%; }
.cursor-pointer { cursor: pointer; }

/* Custom Colors & Buttons */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; border-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.1) !important; }
.border-neon { border-color: #00FF33 !important; }
.bg-light-green { background-color: #f6f8f5; }

/* Nút bấm */
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s all; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); box-shadow: 0 4px 15px rgba(0,255,51,0.3) !important; }
.hover-gray:hover { background-color: #e9ecef !important; }
.hover-primary:hover { color: #00FF33 !important; }
.hover-underline:hover { text-decoration: underline !important; }

/* Hiệu ứng Card Review */
.review-card { transition: 0.3s ease; }
.review-card:hover { border-color: rgba(0, 255, 51, 0.5) !important; box-shadow: 0 10px 25px rgba(0,0,0,0.05) !important; transform: translateY(-2px); }
.review-title { transition: 0.2s; }
.review-title:hover { color: #00FF33 !important; }

/* Hình ảnh placeholder (Icon) */
.img-wrapper i { transition: 0.3s ease; }
.review-card:hover .img-wrapper i { transform: scale(1.1); color: #00FF33 !important; opacity: 1 !important; }

/* Responsive */
@media (max-width: 768px) {
  .max-w-75 { max-width: 100%; }
  .img-wrapper { width: 100% !important; height: 160px !important; }
}
</style>