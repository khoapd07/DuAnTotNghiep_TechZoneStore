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
            <div v-for="item in cartItems" :key="item.productId" class="card border-0 shadow-sm rounded-3 overflow-hidden">
              <div class="card-body p-4 d-flex align-items-center gap-4">
                
                <div class="form-check custom-checkbox">
                  <input class="form-check-input shadow-none" type="checkbox" v-model="item.selected" @change="saveGuestCartState">
                </div>

                <div class="product-img-box bg-light rounded-3 d-flex align-items-center justify-content-center p-2" style="width: 100px; height: 100px;">
                  <img :src="item.img" class="img-fluid object-fit-contain" alt="Product">
                </div>

                <div class="flex-grow-1">
                  <h6 class="fw-bold mb-1 fs-6">{{ item.name }}</h6>
                  <p class="text-muted fs-8 mb-2">{{ item.desc }}</p>
                  
                  <div class="d-flex align-items-center gap-3">
                    <div class="quantity-selector d-flex align-items-center bg-light border rounded-2">
                      <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="decreaseQty(item)">-</button>
                      <input type="number" v-model="item.quantity" @change="updateQty(item)" class="form-control form-control-sm border-0 text-center fw-bold bg-transparent p-0" style="width: 35px;" min="1">
                      <button class="btn btn-sm border-0 px-2 fw-bold text-muted" @click="increaseQty(item)">+</button>
                    </div>
                    <button class="btn btn-link text-muted p-0 fs-5 hover-danger" @click="removeItem(item.productId)">
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

              <button @click="goToCheckout" class="btn btn-neon w-100 fw-black py-3 rounded-3 d-flex align-items-center justify-content-center gap-2 fs-7 text-dark shadow-sm">
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
import { ref, computed, onMounted } from 'vue';
import axios from 'axios'; 

import { useRouter } from 'vue-router';

const router = useRouter();

const API_URL = 'http://localhost:8080/api/cart';

const cartItems = ref([]);
const selectAll = ref(false);

let debounceTimer;

const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      return JSON.parse(userInfoString).userId;
    } catch (e) { return null; }
  }
  return null;
};

// -----------------------------------------
// 1. KẾT NỐI API / LOCALSTORAGE: LẤY DỮ LIỆU
// -----------------------------------------
const fetchCart = async () => {
  const userId = getCurrentUserId();
  
  if (!userId) {
    // KHÁCH VÃNG LAI: Lấy từ localStorage
    const guestCart = localStorage.getItem('guest_cart');
    if (guestCart) {
      const parsedCart = JSON.parse(guestCart);
      
      // SỬA LỖI Ở ĐÂY: Gắn mặc định selected = true cho các item từ localStorage
      cartItems.value = parsedCart.map(item => ({
        ...item,
        // Nếu đã có trạng thái selected lưu trước đó thì lấy, không thì mặc định là true (được tích sẵn)
        selected: item.selected !== undefined ? item.selected : true 
      }));
    } else {
      cartItems.value = [];
    }
    return;
  }

  // ĐÃ ĐĂNG NHẬP: Lấy từ API
  try {
    const response = await axios.get(`${API_URL}/${userId}`);
    const backendItems = response.data.items || response.data;

    cartItems.value = backendItems.map(bItem => {
      const existingItem = cartItems.value.find(i => i.productId === bItem.productId);
      return {
        productId: bItem.productId,
        name: bItem.productName,
        desc: 'Sản phẩm chính hãng TechZone',
        price: bItem.salePrice || bItem.price,
        oldPrice: bItem.salePrice ? bItem.price : null,
        quantity: bItem.quantity,
        selected: existingItem ? existingItem.selected : true,
        img: bItem.imageUrl
      };
    });
  } catch (error) {
    console.error('Lỗi khi tải giỏ hàng:', error);
  }
};

onMounted(() => {
  fetchCart(); 
});

// -----------------------------------------
// 2. LOGIC GIAO DIỆN (CHECKBOX & TÍNH TIỀN)
// -----------------------------------------
// Hàm lưu lại trạng thái selected vào localStorage cho khách vãng lai
const saveGuestCartState = () => {
  if (!getCurrentUserId()) {
    localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
  }
};

const toggleSelectAll = () => {
  cartItems.value.forEach(item => item.selected = selectAll.value);
  saveGuestCartState(); // Lưu lại khi nhấn chọn tất cả
};

const selectedCount = computed(() => cartItems.value.filter(item => item.selected).length);

const subtotal = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((total, item) => total + (item.price * item.quantity), 0);
});

const tax = computed(() => subtotal.value * 0.1);
const total = computed(() => subtotal.value + tax.value);

