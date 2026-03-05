<template>
  <div class="shipping-page bg-light-gray min-vh-100 pb-5">
    
    <header class="bg-white shadow-sm sticky-top" style="z-index: 1020;">
      <div class="container-fluid narrow-container py-3 d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center gap-3">
          <div class="bg-neon-light text-neon rounded-3 d-flex align-items-center justify-content-center" style="width: 45px; height: 45px;">
            <i class="bi bi-truck fs-4"></i>
          </div>
          <div>
            <h4 class="fw-black text-dark mb-0 fs-5 text-uppercase">Trung tâm vận chuyển</h4>
            <span class="text-muted fs-8">Hệ thống xử lý đơn hàng TechZone</span>
          </div>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="text-end d-none d-md-block">
            <h6 class="fw-bold m-0 fs-7">Shipper: Nguyễn Văn Tài</h6>
            <small class="text-success fw-bold fs-8">● Đang hoạt động</small>
          </div>
          <div class="avatar bg-dark text-white fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
            T
          </div>
        </div>
      </div>
    </header>

    <div class="container-fluid narrow-container pt-4">
      
      <div class="row g-3 mb-4">
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0 d-flex align-items-center gap-2">
              <i class="bi bi-box-seam text-primary fs-5"></i> Chờ lấy hàng
            </p>
            <h2 class="fw-black text-dark m-0 mt-2">12</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0 d-flex align-items-center gap-2">
              <i class="bi bi-bicycle text-warning fs-5"></i> Đang giao
            </p>
            <h2 class="fw-black text-dark m-0 mt-2">8</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0 d-flex align-items-center gap-2">
              <i class="bi bi-check-circle text-success fs-5"></i> Hoàn thành
            </p>
            <h2 class="fw-black text-dark m-0 mt-2">45</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-dark text-white h-100">
            <p class="text-gray fs-8 fw-bold text-uppercase m-0">Tiền thu hộ (COD)</p>
            <h3 class="fw-black text-neon m-0 mt-2">12,500,000₫</h3>
          </div>
        </div>
      </div>

      <div class="d-flex gap-2 overflow-auto hide-scrollbar mb-4 pb-1">
        <button 
          v-for="tab in tabs" 
          :key="tab.value"
          @click="activeTab = tab.value"
          class="btn fw-bold fs-7 rounded-pill px-4 text-nowrap transition-all"
          :class="activeTab === tab.value ? 'btn-dark text-white shadow' : 'btn-white text-muted border bg-white'">
          {{ tab.label }} ({{ getOrderCount(tab.value) }})
        </button>
      </div>

      <div class="row g-4">
        <div class="col-md-6 col-lg-4" v-for="order in filteredOrders" :key="order.id">
          <div class="card border-0 shadow-sm rounded-4 overflow-hidden h-100 order-card transition-all">
            
            <div class="card-header bg-white border-bottom p-3 d-flex justify-content-between align-items-center">
              <span class="fw-black fs-6">{{ order.code }}</span>
              <span class="badge fw-bold px-3 py-2 rounded-pill fs-8" :class="getStatusBadge(order.status)">
                {{ order.statusText }}
              </span>
            </div>

            <div class="card-body p-4">
              <div class="mb-3">
                <small class="text-muted d-block fs-8 fw-bold text-uppercase mb-1">Người nhận</small>
                <div class="d-flex justify-content-between align-items-center">
                  <span class="fw-bold fs-6 text-dark">{{ order.customerName }}</span>
                  <button class="btn btn-sm btn-light border text-primary fw-bold fs-8 rounded-pill d-flex gap-1 align-items-center">
                    <i class="bi bi-telephone-fill"></i> Gọi
                  </button>
                </div>
                <div class="fs-7 text-dark mt-1">{{ order.phone }}</div>
              </div>

              <div class="mb-4">
                <small class="text-muted d-block fs-8 fw-bold text-uppercase mb-1">Địa chỉ giao hàng</small>
                <div class="d-flex align-items-start gap-2">
                  <i class="bi bi-geo-alt-fill text-danger mt-1"></i>
                  <span class="fs-7 text-dark line-height-base">{{ order.address }}</span>
                </div>
              </div>

              <div class="bg-light-gray p-3 rounded-3 border">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <span class="fs-8 fw-bold text-muted text-uppercase">Tổng hóa đơn:</span>
                  <span class="fs-7 fw-bold">{{ formatCurrency(order.total) }}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center pt-2 border-top">
                  <span class="fs-8 fw-bold text-dark text-uppercase">Cần thu (COD):</span>
                  <span class="fs-5 fw-black" :class="order.cod > 0 ? 'text-danger' : 'text-success'">
                    {{ order.cod > 0 ? formatCurrency(order.cod) : 'Đã thanh toán' }}
                  </span>
                </div>
              </div>
            </div>

            <div class="card-footer bg-white border-top p-3 d-flex gap-2">
              
              <template v-if="order.status === 1">
                <button class="btn btn-outline-dark fw-bold fs-7 flex-grow-1 py-2 rounded-3">Xem chi tiết</button>
                <button class="btn btn-neon text-dark fw-bold fs-7 flex-grow-1 py-2 rounded-3">
                  NHẬN ĐƠN <i class="bi bi-box-arrow-right ms-1"></i>
                </button>
              </template>

              <template v-if="order.status === 2">
                <button class="btn btn-outline-danger fw-bold fs-7 flex-grow-1 py-2 rounded-3">Giao thất bại</button>
                <button class="btn btn-dark text-neon fw-bold fs-7 flex-grow-1 py-2 rounded-3">
                  GIAO THÀNH CÔNG <i class="bi bi-check2-circle ms-1"></i>
                </button>
              </template>

              <template v-if="order.status === 3">
                <button class="btn btn-light border text-muted fw-bold fs-7 w-100 py-2 rounded-3 disabled">
                  Đã hoàn tất lúc 14:30
                </button>
              </template>

            </div>
          </div>
        </div>
      </div>

      <div v-if="filteredOrders.length === 0" class="text-center py-5">
        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center shadow-sm mb-3" style="width: 80px; height: 80px;">
          <i class="bi bi-box-seam text-muted fs-1"></i>
        </div>
        <h5 class="fw-bold text-muted">Không có đơn hàng nào</h5>
        <p class="text-muted fs-7">Hiện tại không có đơn hàng nào trong danh mục này.</p>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const activeTab = ref(1); // Mặc định hiển thị tab "Chờ lấy hàng" (Confirmed = 1)

