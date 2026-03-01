<template>
  <footer class="bg-dark-custom text-white py-3 mt-auto">
    <div class="container narrow-container">
      <div class="row g-3 pt-2"> <div class="col-lg-4 col-md-6 pe-md-5">
          <router-link class="text-decoration-none d-inline-block mb-2" to="/">
            <h3 class="fw-black text-white m-0 d-flex align-items-center gap-1">
              TECHZONE <span class="text-neon">//</span>
            </h3>
          </router-link>
          <p class="text-light fs-7 lh-base mb-2 pe-md-4">
            Điểm đến tin cậy cho cộng đồng đam mê công nghệ và game thủ. Chúng tôi cung cấp những đánh giá khách quan và cập nhật tin tức công nghệ nhanh nhất.
          </p>
        </div>

        <div class="col-lg-2 col-md-6">
          <h6 class="fw-bold text-uppercase mb-3 fs-7 text-white">DANH MỤC</h6> <ul class="list-unstyled d-flex flex-column gap-2 fs-7 mb-0"> <li><a href="#" class="text-white text-decoration-none footer-link">Review phần cứng</a></li>
            <li><a href="#" class="text-white text-decoration-none footer-link">Tin tức công nghệ</a></li>
            <li><a href="#" class="text-white text-decoration-none footer-link">Thủ thuật Game</a></li>
            <li><a href="#" class="text-white text-decoration-none footer-link">Build PC</a></li>
          </ul>
        </div>

        <div class="col-lg-3 col-md-6 mt-4 mt-lg-0">
          <h6 class="fw-bold text-uppercase mb-3 fs-7 text-white">HỖ TRỢ</h6>
          <ul class="list-unstyled d-flex flex-column gap-2 fs-7 mb-0">
            <li><a href="#" class="text-white text-decoration-none footer-link">Liên hệ quảng cáo</a></li>
            <li><a href="#" class="text-white text-decoration-none footer-link">Chính sách bảo mật</a></li>
            <li><a href="#" class="text-white text-decoration-none footer-link">Điều khoản dịch vụ</a></li>
          </ul>
        </div>

        <div class="col-lg-3 col-md-6 mt-4 mt-lg-0">
          <h6 class="fw-bold text-uppercase mb-3 fs-7 text-white">THEO DÕI</h6>
          <div class="d-flex gap-3">
            <a href="#" class="social-icon bg-neon-dark text-white rounded-circle d-flex align-items-center justify-content-center text-decoration-none">
              <i class="bi bi-facebook fs-6"></i>
            </a>
            <a href="#" class="social-icon bg-neon-dark text-white rounded-circle d-flex align-items-center justify-content-center text-decoration-none">
              <i class="bi bi-youtube fs-6"></i>
            </a>
          </div>
        </div>

      </div>

      <div class="d-flex flex-column flex-md-row justify-content-between align-items-center border-top border-secondary pt-3 mt-3 pb-2 fs-7 text-light">
        <span>© 2026 TechZone Media. All rights reserved.</span>
        <span class="mt-2 mt-md-0">Designed for Gaming Excellence</span>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.text-neon { color: #00FF33 !important; }

/* Custom Dark Background */
.bg-dark-custom {
  background-color: #161616;
}

/* Background cho các icon social */
.bg-neon-dark {
  background-color: #0b3d16;
  transition: all 0.3s ease;
}

.social-icon {
  width: 35px;
  height: 35px;
}

.social-icon:hover {
  background-color: #00FF33;
  color: #000 !important;
  transform: translateY(-3px);
}

/* Custom Container */
.narrow-container {
  max-width: 1200px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Link Hover: Giữ hiệu ứng chuyển màu neon */
.footer-link {
  transition: color 0.2s ease;
  opacity: 0.8; 
}
.footer-link:hover {
  color: #00FF33 !important;
  opacity: 1; 
}

.border-secondary {
  border-color: #333333 !important; 
}
</style>