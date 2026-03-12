<template>
  <div class="checkout-page bg-light-gray pb-5 pt-4 min-vh-100">
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
                <input v-model="paymentMethod" value="COD" class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="cod">
                <label class="form-check-label d-block cursor-pointer ps-4" for="cod">
                  <span class="fw-bold fs-7 d-block">Thanh toán khi nhận hàng (COD)</span>
                  <small class="text-muted fs-8">Nhận hàng rồi mới thanh toán tiền mặt</small>
                </label>
              </div>

              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input v-model="paymentMethod" value="BANK" class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="bank">
                <label class="form-check-label d-block cursor-pointer ps-4" for="bank">
                  <span class="fw-bold fs-7 d-block">Chuyển khoản qua mã QR</span>
                  <small class="text-muted fs-8">Quét mã bằng ứng dụng ngân hàng hoặc ví điện tử</small>
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

    <div class="modal fade" id="qrPaymentModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-black fs-4 text-center w-100">Thanh toán đơn hàng</h5>
          </div>
          <div class="modal-body text-center pt-2 pb-4">
            <p class="text-muted fs-7 mb-3">Mở ứng dụng Ngân hàng của bạn và quét mã QR dưới đây để thanh toán.</p>
            
            <div class="bg-light p-3 rounded-4 d-inline-block mb-3 border">
              <img v-if="generatedQrUrl" :src="generatedQrUrl" alt="QR Code Thanh Toán" style="width: 250px; height: 250px; object-fit: contain;">
              <div v-else class="spinner-border text-neon m-5" role="status"></div>
            </div>

            <div class="bg-light-gray p-3 rounded-3 text-start mx-auto border" style="max-width: 320px;">
              <div class="d-flex justify-content-between mb-2 fs-7">
                <span class="text-muted">Người đặt:</span>
                <span class="fw-bold text-dark">{{ shippingInfo.fullName }}</span>
              </div>
              <div class="d-flex justify-content-between mb-2 fs-7">
                <span class="text-muted">Số tiền:</span>
                <span class="fw-black text-danger fs-5">{{ formatCurrency(subtotal) }}</span>
              </div>
              <div class="d-flex justify-content-between fs-7">
                <span class="text-muted">Nội dung CK:</span>
                <span class="fw-bold text-primary">TZ {{ shippingInfo.phone }}</span>
              </div>
            </div>
            
            <p class="fs-8 text-danger fw-bold mt-3 mb-0">
              <i class="bi bi-exclamation-triangle"></i> Lưu ý: Bạn chưa hoàn tất đặt hàng. Nhấn nút bên dưới sau khi chuyển khoản.
            </p>
          </div>
          <div class="modal-footer border-top-0 pt-0 d-flex gap-2 justify-content-center pb-4">
            <button type="button" class="btn btn-outline-dark fw-bold px-4" @click="handlePayLater">Đóng / Chọn lại</button>
            <button type="button" class="btn btn-neon fw-bold px-4 text-dark" @click="handlePaymentDone" :disabled="loading">
              <span v-if="loading" class="spinner-border spinner-border-sm me-1"></span> TÔI ĐÃ CHUYỂN KHOẢN
            </button>
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

const paymentMethod = ref('COD'); 
const generatedQrUrl = ref('');
const successOrderCode = ref('');
let qrModalInstance = null; 

// CẤU HÌNH NGÂN HÀNG
const BANK_ID = 'TPB'; 
const BANK_ACCOUNT_NO = '31413122007'; 
const BANK_ACCOUNT_NAME = 'PHAM DANG KHOA'; 

const shippingInfo = ref({ fullName: '', phone: '', email: '', address: '' });

const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try { return JSON.parse(userInfoString).userId; } catch (e) { return null; }
  }
  return null;
};

const getAuthConfig = () => {
  const token = localStorage.getItem('jwt_token'); 
  return { headers: { Authorization: `Bearer ${token}` } };
};

const fetchUserProfile = async () => {
  const userId = getCurrentUserId();
  if (!userId) return;

  try {
    const response = await axios.get(`${API_BASE}/profile/${userId}`, getAuthConfig());
    const user = response.data;
    shippingInfo.value.fullName = user.fullName || '';
    shippingInfo.value.phone = user.phoneNumber || '';
    shippingInfo.value.email = user.email || '';
    shippingInfo.value.address = user.address || '';
  } catch (error) {
    console.error('Lỗi khi tải thông tin user:', error);
  }
};

