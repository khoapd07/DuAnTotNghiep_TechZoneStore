<template>
  <div class="cart-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">
      
      <div class="d-flex align-items-center gap-2 mb-4">
        <h2 class="fw-black m-0 text-dark">Giỏ hàng của bạn</h2>
        <span class="badge bg-neon-light text-neon rounded-pill px-3 py-1 fs-8 fw-bold">
          {{ cartItems.length }} Sản phẩm
        </span>
      </div>

      <div class="row g-4">
        <div class="col-lg-8">
          
          <div class="card border-0 shadow-sm rounded-3 mb-3">
            <div class="card-body py-3 px-4 d-flex align-items-center">
              <div class="form-check custom-checkbox">
                <input class="form-check-input shadow-none" type="checkbox" id="selectAll" v-model="selectAll" @change="toggleSelectAll">
                <label class="form-check-label fw-bold fs-7 text-uppercase ms-2" for="selectAll">
                  CHỌN TẤT CẢ ({{ cartItems.length }})
                </label>
              </div>
            </div>
          </div>

          <div class="d-flex flex-column gap-3">
            <div v-for="(item, index) in cartItems" :key="item.id" class="card border-0 shadow-sm rounded-3 overflow-hidden">
              <div class="card-body p-4 d-flex align-items-center gap-4">
                
                <div class="form-check custom-checkbox">
                  <input class="form-check-input shadow-none" type="checkbox" v-model="item.selected">
                </div>

                <div class="product-img-box bg-light rounded-3 d-flex align-items-center justify-content-center p-2" style="width: 100px; height: 100px;">
                  <img :src="item.img" class="img-fluid object-fit-contain" alt="Product">
                </div>

                <div class="flex-grow-1">
                  <h6 class="fw-bold mb-1 fs-6">{{ item.name }}</h6>
                  <p class="text-muted fs-8 mb-2">{{ item.desc }}</p>
                  
                  <div class="d-flex align-items-center gap-3">
                    <div class="quantity-selector d-flex align-items-center bg-light border rounded-2">
                      <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="item.quantity > 1 && item.quantity--">-</button>
                      <input type="number" v-model="item.quantity" class="form-control form-control-sm border-0 text-center fw-bold bg-transparent p-0" style="width: 35px;" min="1">
                      <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="item.quantity++">+</button>
                    </div>
                    <button class="btn btn-link text-muted p-0 fs-5 hover-danger" @click="removeItem(index)">
                      <i class="bi bi-trash3"></i>
                    </button>
                  </div>
                </div>

                <div class="text-end" style="min-width: 120px;">
                  <h6 class="fw-black text-dark mb-0">{{ formatCurrency(item.price) }}</h6>
                  <small v-if="item.oldPrice" class="text-muted text-decoration-line-through fs-8">{{ formatCurrency(item.oldPrice) }}</small>
                </div>

              </div>
            </div>
          </div>

          <div class="d-flex justify-content-between align-items-center mt-4 px-2">
            <router-link to="/products" class="text-muted text-decoration-none fs-7 fw-bold hover-dark">
              <i class="bi bi-arrow-left me-2"></i> Tiếp tục mua sắm
            </router-link>
            <button class="btn btn-link text-muted text-decoration-none fs-7 fw-bold p-0" @click="clearCart">
              Làm trống giỏ hàng
            </button>
          </div>
        </div>

        <div class="col-lg-4">
          <div class="sticky-lg-top" style="top: 20px;">
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
              <h5 class="fw-black text-uppercase mb-4 fs-6">TÓM TẮT ĐƠN HÀNG</h5>
              
              <div class="d-flex flex-column gap-3 fs-7 border-bottom pb-4 mb-4">
                <div class="d-flex justify-content-between">
                  <span class="text-muted">Tạm tính ({{ selectedCount }} SP)</span>
                  <span class="fw-bold">{{ formatCurrency(subtotal) }}</span>
                </div>
                <div class="d-flex justify-content-between">
                  <span class="text-muted">Phí vận chuyển</span>
                  <span class="text-success fw-bold">Miễn phí</span>
                </div>
                <div class="d-flex justify-content-between">
                  <span class="text-muted">Thuế (VAT 10%)</span>
                  <span class="fw-bold">{{ formatCurrency(tax) }}</span>
                </div>
              </div>

              <div class="d-flex justify-content-between align-items-end mb-4">
                <h6 class="fw-black m-0 fs-6">Tổng cộng</h6>
                <div class="text-end">
                  <h4 class="text-neon fw-black mb-0">{{ formatCurrency(total) }}</h4>
                  <small class="text-muted fs-9">DỰA TRÊN SẢN PHẨM ĐÃ CHỌN</small>
                </div>
              </div>

              <button class="btn btn-neon w-100 fw-black py-3 rounded-3 d-flex align-items-center justify-content-center gap-2 fs-7 text-dark shadow-sm">
                TIẾN HÀNH THANH TOÁN <i class="bi bi-lightning-fill"></i>
              </button>
            </div>

            <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
              <div class="row g-2 text-center fs-9 fw-bold text-muted text-uppercase">
                <div class="col-4 border-end">
                  <i class="bi bi-shield-check d-block fs-5 mb-1 text-dark"></i> Bảo mật
                </div>
                <div class="col-4 border-end">
                  <i class="bi bi-truck d-block fs-5 mb-1 text-dark"></i> Giao nhanh
                </div>
                <div class="col-4">
                  <i class="bi bi-patch-check d-block fs-5 mb-1 text-dark"></i> Chính hãng
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
import { ref, computed } from 'vue';

