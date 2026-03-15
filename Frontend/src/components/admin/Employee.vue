<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Nhân sự</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Nhân Viên</h3>
          <p class="text-muted m-0 fs-7">Quản lý hồ sơ, phân quyền và trạng thái làm việc của nhân viên.</p>
        </div>
        <div class="d-flex align-items-center gap-3">
          <button @click="openAddModal" class="btn btn-success fw-bold fs-7 text-white rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm" data-bs-toggle="modal" data-bs-target="#employeeModal">
            <i class="bi bi-person-plus-fill"></i> Thêm nhân viên
          </button>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="p-3 border-bottom d-flex gap-2 bg-white rounded-top-4">
          <div class="input-group bg-light border rounded-3 overflow-hidden flex-grow-1">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input v-model="searchQuery" type="text" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm theo mã NV, tên, email...">
          </div>
          
          <select v-model="roleFilter" class="form-select form-select-sm border bg-white fw-bold text-dark shadow-none fs-7" style="width: 150px;">
            <option value="ALL">Tất cả Quyền</option>
            <option :value="2">Admin</option>
            <option :value="1">Staff</option>
            <option :value="3">Shipper</option>
          </select>
          
          <select v-model="statusFilter" class="form-select form-select-sm border bg-white fw-bold text-dark shadow-none fs-7" style="width: 150px;">
            <option value="ALL">Tất cả trạng thái</option>
            <option value="ACTIVE">Đang làm việc</option>
            <option value="LOCKED">Đã khóa</option>
          </select>
        </div>
        
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-4 fw-bold border-0">Mã NV</th>
                <th scope="col" class="py-3 fw-bold border-0">Họ và tên</th>
                <th scope="col" class="py-3 fw-bold border-0">Email / Số ĐT</th>
                <th scope="col" class="py-3 fw-bold border-0">Phân Quyền</th> <th scope="col" class="py-3 fw-bold border-0">Mức lương</th>
                <th scope="col" class="py-3 fw-bold border-0">Trạng thái</th>
                <th scope="col" class="py-3 fw-bold border-0 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="isLoading">
                <td colspan="7" class="text-center py-4"><div class="spinner-border text-success"></div></td>
              </tr>
              <tr v-else v-for="emp in filteredEmployees" :key="emp.userId" class="border-bottom-dashed">
                <td class="px-4 py-3 fw-bold text-primary fs-7">{{ emp.employeeCode }}</td>
                <td class="py-3 fw-bold fs-7 text-dark" :class="{'text-decoration-line-through text-muted': !emp.status}">
                  {{ emp.fullName || emp.username }}
                </td>
                <td class="py-3">
                  <div class="d-flex flex-column justify-content-center h-100">
                    <span class="fs-7 text-dark fw-medium" :class="{'text-decoration-line-through text-muted': !emp.status}">{{ emp.email || 'N/A' }}</span>
                    <span class="fs-8 text-muted">{{ emp.phoneNumber || 'N/A' }}</span>
                  </div>
                </td>
                
                <td class="py-3">
                  <span class="badge px-3 py-1 fs-8 fw-bold" 
                        :class="{
                          'bg-danger-subtle text-danger': emp.roleId === 2, 
                          'bg-primary-subtle text-primary': emp.roleId === 1,
                          'bg-warning-subtle text-warning': emp.roleId === 3
                        }">
                    {{ emp.roleName }}
                  </span>
                </td>
                
                <td class="text-dark fw-bold fs-7 py-3">{{ formatCurrency(emp.salary) }}</td>
                <td class="py-3">
                  <div class="d-flex align-items-center gap-2 fs-8 fw-bold" :class="!emp.status ? 'text-danger' : 'text-success'">
                    <i class="bi bi-circle-fill" style="font-size: 6px;"></i> 
                    {{ emp.status ? 'ĐANG HOẠT ĐỘNG' : 'ĐÃ KHÓA' }}
                  </div>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(emp)" class="btn btn-link p-0 text-primary shadow-none" data-bs-toggle="modal" data-bs-target="#employeeModal"><i class="bi bi-pencil-fill"></i></button>
                    <button v-if="emp.status" @click="toggleStatus(emp.userId)" class="btn btn-link p-0 text-danger shadow-none" title="Khóa tài khoản"><i class="bi bi-slash-circle"></i></button>
                    <button v-else @click="toggleStatus(emp.userId)" class="btn btn-link p-0 text-success shadow-none" title="Mở khóa tài khoản"><i class="bi bi-check-circle-fill"></i></button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <div class="modal fade" id="employeeModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold">{{ isEditMode ? 'Cập nhật Nhân viên' : 'Thêm Tài khoản mới' }}</h5>
            <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveEmployee">
              
              <div class="row mb-3">
                <div class="col-md-6" v-if="!isEditMode">
                  <label class="form-label fs-8 fw-bold text-muted">Tên đăng nhập (*)</label>
                  <input v-model="form.username" type="text" class="form-control shadow-none fs-7" required>
                  <small class="text-success fs-8">Mật khẩu mặc định sẽ là: 123456</small>
                </div>
                <div class="col-md-6" v-else>
                  <label class="form-label fs-8 fw-bold text-muted">Tên đăng nhập</label>
                  <input v-model="form.username" type="text" class="form-control shadow-none fs-7" disabled>
                </div>
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Mã nhân viên (*)</label>
                  <input v-model="form.employeeCode" type="text" class="form-control shadow-none fs-7" required>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Họ và tên (*)</label>
                  <input v-model="form.fullName" type="text" class="form-control shadow-none fs-7" required>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Cấp quyền (*)</label>
                  <select v-model="form.roleId" class="form-select shadow-none fs-7" required>
                    <option :value="1">Staff (Nhân viên)</option>
                    <option :value="2">Admin (Quản trị viên)</option>
                    <option :value="3">Shipper (Giao hàng)</option>
                  </select>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Email</label>
                  <input v-model="form.email" type="email" class="form-control shadow-none fs-7">
                </div>
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Số điện thoại</label>
                  <input v-model="form.phoneNumber" type="text" class="form-control shadow-none fs-7">
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-4">
                  <label class="form-label fs-8 fw-bold text-muted">Ngày vào làm</label>
                  <input v-model="form.hireDate" type="date" class="form-control shadow-none fs-7" required>
                </div>
                <div class="col-md-4">
                  <label class="form-label fs-8 fw-bold text-muted">Mức lương (VNĐ)</label>
                  <input v-model="form.salary" type="number" min="0" class="form-control shadow-none fs-7" required>
                </div>
                <div class="col-md-4">
                  <label class="form-label fs-8 fw-bold text-muted">Trạng thái</label>
                  <select v-model="form.status" class="form-select shadow-none fs-7">
                    <option :value="true">Đang hoạt động</option>
                    <option :value="false">Khóa tài khoản</option>
                  </select>
                </div>
              </div>

              <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-light fw-bold fs-7" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success fw-bold fs-7">Lưu thông tin</button>
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

