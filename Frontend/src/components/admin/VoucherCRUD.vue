<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    <main class="flex-grow-1 p-4 overflow-auto">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 class="fw-black text-dark mb-1 fs-4 text-uppercase">Quản Lý Voucher</h3>
        </div>
        
        <div class="d-flex gap-3 align-items-center">
          <div class="input-group" style="width: 250px;">
            <span class="input-group-text bg-white border-end-0 text-muted">
              <i class="bi bi-search"></i>
            </span>
            <input type="text" v-model="searchQuery" class="form-control border-start-0 ps-0 bg-white shadow-none" placeholder="Tìm kiếm mã giảm giá...">
          </div>

          <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-plus-lg"></i> TẠO VOUCHER MỚI
          </button>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">STT</th>
                <th class="py-3 fw-bold border-0">Mã Voucher</th>
                <th class="py-3 fw-bold border-0" style="max-width: 150px;">Tên Chương Trình</th>
                <th class="py-3 fw-bold border-0">Giảm Giá</th>
                <th class="py-3 fw-bold border-0">Đơn Hàng Tối Thiểu</th>
                <th class="py-3 fw-bold border-0">Hiệu Lực</th>
                <th class="py-3 fw-bold border-0 text-center">Số Lượng</th>
                <th class="py-3 fw-bold border-0 text-center">Trạng Thái</th>
                <th class="py-3 fw-bold border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(voucher, index) in filteredVouchers" :key="voucher.voucherId" class="border-bottom-dashed">
                <td class="py-3 px-4 text-muted">{{ index + 1 }}</td>
                <td class="py-3">
                  <span class="badge bg-light text-dark border fs-7 px-2 py-1 text-uppercase">{{ voucher.code }}</span>
                </td>
                <td class="py-3 text-wrap" style="max-width: 150px;">
                  <span class="fw-bold fs-8 text-dark">{{ voucher.name }}</span>
                </td>
                <td class="py-3 fw-bold fs-7 text-dark">{{ formatCurrency(voucher.discountAmount) }}</td>
                <td class="py-3 text-muted fs-8">{{ formatCurrency(voucher.minOrderValue) }}</td>
                <td class="py-3 fs-8 text-muted">
                  {{ formatDate(voucher.startDate) }} <br/>
                  - <br/>
                  {{ formatDate(voucher.endDate) }}
                </td>
                <td class="text-center py-3 fw-bold fs-7">{{ voucher.quantity }}</td>
                <td class="text-center py-3">
                  <span class="badge rounded-pill fs-8 px-3 py-2" :class="getStatusBadgeClass(voucher)">
                    {{ getStatusText(voucher) }}
                  </span>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(voucher)" class="btn btn-link p-0 text-primary shadow-none">
                      <i class="bi bi-pencil-square fs-6"></i>
                    </button>
                    <button @click="deleteVoucher(voucher.voucherId)" class="btn btn-link p-0 text-danger shadow-none">
                      <i class="bi bi-trash fs-6"></i>
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="filteredVouchers.length === 0">
                <td colspan="9" class="text-center py-4 text-muted fs-7">Không tìm thấy voucher nào.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT VOUCHER' : 'TẠO VOUCHER MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="row g-3">
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Mã Voucher</label>
                <input type="text" v-model="form.code" class="form-control fs-7 text-uppercase" placeholder="VD: SUMMER2024">
              </div>
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên chương trình</label>
                <input type="text" v-model="form.name" class="form-control fs-7" placeholder="Nhập tên chương trình">
              </div>
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giảm giá (VNĐ)</label>
                <input type="number" v-model="form.discountAmount" class="form-control fs-7" placeholder="0">
              </div>
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Đơn hàng tối thiểu (VNĐ)</label>
                <input type="number" v-model="form.minOrderValue" class="form-control fs-7" placeholder="0">
              </div>
              <div class="col-md-4">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Số lượng</label>
                <input type="number" v-model="form.quantity" class="form-control fs-7" placeholder="100">
              </div>
              <div class="col-md-4">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Ngày bắt đầu</label>
                <input type="datetime-local" v-model="form.startDate" class="form-control fs-7">
              </div>
              <div class="col-md-4">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Ngày kết thúc</label>
                <input type="datetime-local" 
                        v-model="form.endDate" 
                        :min="form.startDate" class="form-control fs-7">
                </div>
              <div class="col-md-12 mt-3">
                <div class="form-check form-switch">
                  <input class="form-check-input" type="checkbox" role="switch" id="statusSwitch" v-model="form.status">
                  <label class="form-check-label fs-7 fw-bold" for="statusSwitch">
                    Kích hoạt (Hoạt động)
                  </label>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" @click="saveVoucher">
              {{ isEditing ? 'Cập Nhật' : 'Lưu Voucher' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';

const voucherList = ref([]);
const searchQuery = ref('');
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);

// Form data tương ứng với Model Voucher
const form = reactive({ 
  code: '', 
  name: '', 
  discountAmount: 0, 
  minOrderValue: 0, 
  quantity: 1, 
  startDate: '', 
  endDate: '',
  status: true 
});

const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

// Gọi API lấy list voucher
const fetchVouchers = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/vouchers', { headers: getAuthHeader() });
    voucherList.value = res.data;
  } catch (e) { 
    console.error(e); 
  }
};

