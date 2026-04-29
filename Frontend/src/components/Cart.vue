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
            <div v-for="item in cartItems" :key="item.productId + '-' + item.variantId" class="card border-0 shadow-sm rounded-3 overflow-hidden">
              <div class="card-body p-4 d-flex align-items-center gap-4">
                
                <div class="form-check custom-checkbox">
                  <input class="form-check-input shadow-none" type="checkbox" v-model="item.selected" @change="saveGuestCartState">
                </div>

                <div class="product-img-box bg-light rounded-3 d-flex align-items-center justify-content-center p-2" style="width: 100px; height: 100px;">
                  <img :src="item.img" class="img-fluid object-fit-contain" alt="Product">
                </div>

                <div class="flex-grow-1">
                  <h6 class="fw-bold mb-1 fs-6">{{ item.name }}</h6>
                  <p class="text-muted fs-8 mb-2">
                    Phân loại: {{ item.colorName || 'Mặc định' }} 
                    <span v-if="item.option2Value"> - {{ item.option2Value }}</span>
                  </p>
                  
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
              </div>

              <div class="d-flex justify-content-between align-items-end mb-4">
                <h5 class="fw-black m-0 fs-6">Tổng cộng</h5>
                <div class="text-end">
                  <h5 class=" fw-black mb-0">{{ formatCurrency(subtotal) }}</h5>
                </div>
              </div>
              
              <div class="d-flex justify-content-between align-items-end mb-4">
                  <small class="text-muted fs-9">TỔNG CỘNG GIÁ TRỊ ĐÃ BAO GỒM THUẾ VAT</small>
              </div>

              <button @click="goToCheckout" :disabled="selectedCount === 0 || isCheckingOut" class="btn btn-neon w-100 fw-black py-3 rounded-3 d-flex align-items-center justify-content-center gap-2 fs-7 text-dark shadow-sm">
                <span v-if="isCheckingOut" class="spinner-border spinner-border-sm me-1"></span>
                <template v-else>TIẾN HÀNH THANH TOÁN <i class="bi bi-lightning-fill"></i></template>
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
// Xóa import axios từ thư viện gốc, thay bằng Instance (thực thể) api của bạn
import api from '../utils/axios'; 
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2'; 

const router = useRouter();
// Thay đổi hằng số API_URL (địa chỉ URL giao diện lập trình) chỉ còn path tương đối
const API_URL = '/cart';

const cartItems = ref([]);
const selectAll = ref(false);
const isCheckingOut = ref(false);
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