// -----------------------------------------
// 3. LOGIC CẬP NHẬT SỐ LƯỢNG
// -----------------------------------------
const syncQuantityWithBackend = (productId, newQuantity) => {
  const userId = getCurrentUserId();

  if (!userId) {
    // Xử lý Local Storage cho khách vãng lai (giữ nguyên code cũ)
    const itemIndex = cartItems.value.findIndex(i => i.productId === productId);
    if (itemIndex !== -1) {
      cartItems.value[itemIndex].quantity = newQuantity;
      localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
      window.dispatchEvent(new Event('cart-updated'));
    }
    return;
  }

  // ĐÃ ĐĂNG NHẬP: GỌI API
  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(async () => {
    try {
      // ĐÃ SỬA: URL có thêm /${userId}/add
      await axios.put(`${API_URL}/${userId}/update`, { 
        productId: productId,
        quantity: newQuantity
      });
      await fetchCart();
      window.dispatchEvent(new Event('cart-updated'));
    } catch (error) {
      alert(error.response?.data || 'Cập nhật thất bại');
      fetchCart(); 
    }
  }, 1000); 
};

const increaseQty = (item) => {
  item.quantity++; 
  syncQuantityWithBackend(item.productId, item.quantity);
};

const decreaseQty = (item) => {
  if (item.quantity > 1) {
    item.quantity--; 
    syncQuantityWithBackend(item.productId, item.quantity);
  }
};

const updateQty = (item) => {
  if (item.quantity < 1) item.quantity = 1;
  syncQuantityWithBackend(item.productId, item.quantity);
};

// -----------------------------------------
// 4. LOGIC XÓA SẢN PHẨM
// -----------------------------------------
const removeItem = async (productId) => {
  if (confirm('Bạn muốn xóa sản phẩm này khỏi giỏ hàng?')) {
    const userId = getCurrentUserId();

    if (!userId) {
      // KHÁCH VÃNG LAI
      cartItems.value = cartItems.value.filter(i => i.productId !== productId);
      localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
      window.dispatchEvent(new Event('cart-updated'));
      return;
    }

    // ĐÃ ĐĂNG NHẬP
    try {
      await axios.delete(`${API_URL}/remove/${productId}`); 
      await fetchCart();
      window.dispatchEvent(new Event('cart-updated'));
    } catch (error) {
      console.error('Lỗi xóa sản phẩm:', error);
    }
  }
};

const clearCart = async () => {
  if (confirm('Làm trống toàn bộ giỏ hàng?')) {
    const userId = getCurrentUserId();

    if (!userId) {
      // KHÁCH VÃNG LAI
      cartItems.value = [];
      localStorage.removeItem('guest_cart');
      window.dispatchEvent(new Event('cart-updated'));
      return;
    }

    // ĐÃ ĐĂNG NHẬP
    try {
      await axios.delete(`${API_URL}/${userId}/clear`);
      cartItems.value = [];
      window.dispatchEvent(new Event('cart-updated')); 
    } catch (error) {
      console.error('Lỗi dọn giỏ hàng:', error);
    }
  }
};

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0).replace('₫', 'đ');
};

const goToCheckout = () => {
  const userId = getCurrentUserId();
  
  // if (!userId) {
  //   alert('Vui lòng đăng nhập để tiến hành thanh toán!');
  //   // router.push('/login'); // Chuyển sang trang đăng nhập nếu cần
  //   return;
  // }

  // Kiểm tra xem có sản phẩm nào được chọn không (dựa trên biến computed selectedCount của bạn)
  if (selectedCount.value === 0) {
    alert('Vui lòng chọn ít nhất 1 sản phẩm để thanh toán!');
    return;
  }

  // Lưu thông tin tóm tắt đơn hàng vào sessionStorage
  const selectedItems = cartItems.value.filter(item => item.selected);
  const checkoutData = {
    items: selectedItems,
    subtotal: subtotal.value,
    tax: tax.value,
    total: total.value
  };
  sessionStorage.setItem('checkout_data', JSON.stringify(checkoutData));

  // Chuyển hướng sang trang Thanh toán
  router.push('/checkout');
};


</script>

<style scoped>
/* Toàn bộ CSS của bạn được giữ nguyên không mất một dòng nào */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.1); }
.btn-neon { background-color: #00FF33; color: #000; border: none; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); transition: all 0.2s; }
.narrow-container { max-width: 1100px !important; margin: 0 auto; }
.custom-checkbox .form-check-input { width: 20px; height: 20px; border-radius: 4px; border-color: #ccc; cursor: pointer; }
.custom-checkbox .form-check-input:checked { background-color: #00FF33; border-color: #00FF33; }
.custom-checkbox .form-check-input:checked[type=checkbox] {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'%3e%3cpath fill='none' stroke='%23000' stroke-linecap='round' stroke-linejoin='round' stroke-width='3' d='M6 10l3 3l6-6'/%3e%3c/svg%3e");
}
.hover-danger:hover { color: #dc3545 !important; }
.hover-dark:hover { color: #000 !important; }
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { -webkit-appearance: none; margin: 0; }
</style>