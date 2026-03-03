<template>
  <div class="checkout-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">
      
      <div class="checkout-stepper d-flex justify-content-center align-items-center gap-4 mb-5 fs-7 fw-bold text-uppercase">
        <div class="step-item text-muted">1. Giỏ hàng</div> 
        <div class="step-divider"></div>
        <div class="step-item text-neon">2. Thanh toán</div> 
        <div class="step-divider"></div>
        <div class="step-item text-muted">3. Hoàn tất</div> 
      </div>

      <div class="row g-4">
        <div class="col-lg-7">
          <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5">
            <h4 class="fw-black text-dark mb-4 text-uppercase">Thông tin nhận hàng</h4>
            <form @submit.prevent="handlePlaceOrder">
              <div class="row g-3">
                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Họ và tên</label>
                  <input v-model="shippingInfo.fullName" type="text" class="form-control custom-input" placeholder="Nhập họ tên người nhận" required>
                </div>

                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Số điện thoại</label>
                  <input v-model="shippingInfo.phone" type="text" class="form-control custom-input" placeholder="0xxx xxx XXX" required>
                </div>
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Email (Tùy chọn)</label>
                  <input v-model="shippingInfo.email" type="email" class="form-control custom-input" placeholder="example@gmail.com">
                </div>

                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Địa chỉ chi tiết</label>
                  <input v-model="shippingInfo.address" type="text" class="form-control custom-input" placeholder="Số nhà, tên đường, Phường/Xã..." required>
                </div>

                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Ghi chú đơn hàng</label>
                  <textarea v-model="orderNote" class="form-control custom-input" rows="3" placeholder="Ghi chú về thời gian giao hàng, chỉ dẫn địa chỉ..."></textarea>
                </div>
              </div>
            </form>
          </div>
        </div>

        <div class="col-lg-5">
          <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
            <h5 class="fw-black text-uppercase mb-4">Đơn hàng của bạn ({{ cartData.items.length }})</h5>
            
            <div class="order-items-list d-flex flex-column gap-3 mb-4">
              <div v-for="item in cartData.items" :key="item.productId" class="d-flex align-items-center gap-3">
                <div class="bg-light rounded-3 p-1" style="width: 60px; height: 60px;">
                  <img :src="item.imageUrl" class="img-fluid object-fit-contain" alt="Product">
                </div>
                <div class="flex-grow-1">
                  <h6 class="fw-bold fs-7 mb-0 line-clamp-1">{{ item.productName }}</h6>
                  <small class="text-muted fs-8">Số lượng: {{ item.quantity }}</small>
                </div>
                <div class="fw-bold fs-7">{{ formatCurrency(item.subTotal) }}</div>
              </div>
            </div>

            <label class="form-label fs-8 fw-bold text-muted text-uppercase">Mã giảm giá</label>
            <div class="input-group mb-4">
              <input v-model="voucherCode" type="text" class="form-control custom-input fs-7" placeholder="Nhập mã của bạn">
              <button @click="applyVoucher" class="btn btn-dark fw-bold px-3 fs-7 border-0" type="button">ÁP DỤNG</button>
            </div>

            <div class="d-flex flex-column gap-2 fs-7 mb-3">
              <div class="d-flex justify-content-between">
                <span class="text-muted">Tạm tính</span>
                <span class="fw-bold">{{ formatCurrency(subtotal) }}</span>
              </div>
              <div class="d-flex justify-content-between">
                <span class="text-muted">Phí vận chuyển</span>
                <span class="text-success fw-bold">Miễn phí</span>
              </div>
              <hr class="my-2 opacity-10">
              <div class="d-flex justify-content-between align-items-end">
                <span class="fw-black text-uppercase">Tổng cộng</span>
                <h4 class="text-neon fw-black mb-0">{{ formatCurrency(subtotal) }}</h4>
              </div>
            </div>
          </div>

          <div class="card border-0 shadow-sm rounded-4 p-4">
            <h5 class="fw-black text-uppercase mb-4">Phương thức thanh toán</h5>
            <div class="payment-methods d-flex flex-column gap-3">
              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="cod" checked>
                <label class="form-check-label d-block cursor-pointer ps-4" for="cod">
                  <span class="fw-bold fs-7 d-block">Thanh toán khi nhận hàng (COD)</span>
                  <small class="text-muted fs-8">Nhận hàng rồi mới thanh toán tiền mặt</small>
                </label>
              </div>

              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="bank">
                <label class="form-check-label d-block cursor-pointer ps-4" for="bank">
                  <span class="fw-bold fs-7 d-block">Chuyển khoản ngân hàng</span>
                  <small class="text-muted fs-8">Miễn phí phí chuyển tiền liên ngân hàng</small>
                </label>
              </div>
            </div>

            <button @click="handlePlaceOrder" :disabled="loading || cartData.items.length === 0" class="btn btn-neon w-100 fw-black py-3 rounded-3 mt-4 text-dark shadow-sm">
              <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
              {{ loading ? 'ĐANG XỬ LÝ...' : 'XÁC NHẬN ĐẶT HÀNG' }}
            </button>
            
            <router-link to="/cart" class="btn btn-link text-muted fw-bold fs-8 w-100 mt-2 text-decoration-none text-center d-block">
              <i class="bi bi-arrow-left me-1"></i> Quay lại giỏ hàng
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const API_BASE = 'http://localhost:8080/api';

const cartData = ref({ items: [], cartTotal: 0 });
const loading = ref(false);
const voucherCode = ref('');
const orderNote = ref('');

const shippingInfo = ref({
  fullName: '',
  phone: '',
  email: '',
  address: ''
});

// Hàm lấy User ID từ localStorage
const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      return JSON.parse(userInfoString).userId;
    } catch (e) { return null; }
  }
  return null;
};