const fetchCart = async () => {
  const userId = getCurrentUserId();
  
  if (!userId) {
    const guestCart = localStorage.getItem('guest_cart');
    if (guestCart) {
      const parsedCart = JSON.parse(guestCart);
      cartItems.value = parsedCart.map(item => ({
        ...item,
        selected: item.selected !== undefined ? item.selected : true 
      }));
    } else {
      cartItems.value = [];
    }
    return;
  }

  try {
    // Thay đổi axios.get thành api.get
    const response = await api.get(`${API_URL}/${userId}`);
    const backendItems = response.data.items || response.data;

    cartItems.value = backendItems.map(bItem => {
      const existingItem = cartItems.value.find(i => i.productId === bItem.productId && i.variantId === bItem.variantId);
      return {
        productId: bItem.productId,
        variantId: bItem.variantId,
        colorName: bItem.colorName,
        option2Value: bItem.option2Value,
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

const saveGuestCartState = () => {
  if (!getCurrentUserId()) {
    localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
  }
};

const toggleSelectAll = () => {
  cartItems.value.forEach(item => item.selected = selectAll.value);
  saveGuestCartState(); 
};

const selectedCount = computed(() => cartItems.value.filter(item => item.selected).length);

const subtotal = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((total, item) => total + (item.price * item.quantity), 0);
});

// ==========================================
// 1. LOGIC XỬ LÝ SỐ LƯỢNG KHI CÓ LỖI TỒN KHO
// ==========================================
const syncQuantityWithBackend = (item, newQuantity) => {
  const userId = getCurrentUserId();

  if (!userId) {
    const itemIndex = cartItems.value.findIndex(i => i.productId === item.productId && i.variantId === item.variantId);
    if (itemIndex !== -1) {
      cartItems.value[itemIndex].quantity = newQuantity;
      localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
      window.dispatchEvent(new Event('cart-updated'));
    }
    return;
  }

  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(async () => {
    try {
      // Thay đổi axios.put thành api.put
      await api.put(`${API_URL}/${userId}/update`, { 
        productId: item.productId,
        variantId: item.variantId,
        quantity: newQuantity
      });
      await fetchCart();
      window.dispatchEvent(new Event('cart-updated'));
    } catch (error) {
      // Axios trả về lỗi trực tiếp ở error.response.data
      const errorMsg = error.response?.data || 'Cập nhật thất bại';
      
      const match = typeof errorMsg === 'string' ? errorMsg.match(/Kho chỉ còn (\d+)/i) : null;
      
      if (match && match[1]) {
        const maxStock = parseInt(match[1], 10);
        
        Swal.fire({
          title: 'Vượt quá số lượng!',
          text: `Sản phẩm này hiện chỉ còn tối đa ${maxStock} chiếc trong kho!`,
          icon: 'warning',
          confirmButtonText: 'Đã hiểu',
          confirmButtonColor: '#00FF33',
          background: '#fff',
          customClass: { confirmButton: 'text-dark fw-bold' }
        });
        
        item.quantity = maxStock;

        try {
          // Thay đổi axios.put thành api.put
          await api.put(`${API_URL}/${userId}/update`, { 
            productId: item.productId,
            variantId: item.variantId,
            quantity: maxStock
          });
          await fetchCart();
          window.dispatchEvent(new Event('cart-updated'));
        } catch(e) {
          await fetchCart();
        }
      } else {
        Swal.fire({
          title: 'Lỗi',
          text: typeof errorMsg === 'string' ? errorMsg : 'Có lỗi xảy ra',
          icon: 'error',
          confirmButtonText: 'Đóng',
          confirmButtonColor: '#dc3545'
        });
        await fetchCart(); 
      }
    }
  }, 600); 
};

const increaseQty = (item) => {
  item.quantity++; 
  syncQuantityWithBackend(item, item.quantity);
};

const decreaseQty = (item) => {
  if (item.quantity > 1) {
    item.quantity--; 
    syncQuantityWithBackend(item, item.quantity);
  }
};

const updateQty = (item) => {
  if (item.quantity < 1) item.quantity = 1;
  syncQuantityWithBackend(item, item.quantity);
};

const removeItem = async (productId) => {
  const result = await Swal.fire({
    title: 'Xóa sản phẩm?',
    text: "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#dc3545',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  });

  if (result.isConfirmed) {
    const userId = getCurrentUserId();

    if (!userId) {
      cartItems.value = cartItems.value.filter(i => i.productId !== productId);
      localStorage.setItem('guest_cart', JSON.stringify(cartItems.value));
      window.dispatchEvent(new Event('cart-updated'));
      return;
    }

    try {
      // Thay đổi axios.delete thành api.delete
      await api.delete(`${API_URL}/${userId}/remove/${productId}`);
      await fetchCart();
      window.dispatchEvent(new Event('cart-updated'));
    } catch (error) {
      console.error('Lỗi xóa sản phẩm:', error);
    }
  }
};

const clearCart = async () => {
  const result = await Swal.fire({
    title: 'Làm trống giỏ hàng?',
    text: "Toàn bộ sản phẩm sẽ bị xóa khỏi giỏ. Bạn có chắc không?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#dc3545',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Làm trống',
    cancelButtonText: 'Hủy'
  });

  if (result.isConfirmed) {
    const userId = getCurrentUserId();

    if (!userId) {
      cartItems.value = [];
      localStorage.removeItem('guest_cart');
      window.dispatchEvent(new Event('cart-updated'));
      return;
    }

    try {
      // Thay đổi axios.delete thành api.delete
      await api.delete(`${API_URL}/${userId}/clear`);
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

// ==========================================
// 2. CHỐT CHẶN DOUBLE-CHECK TỒN KHO KHI THANH TOÁN
// ==========================================
const goToCheckout = async () => {
  const userId = getCurrentUserId();
  const selectedItems = cartItems.value.filter(item => item.selected);
  
  if (selectedItems.length === 0) return;
  
  isCheckingOut.value = true;

  try {
    for (const item of selectedItems) {
      // Thay đổi URL gọi đến product API
      const res = await api.get(`/product/${item.productId}`);
      const productData = res.data;

      let maxStock = productData.totalStock;
      if (item.variantId && productData.variants) {
        const variant = productData.variants.find(v => v.variantId === item.variantId);
        if (variant) maxStock = variant.stockQuantity;
      }

      if (item.quantity > maxStock) {
        Swal.fire({
          title: 'Lỗi tồn kho!',
          html: `Sản phẩm <b>"${item.name}" ${item.colorName ? '('+item.colorName+')' : ''}</b> hiện chỉ còn <b>${maxStock} chiếc</b> do vừa có khách hàng khác đặt mua.<br><br>Hệ thống đã tự động điều chỉnh lại số lượng trong giỏ của bạn.`,
          icon: 'error',
          confirmButtonText: 'Đã hiểu',
          confirmButtonColor: '#00FF33',
          customClass: { confirmButton: 'text-dark fw-bold' }
        });
        
        item.quantity = maxStock;

        if (userId) {
           // Thay đổi axios.put thành api.put
           await api.put(`${API_URL}/${userId}/update`, {
             productId: item.productId,
             variantId: item.variantId,
             quantity: maxStock
           });
           await fetchCart();
        } else {
           saveGuestCartState();
        }

        isCheckingOut.value = false;
        return; 
      }
    }

    const checkoutData = {
      items: selectedItems,
      subtotal: subtotal.value,
      total: subtotal.value
    };
    sessionStorage.setItem('checkout_data', JSON.stringify(checkoutData));
    router.push('/checkout');

  } catch (error) {
    console.error("Lỗi kiểm tra tồn kho:", error);
    Swal.fire('Lỗi', 'Có lỗi xảy ra khi kiểm tra giỏ hàng. Vui lòng tải lại trang!', 'error');
  } finally {
    isCheckingOut.value = false;
  }
};
</script>

<style scoped>
/* Toàn bộ CSS của bạn được giữ nguyên 100% */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.1); }
.btn-neon { background-color: #00FF33; color: #000; border: none; transition: 0.2s; }
.btn-neon:hover:not(:disabled) { background-color: #00cc29; transform: translateY(-2px); }
.btn-neon:disabled { background-color: #00FF33; opacity: 0.7; cursor: not-allowed; }
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