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
        
        <div v-if="loading" class="text-center py-5">
           <div class="spinner-border text-neon" role="status"></div>
           <p class="mt-2 text-muted">Đang tải đơn hàng...</p>
        </div>

        <div v-else-if="filteredOrders.length === 0" class="text-center py-5 bg-white rounded-4 shadow-sm">
           <i class="bi bi-bag-x fs-1 text-muted opacity-25"></i>
           <p class="mt-3 fw-bold text-muted">Không có đơn hàng nào trong mục này.</p>
        </div>

        <div v-for="order in filteredOrders" :key="order.orderId" class="card border-0 shadow-sm rounded-4 overflow-hidden order-card transition-all" :class="{'opacity-75': translateStatus(order.statusName) === 'Đã hủy'}">
          
          <div class="card-header bg-white border-bottom p-3 px-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
            <div class="d-flex align-items-center gap-3 fs-7">
              <span class="fw-bold text-dark">#{{ order.orderCode }}</span>
              <span class="text-muted">|</span>
              <span class="text-muted">{{ formatDate(order.orderDate) }}</span>
            </div>
            <div>
              <span class="badge rounded-pill px-3 py-2 d-flex align-items-center gap-1 fs-8 fw-bold border" :class="getStatusBadge(translateStatus(order.statusName))">
                <i :class="getStatusIcon(translateStatus(order.statusName))" class="fs-7"></i> {{ translateStatus(order.statusName) }}
              </span>
            </div>
          </div>

          <div class="card-body p-4">
            <div v-for="(item, index) in order.orderDetails" :key="index" class="d-flex flex-column flex-sm-row gap-3" :class="{'mt-4 pt-4 border-top': index > 0}">
              <div class="bg-light rounded-3 d-flex align-items-center justify-content-center border flex-shrink-0 overflow-hidden" style="width: 80px; height: 80px;" :class="{'grayscale': translateStatus(order.statusName) === 'Đã hủy'}">
                <img v-if="item.imageUrl" :src="item.imageUrl" class="img-fluid object-fit-contain p-2" alt="product">
                <i v-else class="bi bi-box text-secondary opacity-50 fs-2"></i>
              </div>
              
              <div class="flex-grow-1">
                <h6 class="fw-bold text-dark mb-1 line-clamp-2">{{ item.productName }}</h6>
                <p class="text-muted fs-8 mb-2">Giá niêm yết: {{ formatCurrency(item.price) }}</p>
                <div class="d-flex align-items-center justify-content-between mt-auto">
                  <span class="text-muted fs-7">x{{ item.quantity }}</span>
                  <span class="fw-bold text-dark fs-7">{{ formatCurrency(item.subTotal) }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer bg-white border-top p-3 px-4 d-flex flex-column flex-sm-row align-items-sm-center justify-content-between gap-3">
            <div class="text-end text-sm-start order-1 order-sm-0">
              <span class="text-muted fs-7">Tổng tiền thực trả:</span>
              <span class="fw-black text-dark fs-5 ms-2" :class="{'text-decoration-line-through text-muted': translateStatus(order.statusName) === 'Đã hủy'}">
                {{ formatCurrency(order.finalAmount) }}
              </span>
            </div>
            
            <div class="d-flex flex-wrap justify-content-end gap-2 order-0 order-sm-1">
              <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Xem chi tiết</button>
              
              <template v-if="translateStatus(order.statusName) === 'Giao hàng thành công'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
                <router-link to="/OrderVote" class="btn btn-neon fw-bold text-dark fs-8 rounded-3 px-3 py-2 shadow-sm">Đánh giá</router-link>
              </template>
              
              <template v-if="translateStatus(order.statusName) === 'Đang giao hàng'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2 disabled opacity-50">Đánh giá</button>
              </template>
              
              <template v-if="translateStatus(order.statusName) === 'Chờ xác nhận'">
                <button class="btn btn-outline-danger fw-bold fs-8 rounded-3 px-3 py-2">Hủy đơn hàng</button>
              </template>

              <template v-if="translateStatus(order.statusName) === 'Đã hủy'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
              </template>
            </div>
          </div>

        </div>

      </div>

    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const activeTab = ref('Tất cả');
const tabs = ['Tất cả', 'Chờ xác nhận', 'Đang giao', 'Đã giao', 'Đã hủy'];
const orders = ref([]);
const loading = ref(true);

// Mock USER_ID - Sau này lấy từ Auth Store
const CURRENT_USER_ID = 1;

// 1. Lấy dữ liệu từ Backend
const fetchOrders = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`http://localhost:8080/api/orders/${CURRENT_USER_ID}/history`);
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi lấy lịch sử đơn hàng:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchOrders();
});

