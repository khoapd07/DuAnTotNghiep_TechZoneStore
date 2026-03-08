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
            <h6 class="fw-bold m-0 fs-7">Shipper: <span class="text-primary">{{ shipperName }}</span></h6>
            <small class="text-success fw-bold fs-8">● Đang hoạt động</small>
          </div>
          <div class="avatar bg-dark text-white fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
            {{ getInitials(shipperName) }}
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
            <h2 class="fw-black text-dark m-0 mt-2">{{ stats.waiting }}</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0 d-flex align-items-center gap-2">
              <i class="bi bi-bicycle text-warning fs-5"></i> Đang giao
            </p>
            <h2 class="fw-black text-dark m-0 mt-2">{{ stats.shipping }}</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0 d-flex align-items-center gap-2">
              <i class="bi bi-check-circle text-success fs-5"></i> Hoàn thành
            </p>
            <h2 class="fw-black text-dark m-0 mt-2">{{ stats.delivered }}</h2>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-dark text-white h-100">
            <p class="text-gray fs-8 fw-bold text-uppercase m-0">Tiền thu hộ (COD)</p>
            <h3 class="fw-black text-neon m-0 mt-2">{{ formatCurrency(stats.totalCOD) }}</h3>
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

      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-neon" role="status"></div>
        <p class="text-muted mt-2">Đang tải nhiệm vụ...</p>
      </div>

      <div v-else class="row g-4">
        <div class="col-md-6 col-lg-4" v-for="order in filteredOrders" :key="order.orderId">
          <div class="card border-0 shadow-sm rounded-4 overflow-hidden h-100 order-card transition-all">
            
            <div class="card-header bg-white border-bottom p-3 d-flex justify-content-between align-items-center">
              <span class="fw-black fs-6">{{ order.orderCode }}</span>
              <span class="badge fw-bold px-3 py-2 rounded-pill fs-8" :class="getStatusBadge(order.statusName)">
                {{ translateStatus(order.statusName) }}
              </span>
            </div>

            <div class="card-body p-4">
              <div class="mb-3">
                <small class="text-muted d-block fs-8 fw-bold text-uppercase mb-1">Người nhận</small>
                <div class="d-flex justify-content-between align-items-center">
                  <span class="fw-bold fs-6 text-dark">{{ extractCustomerInfo(order.note).name }}</span>
                  <a :href="'tel:' + extractCustomerInfo(order.note).phone" class="btn btn-sm btn-light border text-primary fw-bold fs-8 rounded-pill d-flex gap-1 align-items-center text-decoration-none">
                    <i class="bi bi-telephone-fill"></i> Gọi
                  </a>
                </div>
                <div class="fs-7 text-dark mt-1">{{ extractCustomerInfo(order.note).phone }}</div>
              </div>

              <div class="mb-4">
                <small class="text-muted d-block fs-8 fw-bold text-uppercase mb-1">Địa chỉ giao hàng</small>
                <div class="d-flex align-items-start gap-2">
                  <i class="bi bi-geo-alt-fill text-danger mt-1"></i>
                  <span class="fs-7 text-dark line-height-base">{{ extractCustomerInfo(order.note).address }}</span>
                </div>
              </div>

              <div class="bg-light-gray p-3 rounded-3 border">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <span class="fs-8 fw-bold text-muted text-uppercase">Tổng hóa đơn:</span>
                  <span class="fs-7 fw-bold">{{ formatCurrency(order.finalAmount) }}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center pt-2 border-top">
                  <span class="fs-8 fw-bold text-dark text-uppercase">Cần thu (COD):</span>
                  
                  <span v-if="order.paymentStatus" class="fs-5 fw-black text-success">
                    0đ <small class="fs-9 fw-bold border border-success rounded px-1 ms-1 pb-1">ĐÃ CK</small>
                  </span>
                  
                  <span v-else class="fs-5 fw-black text-danger">
                    {{ formatCurrency(order.finalAmount) }} 
                  </span>
                </div>
              </div>
            </div>

            <div class="card-footer bg-white border-top p-3 d-flex gap-2">
              
              <template v-if="order.statusName === 'Confirmed' || order.statusName == '1'">
                <button class="btn btn-outline-dark fw-bold fs-7 flex-grow-1 py-2 rounded-3" @click="viewDetails(order.orderCode)">Chi tiết</button>
                <button @click="updateStatus(order.orderId, 2)" class="btn btn-neon text-dark fw-bold fs-7 flex-grow-1 py-2 rounded-3">
                  NHẬN ĐƠN <i class="bi bi-box-arrow-right ms-1"></i>
                </button>
              </template>

              <template v-if="order.statusName === 'Shipping' || order.statusName == '2'">
                <button @click="handleFailDelivery(order.orderId)" class="btn btn-outline-danger fw-bold fs-7 flex-grow-1 py-2 rounded-3">Giao thất bại</button>
                <button @click="updateStatus(order.orderId, 3)" class="btn btn-dark text-neon fw-bold fs-7 flex-grow-1 py-2 rounded-3">
                  THÀNH CÔNG <i class="bi bi-check2-circle ms-1"></i>
                </button>
              </template>

              <template v-if="order.statusName === 'Delivered' || order.statusName == '3'">
                <button class="btn btn-light border text-success fw-bold fs-7 w-100 py-2 rounded-3 disabled">
                  <i class="bi bi-check-lg"></i> Đã giao thành công
                </button>
              </template>

            </div>
          </div>
        </div>
      </div>

      <div v-if="!loading && filteredOrders.length === 0" class="text-center py-5">
        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center shadow-sm mb-3" style="width: 80px; height: 80px;">
          <i class="bi bi-box-seam text-muted fs-1"></i>
        </div>
        <h5 class="fw-bold text-muted">Không có đơn hàng nào</h5>
        <p class="text-muted fs-7">Hiện tại không có đơn hàng nào cần xử lý trong mục này.</p>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const orders = ref([]);
