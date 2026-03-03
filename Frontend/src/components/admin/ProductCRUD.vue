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
            <a href="#" class="nav-link active rounded-3 d-flex align-items-center gap-3">
              <i class="bi bi-box-seam-fill"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </a>
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
              <li class="breadcrumb-item active text-dark" aria-current="page">Sản phẩm</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Sản Phẩm</h3>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="input-group bg-white border rounded-3 overflow-hidden shadow-sm" style="width: 250px;">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input type="text" v-model="searchQuery" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm sản phẩm...">
          </div>
          <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-plus-lg"></i> Thêm Sản Phẩm Mới
          </button>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng sản phẩm</p><h2 class="fw-black text-dark m-0">{{ stats.total }}</h2></div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-box-seam fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Sắp hết hàng</p><h2 class="fw-black text-dark m-0">{{ stats.lowStock }}</h2></div>
            <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-exclamation-triangle fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Danh mục</p><h2 class="fw-black text-dark m-0">{{ stats.categories }}</h2></div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-tags fs-4"></i></div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Hình Ảnh</th>
                <th class="py-3 fw-bold border-0">Tên Sản Phẩm</th>
                <th class="py-3 fw-bold border-0">Danh Mục</th>
                <th class="py-3 fw-bold border-0">Giá Bán</th>
                <th class="py-3 fw-bold border-0">Tồn Kho</th>
                <th class="py-3 fw-bold border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in paginatedProducts" :key="product.id" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <div class="bg-light border rounded-3 d-flex align-items-center justify-content-center overflow-hidden" style="width: 60px; height: 60px;">
                    <img v-if="product.imageUrl" :src="product.imageUrl" class="img-fluid object-fit-contain p-1">
                    <i v-else class="bi bi-image text-muted fs-4"></i>
                  </div>
                </td>
                <td class="py-3"><span class="fw-bold fs-7 text-dark">{{ product.name }}</span></td>
                <td class="py-3"><span class="badge bg-light text-dark border rounded-pill px-3 py-1 fs-8 fw-bold">{{ product.categoryName || 'Chưa rõ' }}</span></td>
                <td class="fw-bold text-dark fs-7 py-3">{{ formatCurrency(product.price) }}</td>
                <td class="py-3"><span class="fs-7 fw-bold" :class="product.stockQuantity <= 15 ? 'text-danger' : 'text-dark'">{{ product.stockQuantity }}</span></td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(product)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteProduct(product.id || product.productId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
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
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT SẢN PHẨM' : 'THÊM SẢN PHẨM MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="row g-3">
              <div class="col-12"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên sản phẩm</label><input type="text" v-model="form.name" class="form-control fs-7"></div>
              
              <!-- DANH MỤC (bind bằng ID) -->
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Danh mục</label>
                <select v-model="form.categoryId" class="form-select fs-7 shadow-none">
                  <option value="">Chọn danh mục</option>
                  <option v-for="cat in categoryList" :key="cat.categoryId" :value="cat.categoryId">{{ cat.categoryName }}</option>
                </select>
              </div>

              <!-- THƯƠNG HIỆU (bind bằng ID) -->
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Thương hiệu</label>
                <select v-model="form.brandId" class="form-select fs-7 shadow-none">
                  <option value="">Chọn thương hiệu</option>
                  <option v-for="b in brandList" :key="b.brandId" :value="b.brandId">{{ b.brandName }}</option>
                </select>
              </div>

              <div class="col-md-6"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Số lượng</label><input type="number" v-model="form.stockQuantity" class="form-control fs-7"></div>
              <div class="col-md-6"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giá bán</label><input type="number" v-model="form.price" class="form-control fs-7"></div>
              <div class="col-md-6"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Link hình ảnh</label><input type="text" v-model="form.imageUrl" class="form-control fs-7"></div>
              <div class="col-12"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Mô tả ngắn</label><textarea v-model="form.description" class="form-control fs-7" rows="2"></textarea></div>
            </div>
          </div>
          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" @click="saveProduct">{{ isEditing ? 'Cập Nhật' : 'Thêm Mới' }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue';
import axios from 'axios';

const productList = ref([]);
const categoryList = ref([]);
const brandList = ref([]);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);

const form = reactive({ 
  name: '', 
  categoryId: null,      // ← SỬA: bind bằng ID
  brandId: null,         // ← SỬA: bind bằng ID
  price: 0, 
  stockQuantity: 0, 
  imageUrl: '', 
  description: '' 
});

const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const fetchProducts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/product', { headers: getAuthHeader() });
    const data = response.data;
    productList.value = data.content || data.data || (Array.isArray(data) ? data : []);
  } catch (error) { console.error("Lỗi tải sản phẩm:", error); }
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories', { headers: getAuthHeader() });
    categoryList.value = response.data;
  } catch (error) { console.error("Lỗi tải danh mục:", error); categoryList.value = []; }
};

const fetchBrands = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/brands', { headers: getAuthHeader() });
    brandList.value = response.data;
  } catch (error) { console.error("Lỗi tải thương hiệu:", error); brandList.value = []; }
};

const stats = computed(() => ({
  total: productList.value.length,
  lowStock: productList.value.filter(p => p.stockQuantity <= 15).length,
  categories: [...new Set(productList.value.map(p => p.categoryName))].filter(Boolean).length
}));

const filteredProducts = computed(() => {
  let res = [...productList.value];
  if (searchQuery.value) res = res.filter(p => p.name.toLowerCase().includes(searchQuery.value.toLowerCase()));
  return res;
});

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  Object.assign(form, { name: '', categoryId: null, brandId: null, price: 0, stockQuantity: 0, imageUrl: '', description: '' });
  showModal.value = true;
};

const openEditModal = (p) => {
  isEditing.value = true;
  currentId.value = p.id || p.productId; 
  Object.assign(form, p);                    // copy hết dữ liệu cũ
  form.categoryId = p.categoryId;            // ← ép bind ID đúng
  form.brandId = p.brandId;                  // ← ép bind ID đúng
  showModal.value = true;
};

const saveProduct = async () => {
  try {
    const headers = getAuthHeader();
    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/product/${currentId.value}`, form, { headers });
    } else {
      await axios.post('http://localhost:8080/api/product', form, { headers });
    }
    showModal.value = false;
    fetchProducts();
    alert("Thành công!");
  } catch (error) {
    alert("Lỗi: " + (error.response?.data?.message || "Không thể thực hiện"));
  }
};

const deleteProduct = async (id) => {
  console.log("Đang thực hiện xóa ID:", id);
  if (!id) { alert("Không tìm thấy ID sản phẩm!"); return; }
  if (confirm("Xóa sản phẩm này?")) {
    try {
      await axios.delete(`http://localhost:8080/api/product/${id}`, { headers: getAuthHeader() });
      fetchProducts();
      alert("Xóa thành công!");
    } catch (error) { alert("Lỗi khi xóa!"); }
  }
};

const formatCurrency = (v) => new Intl.NumberFormat('vi-VN').format(v || 0) + '₫';

onMounted(() => { 
  fetchProducts(); 
  fetchCategories(); 
  fetchBrands(); 
});
</script>