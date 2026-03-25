<template>
  <div class="bg-white min-vh-100 py-5">
    <main class="container-fluid" style="max-width: 1400px;">
      
      <header class="d-flex justify-content-between align-items-center mb-4 mt-2">
        <div>
          <div class="text-muted fw-semibold small mb-1">
            Admin <span class="mx-1">/</span> <span class="text-dark fw-bold">Nhà cung cấp</span>
          </div>
          <h2 class="fw-bolder text-dark mb-0" style="font-size: 1.8rem; letter-spacing: -0.5px; font-family: 'Space Grotesk', sans-serif;">
            Quản Lý Nhà Cung Cấp
          </h2>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="input-group input-group-lg shadow-sm" style="width: 320px;">
            <span class="input-group-text bg-light border-end-0" id="search-icon">
              <i class="bi bi-search text-muted"></i>
            </span>
            <input 
              type="text" 
              class="form-control border-start-0 bg-light fs-6 shadow-none" 
              placeholder="Tìm kiếm theo tên, ID..." 
              v-model="searchQuery"
            />
          </div>
        </div>
      </header>

      <div class="card border-0 shadow-sm rounded-4 mb-4" style="background: rgba(255, 255, 255, 1); border: 1px solid rgba(171, 173, 174, 0.2) !important;">
        <div class="card-body p-4 p-md-5">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="fw-bold mb-0" style="font-family: 'Space Grotesk', sans-serif;">Danh Sách Đối Tác</h4>
            
            <button @click="openAddModal" class="btn btn-success px-4 py-2 fw-bold d-flex align-items-center gap-2 shadow-sm rounded-3" style="background-color: #00DF3A; border-color: #00DF3A;">
              <i class="bi bi-person-plus-fill"></i> Thêm Nhà Cung Cấp
            </button>
          </div>

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead>
                <tr class="text-muted" style="font-size: 12px; letter-spacing: 0.1em;">
                  <th class="pb-3 text-uppercase border-bottom">ID</th>
                  <th class="pb-3 text-uppercase border-bottom">Nhà cung cấp</th>
                  <th class="pb-3 text-uppercase border-bottom">Người liên hệ</th>
                  <th class="pb-3 text-uppercase border-bottom">Số điện thoại</th>
                  <th class="pb-3 text-uppercase border-bottom">Email</th>
                  <th class="pb-3 text-uppercase border-bottom">Địa chỉ</th>
                  <th class="pb-3 text-uppercase border-bottom">Trạng thái</th>
                  <th class="pb-3 border-bottom text-end">Thao tác</th>
                </tr>
              </thead>
              <tbody class="border-top-0">
                <tr v-if="filteredSuppliers.length === 0">
                  <td colspan="8" class="text-center py-4 text-muted">Không tìm thấy dữ liệu.</td>
                </tr>

                <tr v-for="(supplier, index) in filteredSuppliers" :key="index" class="position-relative group-hover-show">
                  <td class="py-3 text-muted font-monospace" style="font-size: 13px;">SUP-{{ supplier.id || supplier.supplierId }}</td>
                  <td class="py-3"><p class="fw-bold text-dark mb-0">{{ supplier.name || supplier.supplierName }}</p></td>
                  <td class="py-3 text-dark" style="font-size: 14px;">{{ supplier.contact || supplier.contactName }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.phone || supplier.phoneNumber }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.email }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.address }}</td>
                  <td class="py-3">
                    <span 
                      class="badge rounded-pill border py-2 px-3 text-uppercase"
                      :class="supplier.status ? 'bg-success-subtle text-success border-success-subtle' : 'bg-secondary-subtle text-secondary border-secondary-subtle'"
                      style="font-size: 10px; font-weight: 700;"
                    >
                      {{ supplier.status ? 'Đang hợp tác' : 'Ngừng hợp tác' }}
                    </span>
                  </td>
                  <td class="py-3 text-end">
                    <div class="d-flex justify-content-end gap-1 action-buttons">
                      <button @click="openEditModal(supplier)" class="btn btn-sm btn-light text-primary hover-bg-primary rounded-circle shadow-none" title="Sửa">
                        <i class="bi bi-pencil-square"></i>
                      </button>
                      <button @click="deleteSupplier(supplier.id || supplier.supplierId)" class="btn btn-sm btn-light text-danger hover-bg-danger rounded-circle shadow-none" title="Ngừng hợp tác (Xóa)">
                        <i class="bi bi-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <transition name="modal-fade">
      <div v-if="showModal" class="modal-custom-overlay">
        <div class="modal-dialog modal-dialog-centered" style="width: 100%; max-width: 600px; margin: auto;">
          <div class="modal-content rounded-4 border-0 shadow-lg bg-white">
            
            <div class="modal-header border-bottom p-4">
              <h5 class="modal-title fw-bold m-0" style="font-family: 'Space Grotesk', sans-serif; font-size: 1.25rem;">
                {{ isEdit ? 'Cập Nhật Nhà Cung Cấp' : 'Thêm Nhà Cung Cấp Mới' }}
              </h5>
              <button type="button" class="btn-close shadow-none" @click="closeModal"></button>
            </div>
            
            <div class="modal-body p-4">
              <form @submit.prevent="saveSupplier">
                <div class="mb-3">
                  <label class="form-label fw-semibold small text-muted">Tên nhà cung cấp <span class="text-danger">*</span></label>
                  <input type="text" class="form-control bg-light shadow-none border-0 py-2" v-model="formData.name" required placeholder="Nhập tên đối tác...">
                </div>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold small text-muted">Người liên hệ</label>
                    <input type="text" class="form-control bg-light shadow-none border-0 py-2" v-model="formData.contact" placeholder="Tên đại diện...">
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold small text-muted">Số điện thoại</label>
                    <input type="text" class="form-control bg-light shadow-none border-0 py-2" v-model="formData.phone" placeholder="09xx...">
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small text-muted">Email</label>
                  <input type="email" class="form-control bg-light shadow-none border-0 py-2" v-model="formData.email" placeholder="email@example.com">
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small text-muted">Địa chỉ</label>
                  <textarea class="form-control bg-light shadow-none border-0 py-2" v-model="formData.address" rows="2" placeholder="Nhập địa chỉ chi tiết..."></textarea>
                </div>
                
                <div class="mb-3 form-check form-switch" v-if="isEdit">
                  <input class="form-check-input shadow-none cursor-pointer" type="checkbox" id="statusSwitch" v-model="formData.status">
                  <label class="form-check-label fw-semibold cursor-pointer ms-2" for="statusSwitch">Đang hợp tác</label>
                </div>

                <div class="d-flex justify-content-end gap-3 mt-4 pt-3 border-top">
                  <button type="button" class="btn btn-light fw-bold px-4" @click="closeModal">Hủy</button>
                  <button type="submit" class="btn text-white fw-bold px-4 shadow-sm" style="background-color: #00DF3A; border-color: #00DF3A;">
                    {{ isEdit ? 'Cập Nhật Đối Tác' : 'Lưu Nhà Cung Cấp' }}
                  </button>
                </div>
              </form>
            </div>

          </div>
        </div>
      </div>
    </transition>

  </div>
