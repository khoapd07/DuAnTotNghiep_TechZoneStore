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

      <div class="row g-4" v-if="!showQrScreen">
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
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Địa chi chi tiết</label>
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
                  <p class="text-muted fs-8 mb-1">
                    Phân loại: {{ item.colorName || 'Mặc định' }} 
                    <span v-if="item.option2Value"> - {{ item.option2Value }}</span>
                  </p>
                  <small class="text-muted fs-8">Số lượng: {{ item.quantity }}</small>
                </div>
                <div class="fw-bold fs-7">{{ formatCurrency(item.subTotal) }}</div>
              </div>
            </div>

            <label class="form-label fs-8 fw-bold text-muted text-uppercase">Mã giảm giá</label>
            <div class="input-group mb-4">
              <input v-model="voucherCode" type="text" class="form-control custom-input fs-7" placeholder="Nhập mã của bạn" :disabled="discountAmount > 0">
              <button v-if="discountAmount === 0" @click="applyVoucher" class="btn btn-dark fw-bold px-3 fs-7 border-0" type="button" :disabled="isCheckingVoucher">
                <span v-if="isCheckingVoucher" class="spinner-border spinner-border-sm me-1"></span> ÁP DỤNG
              </button>
              <button v-else @click="removeVoucher" class="btn btn-danger fw-bold px-3 fs-7 border-0" type="button">HỦY MÃ</button>
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
              
              <div v-if="discountAmount > 0" class="d-flex justify-content-between text-danger">
                <span class="fw-bold">Giảm giá (Voucher)</span>
                <span class="fw-bold">- {{ formatCurrency(discountAmount) }}</span>
              </div>

              <hr class="my-2 opacity-10">
              <div class="d-flex justify-content-between align-items-end">
                <span class="fw-black text-uppercase">Tổng cộng</span>
                <h4 class="text-neon fw-black mb-0">{{ formatCurrency(finalAmount) }}</h4>
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

      <div v-else class="checkout-qr-wrapper py-4 fade-in">
        <div class="card bg-dark-panel border-0 rounded-4 shadow-lg overflow-hidden text-white mx-auto" style="max-width: 750px;">
          
          <div class="p-3 border-bottom border-secondary border-opacity-25 bg-dark-panel d-flex justify-content-between align-items-center">
            <button @click="cancelQrPayment" class="btn btn-link text-white text-decoration-none d-flex align-items-center gap-2 fw-bold custom-hover p-0">
              <span class="material-symbols-outlined fs-5">arrow_back</span>
              Hủy và quay lại
            </button>
            <span class="badge bg-dark border border-secondary text-neon px-3 py-2 rounded-pill d-flex align-items-center gap-2">
              <span class="material-symbols-outlined fs-6">verified_user</span> An toàn
            </span>
          </div>

          <div class="row g-0">
            <div class="col-lg-5 p-4 border-end border-secondary border-opacity-25">
              <h5 class="fw-bold mb-4 text-white text-uppercase">Thông tin thanh toán</h5>

              <div class="d-flex flex-column gap-3">
                <div>
                  <p class="text-neon small fw-bold mb-1 text-uppercase">Mã đơn hàng</p>
                  <div class="fw-bold fs-6 text-white">{{ successOrderCode }}</div>
                </div>

                <div>
                  <p class="text-neon small fw-bold mb-1 text-uppercase">Số tiền cần chuyển</p>
                  <h3 class="text-white fw-black mb-0">{{ formatCurrency(finalAmount) }}</h3>
                </div>

                <div class="p-3 mt-1 rounded-3 bg-dark-lighter border border-secondary border-opacity-25">
                  <div class="mb-3">
                    <p class="text-neon small fw-bold mb-1 text-uppercase">Chủ tài khoản</p>
                    <div class="fw-bold text-white">{{ bankAccountName }}</div>
                  </div>
                  <div class="mb-3">
                    <p class="text-neon small fw-bold mb-1 text-uppercase">Số tài khoản</p>
                    <div class="d-flex align-items-center justify-content-between gap-2">
                      <span class="fw-bold fs-5 text-white">{{ bankAccountNumber }}</span>
                      <button @click="copyText(bankAccountNumber)" class="btn btn-sm btn-outline-secondary border-0 p-1 text-neon custom-hover">
                        <span class="material-symbols-outlined fs-6">content_copy</span>
                      </button>
                    </div>
                  </div>
                  <div>
                    <p class="text-neon small fw-bold mb-1 text-uppercase">Nội dung chuyển khoản</p>
                    <div class="d-flex align-items-center justify-content-between gap-2">
                      <span class="fw-bold fs-6 text-white">{{ payosTransferContent }}</span>
                      <button @click="copyText(payosTransferContent)" class="btn btn-sm btn-outline-secondary border-0 p-1 text-neon custom-hover">
                        <span class="material-symbols-outlined fs-6">content_copy</span>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-7 p-4 bg-dark-lighter d-flex flex-column align-items-center justify-content-center text-center">
              
              <div class="mb-3 d-flex flex-column align-items-center">
                <h6 class="text-neon fw-bold d-flex align-items-center gap-2 mb-2">
                  <span class="material-symbols-outlined">timer</span>
                  Đơn hàng hết hạn sau
                </h6>
                <div class="d-flex gap-2 fw-black fs-3 text-neon">
                  <div class="bg-dark px-3 py-1 rounded border border-secondary">{{ minutes }}</div>
                  <span>:</span>
                  <div class="bg-dark px-3 py-1 rounded border border-secondary">{{ seconds }}</div>
                </div>
              </div>

              <div class="position-relative bg-white p-2 rounded-4 shadow-sm mb-4" style="width: 280px; height: 280px;">
                <img :src="generatedQrUrl" alt="VietQR" class="w-100 h-100 object-fit-contain rounded-3">
                <div class="scan-line"></div>
              </div>

              <div class="d-flex gap-2 justify-content-center mb-3">
                <span class="badge bg-neon text-dark px-3 py-2 rounded-pill d-flex align-items-center gap-1 fw-bold">
                  <span class="material-symbols-outlined fs-6">bolt</span> VietQR
                </span>
                <span class="badge bg-dark border border-secondary text-light px-3 py-2 rounded-pill d-flex align-items-center gap-1">
                  Napas 247
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="position-fixed top-0 end-0 p-3" style="z-index: 1100">
        <div v-if="showToast" class="toast show bg-neon text-dark fw-bold border-0 shadow">
          <div class="toast-body d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">check_circle</span>
            Đã sao chép: <b>{{ copiedText }}</b>
          </div>
        </div>
      </div>
      
    </div>

    <div v-if="customModal.show" class="custom-modal-overlay d-flex justify-content-center align-items-center">
      <div class="custom-modal bg-white rounded-4 p-4 text-center shadow-lg mx-3">
        <div class="mb-3">
          <i v-if="customModal.icon === 'success'" class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>
          <i v-else-if="customModal.icon === 'error'" class="bi bi-x-circle-fill text-danger" style="font-size: 3.5rem;"></i>
          <i v-else-if="customModal.icon === 'warning'" class="bi bi-exclamation-triangle-fill text-warning" style="font-size: 3.5rem;"></i>
        </div>
        <h5 class="fw-bold mb-2">{{ customModal.title }}</h5>
        <p class="text-muted fs-8 mb-4" v-html="customModal.message"></p>
        <div class="d-flex justify-content-center">
          <button @click="closeCustomModal" class="btn btn-dark fs-8 fw-bold px-4 py-2 rounded-2 w-100 text-uppercase">
            XÁC NHẬN
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import Swal from 'sweetalert2';

