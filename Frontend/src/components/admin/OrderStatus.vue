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
            <div class="avatar bg-light text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center border" style="width: 35px; height: 35px;">
              {{ getInitials(currentUser.name) }}
            </div>
            <span class="fw-bold fs-7">{{ currentUser.name }}</span>
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
                  <span class="badge fw-bold px-2 py-1 rounded-2 fs-8" :class="getStatusClass(order.statusId)">
                    {{ getStatusVietnamese(order.statusId) }}
                  </span>
                </td>
                <td class="text-center">
                  <button class="btn btn-sm btn-outline-dark fs-8 fw-bold px-3 py-1 rounded-pill"
                          data-bs-toggle="offcanvas" data-bs-target="#orderDetailOffcanvas" 
                          @click="viewOrderDetail(order)">
                    Xem
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
            <select class="form-select form-select-sm fw-bold border-dark shadow-none" 
                    v-model="selectedOrderEditStatus" 
                    :disabled="availableStatuses.length <= 1">
              <option v-for="st in availableStatuses" :key="st.statusId" :value="st.statusId">
                {{ st.description }}
              </option>
            </select>

            <button class="btn btn-sm btn-dark fw-bold px-3" 
                    @click="updateStatus" 
                    :disabled="isSaving || availableStatuses.length <= 1">
              {{ isSaving ? '...' : 'Lưu' }}
            </button>
          </div>
          
          <small v-if="availableStatuses.length <= 1" class="text-danger fs-9 mt-1 d-block fw-bold">
            Đơn hàng đã kết thúc quy trình, không thể thay đổi trạng thái.
          </small>
        </div>

        <div class="p-3 border-bottom">
          <h6 class="fw-bold fs-6 mb-3">Thông tin liên hệ</h6>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">HỌ TÊN KHÁCH</small>
            <span class="fs-7 fw-bold text-dark">{{ extractCustomerInfo(selectedOrder.note).name }}</span>
          </div>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">ĐIỆN THOẠI</small>
            <span class="fs-7 fw-bold text-dark">{{ extractCustomerInfo(selectedOrder.note).phone }}</span>
          </div>
          <div class="mb-4">
            <small class="text-muted d-block fs-8 fw-bold">ĐỊA CHỈ</small>
            <span class="fs-7 text-dark">{{ extractCustomerInfo(selectedOrder.note).address }}</span>
          </div>
          
          <div class="bg-light p-3 rounded-3 border">
            <div class="d-flex align-items-center justify-content-between mb-2">
              <small class="text-muted fs-8 fw-bold"><i class="bi bi-person-check-fill me-1"></i> NV XÁC NHẬN:</small>
              <span class="fs-8 fw-bold" :class="selectedOrder.employeeName ? 'text-primary' : 'text-muted fst-italic'">
                {{ selectedOrder.employeeName || 'Chưa duyệt' }}
              </span>
            </div>
            <div class="d-flex align-items-center justify-content-between border-top pt-2">
              <small class="text-muted fs-8 fw-bold"><i class="bi bi-truck me-1"></i> SHIPPER:</small>
              <span class="fs-8 fw-bold" :class="selectedOrder.shipperName ? 'text-success' : 'text-muted fst-italic'">
                {{ selectedOrder.shipperName || 'Chưa điều phối' }}
              </span>
            </div>
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
          <div class="d-flex justify-content-between pt-2 border-top mb-3">
            <span class="fw-bold text-dark">Tổng cộng</span>
            <span class="fw-black text-danger fs-5">{{ formatCurrency(selectedOrder.finalAmount) }}</span>
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
const dbStatuses = ref([]);
const loading = ref(true);
const selectedOrder = ref(null);
const selectedOrderEditStatus = ref(0);
const isSaving = ref(false);

// Biến lưu thông tin người đang đăng nhập
const currentUser = ref({
  id: null,
  name: 'Admin TechZone'
});

const API_URL = 'http://localhost:8080/api/orders';

onMounted(async () => {
  // Lấy thông tin user từ LocalStorage
  const userStr = localStorage.getItem('user_info');
  if (userStr) {
    const userObj = JSON.parse(userStr);
    currentUser.value.id = userObj.userId;
    currentUser.value.name = userObj.fullName || userObj.username;
  }

  await fetchOrderStatuses();
  fetchOrders();
});

const fetchOrderStatuses = async () => {
  try {
    const res = await axios.get(`${API_URL}/statuses`);
    dbStatuses.value = res.data;
  } catch (error) {
    console.error("Lỗi tải trạng thái:", error);
  }
};

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

