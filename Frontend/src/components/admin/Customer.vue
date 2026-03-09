<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Khách hàng</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Người Dùng</h3>
          <p class="text-muted m-0 fs-7">Theo dõi, quản lý và phân quyền thành viên trong hệ thống.</p>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="p-3 border-bottom d-flex gap-2 bg-white rounded-top-4">
          <div class="input-group bg-light border rounded-3 overflow-hidden flex-grow-1">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input v-model="searchQuery" type="text" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm theo tên, email...">
          </div>
          <select v-model="statusFilter" class="form-select form-select-sm border bg-white fw-bold text-dark shadow-none fs-7" style="width: 150px;">
            <option value="ALL">Tất cả trạng thái</option>
            <option value="ACTIVE">Đang hoạt động</option>
            <option value="LOCKED">Bị khóa</option>
          </select>
        </div>
        
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-4 fw-bold border-0">Khách hàng</th>
                <th scope="col" class="py-3 fw-bold border-0">Email / Số điện thoại</th>
                <th scope="col" class="py-3 fw-bold border-0">Địa chỉ giao hàng</th>
                <th scope="col" class="py-3 fw-bold border-0">Điểm tích lũy</th>
                <th scope="col" class="py-3 fw-bold border-0">Trạng thái</th>
                <th scope="col" class="py-3 fw-bold border-0 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="isLoading">
                <td colspan="6" class="text-center py-4">
                  <div class="spinner-border text-success" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </td>
              </tr>
              <tr v-else v-for="user in filteredCustomers" :key="user.userId" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <span class="fw-bold fs-7 text-dark" :class="{'text-decoration-line-through text-muted': !user.status}">
                    {{ user.fullName || 'Chưa cập nhật' }}
                  </span>
                </td>
                <td class="py-3">
                  <div class="d-flex flex-column justify-content-center h-100">
                    <span class="fs-7 text-dark fw-medium" :class="{'text-decoration-line-through text-muted': !user.status}">{{ user.email || 'Chưa cập nhật email' }}</span>
                    <span class="fs-8 text-muted">{{ user.phoneNumber || 'Chưa cập nhật SĐT' }}</span>
                  </div>
                </td>
                <td class="text-muted fs-7 py-3">{{ user.shippingAddress || 'Chưa cập nhật' }}</td>
                <td class="text-dark fw-bold fs-7 py-3">{{ user.loyaltyPoints }} đ</td>
                <td class="py-3">
                  <div class="d-flex align-items-center gap-2 fs-8 fw-bold" :class="!user.status ? 'text-danger' : 'text-success'">
                    <i class="bi bi-circle-fill" style="font-size: 6px;"></i> 
                    {{ user.status ? 'ĐANG HOẠT ĐỘNG' : 'BỊ KHÓA' }}
                  </div>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(user)" class="btn btn-link p-0 text-primary shadow-none" title="Chỉnh sửa" data-bs-toggle="modal" data-bs-target="#customerModal">
                      <i class="bi bi-pencil-fill"></i>
                    </button>
                    <button v-if="user.status" @click="toggleStatus(user.userId)" class="btn btn-link p-0 text-danger shadow-none" title="Khóa tài khoản nhanh">
                      <i class="bi bi-slash-circle"></i>
                    </button>
                    <button v-else @click="toggleStatus(user.userId)" class="btn btn-link p-0 text-success shadow-none" title="Mở khóa tài khoản nhanh">
                      <i class="bi bi-check-circle-fill"></i>
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="!isLoading && filteredCustomers.length === 0">
                <td colspan="6" class="text-center py-4 text-muted">Không tìm thấy khách hàng nào. (Kiểm tra lại DB đã có dữ liệu ở bảng Customers chưa nhé!)</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </main>

    <div class="modal fade" id="customerModal" tabindex="-1" aria-labelledby="customerModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold" id="customerModalLabel">Cập nhật Khách Hàng</h5>
            <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveCustomer">
              <div class="mb-3">
                <label class="form-label fs-8 fw-bold text-muted">Họ và tên</label>
                <input v-model="form.fullName" type="text" class="form-control shadow-none fs-7" required>
              </div>
              <div class="mb-3">
                <label class="form-label fs-8 fw-bold text-muted">Số điện thoại</label>
                <input v-model="form.phoneNumber" type="text" class="form-control shadow-none fs-7">
              </div>
              <div class="mb-3">
                <label class="form-label fs-8 fw-bold text-muted">Địa chỉ giao hàng</label>
                <textarea v-model="form.shippingAddress" class="form-control shadow-none fs-7" rows="2"></textarea>
              </div>
              <div class="row">
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Điểm tích lũy</label>
                  <input v-model="form.loyaltyPoints" type="number" min="0" class="form-control shadow-none fs-7">
                </div>
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Trạng thái tài khoản</label>
                  <select v-model="form.status" class="form-select shadow-none fs-7">
                    <option :value="true">Đang hoạt động</option>
                    <option :value="false">Bị khóa</option>
                  </select>
                </div>
              </div>
              <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-light fw-bold fs-7" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success fw-bold fs-7">Lưu thay đổi</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const customers = ref([]);
