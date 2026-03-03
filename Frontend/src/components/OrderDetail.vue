<template>
  <div class="order-tracking-page pb-5 pt-4 min-vh-100" style="background-color: #F8FBF9;">
    <div class="container narrow-container">
      
      <div class="d-flex flex-wrap justify-content-between align-items-end mb-4">
        <div>
          <h3 class="fw-black text-dark mb-1">Theo Dõi Đơn Hàng</h3>
          <span class="text-muted fs-7">Mã đơn hàng: <span class="fw-bold text-dark">{{ orderData.orderCode }}</span></span>
        </div>
        <div class="text-end mt-3 mt-md-0">
          <div class="text-muted fs-8 mb-1">Ngày đặt: {{ orderData.orderDate }}</div>
          <h3 class="text-success fw-black mb-0">{{ formatCurrency(orderData.totalAmount) }}</h3>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        
        <div v-if="orderData.status !== 4" class="stepper-wrapper d-flex justify-content-between position-relative my-4 px-3 px-md-5">
          <div class="stepper-progress-bar">
            <div class="stepper-progress" :style="{ width: progressWidth }"></div>
          </div>

          <div class="stepper-item text-center z-1" :class="{ 'active': orderData.status >= 0 }">
            <div class="step-icon"><i class="bi bi-receipt"></i></div>
            <div class="step-title mt-2">Chờ xác nhận</div>
            <div class="step-time text-success fw-bold" v-if="orderData.status === 0">Hiện tại</div>
            <div class="step-time" v-else>{{ orderData.orderDate }}</div>
          </div>

          <div class="stepper-item text-center z-1" :class="{ 'active': orderData.status >= 1 }">
            <div class="step-icon"><i class="bi bi-box-seam"></i></div>
            <div class="step-title mt-2">Đã xác nhận</div>
            <div class="step-time text-success fw-bold" v-if="orderData.status === 1">Hiện tại</div>
          </div>

          <div class="stepper-item text-center z-1" :class="{ 'active': orderData.status >= 2 }">
            <div class="step-icon"><i class="bi bi-truck"></i></div>
            <div class="step-title mt-2">Đang giao hàng</div>
            <div class="step-time text-success fw-bold" v-if="orderData.status === 2">Hiện tại</div>
          </div>

          <div class="stepper-item text-center z-1" :class="{ 'active': orderData.status >= 3 }">
            <div class="step-icon"><i class="bi bi-check2-circle"></i></div>
            <div class="step-title mt-2">Thành công</div>
            <div class="step-time text-success fw-bold" v-if="orderData.status === 3">Hiện tại</div>
          </div>
        </div>

        <div v-else class="text-center my-4">
          <i class="bi bi-x-circle text-danger" style="font-size: 3rem;"></i>
          <h5 class="text-danger fw-bold mt-2">Đơn hàng đã bị hủy</h5>
        </div>

        <div class="alert border-0 d-flex justify-content-between align-items-center rounded-3 fs-7 fw-bold mt-4 mb-0"
             :class="orderData.status === 4 ? 'bg-danger-subtle text-danger' : 'bg-success-subtle text-success'">
          <div>
            <i class="bi me-2" :class="orderData.status === 4 ? 'bi-exclamation-circle' : 'bi-info-circle'"></i> 
            {{ statusMessage }}
          </div>
          <div v-if="orderData.status < 3" class="text-dark">
            Dự kiến nhận: <span class="text-success">{{ orderData.estimatedDelivery }}</span>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h6 class="fw-bold mb-4 d-flex align-items-center gap-2">
          <i class="bi bi-geo-alt-fill text-success fs-5"></i> Thông tin giao hàng
        </h6>
        
        <div class="row g-3 fs-7">
          <div class="col-md-12 border-bottom pb-3">
            <div class="text-muted fs-8 text-uppercase mb-1">Người nhận</div>
            <div class="fw-bold text-dark fs-6">{{ orderData.shippingInfo.name }}</div>
            <div class="text-muted">{{ orderData.shippingInfo.phone }}</div>
          </div>
          <div class="col-md-12 pt-2">
            <div class="text-muted fs-8 text-uppercase mb-1">Địa chỉ</div>
            <div class="text-dark">{{ orderData.shippingInfo.address }}</div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-4 mb-5">
        <h6 class="fw-bold mb-3 d-flex align-items-center gap-2">
          <i class="bi bi-box-fill text-success fs-5"></i> Đơn vị vận chuyển
        </h6>
        
        <div class="d-flex align-items-center gap-3 border rounded-3 p-3">
          <div class="bg-warning-subtle text-warning d-flex align-items-center justify-content-center rounded" style="width: 40px; height: 40px;">
            <i class="bi bi-lightning-fill fs-5"></i>
          </div>
          <div>
            <div class="fw-bold fs-7">{{ orderData.carrier.name }}</div>
            <div class="text-muted fs-8">Mã vận đơn: {{ orderData.carrier.trackingCode }}</div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <router-link to="/products" class="btn btn-success fw-bold px-5 py-2 rounded-pill shadow-sm mb-3">
          Tiếp tục mua sắm <i class="bi bi-bag-check ms-1"></i>
        </router-link>
        <div class="d-block">
          <a href="#" class="text-muted text-decoration-none fs-8 hover-dark">
            <i class="bi bi-headset me-1"></i> Cần hỗ trợ về đơn hàng này?
          </a>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

