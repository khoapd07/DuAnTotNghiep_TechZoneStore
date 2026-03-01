<template>
  <div class="product-detail-page bg-white pb-5 pt-3">
    <div class="container narrow-container">
      
      <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Trang chủ</a></li>
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Gaming Gear</a></li>
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Bàn phím</a></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">MechStrike K90 Elite</li>
        </ol>
      </nav>

      <div class="row g-4 mb-5">
        
        <div class="col-md-6">
          <div class="main-image-box product-image-bg rounded-3 position-relative d-flex justify-content-center align-items-center mb-2">
            <span class="badge bg-neon text-dark position-absolute top-0 start-0 m-2 z-1 fw-bold fs-8 px-2 py-1">BÁN CHẠY NHẤT</span>
            <button class="btn position-absolute top-0 end-0 m-2 z-1 p-1 text-muted" style="background: transparent; border: none;">
              <i class="bi bi-arrows-fullscreen fs-6"></i>
            </button>
            <img :src="activeImage" class="img-fluid object-fit-contain" style="max-height: 280px; width: 100%; padding: 20px;" alt="Product Image">
          </div>
          <div class="d-flex gap-2 thumbnail-row">
            <div 
              v-for="(img, index) in thumbnails" :key="index"
              class="thumbnail-box bg-white rounded-2 cursor-pointer d-flex justify-content-center align-items-center"
              :class="{ 'border-neon-active': activeImage === img }"
              @click="activeImage = img"
            >
              <img :src="img" class="img-fluid p-1" alt="Thumb">
            </div>
            <div class="thumbnail-box product-image-bg rounded-2 cursor-pointer d-flex justify-content-center align-items-center">
              <i class="bi bi-play-circle text-muted fs-5"></i>
            </div>
          </div>
        </div>

        <div class="col-md-6 d-flex flex-column">
          <h3 class="fw-black mb-1 text-uppercase fs-4">MECHSTRIKE <span class="text-muted fw-normal">K90 ELITE</span></h3>
          
          <div class="d-flex align-items-center gap-2 mb-2 fs-8">
            <div class="text-neon d-flex gap-1">
              <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
            </div>
            <span class="text-muted">128 Đánh giá</span>
            <span class="text-muted">•</span>
            <span class="text-neon fw-bold d-flex align-items-center gap-1">
              <i class="bi bi-circle-fill" style="font-size: 5px;"></i> CÒN HÀNG
            </span>
          </div>

          <div class="d-flex align-items-end gap-2 mb-3">
            <h3 class="text-neon fw-black mb-0 fs-3">4.990.000₫</h3>
            <span class="text-muted text-decoration-line-through fs-7 mb-1">5.990.000₫</span>
            <span class="badge bg-neon-light text-neon fw-bold rounded-1 px-2 py-1 mb-1 fs-8">-20%</span>
          </div>

          <p class="text-muted mb-3 fs-8 lh-base">
            Thống trị đấu trường với MechStrike K90 Elite. Được thiết kế cho các game thủ chuyên nghiệp, trang bị switch quang học tùy chỉnh cho độ trễ gần như bằng không, đệm kê tay nam châm có thể tháo rời và khung nhôm cấp hàng không.
          </p>

          <div class="mb-3">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <span class="fw-bold fs-8 text-uppercase">LOẠI</span>
            </div>
            <div class="row g-2">
              <div class="col-4" v-for="variant in variants" :key="variant.id">
                <div 
                  class="variant-box border rounded-2 p-1 px-2 cursor-pointer position-relative h-100 d-flex flex-column justify-content-center"
                  :class="{ 'border-neon-active': selectedVariant === variant.id }"
                  @click="selectedVariant = variant.id"
                >
                  <i v-if="selectedVariant === variant.id" class="bi bi-check-circle-fill text-neon position-absolute top-0 end-0 m-1" style="font-size: 0.65rem;"></i>
                  <h6 class="fw-bold mb-0 fs-8" :class="{'text-dark': selectedVariant === variant.id, 'text-muted': selectedVariant !== variant.id}">{{ variant.name }}</h6>
                  <small class="text-muted mt-1" style="font-size: 0.6rem;">{{ variant.desc }}</small>
                </div>
              </div>
            </div>
          </div>

          <div class="d-flex gap-2 mt-auto mb-3 pt-2">
            <div class="quantity-selector d-flex align-items-center bg-light border rounded-2">
              <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="quantity > 1 && quantity--">-</button>
              <input type="number" v-model="quantity" class="form-control form-control-sm border-0 text-center fw-bold bg-transparent fs-7" style="width: 40px;" min="1">
              <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="quantity++">+</button>
            </div>
            
            <button class="btn btn-neon fw-bold flex-grow-1 fs-7 text-dark rounded-2 d-flex align-items-center justify-content-center gap-2 py-2">
              <i class="bi bi-cart2 fs-6"></i> THÊM VÀO GIỎ HÀNG
            </button>
            
            <button class="btn btn-outline-secondary rounded-2 d-flex align-items-center justify-content-center" style="width: 42px;">
              <i class="bi bi-heart fs-6"></i>
            </button>
          </div>

          <div class="border-top pt-2 fs-8 text-muted">
            <span class="fw-bold text-dark">Thông tin sản phẩm</span><br>
            <p class="mt-1 mb-1 line-clamp-2">Thống trị đấu trường với MechStrike K90 Elite. Được thiết kế cho các game thủ chuyên nghiệp, trang bị switch quang học tùy chỉnh cho độ trễ gần như bằng không, đệm kê tay nam châm có thể tháo rời và khung nhôm cấp hàng không.</p>
            <a href="#" class="text-dark fw-bold text-decoration-none">xem thêm >></a>
          </div>
        </div>
      </div>

      <div class="mb-5 pt-3">
        <div class="d-flex justify-content-between align-items-center border-bottom border-dark border-1 pb-2 mb-4">
          <h6 class="fw-black mb-0 text-uppercase fs-5">ĐÁNH GIÁ SẢN PHẨM</h6>
        </div>

        <div class="row g-4">
          <div class="col-md-3">
            <div class="bg-light-box rounded-3 p-3 text-center h-100 d-flex flex-column justify-content-center">
              <h2 class="display-5 fw-black mb-0 text-dark">4.8<span class="fs-5 text-muted fw-normal">/5</span></h2>
              <div class="text-neon fs-6 mb-1">
                <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i>
              </div>
              <p class="text-muted fs-8 mb-3">Dựa trên 128 đánh giá</p>
              
              <div class="d-flex flex-column gap-2">
                <div class="d-flex align-items-center gap-2 fs-8" v-for="i in 5" :key="i">
                  <span class="text-muted fw-bold" style="width: 10px;">{{ 6 - i }}</span>
                  <div class="progress flex-grow-1 bg-white" style="height: 5px; border: 1px solid #EAEAEA;">
                    <div class="progress-bar bg-neon" :style="`width: ${[85, 10, 3, 1, 1][i-1]}%`"></div>
                  </div>
                  <span class="text-muted text-end" style="width: 25px;">{{ [85, 10, 3, 1, 1][i-1] }}%</span>
                </div>
              </div>
            </div>
          </div>

          <div class="col-md-9">
            <div class="">
              <div v-for="(review, index) in reviews" :key="index" class="review-item border-bottom border-light pb-3 mb-3">
                <div class="d-flex justify-content-between mb-1">
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-circle bg-neon-light text-neon-dark fw-bold d-flex justify-content-center align-items-center rounded-circle fs-8" style="width:30px; height:30px">
                      {{ review.name.charAt(0) }}
                    </div>
                    <div>
                      <h6 class="mb-0 fw-bold fs-8">{{ review.name }}</h6>
                      <small class="text-muted" style="font-size: 0.65rem;">Người mua đã xác thực</small>
                    </div>
                  </div>
                  <small class="text-muted fs-8">{{ review.date }}</small>
                </div>
                <div class="text-neon fs-8 mb-1">
                  <i class="bi bi-star-fill me-1" v-for="s in review.stars" :key="'s'+s"></i>
                </div>
                <p class="text-dark fs-8 mb-0 lh-base">{{ review.content }}</p>
              </div>
              
              <div class="text-center mt-2">
                <a href="#" class="text-dark fw-bold text-decoration-underline fs-8">Xem thêm đánh giá</a>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mb-5 pt-2">
        <div class="d-flex justify-content-between align-items-end mb-3 border-bottom border-dark border-1 pb-2">
          <h6 class="fw-black mb-0 text-uppercase fs-5">SẢN PHẨM TƯƠNG TỰ</h6>
          <a href="#" class="text-neon text-decoration-none fw-bold fs-8">XEM TẤT CẢ</a>
        </div>

        <div class="row g-3">
          <div class="col-md-3" v-for="item in relatedProducts" :key="item.id">
            <div class="card border-0 h-100 bg-white rounded-3 position-relative p-2 d-flex flex-column custom-card-hover">
              <button class="btn btn-light position-absolute top-0 end-0 m-2 z-1 rounded-circle p-1 d-flex align-items-center justify-content-center text-muted" style="width:28px; height:28px; background: transparent;">
                <i class="bi bi-heart fs-7"></i>
              </button>
              <div class="product-image-bg rounded text-center py-3 mb-2 flex-grow-1 d-flex align-items-center justify-content-center">
                 <img :src="item.img" class="img-fluid" style="height: 100px; object-fit: contain;">
              </div>
              <div class="card-body p-2 d-flex flex-column text-start">
                <h6 class="fw-bold mb-1 fs-8 line-clamp-1">{{ item.name }}</h6>
                <h6 class="fw-bold text-neon mb-0 mt-auto fs-7">{{ item.price }}</h6>
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