onMounted(() => {
  fetchUserProfile();

  const checkoutDataStr = sessionStorage.getItem('checkout_data');
  if (checkoutDataStr) {
    const parsedData = JSON.parse(checkoutDataStr);
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

const subtotal = computed(() => cartData.value.cartTotal || 0);

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0).replace('₫', 'đ');
};

const applyVoucher = () => {
  if (!voucherCode.value) return;
  alert("Mã giảm giá đã được ghi nhận. Hệ thống sẽ áp dụng khi bạn nhấn Đặt hàng.");
};

// ====================================================
// 1. KHI KHÁCH BẤM "XÁC NHẬN ĐẶT HÀNG" TRÊN FORM
// ====================================================
const handlePlaceOrder = () => {
  if (!shippingInfo.value.fullName || !shippingInfo.value.phone || !shippingInfo.value.address) {
    alert("Vui lòng điền đầy đủ thông tin nhận hàng bắt buộc!");
    return;
  }

  if (paymentMethod.value === 'BANK') {
    // CHUYỂN KHOẢN: Chỉ hiện Modal QR, CHƯA GỌI API lưu Data
    showQrModal();
  } else {
    // COD: Gọi API lưu thẳng xuống DB
    submitOrderToBackend();
  }
};

// ====================================================
// 2. HÀM HIỂN THỊ MÃ QR TẠM (Chưa lưu DB)
// ====================================================
const showQrModal = () => {
  const amount = subtotal.value;
  // Sử dụng SĐT làm nội dung chuyển khoản
  const addInfo = encodeURIComponent(`TZ ${shippingInfo.value.phone}`); 
  
  generatedQrUrl.value = `https://img.vietqr.io/image/${BANK_ID}-${BANK_ACCOUNT_NO}-compact2.png?amount=${amount}&addInfo=${addInfo}&accountName=${encodeURIComponent(BANK_ACCOUNT_NAME)}`;
  
  const modalElement = document.getElementById('qrPaymentModal');
  if (window.bootstrap) {
    qrModalInstance = new window.bootstrap.Modal(modalElement);
    qrModalInstance.show();
  } else {
    import('bootstrap').then(bootstrap => {
      qrModalInstance = new bootstrap.Modal(modalElement);
      qrModalInstance.show();
    }).catch(err => {
      console.error(err);
    });
  }
};

// ====================================================
// 3. KHÁCH BẤM ĐÓNG MODAL / ĐỔI PHƯƠNG THỨC 
// ====================================================
const handlePayLater = () => {
  if (qrModalInstance) {
    qrModalInstance.hide();
  }
  cleanupModalBackdrop();
  // KHÔNG LÀM GÌ CẢ. Để khách ở lại trang Checkout tự do thao tác tiếp
};

// ====================================================
// 4. KHÁCH BẤM "TÔI ĐÃ CHUYỂN KHOẢN" TRÊN MODAL
// ====================================================
const handlePaymentDone = async () => {
  // LÚC NÀY MỚI GỌI API ĐỂ LƯU VÀO DB
  await submitOrderToBackend();
};

// ====================================================
// 5. HÀM GỌI API BACKEND CHÍNH THỨC
// ====================================================
const submitOrderToBackend = async () => {
  const userId = getCurrentUserId();
  loading.value = true;
  
  try {
    let response;
    
    if (userId) {
      const payload = {
        note: `Người nhận: ${shippingInfo.value.fullName} - SĐT: ${shippingInfo.value.phone} - Đ/C: ${shippingInfo.value.address}. Ghi chú: ${orderNote.value}`,
        voucherCode: voucherCode.value,
        email: shippingInfo.value.email,
        paymentMethod: paymentMethod.value
      };
      response = await axios.post(`${API_BASE}/orders/${userId}/place`, payload, getAuthConfig());
    } 
    else {
      const payload = {
        note: `Khách vãng lai: ${shippingInfo.value.fullName} - SĐT: ${shippingInfo.value.phone} - Đ/C: ${shippingInfo.value.address}. Ghi chú: ${orderNote.value}`,
        voucherCode: voucherCode.value,
        guestFullName: shippingInfo.value.fullName,
        guestPhone: shippingInfo.value.phone,
        guestEmail: shippingInfo.value.email,
        guestAddress: shippingInfo.value.address,
        items: cartData.value.items.map(item => ({
          productId: item.productId,
          quantity: item.quantity,
          price: item.price
        })),
        paymentMethod: paymentMethod.value
      };
      response = await axios.post(`${API_BASE}/orders/guest/place`, payload);
      localStorage.removeItem('guest_cart');
    }

    successOrderCode.value = response.data.orderCode;
    sessionStorage.removeItem('checkout_data');
    window.dispatchEvent(new Event('cart-updated'));

    // ĐÓNG MODAL (Nếu đang mở bằng BANK)
    if (qrModalInstance) {
      qrModalInstance.hide();
      cleanupModalBackdrop();
    }

    // THÔNG BÁO THEO YÊU CẦU
    if (paymentMethod.value === 'BANK') {
      alert("Đơn hàng của bạn đã được lưu, vui lòng chờ hệ thống xác nhận.");
    } else {
      alert(`🎉 Đặt hàng thành công! Mã đơn của bạn là: ${successOrderCode.value}`);
    }
    
    router.push(`/order/${successOrderCode.value}`);
    
  } catch (error) {
    console.error("Lỗi khi đặt hàng:", error);
    const errorMsg = error.response?.data || "Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.";
    alert("❌ Lỗi: " + errorMsg);
  } finally {
    loading.value = false;
  }
};

const cleanupModalBackdrop = () => {
  const backdrop = document.querySelector('.modal-backdrop');
  if (backdrop) backdrop.remove();
  document.body.classList.remove('modal-open');
  document.body.style.overflow = '';
  document.body.style.paddingRight = '';
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