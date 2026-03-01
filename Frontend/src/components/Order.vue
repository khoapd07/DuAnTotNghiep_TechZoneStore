<template>
  <main class="orders-page py-5 bg-light min-vh-100">
    <div class="container" style="max-width: 1000px;">
      
      <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
          <h1 class="fw-black text-dark mb-1 fs-3">Đơn hàng của tôi</h1>
          <p class="text-muted m-0 fs-7">Quản lý và theo dõi các đơn hàng gần đây của bạn</p>
        </div>
        <button class="btn btn-link text-muted text-decoration-none fw-bold fs-7 d-flex align-items-center gap-2 hover-dark shadow-none p-0">
          <i class="bi bi-question-circle fs-5"></i> Trung tâm trợ giúp
        </button>
      </div>

      <div class="card border-0 shadow-sm rounded-4 mb-4 overflow-hidden">
        <div class="d-flex flex-nowrap overflow-auto hide-scrollbar">
          <button 
            v-for="tab in tabs" 
            :key="tab"
            @click="activeTab = tab"
            class="btn rounded-0 fw-bold fs-7 py-3 px-4 text-nowrap border-0 border-bottom border-3 transition-colors"
            :class="activeTab === tab ? 'border-dark text-dark bg-light' : 'border-transparent text-muted hover-bg-light'"
          >
            {{ tab }}
          </button>
        </div>
      </div>

      <div class="d-flex flex-column gap-4">
        
        <div v-for="order in filteredOrders" :key="order.id" class="card border-0 shadow-sm rounded-4 overflow-hidden order-card transition-all" :class="{'opacity-75': order.status === 'Đã hủy'}">
          
          <div class="card-header bg-white border-bottom p-3 px-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
            <div class="d-flex align-items-center gap-3 fs-7">
              <span class="fw-bold text-dark">{{ order.id }}</span>
              <span class="text-muted">|</span>
              <span class="text-muted">{{ order.date }}</span>
            </div>
            <div>
              <span class="badge rounded-pill px-3 py-2 d-flex align-items-center gap-1 fs-8 fw-bold border" :class="getStatusBadge(order.status)">
                <i :class="getStatusIcon(order.status)" class="fs-7"></i> {{ order.status }}
              </span>
            </div>
          </div>

          <div class="card-body p-4">
            <div v-for="(item, index) in order.items" :key="index" class="d-flex flex-column flex-sm-row gap-3" :class="{'mt-4 pt-4 border-top': index > 0}">
              <div class="bg-light rounded-3 d-flex align-items-center justify-content-center border flex-shrink-0 overflow-hidden" style="width: 80px; height: 80px;" :class="{'grayscale': order.status === 'Đã hủy'}">
                <i :class="item.icon" class="text-secondary opacity-50 fs-2"></i>
              </div>
              
              <div class="flex-grow-1">
                <h6 class="fw-bold text-dark mb-1 line-clamp-2">{{ item.name }}</h6>
                <p class="text-muted fs-8 mb-2">Phân loại: {{ item.variant }}</p>
                <div class="d-flex align-items-center justify-content-between mt-auto">
                  <span class="text-muted fs-7">x{{ item.quantity }}</span>
                  <span class="fw-bold text-dark fs-7">{{ formatCurrency(item.price) }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer bg-white border-top p-3 px-4 d-flex flex-column flex-sm-row align-items-sm-center justify-content-between gap-3">
            <div class="text-end text-sm-start order-1 order-sm-0">
              <span class="text-muted fs-7">Tổng tiền:</span>
              <span class="fw-black text-dark fs-5 ms-2" :class="{'text-decoration-line-through text-muted': order.status === 'Đã hủy'}">
                {{ formatCurrency(order.total) }}
              </span>
            </div>
            
            <div class="d-flex flex-wrap justify-content-end gap-2 order-0 order-sm-1">
              <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Xem chi tiết</button>
              
              <template v-if="order.status === 'Đã giao thành công'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
                <router-link to="/OrderVote" class="btn btn-neon fw-bold text-dark fs-8 rounded-3 px-3 py-2 shadow-sm">Đánh giá</router-link>
              </template>
              
              <template v-if="order.status === 'Đang giao hàng'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2 disabled opacity-50">Đánh giá</button>
              </template>
              
              <template v-if="order.status === 'Chờ xác nhận'">
                <button class="btn btn-outline-danger fw-bold fs-8 rounded-3 px-3 py-2">Hủy đơn hàng</button>
              </template>

              <template v-if="order.status === 'Đã hủy'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
              </template>
            </div>
          </div>

        </div>

      </div>

      <div class="d-flex justify-content-center mt-5 gap-2">
        <button class="btn btn-light border px-3 text-muted rounded-3"><i class="bi bi-chevron-left"></i></button>
        <button class="btn btn-dark fw-bold px-3 rounded-3 text-white">1</button>
        <button class="btn btn-light border fw-bold px-3 rounded-3 text-dark hover-bg-light">2</button>
        <button class="btn btn-light border fw-bold px-3 rounded-3 text-dark hover-bg-light">3</button>
        <button class="btn btn-light border px-3 text-muted rounded-3 hover-bg-light"><i class="bi bi-chevron-right"></i></button>
      </div>

    </div>
  </main>
</template>

<script setup>
import { ref, computed } from 'vue';

const activeTab = ref('Tất cả');
const tabs = ['Tất cả', 'Chờ xác nhận', 'Đang giao', 'Đã giao', 'Đã hủy'];

const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + '₫';
};

