<template>
  <div class="blog-detail-page bg-white pb-5 pt-3">
    <div class="container narrow-container">
      
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
          <li class="breadcrumb-item"><router-link to="/blog" class="text-muted text-decoration-none">Blog</router-link></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Tin Công Nghệ</li>
        </ol>
      </nav>

      <div class="row g-5">
        <div class="col-lg-8">
          <article class="post-content">
            <div class="mb-4">
              <span class="badge bg-neon-light text-neon fw-bold mb-2">TIN CÔNG NGHỆ</span>
              <h1 class="fw-black display-6 mb-3 lh-sm text-uppercase">
                Đánh giá chi tiết NVIDIA RTX 4090: Quái vật hiệu năng cho Game thủ & Đồ họa
              </h1>
              <div class="d-flex align-items-center gap-3 text-muted fs-8">
                <span class="d-flex align-items-center gap-1">
                  <i class="bi bi-person-circle"></i> Admin TechZone
                </span>
                <span class="d-flex align-items-center gap-1">
                  <i class="bi bi-calendar3"></i> 24 Tháng 5, 2024
                </span>
                <span class="d-flex align-items-center gap-1">
                  <i class="bi bi-clock"></i> 8 phút đọc
                </span>
              </div>
            </div>

            <div class="main-post-image rounded-4 overflow-hidden mb-4 shadow-sm">
              <img src="https://images.unsplash.com/photo-1695056721201-081467bd18de?q=80&w=2070&auto=format&fit=crop" class="w-100 object-fit-cover" alt="RTX 4090">
            </div>

            <div class="quote-box border-start border-neon border-4 ps-4 py-1 mb-4 bg-light-gray rounded-end-3">
              <p class="fst-italic text-dark m-0 fs-7 lh-lg">
                NVIDIA RTX 4090 không chỉ là một chiếc card đồ họa mới; nó là một bước nhảy vọt về công nghệ mà chúng ta chưa từng thấy trong một thập kỷ qua. Nhưng liệu nó có xứng đáng với mức giá "khủng"?
              </p>
            </div>

            <div class="post-text fs-7 lh-lg text-secondary">
              <h4 class="fw-bold text-dark mt-4 mb-3">1. Thông số kỹ thuật ấn tượng</h4>
              <p>
                Được xây dựng trên kiến trúc Ada Lovelace mới nhất, RTX 4090 sở hữu 16.384 nhân CUDA và 24GB bộ nhớ GDDR6X. Đây là những con số đủ sức cân bằng mọi tựa game AAA ở độ phân giải 4K với mức thiết lập cao nhất.
              </p>

              <div class="row g-3 my-4">
                <div class="col-md-6">
                  <div class="pros-box border rounded-3 p-3 h-100 bg-white shadow-sm">
                    <h6 class="fw-bold text-success mb-3"><i class="bi bi-plus-circle-fill me-2"></i>Ưu điểm</h6>
                    <ul class="list-unstyled d-flex flex-column gap-2 fs-8">
                      <li>• Hiệu năng 4K vô đối trên thị trường.</li>
                      <li>• DLSS 3.0 mang lại tốc độ khung hình vượt trội.</li>
                      <li>• Khả năng tản nhiệt tốt, vận hành êm ái.</li>
                    </ul>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="cons-box border rounded-3 p-3 h-100 bg-white shadow-sm">
                    <h6 class="fw-bold text-danger mb-3"><i class="bi bi-dash-circle-fill me-2"></i>Nhược điểm</h6>
                    <ul class="list-unstyled d-flex flex-column gap-2 fs-8">
                      <li>• Giá thành cực kỳ đắt đỏ.</li>
                      <li>• Kích thước quá lớn, kén chọn vỏ case.</li>
                      <li>• Tiêu thụ điện năng lên đến 450W+.</li>
                    </ul>
                  </div>
                </div>
              </div>

              <h4 class="fw-bold text-dark mt-4 mb-3">2. Trải nghiệm Gaming thực tế</h4>
              <p>
                Trong thử nghiệm với Cyberpunk 2077, khi bật tính năng Ray Tracing Overdrive và DLSS 3, chúng tôi ghi nhận được tốc độ khung hình ổn định ở mức 120 FPS. Một con số không tưởng đối với một tựa game nặng ký như vậy.
              </p>
              
              <div class="text-center my-4">
                <img src="https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=2070&auto=format&fit=crop" class="img-fluid rounded-3 shadow-sm mb-2" alt="Gaming Setup">
                <p class="text-muted fst-italic fs-9">Hệ thống PC thử nghiệm tại TechZone Studio</p>
              </div>

              <h4 class="fw-bold text-dark mt-4 mb-3">3. Lời kết</h4>
              <p>
                Nếu bạn là một người dùng chuyên nghiệp làm việc với 3D Render, AI hoặc là một hardcore gamer muốn trải nghiệm đỉnh cao nhất thì RTX 4090 là sự lựa chọn duy nhất ở thời điểm hiện tại.
              </p>
            </div>

            <div class="d-flex flex-wrap gap-2 mt-5 pt-3 border-top">
              <span class="fs-8 fw-bold text-dark me-2">TAGS:</span>
              <a href="#" class="btn btn-sm btn-light fs-9 rounded-1 text-muted">RTX 4090</a>
              <a href="#" class="btn btn-sm btn-light fs-9 rounded-1 text-muted">NVIDIA</a>
              <a href="#" class="btn btn-sm btn-light fs-9 rounded-1 text-muted">PC GAMING</a>
              <a href="#" class="btn btn-sm btn-light fs-9 rounded-1 text-muted">HARDWARE</a>
            </div>
          </article>
        </div>

        <div class="col-lg-4">
          <div class="sticky-lg-top" style="top: 20px; z-index: 1;">
            
            <div class="sidebar-widget border rounded-4 p-4 mb-4 bg-white shadow-sm">
              <h6 class="fw-black text-uppercase border-start border-neon border-3 ps-3 mb-4">Bài viết xu hướng</h6>
              <div class="trending-posts d-flex flex-column gap-3">
                <div v-for="i in 3" :key="i" class="trending-item d-flex gap-3 align-items-center">
                  <span class="trending-num display-6 fw-black opacity-10 text-dark">0{{ i }}</span>
                  <a href="#" class="text-dark fw-bold fs-8 text-decoration-none hover-neon line-clamp-2 lh-sm">
                    {{ i === 1 ? 'Mẹo tối ưu Windows 11 cho Gaming 2024' : i === 2 ? 'Đánh giá nhanh Bàn phím Cơ thế hệ mới' : 'Build PC 20 Triệu chơi mượt AAA' }}
                  </a>
                </div>
              </div>
            </div>

            <div class="sidebar-widget banner-widget rounded-4 overflow-hidden position-relative mb-4 p-4 shadow-sm" style="height: 250px;">
              <div class="banner-overlay position-absolute top-0 start-0 w-100 h-100 bg-dark opacity-75"></div>
              <div class="position-relative z-1 d-flex flex-column justify-content-center h-100 text-center">
                <h5 class="fw-black text-white text-uppercase mb-2">Build PC Đỉnh Cao</h5>
                <p class="text-white-50 fs-9 mb-3">Nhận ngay ưu đãi 15% tại TechZone</p>
                <button class="btn btn-neon fw-bold fs-9 rounded-1 py-2 mx-auto" style="width: 120px;">BUILD NGAY</button>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div class="related-posts-section mt-5 pt-5 border-top">
        <div class="d-flex align-items-center justify-content-between mb-4">
          <h4 class="fw-black text-uppercase m-0">Bài viết liên quan</h4>
          <div class="related-line flex-grow-1 bg-neon opacity-25 ms-4" style="height: 1px;"></div>
        </div>
        
        <div class="row g-4">
          <div class="col-md-4" v-for="post in relatedPosts" :key="post.id">
            <div class="card border-0 h-100 bg-white rounded-3 shadow-sm custom-card-hover overflow-hidden">
              <img :src="post.img" class="card-img-top object-fit-cover" style="height: 160px;" alt="Related Post">
              <div class="card-body p-3">
                <span class="badge bg-neon text-dark fw-bold fs-9 mb-2">{{ post.badge }}</span>
                <h6 class="fw-bold mb-2 fs-7 lh-base line-clamp-2 text-dark">{{ post.title }}</h6>
                <small class="text-muted fs-9">{{ post.date }}</small>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Dữ liệu bài viết liên quan
