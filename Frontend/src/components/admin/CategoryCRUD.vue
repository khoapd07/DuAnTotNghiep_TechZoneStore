<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    <aside class="sidebar bg-white border-end d-flex flex-column sticky-top" style="width: 220px; height: 100vh;">
      <div class="p-3 border-bottom">
        <h4 class="fw-black text-dark m-0 d-flex align-items-center gap-1 fs-5">
          TECHZONE <span class="text-neon fs-6">//</span>
        </h4>
        <small class="text-muted fw-bold" style="font-size: 0.65rem; letter-spacing: 1px;">QUẢN TRỊ</small>
      </div>

      <div class="p-2 flex-grow-1 overflow-auto">
        <ul class="nav flex-column gap-1 custom-nav">
          <li class="nav-item">
            <router-link to="/admin" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-grid-1x2"></i> <span class="fw-bold fs-7">Tổng quan</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/products" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-box-seam"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </router-link>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link active rounded-3 d-flex align-items-center gap-3">
              <i class="bi bi-tags-fill"></i> <span class="fw-bold fs-7">Danh mục</span>
            </a>
          </li>
          <li class="nav-item">
            <router-link to="/admin/brands" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-bookmark"></i> <span class="fw-bold fs-7">Thương hiệu</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/orders" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-receipt"></i> <span class="fw-bold fs-7">Đơn hàng</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/users" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-people"></i> <span class="fw-bold fs-7">Khách hàng</span>
            </router-link>
          </li>
          <li class="nav-item mt-2">
            <router-link to="/admin/report" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-bar-chart-line"></i> <span class="fw-bold fs-7">Báo cáo</span>
            </router-link>
          </li>
        </ul>
      </div>

      <div class="p-3 border-top">
        <a href="#" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark mb-3 px-2">
          <i class="bi bi-gear"></i> <span class="fw-bold fs-7">Cài đặt</span>
        </a>
        <div class="d-flex align-items-center gap-2 px-1">
          <div class="avatar bg-neon text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 35px; height: 35px; font-size: 0.85rem;">
            A
          </div>
          <div>
            <h6 class="m-0 fw-bold fs-7">Admin TechZone</h6>
            <small class="text-muted" style="font-size: 0.65rem;">admin@techzone.vn</small>
          </div>
        </div>
      </div>
    </aside>

    <main class="flex-grow-1 p-4 overflow-auto">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Danh mục</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Danh Mục</h3>
        </div>
        <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
          <i class="bi bi-plus-lg"></i> Thêm Danh Mục Mới
        </button>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng danh mục</p>
              <h2 class="fw-black text-dark m-0">{{ categoryList.length }}</h2>
            </div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-tags fs-4"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Tên Danh Mục</th>
                <th class="py-3 fw-bold border-0 text-center" style="width: 150px;">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cat in categoryList" :key="cat.categoryId" class="border-bottom-dashed">
                <td class="py-3 px-4">
                  <span class="fw-bold fs-7 text-dark">{{ cat.categoryName }}</span>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(cat)" class="btn btn-link p-0 text-primary shadow-none">
                      <i class="bi bi-pencil-square fs-6"></i>
                    </button>
                    <button @click="deleteCategory(cat.categoryId)" class="btn btn-link p-0 text-danger shadow-none">
                      <i class="bi bi-trash fs-6"></i>
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="categoryList.length === 0">
                <td colspan="2" class="text-center py-4 text-muted fs-7">Chưa có danh mục nào.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT DANH MỤC' : 'THÊM DANH MỤC MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="mb-3">
              <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên danh mục</label>
              <input type="text" v-model="form.categoryName" class="form-control fs-7" placeholder="Nhập tên danh mục">
            </div>
          </div>
          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" @click="saveCategory">{{ isEditing ? 'Cập Nhật' : 'Thêm Mới' }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import axios from 'axios';

const categoryList = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);
const form = reactive({ categoryName: '' });

const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const fetchCategories = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/categories', { headers: getAuthHeader() });
    categoryList.value = res.data;
  } catch (e) { console.error(e); }
};

const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  form.categoryName = '';
  showModal.value = true;
};

const openEditModal = (cat) => {
  isEditing.value = true;
  currentId.value = cat.categoryId;
  form.categoryName = cat.categoryName;
  showModal.value = true;
};

const saveCategory = async () => {
  try {
    const headers = getAuthHeader();
    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/categories/${currentId.value}`, form, { headers });
    } else {
      await axios.post('http://localhost:8080/api/categories', form, { headers });
    }
    showModal.value = false;
    fetchCategories();
    alert("Thành công!");
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || "Không thể thực hiện"));
  }
};

const deleteCategory = async (id) => {
  if (confirm("Xóa danh mục này?")) {
    try {
      await axios.delete(`http://localhost:8080/api/categories/${id}`, { headers: getAuthHeader() });
      fetchCategories();
      alert("Xóa thành công!");
    } catch (error) {
      // Bóc tách thông báo lỗi từ Backend gửi về
      const errorMsg = error.response?.data?.message;
      
      if (errorMsg) {
        alert(errorMsg); // Sẽ hiển thị: "Không thể xóa danh mục này vì đang chứa sản phẩm..."
      } else {
        alert("Lỗi khi xóa! Vui lòng thử lại sau."); // Câu dự phòng nếu lỗi mạng hoặc server chết hẳn
      }
    }
  }
};

onMounted(() => fetchCategories());
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

/* Sidebar Nav */
.custom-nav .nav-link {
  padding: 0.6rem 1rem;
  transition: all 0.2s ease;
}
.custom-nav .nav-link.active {
  background-color: rgba(0, 255, 51, 0.1);
  color: #00FF33 !important;
  border-left: 4px solid #00FF33;
  border-top-left-radius: 0 !important;
  border-bottom-left-radius: 0 !important;
}
.custom-nav .nav-link:hover:not(.active) {
  background-color: #f8f9fa;
}

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