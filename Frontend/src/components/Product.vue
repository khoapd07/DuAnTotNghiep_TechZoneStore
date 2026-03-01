<template>
  <div class="product-catalog-page pb-5 pt-3">
    <div class="container narrow-container">
      
      <div class="mb-4">
        <nav aria-label="breadcrumb" class="mb-2">
          <ol class="breadcrumb fs-8 fw-medium mb-0">
            <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
            <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Thiết bị Gaming</a></li>
            <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Tất cả sản phẩm</li>
          </ol>
        </nav>
        
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3">
          <div>
            <h1 class="fw-black text-uppercase fs-3 mb-1">DANH MỤC SẢN PHẨM</h1>
            <p class="text-muted m-0 fs-8">Thiết bị ngoại vi được chế tạo chính xác cho lợi thế cạnh tranh tối thượng.</p>
          </div>
          <div class="d-flex align-items-center gap-2">
            <span class="text-muted fw-bold fs-8 text-nowrap">SẮP XẾP:</span>
            <select class="form-select form-select-sm border-0 bg-light fw-bold custom-select-width shadow-none fs-8">
              <option value="1">Nổi bật</option>
              <option value="2">Giá tăng dần</option>
              <option value="3">Giá giảm dần</option>
              <option value="4">Mới nhất</option>
            </select>
          </div>
        </div>
      </div>

      <div class="row g-4">
        <div class="col-lg-3">
          
          <div class="filter-box border rounded-3 p-3 mb-3">
            <h6 class="fw-bold fs-8 d-flex justify-content-between align-items-center mb-3">
              KHOẢNG GIÁ <i class="bi bi-chevron-up text-muted"></i>
            </h6>
            <select class="form-select form-select-sm mb-3 shadow-none bg-light border-0 fs-8">
              <option>Chọn khoảng giá...</option>
              <option>Dưới 1 triệu</option>
              <option>1 - 3 triệu</option>
              <option>Trên 3 triệu</option>
            </select>
            <label class="form-label fs-8 text-muted fw-bold mb-1">GIÁ TỐI ĐA TÙY CHỈNH</label>
            <div class="input-group input-group-sm">
              <span class="input-group-text bg-white border-end-0 text-muted px-2 py-1 fs-8">$</span>
              <input type="number" class="form-control border-start-0 shadow-none px-2 py-1 fs-8" placeholder="2000">
            </div>
          </div>

          <div class="filter-box border rounded-3 p-3 mb-3">
            <h6 class="fw-bold fs-8 d-flex justify-content-between align-items-center mb-3">
              DANH MỤC <i class="bi bi-chevron-up text-muted"></i>
            </h6>
            <div class="d-flex flex-column gap-2 fs-8">
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="cat1">
                <label class="form-check-label text-muted" for="cat1">Chuột (12)</label>
              </div>
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="cat2" checked>
                <label class="form-check-label text-dark fw-bold" for="cat2">Bàn phím (8)</label>
              </div>
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="cat3">
                <label class="form-check-label text-muted" for="cat3">Tai nghe (15)</label>
              </div>
            </div>
          </div>

          <div class="filter-box border rounded-3 p-3">
            <h6 class="fw-bold fs-8 d-flex justify-content-between align-items-center mb-3">
              THƯƠNG HIỆU <i class="bi bi-chevron-up text-muted"></i>
            </h6>
            <div class="d-flex flex-column gap-2 fs-8">
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="brand1">
                <label class="form-check-label text-muted" for="brand1">ApexGear</label>
              </div>
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="brand2">
                <label class="form-check-label text-muted" for="brand2">Titan</label>
              </div>
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="brand3">
                <label class="form-check-label text-muted" for="brand3">Phantom Labs</label>
              </div>
            </div>
          </div>

        </div>

        <div class="col-lg-9">
          <div class="row g-3"> <div class="col-md-6 col-lg-4" v-for="product in products" :key="product.id">
              <div class="card h-100 border-0 product-card custom-card-hover rounded-3 overflow-hidden">
                
                <div class="img-wrapper position-relative d-flex justify-content-center align-items-center p-3">
                  <span v-if="product.badge === 'MỚI'" class="badge bg-neon text-dark position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">{{ product.badge }}</span>
                  <span v-else-if="product.badge === 'BÁN CHẠY'" class="badge bg-dark text-white border border-secondary position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">{{ product.badge }}</span>
                  <span v-else-if="product.badge === '-20%'" class="badge bg-danger text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">{{ product.badge }}</span>
                  
                  <i class="bi bi-heart position-absolute top-0 end-0 m-2 fs-6 text-white cursor-pointer heart-icon"></i>
                  
                  <img :src="product.img" class="img-fluid object-fit-contain" style="height: 140px;" :alt="product.name">
                </div>

                <div class="info-wrapper p-3 d-flex flex-column flex-grow-1">
                  <h6 class="fw-bold mb-1 text-uppercase fs-8 line-clamp-1" :title="product.name">{{ product.name }}</h6>
                  <small class="text-muted mb-2 d-block fs-9">{{ product.desc }}</small>
                  
                  <div class="mt-auto mb-2">
                    <h6 class="fw-black m-0 d-inline-block fs-7">{{ formatCurrency(product.price) }}</h6>
                    <span v-if="product.oldPrice" class="text-muted text-decoration-line-through ms-2 fs-9">{{ formatCurrency(product.oldPrice) }}</span>
                  </div>

                  <div class="d-flex gap-2">
                    <button class="btn btn-outline-dark btn-cart-icon d-flex align-items-center justify-content-center rounded-2 p-1" title="Thêm vào giỏ">
                      <i class="bi bi-cart2 fs-6"></i>
                    </button>
                    <button class="btn btn-neon fw-bold flex-grow-1 fs-8 p-1 rounded-2 text-dark">
                      MUA NGAY
                    </button>
                  </div>
                </div>

              </div>
            </div>

          </div>

          <div class="d-flex justify-content-center mt-4">
            <nav aria-label="Page navigation">
              <ul class="pagination pagination-sm gap-1 mb-0"> <li class="page-item disabled">
                  <a class="page-link border-0 rounded-2 text-muted px-2 py-1" href="#" tabindex="-1"><i class="bi bi-chevron-left fs-8"></i></a>
                </li>
                <li class="page-item"><a class="page-link border-0 rounded-2 active-page text-dark fw-bold px-2 py-1 fs-8" href="#">1</a></li>
                <li class="page-item"><a class="page-link border-0 rounded-2 text-muted fw-bold px-2 py-1 fs-8" href="#">2</a></li>
                <li class="page-item"><a class="page-link border-0 rounded-2 text-muted fw-bold px-2 py-1 fs-8" href="#">3</a></li>
                <li class="page-item"><span class="page-link border-0 bg-transparent text-muted px-2 py-1 fs-8">...</span></li>
                <li class="page-item"><a class="page-link border-0 rounded-2 text-muted fw-bold px-2 py-1 fs-8" href="#">12</a></li>
                <li class="page-item">
                  <a class="page-link border-0 rounded-2 text-dark px-2 py-1" href="#"><i class="bi bi-chevron-right fs-8"></i></a>
                </li>
              </ul>
            </nav>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Hàm format tiền tệ (mô phỏng)