// Cấu hình màu sắc Badge
const getStatusBadge = (status) => {
  switch(status) {
    case 'Đã giao thành công': return 'bg-success-subtle text-success border-success-subtle';
    case 'Đang giao hàng': return 'bg-warning-subtle text-warning border-warning-subtle';
    case 'Chờ xác nhận': return 'bg-primary-subtle text-primary border-primary-subtle';
    case 'Đã hủy': return 'bg-danger-subtle text-danger border-danger-subtle';
    default: return 'bg-light text-dark border-light';
  }
};

// Cấu hình Icon Badge
const getStatusIcon = (status) => {
  switch(status) {
    case 'Đã giao thành công': return 'bi-check-circle-fill';
    case 'Đang giao hàng': return 'bi-truck';
    case 'Chờ xác nhận': return 'bi-hourglass-split';
    case 'Đã hủy': return 'bi-x-circle-fill';
    default: return 'bi-box';
  }
};

// Dữ liệu Mock
const orders = ref([
  {
    id: '#TZ12345',
    date: '20/10/2023 14:30',
    status: 'Đã giao thành công',
    total: 36280000,
    items: [
      { name: 'iPhone 15 Pro Max 256GB - Titan Tự Nhiên', variant: '256GB', quantity: 1, price: 34990000, icon: 'bi-phone' },
      { name: 'Ốp lưng MagSafe iPhone 15 Pro Max', variant: 'Trong suốt', quantity: 1, price: 1290000, icon: 'bi-phone-flip' }
    ]
  },
  {
    id: '#TZ12348',
    date: '22/10/2023 09:15',
    status: 'Đang giao hàng',
    total: 26990000,
    items: [
      { name: 'MacBook Air M2 13.6 inch 8GB/256GB', variant: 'Midnight', quantity: 1, price: 26990000, icon: 'bi-laptop' }
    ]
  },
  {
    id: '#TZ12350',
    date: '23/10/2023 10:00',
    status: 'Chờ xác nhận',
    total: 9590000,
    items: [
      { name: 'Apple Watch Series 9 GPS 41mm', variant: 'Ánh sao', quantity: 1, price: 9590000, icon: 'bi-smartwatch' }
    ]
  },
  {
    id: '#TZ12340',
    date: '15/10/2023 08:20',
    status: 'Đã hủy',
    total: 6990000,
    items: [
      { name: 'Tai nghe Bluetooth Sony WH-1000XM5', variant: 'Bạc', quantity: 1, price: 6990000, icon: 'bi-headset' }
    ]
  }
]);

// Logic lọc đơn hàng theo Tab
const filteredOrders = computed(() => {
  if (activeTab.value === 'Tất cả') return orders.value;
  if (activeTab.value === 'Đã giao') return orders.value.filter(o => o.status === 'Đã giao thành công');
  if (activeTab.value === 'Đang giao') return orders.value.filter(o => o.status === 'Đang giao hàng');
  return orders.value.filter(o => o.status === activeTab.value);
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&family=Space+Grotesk:wght@700&display=swap');

.orders-page {
  font-family: 'Inter', system-ui, sans-serif;
}

/* Utilities */
.fw-black { font-weight: 900; font-family: 'Space Grotesk', sans-serif; }
.fs-7 { font-size: 0.9rem; }
.fs-8 { font-size: 0.8rem; }
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}
.border-transparent { border-color: transparent !important; }
.border-dashed { border-style: dashed !important; }
.grayscale { filter: grayscale(100%); }

/* Custom Colors & Buttons */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; border-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s all; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); box-shadow: 0 4px 15px rgba(0,255,51,0.3) !important; }

/* Tương tác hover */
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.hover-dark:hover { color: #212529 !important; }
.transition-colors { transition: color 0.2s, background-color 0.2s, border-color 0.2s; }
.order-card { transition: 0.3s ease; }
.order-card:hover { transform: translateY(-2px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.08)!important; }

/* Ẩn thanh cuộn của Tabs trên Mobile */
.hide-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
.hide-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>