</template>

<script setup>
// ==========================================
// 1. IMPORT
// ==========================================
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// ==========================================
// 2. KHAI BÁO BIẾN TRẠNG THÁI (STATE)
// ==========================================
const searchQuery = ref("");
const suppliers = ref([]);
const showModal = ref(false);
const isEdit = ref(false);

const formData = ref({
  id: null,
  name: '',
  contact: '',
  phone: '',
  email: '',
  address: '',
  status: true
});

const apiUrl = '/api/admin/suppliers';

// Lấy Token xác thực cho mọi Request
const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

// ==========================================
// 3. TÍNH TOÁN & LỌC DỮ LIỆU
// ==========================================
const filteredSuppliers = computed(() => {
  if (!searchQuery.value) return suppliers.value;
  const lowerQuery = searchQuery.value.toLowerCase();
  return suppliers.value.filter(sup => 
    ((sup.name || sup.supplierName) && (sup.name || sup.supplierName).toLowerCase().includes(lowerQuery)) || 
    ((sup.id || sup.supplierId) && (sup.id || sup.supplierId).toString().includes(lowerQuery))
  );
});

// ==========================================
// 4. LOGIC MODAL & API
// ==========================================
const fetchSuppliers = async () => {
  try {
    const response = await axios.get(apiUrl, { headers: getAuthHeader() });
    suppliers.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách:", error);
  }
};