const router = useRouter();
const API_BASE = 'http://localhost:8080/api';

const cartData = ref({ items: [], cartTotal: 0 });
const loading = ref(false);
const voucherCode = ref('');
const orderNote = ref('');

const discountAmount = ref(0);
const isCheckingVoucher = ref(false);

const paymentMethod = ref('COD'); 
// Biến này giờ sẽ dùng chung: Lúc đầu chứa mã tạm của PayOS, lúc sau chứa mã thật của DB
const successOrderCode = ref('');
const shippingInfo = ref({ fullName: '', phone: '', email: '', address: '' });

const showQrScreen = ref(false);
const timeRemaining = ref(5 * 60); 
let timerInterval = null;
let checkPaymentInterval = null;

const minutes = computed(() => Math.floor(timeRemaining.value / 60).toString().padStart(2, '0'));
const seconds = computed(() => (timeRemaining.value % 60).toString().padStart(2, '0'));

const generatedQrUrl = ref('');
const bankAccountName = ref('');
const bankAccountNumber = ref('');
const payosTransferContent = ref('');

// --- CẤU HÌNH CUSTOM MODAL ---
const customModal = ref({
  show: false,
  icon: 'success', // 'success', 'error', 'warning'
  title: '',
  message: '',
  onClose: null // Function (hàm) callback thực thi sau khi bấm ĐÓNG
});

