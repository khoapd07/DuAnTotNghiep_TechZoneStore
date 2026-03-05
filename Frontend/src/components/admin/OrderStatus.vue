<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    <main class="flex-grow-1 p-3 overflow-auto position-relative">
      
      <header class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản lý đơn hàng</h3>
          <div class="d-flex align-items-center gap-2 text-muted fs-7">
            <i class="bi bi-search"></i> <span>Tìm kiếm mã đơn hàng, khách hàng...</span>
          </div>
        </div>
        <div class="d-flex gap-3 align-items-center">
          <div class="d-flex align-items-center gap-2">
            <div class="avatar bg-light text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center border" style="width: 35px; height: 35px;">A</div>
            <span class="fw-bold fs-7">Admin TechZone</span>
          </div>
        </div>
      </header>

      <div class="row g-3 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-primary">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Tổng đơn hàng</p>
            <h3 class="fw-black text-dark m-0 mt-1">{{ stats.total }}</h3>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-warning">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Chờ xử lý</p>
            <h3 class="fw-black text-dark m-0 mt-1">{{ stats.pending }}</h3>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-info">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Đang giao</p>
            <h3 class="fw-black text-dark m-0 mt-1">{{ stats.shipping }}</h3>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-success">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Hoàn thành</p>
            <h3 class="fw-black text-dark m-0 mt-1">{{ stats.delivered }}</h3>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-3 mb-3">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="border-bottom bg-light">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-3 fw-bold">Mã Đơn</th>
                <th scope="col" class="py-3 fw-bold">Khách Hàng</th>
                <th scope="col" class="py-3 fw-bold">Ngày Đặt</th>
                <th scope="col" class="py-3 fw-bold">Tổng Tiền</th>
                <th scope="col" class="py-3 fw-bold">Trạng Thái</th>
                <th scope="col" class="py-3 fw-bold text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody v-if="!loading">
              <tr v-for="order in orderList" :key="order.orderId" class="border-bottom-dashed cursor-pointer">
                <td class="fw-bold fs-7 text-dark px-3">{{ order.orderCode }}</td>
                <td>
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-text bg-secondary text-white fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; font-size: 0.75rem;">
                      {{ getInitials(extractCustomerInfo(order.note).name) }}
                    </div>
                    <span class="fw-bold fs-7 text-dark">{{ extractCustomerInfo(order.note).name }}</span>
                  </div>
                </td>
                <td class="text-muted fs-7">{{ formatDate(order.orderDate) }}</td>
                <td class="fw-bold fs-7 text-dark">{{ formatCurrency(order.finalAmount) }}</td>
                <td>
                  <span class="badge fw-bold px-2 py-1 rounded-2 fs-8" :class="getStatusClass(order.statusName)">
                    {{ translateStatus(order.statusName) }}
                  </span>
                </td>
                <td class="text-center">
                  <button 
                    class="btn btn-sm btn-outline-dark fs-8 fw-bold px-3 py-1 rounded-pill"
                    data-bs-toggle="offcanvas" 
                    data-bs-target="#orderDetailOffcanvas" 
                    @click="viewOrderDetail(order)">
                    Xem chi tiết
                  </button>
                </td>
              </tr>
            </tbody>
            <tbody v-else>
              <tr><td colspan="6" class="text-center py-4"><span class="spinner-border text-success"></span> Đang tải...</td></tr>
            </tbody>
          </table>
        </div>
      </div>

    </main>

    <div class="offcanvas offcanvas-end shadow" tabindex="-1" id="orderDetailOffcanvas" style="width: 450px; border-left: none;">
      <div class="offcanvas-header border-bottom bg-light">
        <h5 class="offcanvas-title fw-black fs-5">{{ selectedOrder?.orderCode }}</h5>
        <button type="button" class="btn-close shadow-none" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body p-0" v-if="selectedOrder">
        
        <div class="p-3 border-bottom bg-light-gray">
          <p class="fs-8 text-muted fw-bold mb-1">NGÀY ĐẶT: {{ formatDate(selectedOrder.orderDate) }}</p>
          <label class="fs-8 fw-bold text-dark mb-2">TRẠNG THÁI XỬ LÝ</label>
          <div class="d-flex gap-2">
            <select class="form-select form-select-sm fw-bold border-dark shadow-none" v-model="selectedOrderEditStatus">
              <option value="0">Chờ xác nhận</option>
              <option value="1">Đã xác nhận</option>
              <option value="2">Đang giao hàng</option>
              <option value="3">Giao hàng thành công</option>
              <option value="4">Hủy đơn</option>
            </select>
            <button class="btn btn-sm btn-dark fw-bold px-3" @click="updateStatus" :disabled="isSaving">
              {{ isSaving ? '...' : 'Lưu' }}
            </button>
          </div>
        </div>

        <div class="p-3 border-bottom">
          <h6 class="fw-bold fs-6 mb-3">Thông tin khách hàng</h6>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">HỌ TÊN</small>
            <span class="fs-7 fw-bold text-dark">{{ extractCustomerInfo(selectedOrder.note).name }}</span>
          </div>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">ĐIỆN THOẠI</small>
            <span class="fs-7 fw-bold text-dark">{{ extractCustomerInfo(selectedOrder.note).phone }}</span>
          </div>
          <div>
            <small class="text-muted d-block fs-8 fw-bold">ĐỊA CHỈ NHẬN HÀNG</small>
            <span class="fs-7 text-dark">{{ extractCustomerInfo(selectedOrder.note).address }}</span>
          </div>
        </div>

        <div class="p-3 border-bottom overflow-auto" style="max-height: 250px;">
          <h6 class="fw-bold fs-6 mb-3">Sản phẩm ({{ selectedOrder.orderDetails.length }})</h6>
          <div v-for="item in selectedOrder.orderDetails" :key="item.productId" class="d-flex align-items-center gap-3 mb-3">
            <div class="bg-light rounded p-1 border" style="width: 50px; height: 50px;">
              <img :src="item.imageUrl" alt="img" class="img-fluid object-fit-contain h-100 w-100">
            </div>
            <div class="flex-grow-1">
              <h6 class="m-0 fs-8 fw-bold line-clamp-1">{{ item.productName }}</h6>
              <span class="fs-8 text-muted">SL: {{ item.quantity }}</span>
            </div>
            <span class="fw-bold fs-7">{{ formatCurrency(item.subTotal) }}</span>
          </div>
        </div>

        <div class="p-3 bg-light mt-auto">
          <div class="d-flex justify-content-between mb-2 fs-7">
            <span class="text-muted">Tạm tính</span>
            <span class="fw-bold">{{ formatCurrency(selectedOrder.totalMoney) }}</span>
          </div>
          <div class="d-flex justify-content-between mb-2 fs-7">
            <span class="text-muted">Giảm giá Voucher</span>
            <span class="fw-bold text-danger">-{{ formatCurrency(selectedOrder.discountAmount) }}</span>
          </div>
          <div class="d-flex justify-content-between pt-2 border-top mb-3">
            <span class="fw-bold text-dark">Tổng cộng (Thực trả)</span>
            <span class="fw-black text-success fs-5">{{ formatCurrency(selectedOrder.finalAmount) }}</span>
          </div>
        </div>

      </div>
    </div>
    
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const orderList = ref([]);
const loading = ref(true);
const selectedOrder = ref(null);
const selectedOrderEditStatus = ref('0');
const isSaving = ref(false);