const loading = ref(true);
const activeTab = ref(1); // 1 = Confirmed
const shipperName = ref('Nhân Viên Giao Hàng');
const shipperId = ref(null);

const API_URL = 'http://localhost:8080/api/orders';

const tabs = [
  { label: 'Tất cả', value: 'ALL' },
  { label: 'Chờ lấy hàng', value: 1 },
  { label: 'Đang giao', value: 2 },
  { label: 'Hoàn thành', value: 3 }
];

onMounted(() => {
  // Lấy tên shipper từ localStorage (nếu có đăng nhập)
  const userInfoStr = localStorage.getItem('user_info');
  if(userInfoStr) {
    const userObj = JSON.parse(userInfoStr);
    shipperName.value = JSON.parse(userInfoStr).fullName || JSON.parse(userInfoStr).username || 'Shipper';
    shipperId.value = userObj.userId;
  }
  fetchShipperTasks();
});

const fetchShipperTasks = async () => {
  if (!shipperId.value) return;

  loading.value = true;
  try {
    const res = await axios.get(`${API_URL}/shipper/tasks?shipperId=${shipperId.value}`);
    orders.value = res.data;
  } catch (error) {
    console.error('Lỗi lấy nhiệm vụ:', error);
  } finally {
    loading.value = false;
  }
};

// Cập nhật trạng thái
const updateStatus = async (orderId, newStatusId) => {
  try {
    let url = `${API_URL}/admin/${orderId}/status?statusId=${newStatusId}`;
    
    // Nếu Shipper bấm "Nhận đơn" (2) và có ID, thì gửi kèm shipperId lên
    if (newStatusId === 2 && shipperId.value) {
      url += `&shipperId=${shipperId.value}`;
    }

    await axios.put(url);
    fetchShipperTasks(); // Tải lại danh sách
  } catch (error) {
    alert("Lỗi cập nhật: " + error.response?.data);
  }
};