const employees = ref([]);
const isLoading = ref(false);
const searchQuery = ref('');
const statusFilter = ref('ALL');
const roleFilter = ref('ALL'); // Đổi từ deptFilter sang roleFilter

const isEditMode = ref(false);

const form = ref({
  userId: null,
  username: '',
  fullName: '',
  email: '',
  phoneNumber: '',
  employeeCode: '',
  roleId: 1, // Thay department bằng roleId (mặc định là 1: Staff)
  hireDate: '',
  salary: 0,
  status: true
});

const API_URL = 'http://localhost:8080/api/admin/employees';

const getAuthConfig = () => {
  const token = localStorage.getItem('token');
  return token ? { headers: { Authorization: `Bearer ${token}` } } : {};
};

const fetchEmployees = async () => {
  isLoading.value = true;
  try {
    const response = await axios.get(API_URL, getAuthConfig());
    employees.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error);
  } finally {
    isLoading.value = false;
  }
};

const toggleStatus = async (id) => {
  if(!confirm('Xác nhận thay đổi trạng thái làm việc của nhân viên này?')) return;
  try {
    await axios.put(`${API_URL}/${id}/toggle-status`, {}, getAuthConfig());
    fetchEmployees();
  } catch (error) {
    alert('Có lỗi xảy ra!');
  }
};

const openAddModal = () => {
  isEditMode.value = false;
  form.value = {
    userId: null, username: '', fullName: '', email: '', phoneNumber: '', 
    employeeCode: '', roleId: 1, hireDate: new Date().toISOString().split('T')[0], 
    salary: 0, status: true
  };
};

const openEditModal = (emp) => {
  isEditMode.value = true;
  form.value = { 
    ...emp,
    roleId: emp.roleId || 1 // Gán lại roleId khi mở Form sửa
  };
};

const saveEmployee = async () => {
  try {
    if (isEditMode.value) {
      await axios.put(`${API_URL}/${form.value.userId}`, form.value, getAuthConfig());
    } else {
      await axios.post(API_URL, form.value, getAuthConfig());
    }
    alert(isEditMode.value ? 'Cập nhật thành công!' : 'Thêm mới thành công! (Mật khẩu mặc định: 123456)');
    fetchEmployees(); 
    document.getElementById('employeeModal').querySelector('.btn-close').click();
  } catch (error) {
    console.error(error);
    alert('Lưu thất bại! Hãy kiểm tra lại Tên đăng nhập hoặc Mã nhân viên có bị trùng không.');
  }
};

const formatCurrency = (value) => {
  if (!value) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const filteredEmployees = computed(() => {
  return employees.value.filter(emp => {
    const query = searchQuery.value.toLowerCase();
    const matchSearch = !query || 
                        (emp.fullName && emp.fullName.toLowerCase().includes(query)) ||
                        (emp.employeeCode && emp.employeeCode.toLowerCase().includes(query)) ||
                        (emp.email && emp.email.toLowerCase().includes(query));
    
    let matchStatus = true;
    if (statusFilter.value === 'ACTIVE') matchStatus = emp.status === true;
    if (statusFilter.value === 'LOCKED') matchStatus = emp.status === false;

    // Lọc theo Role
    let matchRole = true;
    if (roleFilter.value !== 'ALL') {
      matchRole = emp.roleId === Number(roleFilter.value);
    }

    return matchSearch && matchStatus && matchRole;
  });
});

onMounted(() => { fetchEmployees(); });
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');
.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.text-decoration-line-through { text-decoration: line-through; }
</style>