const thumbnails = ref([
  'https://images.unsplash.com/photo-1595225476474-87563907a212?w=500',
  'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=500',
  'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=500'
]);
const activeImage = ref(thumbnails.value[0]);

const quantity = ref(1);

const variants = ref([
  { id: 1, name: 'Linear Red', desc: 'Mượt và êm ái' },
  { id: 2, name: 'Clicky Blue', desc: 'Cảm giác gõ đầm' },
  { id: 3, name: 'Tactile Brown', desc: 'Khấc bấm nhẹ' }
]);
const selectedVariant = ref(1);

const reviews = ref([
  { name: 'Nguyễn Hoàng', date: '15/05/2024', stars: 5, content: 'Bàn phím cực kỳ nhạy, cảm giác gõ phím switch quang học rất khác biệt so với switch cơ thông thường. Build nhôm rất chắc chắn, led RGB sáng và đẹp. Đáng đồng tiền bát gạo!' },
  { name: 'Trần Anh', date: '12/05/2024', stars: 5, content: 'Cảm giác gõ Red Switch rất mượt, phù hợp cho cả chơi game và làm việc văn phòng. Phần kê tay nam châm cực kỳ tiện lợi, tháo lắp dễ dàng. Shop giao hàng nhanh, đóng gói kỹ.' },
  { name: 'Lê Minh', date: '02/05/2024', stars: 5, content: 'Thiết kế tối giản nhưng vẫn rất gaming. App tùy chỉnh LED và Macro của TechZone dùng khá ổn. Điểm 10 cho chất lượng phục vụ.' }
]);