const relatedPosts = ref([
  {
    id: 1,
    badge: 'TƯ VẤN',
    title: 'Top 5 mainboard tốt nhất dành cho Intel Core i9-14900K',
    date: '3 ngày trước',
    img: 'https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?w=500'
  },
  {
    id: 2,
    badge: 'HƯỚNG DẪN',
    title: 'Hướng dẫn lắp tản nhiệt nước Custom cho người mới bắt đầu',
    date: '5 ngày trước',
    img: 'https://images.unsplash.com/photo-1587831990711-23ca6441447b?w=500'
  },
  {
    id: 3,
    badge: 'CẢNH BÁO',
    title: 'Lưu ý khi chọn nguồn (PSU) cho dàn máy RTX 40-Series',
    date: '1 tuần trước',
    img: 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=500'
  }
]);
</script>

<style scoped>
/* Typography & Reset */
.blog-detail-page { font-family: 'Inter', sans-serif; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }

/* Biến màu chủ đạo */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }
.border-neon { border-color: #00FF33 !important; }
.bg-light-gray { background-color: #F8F9FA; }

/* Layout Narrow */
.narrow-container {
  max-width: 1000px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Sidebar Trending Items */
.trending-num {
  line-height: 1;
  transition: color 0.3s;
}
.trending-item:hover .trending-num {
  color: #00FF33;
  opacity: 0.8 !important;
}
.hover-neon:hover {
  color: #00FF33 !important;
}

/* Banner Widget */
.banner-widget {
  background-image: url('https://images.unsplash.com/photo-1547082299-de196ea013d6?w=500');
  background-size: cover;
  background-position: center;
}

/* Card Hover */
.custom-card-hover {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}
.custom-card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
}

/* Line Clamp */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}

/* Nút Neon */
.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover { background-color: #00cc29; }
</style>