// Giao thất bại -> Chuyển về 4 (Cancelled) hoặc trạng thái khác
const handleFailDelivery = (orderId) => {
  const reason = prompt("Vui lòng nhập lý do giao thất bại (Bom hàng, sai địa chỉ...):");
  if (reason) {
    // Tương lai có thể lưu 'reason' vào API, hiện tại chỉ đổi status
    updateStatus(orderId, 4); 
  }
};

// Mở trang chi tiết nội bộ
const viewDetails = (code) => {
  router.push(`/order/${code}`);
};

// Bóc tách string Status
const getStatusValue = (statusName) => {
  const s = String(statusName).toLowerCase();
  if (s === '1' || s === 'confirmed') return 1;
  if (s === '2' || s === 'shipping') return 2;
  if (s === '3' || s === 'delivered') return 3;
  if (s === '4' || s === 'cancelled') return 4;
  return 0; // Pending
};

// Lọc đơn hàng
const filteredOrders = computed(() => {
  if (activeTab.value === 'ALL') return orders.value;
  return orders.value.filter(o => getStatusValue(o.statusName) === activeTab.value);
});

const getOrderCount = (val) => {
  if (val === 'ALL') return orders.value.length;
  return orders.value.filter(o => getStatusValue(o.statusName) === val).length;
};

// Thống kê
const stats = computed(() => {
  const waiting = orders.value.filter(o => getStatusValue(o.statusName) === 1).length;
  const shipping = orders.value.filter(o => getStatusValue(o.statusName) === 2).length;
  const delivered = orders.value.filter(o => getStatusValue(o.statusName) === 3).length;
  
  // Tính tổng COD: Chỉ tính những đơn ĐANG GIAO (2) VÀ CHƯA THANH TOÁN (paymentStatus == false)
  const totalCOD = orders.value
    .filter(o => getStatusValue(o.statusName) === 2 && !o.paymentStatus)
    .reduce((sum, o) => sum + o.finalAmount, 0);

  return { waiting, shipping, delivered, totalCOD };
});

// Hàm hỗ trợ UI
const extractCustomerInfo = (noteString) => {
  let extName = 'Khách hàng';
  let extPhone = '...';
  let extAddress = noteString || '...';
  if (noteString && noteString.includes(' - SĐT:')) {
    try {
      const p = noteString.split(' - ');
      extName = p[0].replace('Người nhận: ', '').replace('Khách vãng lai: ', '').trim();
      extPhone = p[1].replace('SĐT: ', '').trim();
      extAddress = p[2].replace('Đ/C: ', '').split('. Ghi chú')[0].trim();
    } catch(e){}
  }
  return { name: extName, phone: extPhone, address: extAddress };
};

const getInitials = (name) => {
  if (!name) return 'S';
  const words = name.trim().split(' ');
  return words.length === 1 ? words[0].substring(0, 2).toUpperCase() : (words[0][0] + words[words.length-1][0]).toUpperCase();
};

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + '₫' : "0₫";

const translateStatus = (sName) => {
  const v = getStatusValue(sName);
  if(v===1) return 'Chờ lấy hàng';
  if(v===2) return 'Đang giao hàng';
  if(v===3) return 'Giao thành công';
  if(v===4) return 'Giao thất bại';
  return 'Chờ xác nhận';
};

const getStatusBadge = (sName) => {
  const v = getStatusValue(sName);
  if(v===1) return 'bg-primary-subtle text-primary border border-primary-subtle';
  if(v===2) return 'bg-warning-subtle text-warning border border-warning-subtle';
  if(v===3) return 'bg-success-subtle text-success border border-success-subtle';
  return 'bg-light text-dark';
};
</script>

<style scoped>
/* Toàn bộ CSS giữ nguyên từ thiết kế tĩnh bạn vừa xem */
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

.hide-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
.hide-scrollbar::-webkit-scrollbar { display: none; }

.order-card { transition: transform 0.2s ease, box-shadow 0.2s ease; }
.order-card:hover { transform: translateY(-3px); box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1) !important; }

.bg-primary-subtle { background-color: #e0f3ff !important; }
.bg-warning-subtle { background-color: #fff8e1 !important; }
.bg-success-subtle { background-color: #e8fbf0 !important; }
</style>