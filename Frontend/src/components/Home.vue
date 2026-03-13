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
        <router-link to="/products" @click="scrollToTop" class="text-neon text-decoration-none fw-bold small">XEM TẤT CẢ</router-link>
      </div>

      <div class="row g-4">
        <div class="col-md-6" v-for="product in featuredProducts" :key="'featured-' + product.productId">
          <div class="card h-100 flex-row border-0 shadow-sm overflow-hidden bg-white rounded-3">
            <div class="w-50 bg-dark">
              <img :src="product.imageUrl || 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=500'" class="w-100 h-100 object-fit-cover" :alt="product.name">
            </div>
            <div class="card-body w-50 p-4 d-flex flex-column">
              <h5 class="fw-bold mb-2 line-clamp-1" :title="product.name">{{ product.name }}</h5>
              <p class="text-muted small mb-auto line-clamp-2">{{ product.description || 'Sản phẩm cao cấp tại TechZone.' }}</p>
              <h4 class="text-neon fw-bold my-3">
                {{ formatCurrency(product.salePrice && product.salePrice > 0 ? product.salePrice : product.price) }}
              </h4>
              <button class="btn btn-dark w-100 fw-bold border-neon text-neon">MUA NGAY</button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="d-flex justify-content-between align-items-end mb-4 border-bottom border-dark border-2 pb-2">
        <h3 class="fw-bold mb-0 text-uppercase">SẢN PHẨM MỚI</h3>
        <router-link to="/products" @click="scrollToTop" class="text-neon text-decoration-none fw-bold small">XEM TẤT CẢ</router-link> 
      </div>

      <div class="row g-4">
        <div class="col-md-3" v-for="product in latestProducts" :key="'new-' + product.productId">
          <div class="card border-0 shadow-sm h-100 bg-white rounded-3 position-relative p-2 d-flex flex-column">
            <span class="badge bg-neon text-dark position-absolute top-0 start-0 m-3 z-1">NEW</span>
            <div class="bg-light rounded text-center py-4 mb-2 flex-grow-1 d-flex align-items-center justify-content-center">
               <img :src="product.imageUrl || 'https://via.placeholder.com/300'" class="img-fluid" style="height: 120px; object-fit: contain;">
            </div>
            <div class="card-body p-2 d-flex flex-column">
              <h6 class="fw-bold mb-1 line-clamp-1" :title="product.name">{{ product.name }}</h6>
              <small class="text-muted mb-3 line-clamp-1">{{ product.categoryName || 'Sản phẩm mới' }}</small>
              
              <h5 class="fw-bold mb-3 text-neon">
                {{ formatCurrency(product.salePrice && product.salePrice > 0 ? product.salePrice : product.price) }}
              </h5>

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
          <h2 class="fw-black text-uppercase mb-1 fs-5">
            {{ flashSaleVoucher?.name || 'FLASH SALE CUỐI TUẦN!' }}
          </h2>
          <p class="m-0 fw-medium small" style="font-size: 13px;">
            Ưu đãi giảm ngay <span class="fw-bold text-danger">{{ formatCurrency(flashSaleVoucher?.discountAmount || 1000000) }}</span> cho đơn hàng linh kiện.
          </p>
        </div>
        <div class="d-flex align-items-center gap-2 mt-3 mt-md-0">
          <div class="fw-bold text-dark me-2 small">
            Hôm nay: <span class="bg-dark text-neon px-2 py-1 rounded d-inline-block">{{ currentTime }}</span>
          </div>
          <button @click="claimFlashSale" class="btn btn-dark fw-bold px-3 py-2 fs-7 text-nowrap">SĂN VOUCHER NGAY</button>
        </div>
      </div>
    </section>

    <section class="container narrow-container mb-5">
      <div class="d-flex justify-content-between align-items-end mb-4 border-bottom border-dark border-2 pb-2">
        <h3 class="fw-bold mb-0 text-uppercase">SẢN PHẨM GIẢM GIÁ</h3>
        </div>

      <div class="position-relative">
        
        <button @click="scrollSale('-')" 
                class="btn btn-light border shadow rounded-circle position-absolute top-50 start-0 translate-middle-y z-3 d-flex align-items-center justify-content-center hover-neon" 
                style="width: 45px; height: 45px; margin-left: -20px;" title="Trượt qua trái">
          <i class="bi bi-chevron-left fs-5 text-dark"></i>
        </button>

        <div class="d-flex flex-nowrap overflow-x-auto gap-4 pb-3 pt-2 custom-scrollbar px-2" ref="saleContainer" style="scroll-behavior: smooth;">
          <div class="flex-shrink-0" style="width: 250px;" v-for="product in discountedProducts" :key="'sale-' + product.productId">
            <div class="card border-0 shadow-sm h-100 bg-white rounded-3 position-relative p-2 d-flex flex-column">
              <span class="badge bg-danger position-absolute top-0 end-0 m-3 z-1">
                -{{ calculateDiscount(product.price, product.salePrice) }}%
              </span>
              <div class="bg-light rounded text-center py-4 mb-2 flex-grow-1 d-flex align-items-center justify-content-center">
                 <img :src="product.imageUrl || 'https://via.placeholder.com/300'" class="img-fluid" style="height: 120px; object-fit: contain;">
              </div>
              <div class="card-body p-2 d-flex flex-column text-center">
                <h6 class="fw-bold mb-2 line-clamp-1" :title="product.name">{{ product.name }}</h6>
                <div class="d-flex flex-column justify-content-center gap-1 mb-3">
                  <h5 class="fw-bold text-neon mb-0">{{ formatCurrency(product.salePrice) }}</h5>
                  <span class="text-muted text-decoration-line-through small">{{ formatCurrency(product.price) }}</span>
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
          
          <div v-if="discountedProducts.length === 0" class="text-center w-100 py-4 text-muted">
            Hiện tại chưa có chương trình giảm giá nào.
          </div>
        </div>

        <button @click="scrollSale('+')" 
                class="btn btn-light border shadow rounded-circle position-absolute top-50 end-0 translate-middle-y z-3 d-flex align-items-center justify-content-center hover-neon" 
                style="width: 45px; height: 45px; margin-right: -20px;" title="Trượt qua phải">
          <i class="bi bi-chevron-right fs-5 text-dark"></i>
        </button>

      </div>
    </section>

    <div v-if="showFlashSaleModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
      <div class="modal-content-custom bg-white rounded-4 shadow-lg p-4 position-relative text-center mx-3">
        <button @click="closeModal" class="btn-close position-absolute top-0 end-0 m-3" aria-label="Close"></button>
        
        <div class="mb-2 mt-2">
          <i class="bi bi-ticket-perforated-fill text-neon" style="font-size: 3rem; filter: drop-shadow(0px 4px 6px rgba(0,255,51,0.3));"></i>
        </div>
        
        <h3 class="fw-black mb-3 text-uppercase">🔥 Ưu đãi cực hời 🔥</h3>
        
        <p class="mb-4 text-dark fs-5">
          Mã giảm giá của bạn là:<br>
          <span class="bg-dark text-neon fs-2 fw-black px-4 py-2 rounded-3 d-inline-block mt-2 tracking-wide border border-neon">
            {{ flashSaleVoucher?.code || 'FLASHSALE' }}
          </span>
        </p>

        <p class="text-muted small lh-base mb-4 px-md-2">
          Ưu đãi cực hời vào cuối tuần khi nhập mã Flash Sale vào mỗi ngày <strong>T7, CN hàng tuần</strong>. Sự kiện sẽ kéo dài từ ngày <strong>13/03/2026</strong> đến ngày <strong>14/04/2026</strong>.
        </p>

        <button @click="closeModal" class="btn btn-dark w-100 fw-bold py-2 fs-6 border-neon text-neon">
          ĐÃ HIỂU & LƯU MÃ
        </button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';

