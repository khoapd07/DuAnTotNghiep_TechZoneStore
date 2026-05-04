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
        <button @click="openAddModal" class="btn btn-success fw-bold fs-7 text-white rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm" data-bs-toggle="modal" data-bs-target="#customerModal">
          <i class="bi bi-person-plus-fill"></i> Thêm Khách Hàng Mới
        </button>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng người dùng</p>
              <h2 class="fw-black text-dark m-0">{{ totalCustomers }}</h2>
            </div>
            <div class="bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-people-fill fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Khách mới (Tháng này)</p>
              <h2 class="fw-black text-success m-0">+{{ newCustomersThisMonth }}</h2>
            </div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-person-check-fill fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tài khoản bị khóa</p>
              <h2 class="fw-black text-danger m-0">{{ lockedCustomers }}</h2>
            </div>
            <div class="bg-danger-subtle text-danger rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-person-x-fill fs-4"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="p-3 border-bottom d-flex gap-2 bg-white rounded-top-4">
          <div class="input-group bg-light border rounded-3 overflow-hidden flex-grow-1">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input v-model="searchQuery" type="text" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm theo tên, username, email...">
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
                <th scope="col" class="py-3 fw-bold border-0">Trạng thái</th>
                <th scope="col" class="py-3 fw-bold border-0 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="isLoading">
                <td colspan="6" class="text-center py-4">
                  <div class="spinner-border text-success" role="status"></div>
                </td>
              </tr>
              <tr v-else v-for="user in filteredCustomers" :key="user.userId" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <span class="fw-bold fs-7 text-dark" :class="{'text-decoration-line-through text-muted': !user.status}">
                    {{ user.fullName || user.username }}
                  </span>
                </td>
                <td class="py-3">
                  <div class="d-flex flex-column justify-content-center h-100">
                    <span class="fs-7 text-dark fw-medium" :class="{'text-decoration-line-through text-muted': !user.status}">{{ user.email || 'Chưa cập nhật email' }}</span>
                    <span class="fs-8 text-muted">{{ user.phoneNumber || 'Chưa cập nhật SĐT' }}</span>
                  </div>
                </td>
                <td class="text-muted fs-7 py-3">{{ user.shippingAddress || 'Chưa cập nhật' }}</td>
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
                    <button v-if="user.status" @click="confirmToggle(user.userId)" class="btn btn-link p-0 text-warning shadow-none" title="Khóa tài khoản">
                      <i class="bi bi-slash-circle"></i>
                    </button>
                    <button v-else @click="confirmToggle(user.userId)" class="btn btn-link p-0 text-success shadow-none" title="Mở khóa tài khoản">
                      <i class="bi bi-check-circle-fill"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <div v-if="toast.show" class="position-fixed top-0 start-50 translate-middle-x mt-4 px-4 py-3 rounded-3 shadow-lg d-flex align-items-center gap-2" :class="toast.type === 'success' ? 'bg-dark text-white' : 'bg-danger text-white'" style="z-index: 9999; min-width: 300px; transition: all 0.3s;">
      <i class="bi fs-5" :class="toast.type === 'success' ? 'bi-check-circle-fill text-neon' : 'bi-exclamation-triangle-fill'"></i>
      <span class="fw-bold fs-7">{{ toast.message }}</span>
    </div>

    <div v-if="showToggleModal" class="custom-modal-overlay d-flex justify-content-center align-items-center">
      <div class="custom-modal bg-white rounded-4 p-4 text-center shadow-lg">
        <div class="mb-3">
          <i class="bi bi-shield-exclamation text-warning" style="font-size: 3.5rem;"></i>
        </div>
        <h5 class="fw-bold mb-2">Xác nhận thay đổi</h5>
        <p class="text-muted fs-8 mb-4">Bạn có chắc chắn muốn thay đổi trạng thái (Khóa/Mở khóa) của tài khoản này không?</p>
        <div class="d-flex gap-2 justify-content-center">
          <button @click="showToggleModal = false" class="btn btn-light border fs-8 fw-bold px-4 py-2 rounded-2">Hủy bỏ</button>
          <button @click="executeToggle" class="btn btn-warning text-dark fs-8 fw-bold px-4 py-2 rounded-2">Đồng ý</button>
        </div>
      </div>
    </div>

    <div class="modal fade" id="customerModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold">{{ isEditing ? 'Cập nhật Khách Hàng' : 'Thêm Khách Hàng Mới' }}</h5>
            <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveCustomer">
              
              <div class="alert alert-danger fs-8 py-2" v-if="errors.general">
                <i class="bi bi-exclamation-triangle-fill me-1"></i> {{ errors.general }}
              </div>

              <div v-if="!isEditing" class="row">
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Tên đăng nhập *</label>
                  <input v-model="form.username" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.username}" @input="errors.username = ''">
                  <span v-if="errors.username" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.username }}</span>
                </div>
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Mật khẩu *</label>
                  <input v-model="form.password" type="password" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.password}" @input="errors.password = ''">
                  <span v-if="errors.password" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.password }}</span>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label fs-8 fw-bold text-muted">Họ và tên</label>
                <input v-model="form.fullName" type="text" class="form-control shadow-none fs-7">
              </div>

              <div class="row">
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Email</label>
                  <input v-model="form.email" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.email || errors.contact}" @input="errors.email = ''; errors.contact = ''">
                  <span v-if="errors.email" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.email }}</span>
                </div>
                <div class="col-6 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Số điện thoại</label>
                  <input v-model="form.phoneNumber" type="text" class="form-control shadow-none fs-7" :class="{'is-invalid': errors.phoneNumber || errors.contact}" @input="errors.phoneNumber = ''; errors.contact = ''">
                  <span v-if="errors.phoneNumber" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.phoneNumber }}</span>
                </div>
                <div class="col-12 mt-0 mb-3" v-if="errors.contact">
                  <span class="text-danger fs-8 fw-bold d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.contact }}</span>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label fs-8 fw-bold text-muted">Địa chỉ giao hàng</label>
                <textarea v-model="form.shippingAddress" class="form-control shadow-none fs-7" rows="2"></textarea>
              </div>

              <div class="row">
                <div class="col-12 mb-3">
                  <label class="form-label fs-8 fw-bold text-muted">Trạng thái</label>
                  <select v-model="form.status" class="form-select shadow-none fs-7">
                    <option :value="true">Đang hoạt động</option>
                    <option :value="false">Bị khóa</option>
                  </select>
                </div>
              </div>

              <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-light fw-bold fs-7" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success fw-bold fs-7">{{ isEditing ? 'Lưu thay đổi' : 'Tạo khách hàng' }}</button>
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
import api from '../../utils/axios';

