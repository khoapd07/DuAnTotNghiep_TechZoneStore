<template>
  <div class="home-page bg-light-gray pb-5">
    
    <section class="container mt-4 mb-5">
      <div id="heroSlideshow" class="carousel slide carousel-fade rounded-4 overflow-hidden shadow-lg" data-bs-ride="carousel">
        <div class="carousel-indicators">
          <button type="button" data-bs-target="#heroSlideshow" data-bs-slide-to="0" class="active" aria-current="true"></button>
          <button type="button" data-bs-target="#heroSlideshow" data-bs-slide-to="1"></button>
          <button type="button" data-bs-target="#heroSlideshow" data-bs-slide-to="2"></button>
        </div>

        <div class="carousel-inner hero-carousel-inner">
          <div class="carousel-item active h-100" data-bs-interval="5000">
            <img src="https://images.unsplash.com/photo-1593640408182-31c70c8268f5?q=80&w=2042&auto=format&fit=crop" class="d-block w-100 h-100 object-fit-cover absolute-img" alt="Esports Gaming 1">
            <div class="hero-overlay position-absolute top-0 start-0 w-100 h-100"></div>
            <div class="carousel-caption d-flex flex-column justify-content-center h-100 text-start px-md-5">
              <div class="mb-3">
                <span class="badge bg-neon text-dark fw-bold px-3 py-2 rounded-pill">NEW ERA OF GAMING</span>
              </div>
              <h1 class="display-4 fw-black text-uppercase mb-3">
                NÂNG TẦM TRẢI NGHIỆM <span class="text-neon">ESPORTS</span>
              </h1>
              <p class="fs-5 text-light opacity-75 mb-4 col-md-8">
                Khám phá bộ sưu tập gear gaming thế hệ mới với hiệu năng đột phá và thiết kế tối tân.
              </p>
              <div class="d-flex gap-3">
                <button class="btn btn-neon fw-bold px-4 py-2">MUA NGAY</button>
                <button class="btn btn-outline-light fw-bold px-4 py-2">XEM CATALOGUE</button>
              </div>
            </div>
          </div>

          <div class="carousel-item h-100" data-bs-interval="5000">
            <img src="https://images.unsplash.com/photo-1600861194942-f883de0dfe96?q=80&w=2069&auto=format&fit=crop" class="d-block w-100 h-100 object-fit-cover absolute-img" alt="Esports Gaming 2">
            <div class="hero-overlay position-absolute top-0 start-0 w-100 h-100"></div>
            <div class="carousel-caption d-flex flex-column justify-content-center h-100 text-start px-md-5">
              <div class="mb-3">
                <span class="badge bg-danger text-white fw-bold px-3 py-2 rounded-pill">HOT DEALS 2026</span>
              </div>
              <h1 class="display-4 fw-black text-uppercase mb-3">
                BUILD PC CHUẨN <span class="text-danger">GAMER</span>
              </h1>
              <p class="fs-5 text-light opacity-75 mb-4 col-md-8">
                Linh kiện chính hãng, bảo hành tận nơi. Lắp ráp nhanh chóng bởi đội ngũ chuyên gia TechZone.
              </p>
              <div class="d-flex gap-3">
                <button class="btn btn-danger fw-bold px-4 py-2">BUILD NGAY</button>
              </div>
            </div>
          </div>

          <div class="carousel-item h-100" data-bs-interval="5000">
            <img src="https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=2070&auto=format&fit=crop" class="d-block w-100 h-100 object-fit-cover absolute-img" alt="Esports Gaming 3">
            <div class="hero-overlay position-absolute top-0 start-0 w-100 h-100"></div>
            <div class="carousel-caption d-flex flex-column justify-content-center h-100 text-start px-md-5">
              <div class="mb-3">
                <span class="badge bg-primary text-white fw-bold px-3 py-2 rounded-pill">GAMING GEAR</span>
              </div>
              <h1 class="display-4 fw-black text-uppercase mb-3">
                PHỤ KIỆN TỐI THƯỢNG <span class="text-primary">PRO</span>
              </h1>
              <p class="fs-5 text-light opacity-75 mb-4 col-md-8">
                Bàn phím cơ, chuột không dây siêu nhẹ, tai nghe chống ồn. Đưa kỹ năng của bạn lên tầm cao mới.
              </p>
              <div class="d-flex gap-3">
                <button class="btn btn-primary fw-bold px-4 py-2">SĂN SALE</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container mb-5 border-bottom pb-4">
      <div class="row g-4 justify-content-between">
        <div class="col-md-4 d-flex align-items-center gap-3">
          <i class="bi bi-truck fs-2 text-neon"></i>
          <div>
            <h6 class="fw-bold mb-1">Giao hàng siêu tốc</h6>
            <small class="text-muted">Miễn phí cho đơn từ 2.000.000đ</small>
          </div>
        </div>
        <div class="col-md-4 d-flex align-items-center gap-3">
          <i class="bi bi-shield-check fs-2 text-neon"></i>
          <div>
            <h6 class="fw-bold mb-1">Bảo hành chính hãng</h6>
            <small class="text-muted">Cam kết 100% linh kiện chất lượng</small>
          </div>
        </div>
        <div class="col-md-4 d-flex align-items-center gap-3">
          <i class="bi bi-headset fs-2 text-neon"></i>
          <div>
            <h6 class="fw-bold mb-1">Hỗ trợ 24/7</h6>
            <small class="text-muted">Đội ngũ kỹ thuật chuyên nghiệp</small>
          </div>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="d-flex justify-content-between align-items-end mb-4 border-bottom border-dark border-2 pb-2">
        <h3 class="fw-bold mb-0 text-uppercase">SẢN PHẨM NỔI BẬT</h3>
        <a href="#" class="text-neon text-decoration-none fw-bold small">XEM TẤT CẢ</a>
      </div>

      <div class="row g-4">
        <div class="col-md-6">
          <div class="card h-100 flex-row border-0 shadow-sm overflow-hidden bg-white rounded-3">
            <div class="w-50 bg-dark">
              <img src="https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=500" class="w-100 h-100 object-fit-cover" alt="Laptop">
            </div>
            <div class="card-body w-50 p-4 d-flex flex-column">
              <h5 class="fw-bold mb-2">LAPTOP G-PRO TITAN V3</h5>
              <p class="text-muted small mb-auto">RTX 4090, Intel Core i9-14900HX, 64GB RAM. Quái vật hiệu năng cho mọi tựa game AAA.</p>
              <h4 class="text-neon fw-bold my-3">$2,499.00</h4>
              <button class="btn btn-dark w-100 fw-bold border-neon text-neon">MUA NGAY</button>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card h-100 flex-row border-0 shadow-sm overflow-hidden bg-white rounded-3">
            <div class="w-50 bg-dark">
              <img src="https://images.unsplash.com/photo-1587831990711-23ca6441447b?w=500" class="w-100 h-100 object-fit-cover" alt="PC Build">
            </div>
            <div class="card-body w-50 p-4 d-flex flex-column">
              <h5 class="fw-bold mb-2">PC BUILD: NEON GHOST</h5>
              <p class="text-muted small mb-auto">Custom water cooling, LED ARGB Sync, linh kiện cao cấp từ TechZone.</p>
              <h4 class="text-neon fw-bold my-3">$3,850.00</h4>
              <button class="btn btn-dark w-100 fw-bold border-neon text-neon">MUA NGAY</button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="d-flex justify-content-between align-items-end mb-4 border-bottom border-dark border-2 pb-2">
        <h3 class="fw-bold mb-0 text-uppercase">SẢN PHẨM MỚI</h3>
        <a href="#" class="text-neon text-decoration-none fw-bold small">XEM TẤT CẢ</a>
      </div>

      <div class="row g-4">
        <div class="col-md-3" v-for="i in 4" :key="'new-'+i">
          <div class="card border-0 shadow-sm h-100 bg-white rounded-3 position-relative p-2 d-flex flex-column">
            <span class="badge bg-neon text-dark position-absolute top-0 start-0 m-3 z-1">NEW</span>
            <div class="bg-light rounded text-center py-4 mb-2 flex-grow-1 d-flex align-items-center justify-content-center">
               <img src="https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?w=300" class="img-fluid" style="height: 120px; object-fit: contain;">
            </div>
            <div class="card-body p-2 d-flex flex-column">
              <h6 class="fw-bold mb-1">Monitor UltraWide 49" QLED</h6>
              <small class="text-muted mb-3 line-clamp-1">Samsung Odyssey Series</small>
              <h5 class="fw-bold mb-3 text-neon">$1,199</h5>
              <div class="d-flex gap-2 mt-auto">
                <button class="btn btn-dark flex-grow-1 fw-bold fs-7">MUA</button>
                <button class="btn btn-outline-dark rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;" title="Thêm vào giỏ hàng">
                  <i class="bi bi-cart-plus fs-5"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="bg-neon rounded-4 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center shadow-sm">
        <div class="text-dark mb-2 mb-md-0">
          <h2 class="fw-black text-uppercase mb-1 fs-5">FLASH SALE CUỐI TUẦN!</h2>
          <p class="m-0 fw-medium small" style="font-size: 13px;">Ưu đãi lên đến 40% cho các dòng linh kiện PC.</p>
        </div>
        <div class="d-flex align-items-center gap-2">
          <div class="d-flex gap-1 fw-bold fs-5">
            <div class="bg-dark text-neon rounded px-2 py-1 lh-1">02</div><span class="align-self-center">:</span>
            <div class="bg-dark text-neon rounded px-2 py-1 lh-1">14</div><span class="align-self-center">:</span>
            <div class="bg-dark text-neon rounded px-2 py-1 lh-1">56</div>
          </div>
          <button class="btn btn-dark fw-bold px-3 py-2 ms-2 fs-7">SĂN DEAL NGAY</button>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="d-flex justify-content-between align-items-end mb-4 border-bottom border-dark border-2 pb-2">
        <h3 class="fw-bold mb-0 text-uppercase">SẢN PHẨM GIẢM GIÁ</h3>
        <a href="#" class="text-neon text-decoration-none fw-bold small">XEM TẤT CẢ</a>
      </div>

      <div class="row g-4">
        <div class="col-md-3" v-for="i in 4" :key="'sale-'+i">
          <div class="card border-0 shadow-sm h-100 bg-white rounded-3 position-relative p-2 d-flex flex-column">
            <span class="badge bg-danger position-absolute top-0 end-0 m-3 z-1">-30%</span>
            <div class="bg-light rounded text-center py-4 mb-2 flex-grow-1 d-flex align-items-center justify-content-center">
               <img src="https://images.unsplash.com/photo-1591488320449-011701bb6704?w=300" class="img-fluid" style="height: 120px; object-fit: contain;">
            </div>
            <div class="card-body p-2 d-flex flex-column text-center">
              <h6 class="fw-bold mb-2">RTX 3060 Ti Eagle 8G</h6>
              <div class="d-flex justify-content-center gap-2 mb-3">
                <h5 class="fw-bold text-neon mb-0">$589</h5>
                <span class="text-muted text-decoration-line-through small align-self-end">$899</span>
              </div>
              <div class="d-flex gap-2 mt-auto">
                <button class="btn btn-dark flex-grow-1 fw-bold fs-7">MUA</button>
                <button class="btn btn-outline-dark rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;" title="Thêm vào giỏ hàng">
                  <i class="bi bi-cart-plus fs-5"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Logic gọi API backend Spring Boot 
