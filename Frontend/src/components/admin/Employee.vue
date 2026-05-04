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
                  <input v-model="form.username" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.username}" @input="errors.username = ''">
                  <span v-if="errors.username" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.username }}</span>
                  <small v-else class="text-success fs-8">Mật khẩu mặc định sẽ là: 123456</small>
                </div>
                <div class="col-md-6" v-else>
                  <label class="form-label fs-8 fw-bold text-muted">Tên đăng nhập</label>
                  <input v-model="form.username" type="text" class="form-control shadow-none fs-7" disabled>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Mã nhân viên (*)</label>
                  <input v-model="form.employeeCode" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.employeeCode}" @input="errors.employeeCode = ''">
                  <span v-if="errors.employeeCode" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.employeeCode }}</span>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Họ và tên (*)</label>
                  <input v-model="form.fullName" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.fullName}" @input="errors.fullName = ''">
                  <span v-if="errors.fullName" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.fullName }}</span>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Cấp quyền (*)</label>
                  <select v-model="form.roleId" class="form-select shadow-none fs-7">
                    <option :value="1">Staff (Nhân viên)</option>
                    <option :value="2">Admin (Quản trị viên)</option>
                    <option :value="3">Shipper (Giao hàng)</option>
                  </select>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Email</label>
                  <input v-model="form.email" type="email" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.contact}" @input="errors.contact = ''">
                </div>
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted">Số điện thoại</label>
                  <input v-model="form.phoneNumber" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.contact}" @input="errors.contact = ''">
                </div>
                <div class="col-12 mt-1" v-if="errors.contact">
                  <span class="text-danger fs-8 fw-bold d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.contact }}</span>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-4">
                  <label class="form-label fs-8 fw-bold text-muted">Ngày vào làm (*)</label>
                  <input v-model="form.hireDate" type="date" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.hireDate}" @input="errors.hireDate = ''">
                  <span v-if="errors.hireDate" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.hireDate }}</span>
                </div>
                <div class="col-md-4">
                  <label class="form-label fs-8 fw-bold text-muted">Mức lương (VNĐ) (*)</label>
                  <input v-model="form.salary" type="number" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.salary}" @input="errors.salary = ''">
                  <span v-if="errors.salary" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.salary }}</span>
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
import { ref, onMounted, computed, reactive } from 'vue';
// Import api instance thay cho axios
import api from '../../utils/axios';

const employees = ref([]);
const isLoading = ref(false);
const searchQuery = ref('');
const statusFilter = ref('ALL');
const roleFilter = ref('ALL');

const isEditMode = ref(false);
const errors = reactive({}); // Khởi tạo object (đối tượng) chứa lỗi

const form = ref({
  userId: null,
  username: '',
  fullName: '',
  email: '',
  phoneNumber: '',
  employeeCode: '',
  roleId: 1, 
  hireDate: '',
  salary: 0,
  status: true
});

// Sửa lại API_URL thành đường dẫn tương đối
const API_URL = '/admin/employees';

// Hàm getAuthConfig() đã được loại bỏ

const fetchEmployees = async () => {
  isLoading.value = true;
  try {
    // Dùng api thay vì axios
    const response = await api.get(API_URL);
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
    // Dùng api thay vì axios
    await api.put(`${API_URL}/${id}/toggle-status`);
    fetchEmployees();
  } catch (error) {
    alert('Có lỗi xảy ra!');
  }
};

const openAddModal = () => {
  isEditMode.value = false;
  Object.keys(errors).forEach(k => delete errors[k]); // Reset (Đặt lại) lỗi khi mở form
  form.value = {
    userId: null, username: '', fullName: '', email: '', phoneNumber: '', 
    employeeCode: '', roleId: 1, hireDate: new Date().toISOString().split('T')[0], 
    salary: 0, status: true
  };
};

const openEditModal = (emp) => {
  isEditMode.value = true;
  Object.keys(errors).forEach(k => delete errors[k]); // Reset (Đặt lại) lỗi khi mở form
  form.value = { 
    ...emp,
    roleId: emp.roleId || 1 
  };
};

const saveEmployee = async () => {
  // Validate (Xác thực) dữ liệu đầu vào
  Object.keys(errors).forEach(k => delete errors[k]);
  let isValid = true;

  if (!isEditMode.value && (!form.value.username || form.value.username.trim() === '')) {
    errors.username = "Vui lòng nhập tên đăng nhập!";
    isValid = false;
  }
  if (!form.value.employeeCode || form.value.employeeCode.trim() === '') {
    errors.employeeCode = "Vui lòng nhập mã nhân viên!";
    isValid = false;
  }
  if (!form.value.fullName || form.value.fullName.trim() === '') {
    errors.fullName = "Vui lòng nhập họ và tên!";
    isValid = false;
  }
  if (!form.value.hireDate) {
    errors.hireDate = "Vui lòng chọn ngày vào làm!";
    isValid = false;
  }
  
  // Validate Contact (Xác thực Liên hệ): Ít nhất 1 trong 2
  if ((!form.value.email || form.value.email.trim() === '') && 
      (!form.value.phoneNumber || form.value.phoneNumber.trim() === '')) {
    errors.contact = "Vui lòng nhập ít nhất Email hoặc Số điện thoại!";
    isValid = false;
  }

  // Validate Salary (Xác thực Mức lương)
  if (!form.value.salary || Number(form.value.salary) <= 0) {
    errors.salary = "Mức lương không được để trống và phải lớn hơn 0!";
    isValid = false;
  }

  // Dừng lại nếu có lỗi
  if (!isValid) return;

  try {
    if (isEditMode.value) {
      // Dùng api thay vì axios
      await api.put(`${API_URL}/${form.value.userId}`, form.value);
    } else {
      // Dùng api thay vì axios
      await api.post(API_URL, form.value);
    }
    alert(isEditMode.value ? 'Cập nhật thành công!' : 'Thêm mới thành công! (Mật khẩu mặc định: 123456)');
    fetchEmployees(); 
    document.getElementById('employeeModal').querySelector('.btn-close').click();
  } catch (error) {
    console.error(error);
    const backendErrorMsg = error.response?.data?.message || error.response?.data || 'Lưu thất bại! Vui lòng kiểm tra lại dữ liệu.';
    alert(`Lỗi: ${backendErrorMsg}`);
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