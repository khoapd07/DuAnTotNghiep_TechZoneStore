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

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Tên Thương Hiệu</th>
                <th class="py-3 fw-bold border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="brand in brandList" :key="brand.brandId" class="border-bottom-dashed">
                <td class="py-3"><span class="fw-bold fs-7 text-dark">{{ brand.brandName }}</span></td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(brand)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteBrand(brand.brandId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
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
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT THƯƠNG HIỆU' : 'THÊM THƯƠNG HIỆU MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="mb-3">
              <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên thương hiệu</label>
              <input type="text" v-model="form.brandName" class="form-control fs-7" placeholder="Nhập tên thương hiệu">
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
import { ref, onMounted, reactive } from 'vue';
import axios from 'axios';

const brandList = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);
const form = reactive({ brandName: '' });

const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const fetchBrands = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/brands', { headers: getAuthHeader() });
    brandList.value = res.data;
  } catch (e) { console.error(e); }
};

const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  form.brandName = '';
  showModal.value = true;
};

const openEditModal = (brand) => {
  isEditing.value = true;
  currentId.value = brand.brandId;
  form.brandName = brand.brandName;
  showModal.value = true;
};

const saveBrand = async () => {
  try {
    const headers = getAuthHeader();
    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/brands/${currentId.value}`, form, { headers });
    } else {
      await axios.post('http://localhost:8080/api/brands', form, { headers });
    }
    showModal.value = false;
    fetchBrands();
    alert("Thành công!");
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || "Không thể thực hiện"));
  }
};

const deleteBrand = async (id) => {
  if (confirm("Xóa thương hiệu này?")) {
    try {
      await axios.delete(`http://localhost:8080/api/brands/${id}`, { headers: getAuthHeader() });
      fetchBrands();
      alert("Xóa thành công!");
    } catch (e) { alert("Lỗi khi xóa!"); }
  }
};

onMounted(() => fetchBrands());
</script>