// MOCK DATA: Chờ ráp API thực tế
// Status: 0 (Pending), 1 (Confirmed), 2 (Shipping), 3 (Delivered), 4 (Cancelled)
const orderData = ref({
  orderCode: route.params.id || '#TZ-7798124',
  orderDate: new Date().toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' }),
  totalAmount: 25490000,
  status: 0, // SỬA Ở ĐÂY: Đổi từ 2 thành 0 để mặc định là "Chờ xác nhận"
  estimatedDelivery: 'Đang cập nhật', // Đơn mới đặt thường chưa có ngày dự kiến chính xác
  shippingInfo: {
    name: 'Nguyễn Minh Quân',
    phone: '0908 XXX 123',
    address: 'Số 45, Ngõ 123, Đường Xuân Thủy, Phường Dịch Vọng Hậu, Quận Cầu Giấy, Hà Nội'
  },
  carrier: {
    name: 'Đang chờ điều phối', // Chưa có đơn vị vận chuyển
    trackingCode: 'Chưa có'
  }
});

onMounted(() => {
  // Tương lai bạn sẽ gọi API ở đây:
  // axios.get(`http://localhost:8080/api/orders/${route.params.id}`).then(...)
});

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value).replace('₫', 'đ');
};

// Tính toán thanh tiến trình (Progress bar width)
const progressWidth = computed(() => {
  if (orderData.value.status === 0) return '0%';   // Pending
  if (orderData.value.status === 1) return '33%';  // Confirmed
  if (orderData.value.status === 2) return '66%';  // Shipping
  if (orderData.value.status === 3) return '100%'; // Delivered
  return '0%'; // Cancelled không dùng thanh này
});

// Lời nhắn tự động thay đổi theo chuẩn Database
const statusMessage = computed(() => {
  switch (orderData.value.status) {
    case 0: return 'Đơn hàng (Pending) đang chờ shop kiểm tra và xác nhận.';
    case 1: return 'Đơn hàng (Confirmed) đã được xác nhận và đang đóng gói.';
    case 2: return 'Kiện hàng (Shipping) đang trên đường giao. Vui lòng chú ý điện thoại!';
    case 3: return 'Giao hàng thành công (Delivered). Cảm ơn bạn đã mua sắm!';
    case 4: return 'Đơn hàng (Cancelled) đã bị hủy.';
    default: return 'Đang cập nhật trạng thái...';
  }
});
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.text-success { color: #1ED760 !important; }
.btn-success { background-color: #1ED760; border: none; color: #000; }
.btn-success:hover { background-color: #17c153; color: #000; }
.bg-success-subtle { background-color: #e8fbf0 !important; }
.hover-dark:hover { color: #000 !important; }

.narrow-container { max-width: 800px !important; margin: 0 auto; }

/* --- STEPPER CSS --- */
.stepper-wrapper {
  position: relative;
}
.stepper-progress-bar {
  position: absolute;
  top: 20px;
  left: 5%;
  right: 5%;
  height: 3px;
  background-color: #e9ecef;
  z-index: 0;
}
.stepper-progress {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background-color: #1ED760;
  transition: width 0.4s ease;
}

.stepper-item {
  width: 25%;
}
.step-icon {
  width: 44px;
  height: 44px;
  background-color: #e9ecef;
  color: #adb5bd;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  font-size: 1.2rem;
  transition: all 0.3s ease;
  border: 4px solid #fff; /* Tạo viền cắt ngang đường progress */
}

.step-title {
  font-size: 0.8rem;
  font-weight: 700;
  color: #adb5bd;
}
.step-time {
  font-size: 0.7rem;
  color: #adb5bd;
  margin-top: 2px;
}

/* Trạng thái Active */
.stepper-item.active .step-icon {
  background-color: #1ED760;
  color: #fff;
  box-shadow: 0 0 0 3px rgba(30, 215, 96, 0.2);
}
.stepper-item.active .step-title {
  color: #000;
}
.stepper-item.active .step-time {
  color: #1ED760;
}
</style>