const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + ' VNĐ'; // Theo thiết kế dùng chữ VNĐ
};

// Dữ liệu mô phỏng dựa trên ảnh
const products = ref([
  {
    id: 1,
    name: 'CYBERBLADE PRO WIRELESS',
    desc: '20K DPI • Pin 70h',
    price: 999999,
    oldPrice: null,
    badge: 'MỚI',
    img: 'https://images.unsplash.com/photo-1527814050087-379381547928?w=400' 
  },
  {
    id: 2,
    name: 'MECHSTRIKE K90 ELITE',
    desc: 'Blue Switch • Khung Nhôm',
    price: 999999,
    oldPrice: null,
    badge: 'BÁN CHẠY',
    img: 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=400'
  },
  {
    id: 3,
    name: 'VOIDSOUND 7.1 SURROUND',
    desc: 'Chống Ồn • Driver 50mm',
    price: 999999,
    oldPrice: null,
    badge: null,
    img: 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400'
  },
  {
    id: 4,
    name: 'X-STREAM ULTRAWIDE',
    desc: '165Hz • 1ms',
    price: 999999,
    oldPrice: 1290000,
    badge: '-20%',
    img: 'https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?w=400'
  }
]);
</script>

<style scoped>
/* Typography & Utilities */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; } /* Thêm fs-9 cho text rất nhỏ */
.cursor-pointer { cursor: pointer; }
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}

