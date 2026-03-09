<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
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
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng số lượng SP</p><h2 class="fw-black text-dark m-0">{{ stats.total }}</h2></div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-box-seam fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Sắp hết hàng (< 15)</p><h2 class="fw-black text-dark m-0">{{ stats.lowStock }}</h2></div>
            <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-exclamation-triangle fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng tồn kho</p><h2 class="fw-black text-dark m-0">{{ stats.totalStockQuantity }}</h2></div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-boxes fs-4"></i></div>
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
                
                <td class="py-3">
                  <div v-if="product.salePrice && product.salePrice > 0 && product.salePrice < product.price">
                    <span class="fw-bold text-danger fs-7">{{ formatCurrency(product.salePrice) }}</span>
                    <div class="d-flex align-items-center gap-2 mt-1">
                      <span class="text-muted text-decoration-line-through fs-8">{{ formatCurrency(product.price) }}</span>
                      <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-1 py-0" style="font-size: 0.65rem;">
                        -{{ calculateDiscount(product.price, product.salePrice) }}%
                      </span>
                    </div>
                  </div>
                  <div v-else>
                    <span class="fw-bold text-dark fs-7">{{ formatCurrency(product.price) }}</span>
                  </div>
                </td>
                <td class="py-3"><span class="fs-7 fw-bold" :class="product.stockQuantity < 15 ? 'text-danger' : 'text-dark'">{{ product.stockQuantity }}</span></td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(product)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteProduct(product.id || product.productId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
              <tr v-if="paginatedProducts.length === 0">
                <td colspan="6" class="text-center py-4 text-muted fs-7">Không tìm thấy sản phẩm nào.</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top bg-white rounded-bottom-4">
          <span class="text-muted fs-8 fw-medium">Hiển thị {{ paginatedProducts.length }} / {{ filteredProducts.length }} sản phẩm</span>
          <nav v-if="totalPages > 1">
            <ul class="pagination pagination-sm mb-0 gap-1">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link text-dark shadow-none rounded-2" @click="currentPage--">Trước</button>
              </li>
              <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
                <button class="page-link shadow-none rounded-2" :class="currentPage === page ? 'bg-neon text-dark border-neon fw-bold' : 'text-dark'" @click="currentPage = page">{{ page }}</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link text-dark shadow-none rounded-2" @click="currentPage++">Sau</button>
              </li>
            </ul>
          </nav>
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
              <div class="col-12"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tên sản phẩm <span class="text-danger">*</span></label><input type="text" v-model="form.name" class="form-control fs-7"></div>
              
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Danh mục <span class="text-danger">*</span></label>
                <select v-model="form.categoryId" class="form-select fs-7 shadow-none">               
                  <option v-for="cat in categoryList" :key="cat.categoryId" :value="cat.categoryId">{{ cat.categoryName }}</option>
                </select>
              </div>

              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Thương hiệu <span class="text-danger">*</span></label>
                <select v-model="form.brandId" class="form-select fs-7 shadow-none">
                  <option v-for="b in brandList" :key="b.brandId" :value="b.brandId">{{ b.brandName }}</option>
                </select>
              </div>

              <div class="col-md-4"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Số lượng <span class="text-danger">*</span></label><input type="number" v-model="form.stockQuantity" class="form-control fs-7"></div>
              <div class="col-md-4"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giá bán <span class="text-danger">*</span></label><input type="number" v-model="form.price" class="form-control fs-7"></div>
              <div class="col-md-4"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giá KM</label><input type="number" v-model="form.salePrice" class="form-control fs-7" placeholder="Để trống..."></div>
              <div class="col-12"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Link hình ảnh</label><input type="text" v-model="form.imageUrl" class="form-control fs-7"></div>
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
import { ref, computed, onMounted, reactive, watch } from 'vue';
import axios from 'axios';

const productList = ref([]);
const categoryList = ref([]);
const brandList = ref([]);
const searchQuery = ref('');

const currentPage = ref(1);
const itemsPerPage = 15; 

const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);

const stats = reactive({ total: 0, lowStock: 0, totalStockQuantity: 0 }); // Đã đổi categories thành totalStockQuantity

const form = reactive({ 
  name: '', 
  categoryId: null,
  brandId: null,
  price: 0, 
  salePrice: null, 
  stockQuantity: 0, 
  imageUrl: '', 
  description: '' 
});