// Hàm hỗ trợ lấy Headers chứa Token (Copy từ MyAccount)
const getAuthConfig = () => {
  const token = localStorage.getItem('jwt_token'); 
  return {
    headers: { Authorization: `Bearer ${token}` }
  };
};

// --- HÀM LẤY THÔNG TIN PROFILE ĐỂ AUTO-FILL ---
const fetchUserProfile = async () => {
  const userId = getCurrentUserId();
  if (!userId) return; // Nếu là khách vãng lai thì bỏ qua, để form trống

  try {
    const response = await axios.get(`http://localhost:8080/api/profile/${userId}`, getAuthConfig());
    const user = response.data;
    
    // Gán dữ liệu từ DB vào form (Khớp với các trường bên MyAccount)
    shippingInfo.value.fullName = user.fullName || '';
    shippingInfo.value.phone = user.phoneNumber || '';
    shippingInfo.value.email = user.email || '';
    shippingInfo.value.address = user.address || '';
  } catch (error) {
    console.error('Lỗi khi tải thông tin user:', error);
  }
};

// --- LẤY DỮ LIỆU TỪ SESSION STORAGE ---
onMounted(() => {
  // 1. Gọi hàm lấy thông tin user điền vào form
  fetchUserProfile();

  // 2. Lấy dữ liệu giỏ hàng cần thanh toán
  const checkoutDataStr = sessionStorage.getItem('checkout_data');
  
  if (checkoutDataStr) {
    const parsedData = JSON.parse(checkoutDataStr);
    
    // Đổi key (name, img) từ Cart.vue sang chuẩn (productName, imageUrl) cho Checkout
    cartData.value.items = parsedData.items.map(item => ({
      productId: item.productId,
      productName: item.name,
      imageUrl: item.img,
      quantity: item.quantity,
      price: item.price,
      subTotal: item.price * item.quantity
    }));
    
    cartData.value.cartTotal = parsedData.subtotal;
  } else {
    alert("Không có sản phẩm nào để thanh toán!");
    router.push('/cart');
  }
});

// --- TÍNH TOÁN ---
const subtotal = computed(() => cartData.value.cartTotal || 0);

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0).replace('₫', 'đ');
};

const applyVoucher = () => {
  if (!voucherCode.value) return;
  alert("Mã giảm giá đã được ghi nhận. Hệ thống sẽ áp dụng khi bạn nhấn Đặt hàng.");
};

// --- XỬ LÝ ĐẶT HÀNG ---
const handlePlaceOrder = async () => {
  if (!shippingInfo.value.fullName || !shippingInfo.value.phone || !shippingInfo.value.address) {
    alert("Vui lòng điền đầy đủ thông tin nhận hàng bắt buộc!");
    return;
  }

  const userId = getCurrentUserId();
  loading.value = true;
  
  try {
    let response;
    
    // 1. DÀNH CHO KHÁCH ĐÃ ĐĂNG NHẬP
    if (userId) {
      const payload = {
        note: `Người nhận: ${shippingInfo.value.fullName} - SĐT: ${shippingInfo.value.phone} - Đ/C: ${shippingInfo.value.address}. Ghi chú KH: ${orderNote.value}`,
        voucherCode: voucherCode.value,
        email: shippingInfo.value.email
      };
      // Nhớ truyền token khi gọi API placeOrder nếu backend của bạn yêu cầu bảo mật
      response = await axios.post(`${API_BASE}/orders/${userId}/place`, payload, getAuthConfig());
    } 
    // 2. DÀNH CHO KHÁCH VÃNG LAI
    else {
      const payload = {
        note: `Khách vãng lai: ${shippingInfo.value.fullName} - SĐT: ${shippingInfo.value.phone} - Đ/C: ${shippingInfo.value.address}. Ghi chú KH: ${orderNote.value}`,
        voucherCode: voucherCode.value,
        guestFullName: shippingInfo.value.fullName,
        guestPhone: shippingInfo.value.phone,
        guestEmail: shippingInfo.value.email,
        guestAddress: shippingInfo.value.address,
        items: cartData.value.items.map(item => ({
          productId: item.productId,
          quantity: item.quantity,
          price: item.price
        }))
      };
      response = await axios.post(`${API_BASE}/orders/guest/place`, payload);
      
      localStorage.removeItem('guest_cart');
    }

    alert(`🎉 Đặt hàng thành công! Mã đơn của bạn là: ${response.data.orderCode}`);
    
    sessionStorage.removeItem('checkout_data');
    window.dispatchEvent(new Event('cart-updated'));
    router.push(`/order/${response.data.orderCode}`);
    
  } catch (error) {
    const errorMsg = error.response?.data || "Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.";
    alert("❌ Lỗi: " + errorMsg);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }

.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; color: #000; border: none; transition: all 0.2s; }
.btn-neon:hover:not(:disabled) { background-color: #00cc29; transform: translateY(-2px); }
.btn-neon:disabled { opacity: 0.7; cursor: not-allowed; }

.narrow-container { max-width: 1000px !important; margin: 0 auto; }
.step-divider { width: 40px; height: 2px; background-color: #dee2e6; }

.custom-input {
  background-color: #F9FAFB;
  border: 1px solid #EAEAEA;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-size: 0.85rem;
}
.custom-input:focus {
  background-color: #FFF;
  border-color: #00FF33;
  box-shadow: none;
}

.custom-radio-payment { transition: all 0.2s ease; cursor: pointer; }
.custom-radio-payment:has(.form-check-input:checked) {
  border-color: #00FF33 !important;
  background-color: rgba(0, 255, 51, 0.05);
}
.custom-radio-payment .form-check-input:checked {
  background-color: #00FF33;
  border-color: #00FF33;
}
</style>