const showModal = (icon, title, message, onClose = null) => {
  customModal.value = {
    show: true,
    icon,
    title,
    message,
    onClose
  };
};

const closeCustomModal = () => {
  customModal.value.show = false;
  if (customModal.value.onClose) {
    customModal.value.onClose();
  }
};
// -----------------------------

const showToast = ref(false);
const copiedText = ref('');
const copyText = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    copiedText.value = text;
    showToast.value = true;
    setTimeout(() => { showToast.value = false; }, 3000);
  } catch (err) { }
};

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

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0).replace('₫', 'đ');
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
    console.error('Lỗi tải user:', error);
  }
};

onMounted(async () => {
  fetchUserProfile();
  const checkoutDataStr = sessionStorage.getItem('checkout_data');
  if (checkoutDataStr) {
    const parsedData = JSON.parse(checkoutDataStr);
    cartData.value.items = parsedData.items.map(item => ({
      productId: item.productId,
      variantId: item.variantId,
      productName: item.name,
      colorName: item.colorName,
      option2Value: item.option2Value,
      imageUrl: item.img,
      quantity: item.quantity,
      price: item.price,
      subTotal: item.price * item.quantity
    }));
    cartData.value.cartTotal = parsedData.subtotal;
  } else {
    // SỬ DỤNG MODAL thay vì alert
    showModal('warning', 'Giỏ hàng trống', 'Không có sản phẩm nào để thanh toán!', () => {
      router.push('/cart');
    });
  }
});

const subtotal = computed(() => cartData.value.cartTotal || 0);
const finalAmount = computed(() => {
  const final = subtotal.value - discountAmount.value;
  return final < 0 ? 0 : final;
});

const applyVoucher = async () => {
  if (!voucherCode.value || voucherCode.value.trim() === '') {
    // SỬ DỤNG MODAL thay vì alert
    showModal('warning', 'Thiếu thông tin', 'Vui lòng nhập mã giảm giá để áp dụng!');
    return;
  }
  isCheckingVoucher.value = true;
  try {
    const response = await axios.get(`${API_BASE}/vouchers/check`, {
      params: { code: voucherCode.value.trim(), orderValue: subtotal.value }
    });
    discountAmount.value = response.data;
    // SỬ DỤNG MODAL thay vì alert
    showModal('success', 'Thành công', 'Đã áp dụng mã giảm giá thành công cho đơn hàng!');
  } catch (error) {
    discountAmount.value = 0;
    // SỬ DỤNG MODAL thay vì alert
    showModal('error', 'Không hợp lệ', error.response?.data || "Mã giảm giá không hợp lệ hoặc đã hết hạn.");
  } finally {
    isCheckingVoucher.value = false;
  }
};

const removeVoucher = () => {
  voucherCode.value = '';
  discountAmount.value = 0;
};