const customers = ref([]);
const isLoading = ref(false);
const searchQuery = ref('');
const statusFilter = ref('ALL');
const isEditing = ref(false);

const errors = reactive({}); 

const form = ref({
  userId: null,
  username: '',
  password: '',
  fullName: '',
  email: '',
  phoneNumber: '',
  shippingAddress: '',
  status: true 
});

// THÊM BIẾN TOAST
const toast = reactive({ show: false, message: '', type: 'success' });
const showToast = (message, type = 'success') => {
  toast.message = message; toast.type = type; toast.show = true;
  setTimeout(() => { toast.show = false; }, 3000);
};

// THÊM BIẾN CHO MODAL KHÓA TÀI KHOẢN
const showToggleModal = ref(false);
const idToToggle = ref(null);

const API_URL = '/admin/customers';

const fetchCustomers = async () => {
  isLoading.value = true;
  try {
    const response = await api.get(API_URL);
    customers.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error);
  } finally {
    isLoading.value = false;
  }
};

const totalCustomers = computed(() => customers.value.length);
const lockedCustomers = computed(() => customers.value.filter(c => !c.status).length);
const newCustomersThisMonth = computed(() => {
  const currentMonth = new Date().getMonth();
  const currentYear = new Date().getFullYear();
  return customers.value.filter(c => {
    if(!c.createdAt) return false;
    const date = new Date(c.createdAt);
    return date.getMonth() === currentMonth && date.getFullYear() === currentYear;
  }).length;
});

// --- LOGIC CHO MODAL ĐỔI TRẠNG THÁI ---
const confirmToggle = (id) => {
  idToToggle.value = id;
  showToggleModal.value = true;
};