const calculateDiscount = (price, salePrice) => {
  if (!price || !salePrice || price <= 0 || salePrice >= price) return 0;
  
  const discount = ((price - salePrice) / price) * 100;
  let roundedDiscount = Math.round(discount);
  
  if (roundedDiscount >= 100 && salePrice > 0) {
    return 99;
  }
  
  return roundedDiscount;
};

const getAuthHeader = () => {
  const token = localStorage.getItem('jwt_token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const fetchProducts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/product?size=1000', { headers: getAuthHeader() });
    const data = response.data;
    productList.value = data.content || data.data || (Array.isArray(data) ? data : []);
  } catch (error) { console.error("Lỗi tải sản phẩm:", error); }
};

const fetchStats = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/product/stats', { headers: getAuthHeader() });
    stats.total = response.data.total;
    stats.lowStock = response.data.lowStock;
    stats.totalStockQuantity = response.data.totalStockQuantity; // Nhận giá trị mới từ API
  } catch (error) { console.error("Lỗi tải thống kê:", error); }
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

const filteredProducts = computed(() => {
  let res = [...productList.value];
  if (searchQuery.value) res = res.filter(p => p.name.toLowerCase().includes(searchQuery.value.toLowerCase()));
  return res;
});

const totalPages = computed(() => Math.ceil(filteredProducts.value.length / itemsPerPage));

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

watch(searchQuery, () => {
  currentPage.value = 1;
});

const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  Object.assign(form, { name: '', categoryId: null, brandId: null, price: 0, salePrice: null, stockQuantity: 0, imageUrl: '', description: '' });
  showModal.value = true;
};

const openEditModal = (p) => {
  isEditing.value = true;
  currentId.value = p.id || p.productId; 
  Object.assign(form, p); 
  form.categoryId = p.categoryId; 
  form.brandId = p.brandId; 
  form.salePrice = p.salePrice || null; 
  showModal.value = true;
};

const saveProduct = async () => {
  // 1. VALIDATE Ở FRONTEND
  if (!form.name || form.name.trim() === '') {
    alert("Vui lòng nhập tên sản phẩm!"); return;
  }
  if (!form.categoryId) {
    alert("Vui lòng chọn danh mục!"); return;
  }
  if (!form.brandId) {
    alert("Vui lòng chọn thương hiệu!"); return;
  }
  if (form.price === null || form.price === undefined || form.price < 0) {
    alert("Vui lòng nhập giá bán lớn hơn hoặc bằng 0!"); return;
  }
  if (form.stockQuantity === null || form.stockQuantity === undefined || form.stockQuantity < 0) {
    alert("Vui lòng nhập số lượng lớn hơn hoặc bằng 0!"); return;
  }

  try {
    const headers = getAuthHeader();
    const payload = { ...form };
    
    payload.salePrice = payload.salePrice ? Number(payload.salePrice) : null;

    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/product/${currentId.value}`, payload, { headers });
      alert("Cập nhật thành công!");
    } else {
      await axios.post('http://localhost:8080/api/product', payload, { headers });
      alert("Thêm mới thành công!");
    }
    showModal.value = false;
    fetchProducts();
    fetchStats(); 
    
  } catch (error) {
    // 2. BẮT LỖI TỪ BACKEND
    const errorMsg = error.response?.data?.message || error.response?.data || "Không thể thực hiện. Vui lòng thử lại.";
    if (typeof errorMsg === 'string') {
      alert(errorMsg);
    }
  }
};

const deleteProduct = async (id) => {
  if (!id) { alert("Không tìm thấy ID sản phẩm!"); return; }
  if (confirm("Xóa sản phẩm này?")) {
    try {
      await axios.delete(`http://localhost:8080/api/product/${id}`, { headers: getAuthHeader() });
      fetchProducts();
      fetchStats(); 
      alert("Xóa thành công!");
    } catch (error) { alert("Lỗi khi xóa!"); }
  }
};

const formatCurrency = (v) => new Intl.NumberFormat('vi-VN').format(v || 0) + '₫';

onMounted(() => { 
  fetchProducts(); 
  fetchCategories(); 
  fetchBrands(); 
  fetchStats(); 
});
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
.fs-9 { font-size: 0.65rem; }

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.border-neon { border-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; }
.btn-neon:hover { background-color: #00e62e; }

/* Bảng dữ liệu */
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }

/* Tránh giật UI khi hover dòng bảng */
.table-hover tbody tr:hover td {
  background-color: #f8f9fa;
}

/* Pagination */
.page-link { border: 1px solid #dee2e6; color: #333; }
.page-link:hover { background-color: #e9ecef; }
</style>