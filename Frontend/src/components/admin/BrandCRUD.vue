<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Thương hiệu</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Thương Hiệu</h3>
        </div>
        <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
          <i class="bi bi-plus-lg"></i> Thêm Thương Hiệu Mới
        </button>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng thương hiệu</p>
              <h2 class="fw-black text-dark m-0">{{ brandList.length }}</h2>
            </div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-shop fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng sản phẩm</p>
              <h2 class="fw-black text-dark m-0">{{ totalProducts }}</h2>
            </div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-box-seam fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng tồn kho</p>
              <h2 class="fw-black text-dark m-0">{{ totalStock }}</h2>
            </div>
            <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-box2-fill fs-4"></i></div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Tên Thương Hiệu</th>
                <th class="py-3 fw-bold border-0 text-center">Số Lượng SP</th>
                <th class="py-3 fw-bold border-0 text-center">Tồn Kho</th>
                <th class="py-3 fw-bold border-0 text-center" style="width: 150px;">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="brand in brandList" :key="brand.brandId" class="border-bottom-dashed">
                <td class="py-3 px-4"><span class="fw-bold fs-7 text-dark">{{ brand.brandName }}</span></td>
                <td class="text-center py-3"><span class="badge bg-light text-dark border fs-8 px-2 py-1">{{ getProductCount(brand) }}</span></td>
                <td class="text-center py-3">
                  <span class="badge fs-8 px-2 py-1" :class="getStockCount(brand) > 10 ? 'bg-success-subtle text-success border border-success-subtle' : (getStockCount(brand) > 0 ? 'bg-warning-subtle text-warning border border-warning-subtle' : 'bg-danger-subtle text-danger border border-danger-subtle')">
                    {{ getStockCount(brand) }}
                  </span>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(brand)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteBrand(brand.brandId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
              <tr v-if="brandList.length === 0">
                <td colspan="4" class="text-center py-4 text-muted fs-7">Chưa có thương hiệu nào.</td>
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

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT THƯƠNG HIỆU' : 'THÊM THƯƠNG HIỆU MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="mb-3">
              <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên thương hiệu <span class="text-danger">*</span></label>
              <input type="text" v-model="form.brandName" class="form-control fs-7" :class="{'is-invalid': errors.brandName}" @input="errors.brandName = ''" placeholder="Nhập tên thương hiệu">
              <span v-if="errors.brandName" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.brandName }}</span>
            </div>
          </div>
          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" @click="saveBrand">{{ isEditing ? 'Cập Nhật' : 'Thêm Mới' }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
// Sử dụng Axios Instance đã cấu hình dùng chung
import api from '../../utils/axios';

const brandList = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);
const form = reactive({ brandName: '' });
const errors = reactive({}); // Dùng để bắt lỗi đỏ dưới ô input

// Toast Notification
const toast = reactive({ show: false, message: '', type: 'success' });
const showToast = (message, type = 'success') => {
  toast.message = message; toast.type = type; toast.show = true;
  setTimeout(() => { toast.show = false; }, 3000);
};

const getProductCount = (brand) => brand.productCount !== undefined ? brand.productCount : (brand.products?.length || 0);
const getStockCount = (brand) => brand.totalStock !== undefined ? brand.totalStock : (brand.products?.reduce((sum, p) => sum + (p.stockQuantity || 0), 0) || 0);
const totalProducts = computed(() => brandList.value.reduce((total, brand) => total + getProductCount(brand), 0));
const totalStock = computed(() => brandList.value.reduce((total, brand) => total + getStockCount(brand), 0));

// Hàm getAuthHeader() đã bị loại bỏ vì token được tự động đính kèm qua utils/axios

const fetchBrands = async () => {
  try {
    // Đã thay thế axios bằng api và đổi sang đường dẫn tương đối
    const res = await api.get('/brands');
    brandList.value = res.data;
  } catch (e) { console.error(e); }
};

const openAddModal = () => {
  isEditing.value = false; currentId.value = null; form.brandName = ''; 
  Object.keys(errors).forEach(k => delete errors[k]); // Xóa lỗi cũ
  showModal.value = true;
};

const openEditModal = (brand) => {
  isEditing.value = true; currentId.value = brand.brandId; form.brandName = brand.brandName;
  Object.keys(errors).forEach(k => delete errors[k]);
  showModal.value = true;
};

const saveBrand = async () => {
  Object.keys(errors).forEach(k => delete errors[k]);
  
  if (!form.brandName || form.brandName.trim() === '') {
    errors.brandName = "Vui lòng nhập tên thương hiệu!"; return;
  }

  try {
    if (isEditing.value) {
      // Sử dụng api instance, không cần header thủ công
      await api.put(`/brands/${currentId.value}`, form);
      showToast("Cập nhật thương hiệu thành công!");
    } else {
      // Sử dụng api instance, không cần header thủ công
      await api.post('/brands', form);
      showToast("Thêm thương hiệu mới thành công!");
    }
    showModal.value = false;
    fetchBrands();
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.response?.data || "Lỗi hệ thống!";
    if (typeof errorMsg === 'string') {
      errors.brandName = errorMsg; // Đẩy lỗi từ backend xuống dưới ô input
    } else {
      showToast("Không thể thực hiện! Vui lòng thử lại.", "error");
    }
  }
};

const deleteBrand = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa thương hiệu này không?")) {
    try {
      // Sử dụng api instance, không cần header thủ công
      await api.delete(`/brands/${id}`);
      fetchBrands();
      showToast("Xóa thành công!");
    } catch (error) {
      const errorMsg = error.response?.data?.message || error.response?.data;
      if (typeof errorMsg === 'string') {
        showToast(errorMsg, "error"); 
      } else {
        showToast("Không thể xóa thương hiệu này vì đang chứa sản phẩm!", "error"); 
      }
    }
  }
};

onMounted(() => fetchBrands());
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');
.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; }
.btn-neon:hover { background-color: #00e62e; }
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.table-hover tbody tr:hover td { background-color: #f8f9fa; }
</style>