const openAddModal = () => {
  isEdit.value = false;
  formData.value = { id: null, name: '', contact: '', phone: '', email: '', address: '', status: true };
  showModal.value = true;
};

const openEditModal = (supplier) => {
  isEdit.value = true;
  // Ánh xạ lại tên biến cho khớp giữa dữ liệu trả về và form hiển thị
  formData.value = { 
    id: supplier.id || supplier.supplierId,
    name: supplier.name || supplier.supplierName,
    contact: supplier.contact || supplier.contactName,
    phone: supplier.phone || supplier.phoneNumber,
    email: supplier.email,
    address: supplier.address,
    status: supplier.status
  };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const saveSupplier = async () => {
  try {
    // Map data lại chuẩn với DTO/Entity của Backend Java
    const payload = {
      supplierName: formData.value.name,
      contactName: formData.value.contact,
      phoneNumber: formData.value.phone,
      email: formData.value.email,
      address: formData.value.address,
      status: formData.value.status
    };

    const headers = getAuthHeader();

    if (isEdit.value) {
      await axios.put(`${apiUrl}/${formData.value.id}`, payload, { headers });
      alert('Cập nhật nhà cung cấp thành công!');
    } else {
      await axios.post(apiUrl, payload, { headers });
      alert('Thêm nhà cung cấp thành công!');
    }
    
    closeModal();
    fetchSuppliers(); 
  } catch (error) {
    console.error("Lỗi khi lưu:", error);
    const errorMsg = error.response?.data?.message || error.response?.data || 'Có lỗi xảy ra, vui lòng kiểm tra lại!';
    alert(typeof errorMsg === 'string' ? errorMsg : "Lỗi kết nối máy chủ.");
  }
};

const deleteSupplier = async (id) => {
  if (confirm('Bạn có chắc chắn muốn ngừng hợp tác với nhà cung cấp này không?')) {
    try {
      await axios.delete(`${apiUrl}/${id}`, { headers: getAuthHeader() });
      alert('Đã cập nhật trạng thái ngừng hợp tác!');
      fetchSuppliers(); 
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      alert('Không thể thực hiện thao tác này vì nhà cung cấp đang có giao dịch phát sinh.');
    }
  }
};

onMounted(() => {
  fetchSuppliers();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap');

.cursor-pointer { cursor: pointer; }

/* Nút hành động ẩn hiện khi hover */
.action-buttons {
  opacity: 0;
  transition: opacity 0.2s ease-in-out;
}
.group-hover-show:hover .action-buttons {
  opacity: 1;
}

.hover-bg-primary:hover {
  background-color: #0d6efd !important;
  color: #fff !important;
}
.hover-bg-danger:hover {
  background-color: #dc3545 !important;
  color: #fff !important;
}

/* --- HIỆU ỨNG TRƯỢT MƯỢT CHO MODAL VUE --- */
.modal-custom-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4); /* Nền mờ */
  z-index: 1050;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.98);
}

.modal-fade-enter-to,
.modal-fade-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}
</style>