// Mock data giỏ hàng
const cartItems = ref([
  {
    id: 1,
    name: 'Bàn phím cơ Apex Pro TKL',
    desc: 'Gaming / RGB / OmniPoint Switches',
    price: 4590000,
    oldPrice: 5200000,
    quantity: 1,
    selected: true,
    img: 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=300'
  },
  {
    id: 2,
    name: 'Chuột Gaming G-Pro Wireless',
    desc: 'Wireless / Ultra Lightweight / 25K Sensor',
    price: 2190000,
    oldPrice: null,
    quantity: 1,
    selected: true,
    img: 'https://images.unsplash.com/photo-1527814050087-379381547928?w=300'
  },
  {
    id: 3,
    name: 'Màn hình UltraWide 34” QHD',
    desc: '144Hz / 1ms / HDR400 / Curved',
    price: 12500000,
    oldPrice: null,
    quantity: 1,
    selected: false,
    img: 'https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?w=300'
  }
]);

const selectAll = ref(false);

// Logic chọn tất cả
const toggleSelectAll = () => {
  cartItems.value.forEach(item => item.selected = selectAll.value);
};

// Đếm số lượng SP đang được chọn
const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).length;
});

// Tính toán tiền bạc dựa trên các SP được chọn
const subtotal = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((total, item) => total + (item.price * item.quantity), 0);
});

const tax = computed(() => subtotal.value * 0.1);
const total = computed(() => subtotal.value + tax.value);

// Xóa 1 item
const removeItem = (index) => {
  if(confirm('Bạn muốn xóa sản phẩm này khỏi giỏ hàng?')) {
    cartItems.value.splice(index, 1);
  }
};

// Xóa sạch giỏ
const clearCart = () => {
  if(confirm('Làm trống toàn bộ giỏ hàng?')) {
    cartItems.value = [];
  }
};

// Format tiền tệ
const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value).replace('₫', 'đ');
};
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.1); }
.btn-neon { background-color: #00FF33; color: #000; border: none; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); transition: all 0.2s; }

/* Custom Container */
.narrow-container { max-width: 1100px !important; margin: 0 auto; }

/* Custom Checkbox */
.custom-checkbox .form-check-input {
  width: 20px; height: 20px; border-radius: 4px; border-color: #ccc; cursor: pointer;
}
.custom-checkbox .form-check-input:checked {
  background-color: #00FF33; border-color: #00FF33;
}
.custom-checkbox .form-check-input:checked[type=checkbox] {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'%3e%3cpath fill='none' stroke='%23000' stroke-linecap='round' stroke-linejoin='round' stroke-width='3' d='M6 10l3 3l6-6'/%3e%3c/svg%3e");
}

/* Hover effects */
.hover-danger:hover { color: #dc3545 !important; }
.hover-dark:hover { color: #000 !important; }

/* Input number hidden arrow */
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { -webkit-appearance: none; margin: 0; }
</style>