const tabs = [
  { label: 'Tất cả', value: 'ALL' },
  { label: 'Chờ lấy hàng', value: 1 },
  { label: 'Đang giao', value: 2 },
  { label: 'Hoàn thành', value: 3 }
];

// DỮ LIỆU MOCK (Giả lập theo hướng đi của bạn)
// status: 1 (Confirmed - Cửa hàng đã chốt), 2 (Shipping - Shipper đang giữ), 3 (Delivered)
const mockOrders = ref([
  {
    id: 1, code: '#TZ-G-1001',
    customerName: 'Nguyễn Minh Quân', phone: '0908 123 456',
    address: '45 Xuân Thủy, Dịch Vọng Hậu, Cầu Giấy, Hà Nội',
    total: 25490000, cod: 25490000, // Chọn thanh toán COD
    status: 1, statusText: 'Chờ lấy hàng'
  },
  {
    id: 2, code: '#TZ-1002',
    customerName: 'Trần Thị Lan', phone: '0912 987 654',
    address: 'Tòa nhà Bitexco, Bến Nghé, Quận 1, TP. HCM',
    total: 3500000, cod: 0, // Đã thanh toán qua ngân hàng
    status: 1, statusText: 'Chờ lấy hàng'
  },
  {
    id: 3, code: '#TZ-1005',
    customerName: 'Lê Hoàng Phong', phone: '0933 444 555',
    address: '128 Nguyễn Văn Linh, Ninh Kiều, Cần Thơ',
    total: 8900000, cod: 8900000,
    status: 2, statusText: 'Đang giao hàng'
  },
  {
    id: 4, code: '#TZ-1008',
    customerName: 'Phạm Đăng Khoa', phone: '0988 777 888',
    address: '22 Lê Lợi, Hải Châu, Cần Thơ',
    total: 45000000, cod: 0,
    status: 3, statusText: 'Giao thành công'
  }
]);

// Lọc đơn hàng theo Tab
const filteredOrders = computed(() => {
  if (activeTab.value === 'ALL') return mockOrders.value;
  return mockOrders.value.filter(order => order.status === activeTab.value);
});

// Đếm số lượng cho Tab
const getOrderCount = (statusVal) => {
  if (statusVal === 'ALL') return mockOrders.value.length;
  return mockOrders.value.filter(o => o.status === statusVal).length;
};

// Hàm tiện ích
const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + '₫';
};

const getStatusBadge = (status) => {
  switch(status) {
    case 1: return 'bg-primary-subtle text-primary border border-primary-subtle';
    case 2: return 'bg-warning-subtle text-warning border border-warning-subtle';
    case 3: return 'bg-success-subtle text-success border border-success-subtle';
    default: return 'bg-light text-dark';
  }
};
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }

.bg-light-gray { background-color: #F8F9FA; }
.text-gray { color: #adb5bd; }
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15); }
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s; }
.btn-neon:hover { background-color: #00cc29; }

.narrow-container { max-width: 1200px !important; margin: 0 auto; }
.line-height-base { line-height: 1.5; }

/* Ẩn thanh cuộn của Tab */
.hide-scrollbar {
  -ms-overflow-style: none;  
  scrollbar-width: none;  
}
.hide-scrollbar::-webkit-scrollbar { 
  display: none; 
}

/* Card hover effect */
.order-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.order-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1) !important;
}

.bg-primary-subtle { background-color: #e0f3ff !important; }
.bg-warning-subtle { background-color: #fff8e1 !important; }
.bg-success-subtle { background-color: #e8fbf0 !important; }
</style>