const relatedProducts = ref([
  { id: 1, name: 'CyberBlack Pro Wireless', price: '3.290.000₫', img: 'https://images.unsplash.com/photo-1527814050087-379381547928?w=300' },
  { id: 2, name: 'VoidSound 7.1 Surround', price: '2.190.000₫', img: 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=300' },
  { id: 3, name: 'X-Stream Ultrawide', price: '12.500.000₫', img: 'https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?w=300' },
  { id: 4, name: 'ProGrip Controller X', price: '1.990.000₫', img: 'https://images.unsplash.com/photo-1600080972464-8e5f35f63d08?w=300' }
]);
</script>

<style scoped>
.product-detail-page {
  font-family: 'Inter', sans-serif;
}
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }
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

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }
.text-neon-dark { color: #00cc29 !important; }
.product-image-bg { background-color: #F4F6F8; }
.bg-light-box { background-color: #F8F9FA; border: 1px solid #F0F0F0; }

/* Buttons */
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

/* Images (Thu nhỏ lại) */
.main-image-box {
  height: 320px;
}
.thumbnail-box {
  width: 60px;
  height: 60px;
  border: 1px solid transparent; 
  transition: all 0.2s;
}
.border-neon-active {
  border: 2px solid #00FF33 !important;
}

/* Biến thể */
.variant-box {
  border-color: #EAEAEA;
  transition: all 0.2s;
}
.variant-box:hover {
  border-color: #00FF33;
}

/* Container */
.narrow-container {
  max-width: 950px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Hover Cards */
.custom-card-hover {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  border: 1px solid transparent !important;
}
.custom-card-hover:hover {
  transform: translateY(-5px);
  border-color: #00FF33 !important;
  box-shadow: 0 10px 20px rgba(0,255,51,0.05) !important;
}

/* Number Input */
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
  -webkit-appearance: none; 
  margin: 0; 
}

/* Breadcrumb */
.breadcrumb-item + .breadcrumb-item::before {
  content: "/";
  color: #6c757d;
}
</style>