const featuredProducts = ref([]);
const latestProducts = ref([]);
const discountedProducts = ref([]);
const flashSaleVoucher = ref(null);
const saleContainer = ref(null);
const currentTime = ref('');
let timeInterval = null;

// Biến điều khiển Modal
const showFlashSaleModal = ref(false);

const updateCurrentTime = () => {
  const now = new Date();
  const day = String(now.getDate()).padStart(2, '0');
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const year = now.getFullYear();
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  currentTime.value = `${day}/${month}/${year} - ${hours}:${minutes}:${seconds}`;
};

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'instant' });
};

const scrollSale = (direction) => {
  if (saleContainer.value) {
    const scrollAmount = 300; 
    if (direction === '-') {
      saleContainer.value.scrollLeft -= scrollAmount;
    } else {
      saleContainer.value.scrollLeft += scrollAmount;
    }
  }
};

const formatCurrency = (v) => {
  if (!v && v !== 0) return '';
  return new Intl.NumberFormat('vi-VN').format(v) + '₫';
};

const calculateDiscount = (price, salePrice) => {
  if (!price || !salePrice || price <= 0 || salePrice >= price) return 0;
  
  const discount = ((price - salePrice) / price) * 100;
  let roundedDiscount = Math.round(discount);
  
  if (roundedDiscount >= 100 && salePrice > 0) {
    return 99;
  }
  return roundedDiscount;
};

// ĐÃ XÓA CHECK IF (!flashSaleVoucher.value) GÂY RA ALERT
const claimFlashSale = () => {
  // Bật modal hiển thị mã lên mọi lúc
  showFlashSaleModal.value = true;
};

const closeModal = () => {
  showFlashSaleModal.value = false;
};

const fetchData = async () => {
  try {
    const [featuredRes, latestRes, discountedRes, flashSaleRes] = await Promise.all([
      axios.get('http://localhost:8080/api/product/featured'),
      axios.get('http://localhost:8080/api/product/latest'),
      axios.get('http://localhost:8080/api/product/discounted'),
      axios.get('http://localhost:8080/api/vouchers/code/FLASHSALE').catch(() => null)
    ]);
    
    featuredProducts.value = featuredRes.data;
    latestProducts.value = latestRes.data;
    discountedProducts.value = discountedRes.data;
    if (flashSaleRes && flashSaleRes.data) {
      flashSaleVoucher.value = flashSaleRes.data;
    }
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu trang chủ:", error);
  }
};

onMounted(() => {
  fetchData();
  updateCurrentTime(); 
  timeInterval = setInterval(updateCurrentTime, 1000); 
});

onUnmounted(() => {
  if (timeInterval) clearInterval(timeInterval);
});
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

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
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

/* Nút tùy chỉnh */
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

/* Nút cuộn ngang lơ lửng */
.hover-neon {
  transition: all 0.2s ease;
}
.hover-neon:hover {
  background-color: #00FF33 !important;
  border-color: #00FF33 !important;
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

/* Scrollbar ngang (ẩn mượt) */
.custom-scrollbar::-webkit-scrollbar {
  height: 6px; 
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent; 
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #ddd; 
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #00FF33; 
}

/* =========================================
   CSS CHO CUSTOM MODAL
   ========================================= */
.modal-backdrop-custom {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.65);
  z-index: 1050;
  backdrop-filter: blur(4px);
  animation: fadeIn 0.2s ease-out;
}

.modal-content-custom {
  max-width: 480px;
  width: 100%;
  animation: slideUp 0.3s ease-out;
}

.tracking-wide {
  letter-spacing: 2px;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(30px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>