// Tìm kiếm voucher theo mã hoặc tên
const filteredVouchers = computed(() => {
  if (!searchQuery.value) return voucherList.value;
  const lowerQuery = searchQuery.value.toLowerCase();
  return voucherList.value.filter(v => 
    v.code.toLowerCase().includes(lowerQuery) || 
    v.name.toLowerCase().includes(lowerQuery)
  );
});

// Format Tiền Tệ VNĐ
const formatCurrency = (value) => {
  if (!value && value !== 0) return '';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

// Format Ngày Tháng (VD: 01/06/24)
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = String(date.getFullYear()).slice(-2);
  return `${day}/${month}/${year}`;
};

// Logic tính trạng thái (Đang hoạt động / Hết hạn / Hết số lượng)
const getStatusText = (voucher) => {
  const now = new Date();
  const endDate = new Date(voucher.endDate);
  
  if (voucher.status === false) return 'Ngừng hoạt động';
  if (now > endDate) return 'Hết hạn';
  if (voucher.quantity <= 0) return 'Hết số lượng';
  return 'Đang hoạt động';
};

// Logic màu sắc badge tương ứng hình mẫu
const getStatusBadgeClass = (voucher) => {
  const statusText = getStatusText(voucher);
  if (statusText === 'Đang hoạt động') {
    return 'bg-success-subtle text-success border border-success-subtle';
  } else {
    return 'bg-danger-subtle text-danger border border-danger-subtle'; // Cho Hết hạn / Ngừng HĐ
  }
};

// Format date để set vào input datetime-local
const toDatetimeLocal = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toISOString().slice(0, 16);
};

// Hàm lấy thời gian hiện tại chuẩn định dạng cho input datetime-local (YYYY-MM-DDTHH:mm)
const getCurrentDateTimeLocal = () => {
  const now = new Date();
  // Chỉnh múi giờ về giờ địa phương để không bị lệch giờ UTC
  const offset = now.getTimezoneOffset() * 60000; 
  const localTime = new Date(now.getTime() - offset);
  return localTime.toISOString().slice(0, 16);
};

// Mở modal thêm mới
const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  Object.assign(form, {
    code: '', 
    name: '', 
    discountAmount: 0, 
    minOrderValue: 0, 
    quantity: 100, 
    startDate: getCurrentDateTimeLocal(), // <-- Tự động lấy giờ hiện tại khi bấm nút
    endDate: '', 
    status: true
  });
  showModal.value = true;
};

// Mở modal chỉnh sửa
const openEditModal = (voucher) => {
  isEditing.value = true;
  currentId.value = voucher.voucherId;
  Object.assign(form, {
    code: voucher.code,
    name: voucher.name,
    discountAmount: voucher.discountAmount,
    minOrderValue: voucher.minOrderValue,
    quantity: voucher.quantity,
    startDate: toDatetimeLocal(voucher.startDate),
    endDate: toDatetimeLocal(voucher.endDate),
    status: voucher.status
  });
  showModal.value = true;
};

// Lưu Data
const saveVoucher = async () => {
  // --- THÊM ĐOẠN VALIDATE NÀY VÀO ĐẦU HÀM ---
  if (!form.startDate || !form.endDate) {
    alert("Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!");
    return;
  }
  
  const start = new Date(form.startDate);
  const end = new Date(form.endDate);
  
  if (end <= start) {
    alert("Lỗi: Ngày kết thúc phải lớn hơn ngày bắt đầu!");
    return; // Dừng luôn, không cho chạy xuống API lưu
  }
  // ------------------------------------------

  try {
    const headers = getAuthHeader();
    const payload = { ...form };

    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/vouchers/${currentId.value}`, payload, { headers });
    } else {
      await axios.post('http://localhost:8080/api/vouchers', payload, { headers });
    }
    showModal.value = false;
    fetchVouchers();
    alert("Lưu voucher thành công!");
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || "Không thể thực hiện"));
  }
};

// Xóa Data
const deleteVoucher = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa voucher này không?")) {
    try {
      await axios.delete(`http://localhost:8080/api/vouchers/${id}`, { headers: getAuthHeader() });
      fetchVouchers();
      alert("Xóa thành công!");
    } catch (error) {
      alert("Lỗi khi xóa! Vui lòng thử lại sau.");
    }
  }
};

onMounted(() => fetchVouchers());
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { 
  font-family: 'Inter', system-ui, sans-serif; 
}
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; }
.btn-neon:hover { background-color: #00e62e; }

/* Bảng dữ liệu */
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }

/* Tránh giật UI khi hover dòng bảng */
.table-hover tbody tr:hover td {
  background-color: #f8f9fa;
}
</style>