// 2. Hàm bổ trợ định dạng và hiển thị
const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value).replace('₫', 'đ');
};

const formatDate = (dateString) => {
  if(!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('vi-VN', { 
    day: '2-digit', month: '2-digit', year: 'numeric', 
    hour: '2-digit', minute: '2-digit' 
  });
};

// Hàm dịch Status từ Backend (Tiếng Anh) sang UI (Tiếng Việt)
const translateStatus = (status) => {
  switch(status) {
    case 'Pending': return 'Chờ xác nhận';
    case 'Confirmed': return 'Chờ lấy hàng';
    case 'Shipping': return 'Đang giao hàng';
    case 'Delivered': return 'Giao hàng thành công';
    case 'Cancelled': return 'Đã hủy';
    default: return status;
  }
};

const getStatusBadge = (status) => {
  switch(status) {
    case 'Giao hàng thành công': return 'bg-success-subtle text-success border-success-subtle';
    case 'Đang giao hàng': return 'bg-warning-subtle text-warning border-warning-subtle';
    case 'Chờ xác nhận': return 'bg-primary-subtle text-primary border-primary-subtle';
    case 'Đã hủy': return 'bg-danger-subtle text-danger border-danger-subtle';
    default: return 'bg-light text-dark border-light';
  }
};

const getStatusIcon = (status) => {
  switch(status) {
    case 'Giao hàng thành công': return 'bi-check-circle-fill';
    case 'Đang giao hàng': return 'bi-truck';
    case 'Chờ xác nhận': return 'bi-hourglass-split';
    case 'Đã hủy': return 'bi-x-circle-fill';
    default: return 'bi-box';
  }
};

// 3. Logic lọc đơn hàng theo Tab
const filteredOrders = computed(() => {
  if (activeTab.value === 'Tất cả') return orders.value;
  if (activeTab.value === 'Đã giao') return orders.value.filter(o => translateStatus(o.statusName) === 'Giao hàng thành công');
  if (activeTab.value === 'Đang giao') return orders.value.filter(o => translateStatus(o.statusName) === 'Đang giao hàng');
  return orders.value.filter(o => translateStatus(o.statusName) === activeTab.value);
});
</script>

<style scoped>
/* CSS Giữ nguyên toàn bộ như cũ của bạn */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&family=Space+Grotesk:wght@700&display=swap');

.orders-page { font-family: 'Inter', system-ui, sans-serif; }
.fw-black { font-weight: 900; font-family: 'Space Grotesk', sans-serif; }
.fs-7 { font-size: 0.9rem; }
.fs-8 { font-size: 0.8rem; }
.line-clamp-2 { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.border-transparent { border-color: transparent !important; }
.grayscale { filter: grayscale(100%); }
.text-neon { color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s all; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); box-shadow: 0 4px 15px rgba(0,255,51,0.3) !important; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.hover-dark:hover { color: #212529 !important; }
.transition-colors { transition: color 0.2s, background-color 0.2s, border-color 0.2s; }
.order-card { transition: 0.3s ease; }
.order-card:hover { transform: translateY(-2px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.08)!important; }
.hide-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
.hide-scrollbar::-webkit-scrollbar { display: none; }
</style>