const API_URL = 'http://localhost:8080/api/orders';

// Lấy danh sách đơn hàng
const fetchOrders = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${API_URL}/admin/all`);
    orderList.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải đơn hàng:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => fetchOrders());

// Mở Offcanvas Xem chi tiết
const viewOrderDetail = (order) => {
  selectedOrder.value = order;
  // Map chữ Tiếng Anh/Số về Option Value
  const s = String(order.statusName).toLowerCase();
  if (s === '0' || s === 'pending') selectedOrderEditStatus.value = '0';
  else if (s === '1' || s === 'confirmed') selectedOrderEditStatus.value = '1';
  else if (s === '2' || s === 'shipping') selectedOrderEditStatus.value = '2';
  else if (s === '3' || s === 'delivered') selectedOrderEditStatus.value = '3';
  else if (s === '4' || s === 'cancelled') selectedOrderEditStatus.value = '4';
};

// Cập nhật trạng thái
const updateStatus = async () => {
  if (!selectedOrder.value) return;
  isSaving.value = true;
  try {
    await axios.put(`${API_URL}/admin/${selectedOrder.value.orderId}/status?statusId=${selectedOrderEditStatus.value}`);
    alert("Cập nhật trạng thái thành công!");
    fetchOrders(); // Load lại danh sách
    document.querySelector('.btn-close').click(); // Đóng offcanvas
  } catch (error) {
    alert("Lỗi cập nhật: " + (error.response?.data || error.message));
  } finally {
    isSaving.value = false;
  }
};

// Hàm thống kê
const stats = computed(() => {
  return {
    total: orderList.value.length,
    pending: orderList.value.filter(o => { const s=String(o.statusName).toLowerCase(); return s==='0' || s==='pending'; }).length,
    shipping: orderList.value.filter(o => { const s=String(o.statusName).toLowerCase(); return s==='2' || s==='shipping'; }).length,
    delivered: orderList.value.filter(o => { const s=String(o.statusName).toLowerCase(); return s==='3' || s==='delivered'; }).length,
  };
});

// Hàm trích xuất thông tin khách hàng từ chuỗi "note" đã làm ở trang Checkout
const extractCustomerInfo = (noteString) => {
  let extName = 'Khách hàng';
  let extPhone = 'Đang cập nhật';
  let extAddress = noteString || 'Đang cập nhật';
  
  if (noteString && noteString.includes(' - SĐT:')) {
    try {
      const parts = noteString.split(' - ');
      extName = parts[0].replace('Người nhận: ', '').replace('Khách vãng lai: ', '').trim();
      extPhone = parts[1].replace('SĐT: ', '').trim();
      extAddress = parts[2].replace('Đ/C: ', '').split('. Ghi chú')[0].trim();
    } catch (e) {}
  }
  return { name: extName, phone: extPhone, address: extAddress };
};

// Lấy 2 chữ cái đầu để làm Avatar
const getInitials = (name) => {
  if (!name) return 'KH';
  const words = name.trim().split(' ');
  if (words.length === 1) return words[0].substring(0, 2).toUpperCase();
  return (words[0].charAt(0) + words[words.length - 1].charAt(0)).toUpperCase();
};

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + '₫' : "0₫";

const formatDate = (dateString) => {
  if(!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
};

// Định dạng trạng thái tiếng Việt
const translateStatus = (status) => {
  const s = String(status).toLowerCase();
  if (s === '0' || s === 'pending') return 'CHỜ XÁC NHẬN';
  if (s === '1' || s === 'confirmed') return 'ĐÃ XÁC NHẬN';
  if (s === '2' || s === 'shipping') return 'ĐANG GIAO';
  if (s === '3' || s === 'delivered') return 'HOÀN THÀNH';
  if (s === '4' || s === 'cancelled') return 'ĐÃ HỦY';
  return 'KHÔNG RÕ';
};

const getStatusClass = (status) => {
  const s = String(status).toLowerCase();
  if (s === '0' || s === 'pending') return 'bg-warning text-dark';
  if (s === '1' || s === 'confirmed') return 'bg-primary text-white';
  if (s === '2' || s === 'shipping') return 'bg-info text-dark';
  if (s === '3' || s === 'delivered') return 'bg-success text-white';
  if (s === '4' || s === 'cancelled') return 'bg-danger text-white';
  return 'bg-secondary text-white';
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }

/* Bảng dữ liệu */
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }

.table-hover tbody tr:hover td { background-color: #f8f9fa; }
</style>