const viewOrderDetail = (order) => {
  selectedOrder.value = order;
  selectedOrderEditStatus.value = order.statusId; 
};

// Cập nhật trạng thái (Gửi kèm ID người thao tác)
const updateStatus = async () => {
  if (!selectedOrder.value) return;
  isSaving.value = true;
  
  // Tạo URL gửi request
  let url = `${API_URL}/admin/${selectedOrder.value.orderId}/status?statusId=${selectedOrderEditStatus.value}`;
  
  // Nếu có user đang đăng nhập, đính kèm employeeId vào URL
  if (currentUser.value.id) {
    url += `&employeeId=${currentUser.value.id}`;
  }

  try {
    await axios.put(url);
    alert("Cập nhật trạng thái thành công!");
    fetchOrders(); 
    document.querySelector('.btn-close').click();
  } catch (error) {
    alert("Lỗi cập nhật: " + (error.response?.data || error.message));
  } finally {
    isSaving.value = false;
  }
};

// --- CÁC HÀM HỖ TRỢ BÊN DƯỚI GIỮ NGUYÊN ---
const stats = computed(() => {
  return {
    total: orderList.value.length,
    pending: orderList.value.filter(o => o.statusId === 0).length,
    shipping: orderList.value.filter(o => o.statusId === 2).length,
    delivered: orderList.value.filter(o => o.statusId === 3).length,
  };
});

const extractCustomerInfo = (noteString) => {
  let extName = 'Khách hàng', extPhone = '...', extAddress = noteString || '...';
  if (noteString && noteString.includes(' - SĐT:')) {
    try {
      const p = noteString.split(' - ');
      extName = p[0].replace('Người nhận: ', '').replace('Khách vãng lai: ', '').trim();
      extPhone = p[1].replace('SĐT: ', '').trim();
      extAddress = p[2].replace('Đ/C: ', '').split('. Ghi chú')[0].trim();
    } catch (e) {}
  }
  return { name: extName, phone: extPhone, address: extAddress };
};

const getInitials = (name) => {
  if (!name) return 'KH';
  const words = name.trim().split(' ');
  if (words.length === 1) return words[0].substring(0, 2).toUpperCase();
  return (words[0][0] + words[words.length - 1][0]).toUpperCase();
};

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + '₫' : "0₫";
const formatDate = (dateString) => {
  if(!dateString) return '';
  return new Date(dateString).toLocaleString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
};

const getStatusVietnamese = (id) => {
  const st = dbStatuses.value.find(s => s.statusId === id);
  return st ? st.description : 'Đang xử lý';
};

const getStatusClass = (id) => {
  if (id === 0) return 'bg-warning text-dark';
  if (id === 1) return 'bg-primary text-white';
  if (id === 2) return 'bg-info text-dark';
  if (id === 3) return 'bg-success text-white';
  if (id === 4) return 'bg-danger text-white';
  return 'bg-secondary text-white';
};

// Tùy thuộc vào trạng thái HIỆN TẠI, Admin chỉ được chọn các trạng thái TIẾP THEO hợp lý
const availableStatuses = computed(() => {
  if (!selectedOrder.value) return [];
  
  const currentStatus = selectedOrder.value.statusId;

  // Lọc danh sách status từ DB dựa trên quy tắc
  return dbStatuses.value.filter(st => {
    const targetStatus = st.statusId;
    
    // Luôn cho phép hiển thị lại trạng thái hiện tại
    if (targetStatus === currentStatus) return true;

    // Quy tắc chuyển trạng thái
    switch (currentStatus) {
      case 0: // Từ [Chờ xác nhận] -> Chỉ được [Đã xác nhận] hoặc [Hủy]
        return targetStatus === 1 || targetStatus === 4;
      
      case 1: // Từ [Đã xác nhận] -> Chỉ được [Đang giao] hoặc [Hủy]
        return targetStatus === 2 || targetStatus === 4;
      
      case 2: // Từ [Đang giao] -> Chỉ được [Hoàn thành] hoặc [Hủy/Giao thất bại]
        return targetStatus === 3 || targetStatus === 4;
      
      case 3: // [Hoàn thành] -> Không cho đổi nữa (Kết thúc quy trình)
        return false; 
      
      case 4: // [Đã hủy] -> Không cho đổi nữa
        return false;
        
      default:
        return false;
    }
  });
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');
.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }
.table-hover tbody tr:hover td { background-color: #f8f9fa; }
</style>