// =========================================================
// HÀM 1: CHIA LUỒNG KHI BẤM NÚT "XÁC NHẬN ĐẶT HÀNG"
// =========================================================
const handlePlaceOrder = async () => {
  if (!shippingInfo.value.fullName || !shippingInfo.value.phone || !shippingInfo.value.address) {
    // SỬ DỤNG MODAL thay vì alert
    showModal('warning', 'Thiếu thông tin', 'Vui lòng điền đầy đủ <b>Họ tên, Số điện thoại và Địa chỉ</b> người nhận!');
    return;
  }

  if (paymentMethod.value === 'BANK') {
    // LUỒNG 1: BẬT MÀN HÌNH QR TRƯỚC, CHƯA TẠO ĐƠN
    loading.value = true;
    try {
      const tempTxnCode = Math.floor(Date.now() / 1000); 
      successOrderCode.value = tempTxnCode.toString();

      const payRes = await axios.post(`${API_BASE}/payment/create`, {
        orderCode: tempTxnCode, 
        productName: "Thanh toán đơn hàng",
        description: "TZ " + tempTxnCode,
        returnUrl: "http://localhost:5173", cancelUrl: "http://localhost:5173",
        price: Math.round(finalAmount.value)
      });

      if (payRes.data.error === 0) {
        const payosData = payRes.data.data;
        bankAccountName.value = payosData.accountName;
        bankAccountNumber.value = payosData.accountNumber;
        payosTransferContent.value = payosData.description; 
        generatedQrUrl.value = `https://img.vietqr.io/image/${payosData.bin}-${payosData.accountNumber}-compact2.png?amount=${payosData.amount}&addInfo=${encodeURIComponent(payosData.description)}&accountName=${encodeURIComponent(payosData.accountName)}`;
        
        showQrScreen.value = true;
        startQrTimer();
      } else {
        showModal('error', 'Lỗi thanh toán', "Không thể tạo mã QR PayOS: " + payRes.data.message);
      }
    } catch (e) {
      console.log("PayOS init error", e);
      showModal('error', 'Lỗi kết nối', 'Không thể kết nối đến cổng thanh toán. Vui lòng thử lại sau.');
    } finally {
      loading.value = false;
    }
  } else {
    // LUỒNG 2: COD THÌ TẠO ĐƠN LUÔN VÀ KHÔNG GHI NHẬN ĐÃ THANH TOÁN
    await executeCreateOrder(false); 
  }
};

// =========================================================
// HÀM 2: HÀM GỌI API TẠO ĐƠN (Được gọi cho COD, hoặc sau khi BANK thành công)
// =========================================================
const executeCreateOrder = async (isAlreadyPaid) => {
  loading.value = true;
  const userId = getCurrentUserId();
  try {
    const basePayload = {
      note: `Người nhận: ${shippingInfo.value.fullName} - SĐT: ${shippingInfo.value.phone} - Đ/C: ${shippingInfo.value.address}. Ghi chú: ${orderNote.value}`,
      voucherCode: voucherCode.value.trim(), 
      paymentMethod: paymentMethod.value,
      isPaid: isAlreadyPaid, // Chuyển biến này cho Java Spring Boot
      items: cartData.value.items.map(item => ({
        productId: item.productId,
        variantId: item.variantId, 
        quantity: item.quantity,
        price: item.price
      }))
    };

    let response;
    if (userId) {
      response = await axios.post(`${API_BASE}/orders/${userId}/place`, { ...basePayload, email: shippingInfo.value.email }, getAuthConfig());
    } else {
      response = await axios.post(`${API_BASE}/orders/guest/place`, {
        ...basePayload,
        guestFullName: shippingInfo.value.fullName,
        guestPhone: shippingInfo.value.phone,
        guestEmail: shippingInfo.value.email,
        guestAddress: shippingInfo.value.address,
      });
      // Dọn giỏ hàng guest
      let guestCart = JSON.parse(localStorage.getItem('guest_cart')) || [];
      const boughtItems = cartData.value.items;
      guestCart = guestCart.filter(cartItem => !boughtItems.some(bought => bought.productId === cartItem.productId && bought.variantId === cartItem.variantId));
      localStorage.setItem('guest_cart', JSON.stringify(guestCart));
    }

    // Lấy mã đơn hàng THẬT từ DB trả về
    const realOrderCodeFromDB = response.data.orderCode;
    
    sessionStorage.removeItem('checkout_data'); 
    window.dispatchEvent(new Event('cart-updated')); 

    // CHÚ Ý: Đã thay đổi class "text-neon" thành "text-white" ở dòng hiển thị mã đơn hàng
    if (!isAlreadyPaid) {
      showModal(
        'success', 
        'Đặt hàng thành công!', 
        `Mã đơn hàng của bạn là: <br><span class="bg-dark text-white fs-6 fw-bold px-3 py-1 rounded d-inline-block mt-2 tracking-wide">${realOrderCodeFromDB}</span>`, 
        () => {
          router.push(`/order/${realOrderCodeFromDB}`);
        }
      );
    }

    return realOrderCodeFromDB; // Trả về mã thật cho luồng BANK dùng

  } catch (error) {
    showModal('error', 'Lỗi tạo đơn', error.response?.data || "Vui lòng thử lại sau.");
    return null;
  } finally {
    loading.value = false;
  }
};