const executeToggle = async () => {
  try {
    await api.put(`${API_URL}/${idToToggle.value}/toggle-status`);
    showToggleModal.value = false;
    idToToggle.value = null;
    fetchCustomers();
    showToast("Đã thay đổi trạng thái thành công!");
  } catch (error) { 
    showToast("Có lỗi xảy ra khi đổi trạng thái!", "error"); 
    showToggleModal.value = false;
  }
};

const openAddModal = () => {
  isEditing.value = false;
  Object.keys(errors).forEach(k => delete errors[k]); 
  form.value = { userId: null, username: '', password: '', fullName: '', email: '', phoneNumber: '', shippingAddress: '', status: true };
};

const openEditModal = (user) => {
  isEditing.value = true;
  Object.keys(errors).forEach(k => delete errors[k]); 
  form.value = {
    userId: user.userId,
    username: user.username || '',
    fullName: user.fullName || '',
    email: user.email || '',
    phoneNumber: user.phoneNumber || '',
    shippingAddress: user.shippingAddress || '',
    status: user.status
  };
};

const saveCustomer = async () => {
  Object.keys(errors).forEach(k => delete errors[k]);
  let isValid = true;

  if (!isEditing.value && (!form.value.username || form.value.username.trim() === '')) {
    errors.username = "Vui lòng nhập tên đăng nhập!";
    isValid = false;
  }

  if (!isEditing.value && (!form.value.password || form.value.password.length < 6)) {
    errors.password = "Mật khẩu phải có ít nhất 6 ký tự!";
    isValid = false;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (form.value.email && form.value.email.trim() !== '' && !emailRegex.test(form.value.email)) {
    errors.email = "Email không đúng định dạng! (Ví dụ: abc@gmail.com)";
    isValid = false;
  }

  const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
  if (form.value.phoneNumber && form.value.phoneNumber.trim() !== '' && !phoneRegex.test(form.value.phoneNumber)) {
    errors.phoneNumber = "SĐT không hợp lệ! Vui lòng nhập SĐT Việt Nam gồm 10 số.";
    isValid = false;
  }

  if ((!form.value.email || form.value.email.trim() === '') && 
      (!form.value.phoneNumber || form.value.phoneNumber.trim() === '')) {
    errors.contact = "Vui lòng nhập ít nhất Email hoặc Số điện thoại!";
    isValid = false;
  }

  if (!isValid) return;

  try {
    if (isEditing.value) {
      await api.put(`${API_URL}/${form.value.userId}`, form.value);
      showToast("Cập nhật khách hàng thành công!"); 
    } else {
      await api.post(API_URL, form.value);
      showToast("Tạo khách hàng mới thành công!"); 
    }
    
    fetchCustomers(); 
    
    const modalElement = document.getElementById('customerModal');
    if (modalElement && window.bootstrap) {
      let modalInstance = window.bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) modalInstance.hide();
    }
    
  } catch (error) {
    console.error('Lỗi khi lưu:', error);
    const errorMsg = error.response?.data?.message || error.response?.data || 'Thao tác thất bại. Vui lòng kiểm tra lại!';
    if (typeof errorMsg === 'string') {
      errors.general = errorMsg; 
    } else {
      showToast("Thao tác thất bại. Vui lòng kiểm tra lại!", "error");
    }
  }
};

const filteredCustomers = computed(() => {
  return customers.value.filter(user => {
    const query = searchQuery.value.toLowerCase();
    
    const matchSearch = !query || 
                        (user.fullName && user.fullName.toLowerCase().includes(query)) ||
                        (user.username && user.username.toLowerCase().includes(query)) ||
                        (user.email && user.email.toLowerCase().includes(query)) ||
                        (user.phoneNumber && user.phoneNumber.includes(query));
    
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

/* CSS MODAL CUSTOM (Dùng chung) */
.custom-modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  animation: fadeIn 0.2s ease-in-out;
}
.custom-modal {
  width: 90%;
  max-width: 400px;
  animation: slideDown 0.3s ease-out;
}
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideDown { from { transform: translateY(-20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
</style>