/* Biến màu chủ đạo */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }

/* Custom Container - Đã thu hẹp xuống 1000px */
.narrow-container {
  max-width: 1000px !important; 
  margin-left: auto;
  margin-right: auto;
}

.custom-select-width {
  width: 110px; /* Thu hẹp select một chút */
}

/* ------ CUSTOM SIDEBAR FILTERS ------ */
.filter-box {
  background-color: #FFFFFF;
}

/* Custom Checkbox TechZone Style */
.custom-checkbox .form-check-input {
  border-radius: 3px; /* Bo góc nhỏ hơn */
  border-color: #CCC;
  cursor: pointer;
  width: 14px; /* Thu nhỏ checkbox */
  height: 14px;
  margin-top: 0.15rem;
}
.custom-checkbox .form-check-input:checked {
  background-color: #00FF33;
  border-color: #00FF33;
}
/* Đổi màu dấu tick thành màu đen cho dễ nhìn trên nền xanh neon */
.custom-checkbox .form-check-input:checked[type=checkbox] {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'%3e%3cpath fill='none' stroke='%23000' stroke-linecap='round' stroke-linejoin='round' stroke-width='3' d='M6 10l3 3l6-6'/%3e%3c/svg%3e");
}

/* Loại bỏ nút tăng giảm của input number */
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
  -webkit-appearance: none; 
  margin: 0; 
}

/* ------ CUSTOM PRODUCT CARD ------ */
.product-card {
  background-color: transparent;
}
/* Phần ảnh nền đen nhám - Đã giảm chiều cao */
.img-wrapper {
  background-color: #111111; 
  height: 180px; /* Từ 250px xuống 180px */
}
/* Nút thả tim */
.heart-icon {
  opacity: 0.5;
  transition: opacity 0.2s, color 0.2s;
}
.heart-icon:hover {
  opacity: 1;
  color: #00FF33 !important;
}

/* Phần thông tin nền xám nhạt */
.info-wrapper {
  background-color: #F4F6F8;
}

/* Hover Effect cho Card */
.custom-card-hover {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.custom-card-hover:hover {
  transform: translateY(-4px); /* Nhấc lên ít hơn */
  box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important; /* Bóng đổ nhẹ hơn */
}

/* Nút Mua và Giỏ hàng */
.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover {
  background-color: #00cc29;
}
.btn-cart-icon {
  width: 32px; /* Từ 42px xuống 32px */
  height: 32px;
  border-color: #333;
  color: #333;
}
.btn-cart-icon:hover {
  background-color: #333;
  color: #FFF;
}

/* ------ CUSTOM PAGINATION ------ */
.pagination .page-link {
  color: #333;
  background-color: #F8F9FA;
  /* padding đã được set bằng các class px-2 py-1 ở HTML */
}
.pagination .page-link:hover {
  background-color: #EAEAEA;
}
.pagination .active-page {
  background-color: #00FF33 !important;
  color: #000 !important;
}
</style>