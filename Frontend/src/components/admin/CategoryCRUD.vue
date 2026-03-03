<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <!-- Sidebar giống hệt ProductCRUD -->
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
              <i class="bi bi-box-seam-fill"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </router-link>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link active rounded-3 d-flex align-items-center gap-3">
              <i class="bi bi-tags-fill"></i> <span class="fw-bold fs-7">Danh mục</span>
            </a>
          </li>
          <li class="nav-item">
            <router-link to="/admin/brands" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-bookmark-fill"></i> <span class="fw-bold fs-7">Thương hiệu</span>
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
        </ul>
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

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Tên Danh Mục</th>
                <th class="py-3 fw-bold border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cat in categoryList" :key="cat.categoryId" class="border-bottom-dashed">
                <td class="py-3"><span class="fw-bold fs-7 text-dark">{{ cat.categoryName }}</span></td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(cat)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteCategory(cat.categoryId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <!-- Modal -->
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
    } catch (e) { alert("Lỗi khi xóa!"); }
  }
};

onMounted(() => fetchCategories());
</script>