const startQrTimer = () => {
  timeRemaining.value = 5 * 60; 
  timerInterval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
    } else {
      clearInterval(timerInterval);
      clearInterval(checkPaymentInterval);
      Swal.fire({
        title: 'Đã hết thời gian!',
        text: 'Mã QR thanh toán này đã hết hạn.',
        icon: 'error',
        background: '#1a1a1a', color: '#ffffff', confirmButtonColor: '#dc3545',
        confirmButtonText: 'ĐÓNG'
      }).then(() => cancelQrPayment());
    }
  }, 1000);

  checkPaymentInterval = setInterval(async () => {
    try {
      const res = await axios.get(`${API_BASE}/payment/check-status/${successOrderCode.value}`);
      if (res.data.data === true) { 
        clearInterval(checkPaymentInterval);
        clearInterval(timerInterval);
        await handlePaymentSuccess();
      }
    } catch (error) {}
  }, 2000);
};

// =========================================================
// HÀM 3: XỬ LÝ KHI TIỀN ĐÃ VÀO TÀI KHOẢN (PAYOS TRẢ VỀ TRUE)
// =========================================================
const handlePaymentSuccess = async () => {
  Swal.fire({
    title: 'Đang ghi nhận đơn hàng...',
    text: 'Hệ thống đã nhận được tiền, vui lòng chờ giây lát!',
    allowOutsideClick: false,
    background: '#1a1a1a', color: '#ffffff',
    didOpen: () => { Swal.showLoading(); }
  });

  const realOrderCode = await executeCreateOrder(true); 

  if (realOrderCode) {
    let timerIntervalSwal;
    Swal.fire({
      title: 'Thanh toán thành công!',
      html: 'Đơn hàng đã được tạo.<br>Tự động chuyển trang trong <b></b> giây.',
      icon: 'success',
      background: '#1a1a1a', color: '#ffffff', confirmButtonColor: '#00FF33',
      allowOutsideClick: false, timer: 3000, timerProgressBar: true,
      didOpen: () => {
        const b = Swal.getHtmlContainer().querySelector('b');
        timerIntervalSwal = setInterval(() => { b.textContent = Math.ceil(Swal.getTimerLeft() / 1000); }, 100);
      },
      willClose: () => {
        clearInterval(timerIntervalSwal);
        router.push(`/order/${realOrderCode}`);
      }
    }).then((res) => { if(res.isConfirmed) router.push(`/order/${realOrderCode}`); });
  } else {
      Swal.fire('Lỗi', 'Không thể tạo đơn hàng. Vui lòng liên hệ Hotline!', 'error');
  }
};

// =========================================================
// HÀM 4: HỦY THANH TOÁN QR
// =========================================================
const cancelQrPayment = async () => {
  if (timerInterval) clearInterval(timerInterval);
  if (checkPaymentInterval) clearInterval(checkPaymentInterval);
  showQrScreen.value = false; 
};

onUnmounted(() => {
  if (timerInterval) clearInterval(timerInterval);
  if (checkPaymentInterval) clearInterval(checkPaymentInterval);
});
</script>

<style scoped>
/* TOÀN BỘ CSS CỦA BẠN GIỮ NGUYÊN */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }

.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.text-white { color: #FFFFFF !important; } /* Đảm bảo class này hoạt động */
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

/* DŨNG LƯU Ý SỐ 16: STYLE RIÊNG CHO MÀN HÌNH QR ĐEN & TIA LASER */
.bg-dark-panel { background-color: #1a1a1a !important; }
.bg-dark-lighter { background-color: #242424 !important; }
.bg-neon { background-color: #00FF33 !important; }

.custom-hover:hover {
  color: #00FF33 !important;
  opacity: 0.8;
}

.fade-in { animation: fadeIn 0.4s ease-in-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.scan-line {
  position: absolute;
  top: 10%; left: 10%;
  width: 80%; height: 2px;
  background-color: rgba(0, 255, 51, 0.8);
  box-shadow: 0 0 12px rgba(0, 255, 51, 0.6);
  animation: scan 2s infinite linear;
}
@keyframes scan {
  0% { top: 10%; opacity: 0; }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% { top: 90%; opacity: 0; }
}

/* --- CSS CHO CUSTOM MODAL --- */
.custom-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5); /* Lớp nền đen mờ */
  z-index: 1050; 
  animation: fadeIn 0.2s ease-in-out;
}

.custom-modal {
  width: 90%;
  max-width: 400px;
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>