</script>

<style scoped>
/* Reset & Typography */
.home-page {
  font-family: 'Inter', sans-serif;
}

.bg-light-gray {
  background-color: #F8F9FA;
}

.fw-black {
  font-weight: 900;
}

.fs-7 {
  font-size: 0.85rem;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}

/* Các biến màu chủ đạo từ Figma */
.text-neon {
  color: #00FF33 !important;
}

.bg-neon {
  background-color: #00FF33 !important;
}

.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}

.btn-neon:hover {
  background-color: #00cc29;
  color: #000;
  transform: translateY(-2px);
}

.border-neon {
  border: 1px solid #00FF33 !important;
}

/* Nút giỏ hàng tùy chỉnh */
.btn-outline-dark {
  border-color: #333;
  color: #333;
  transition: all 0.2s;
}
.btn-outline-dark:hover {
  background-color: #00FF33;
  border-color: #00FF33;
  color: #000;
}

/* Custom Container (Bóp nhỏ 2 bên) */
.narrow-container {
  max-width: 960px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Custom Carousel cho Hero Banner */
.hero-carousel-inner {
  height: 500px;
}

.absolute-img {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}

.hero-overlay {
  background: linear-gradient(90deg, rgba(0,0,0,0.9) 0%, rgba(0,0,0,0.6) 50%, rgba(0,0,0,0.1) 100%);
  z-index: 1;
}

.carousel-caption {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 2;
  padding-top: 0;
  padding-bottom: 0;
}

/* Card Hover Effect */
.card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.05) !important;
}
</style>