const isLoading = ref(false);
const searchQuery = ref('');
const statusFilter = ref('ALL');

// Bổ sung thêm 'status' vào State form
const form = ref({
  userId: null,
  fullName: '',
  phoneNumber: '',
  shippingAddress: '',
  loyaltyPoints: 0,
  status: true 
});

const API_URL = 'http://localhost:8080/api/admin/customers';

const getAuthConfig = () => {
  const token = localStorage.getItem('token');
  return token ? { headers: { Authorization: `Bearer ${token}` } } : {};
};

const fetchCustomers = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get(API_URL, getAuthConfig());
    customers.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error);
    if (error.response && error.response.status === 403) {
      alert('Bạn không có quyền hoặc Token đã hết hạn. Vui lòng đăng nhập lại!');
    } else {
      alert('Không thể tải dữ liệu từ server. Hãy kiểm tra Backend.');
    }
  } finally {
    isLoading.value = false;
  }
};

const toggleStatus = async (id) => {
  if(!confirm('Bạn có chắc chắn muốn thay đổi trạng thái tài khoản này?')) return;
  try {
    await axios.put(`${API_URL}/${id}/toggle-status`, {}, getAuthConfig());
    fetchCustomers();
  } catch (error) {
    console.error('Lỗi:', error);
    alert('Có lỗi xảy ra!');
  }
};

// Map dữ liệu khi mở Modal, lấy cả status
const openEditModal = (user) => {
  form.value = {
    userId: user.userId,
    fullName: user.fullName || '',
    phoneNumber: user.phoneNumber || '',
    shippingAddress: user.shippingAddress || '',
    loyaltyPoints: user.loyaltyPoints || 0,
    status: user.status // Map đúng status hiện tại lên UI
  };
};

const saveCustomer = async () => {
  try {
    await axios.put(`${API_URL}/${form.value.userId}`, form.value, getAuthConfig());
    alert('Cập nhật thành công!');
    fetchCustomers(); 
    
    const modalElement = document.getElementById('customerModal');
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    if(modalInstance) modalInstance.hide();
    
  } catch (error) {
    console.error('Lỗi:', error);
    alert('Cập nhật thất bại!');
  }
};

const filteredCustomers = computed(() => {
  return customers.value.filter(user => {
    // 1. Lấy từ khóa tìm kiếm
    const query = searchQuery.value.toLowerCase();
    
    // 2. Lỗi nằm ở đây đã được sửa: 
    // Nếu ô tìm kiếm trống (!query) -> Cho qua luôn.
    // Nếu có tìm kiếm thì mới check fullName, email, hoặc phoneNumber.
    const matchSearch = !query || 
                        (user.fullName && user.fullName.toLowerCase().includes(query)) ||
                        (user.email && user.email.toLowerCase().includes(query)) ||
                        (user.phoneNumber && user.phoneNumber.includes(query));
    
    // 3. Lọc theo trạng thái
    let matchStatus = true;
    if (statusFilter.value === 'ACTIVE') matchStatus = user.status === true;
    if (statusFilter.value === 'LOCKED') matchStatus = user.status === false;

    return matchSearch && matchStatus;
  });
});

onMounted(() => {
  fetchCustomers();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }

.text-decoration-line-through { text-decoration: line-through; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
</style>