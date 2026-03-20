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
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT SẢN PHẨM' : 'THÊM SẢN PHẨM MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4" style="max-height: 70vh; overflow-y: auto;">
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
              <div class="col-md-4"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giá bán Gốc <span class="text-danger">*</span></label><input type="number" v-model="form.price" class="form-control fs-7"></div>
              <div class="col-md-4"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Giá KM Gốc</label><input type="number" v-model="form.salePrice" class="form-control fs-7" placeholder="Để trống..."></div>
              
              <div class="col-12 mt-3">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Link hình ảnh chính</label>
                <input type="text" v-model="form.imageUrl" class="form-control fs-7" placeholder="Nhập link hình ảnh bìa">
              </div>

              <div class="col-12"><label class="fs-8 fw-bold text-muted text-uppercase mb-1">Mô tả ngắn</label><textarea v-model="form.description" class="form-control fs-7" rows="2"></textarea></div>

              <div class="col-12 mt-4">
                <div class="border rounded bg-light p-3">
                  <h6 class="fw-bold text-dark fs-8 text-uppercase mb-3">Tùy chọn mặc định của Ảnh chính</h6>
                  <div class="row g-2">
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Tên màu (Của ảnh chính)</label>
                      <input type="text" v-model="form.mainColorName" class="form-control form-control-sm fs-8" placeholder="VD: Đen, Trắng...">
                    </div>
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Tên nhóm phân loại 2</label>
                      <input type="text" v-model="form.capacityLabel" class="form-control form-control-sm fs-8 text-primary fw-bold" placeholder="VD: Dung lượng, Size, Loại...">
                    </div>
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Giá trị mặc định phân loại 2</label>
                      <input type="text" v-model="form.mainCapacityName" class="form-control form-control-sm fs-8" placeholder="VD: 256GB, Size M...">
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="col-12 mt-4">
                <div class="d-flex justify-content-between align-items-center mb-2 border-top pt-3">
                  <div>
                    <label class="fs-8 fw-bold text-dark text-uppercase mb-0">Các màu sắc khác (Đổi giá nếu cần)</label>
                  </div>
                  <button type="button" @click="addVariant" class="btn btn-sm btn-dark fs-8 fw-bold"><i class="bi bi-plus"></i> Thêm màu phụ</button>
                </div>
                
                <div v-if="form.variants.length === 0" class="text-center text-muted fs-8 py-3 border rounded bg-light">
                  Chưa có màu sắc phụ nào. Bấm "Thêm màu phụ" để tạo mới.
                </div>
                
                <div v-for="(v, index) in form.variants" :key="'var-'+index" class="d-flex gap-2 mb-2 align-items-start p-2 border rounded bg-light">
                  <div class="flex-grow-1 row g-2">
                    <div class="col-md-6">
                      <input type="text" v-model="v.colorName" class="form-control form-control-sm fs-8 fw-bold" placeholder="Tên màu (VD: Đen)">
                    </div>
                    <div class="col-md-6">
                      <input type="text" v-model="v.imageUrl" class="form-control form-control-sm fs-8" placeholder="Link hình ảnh màu này">
                    </div>
                    <div class="col-md-6">
                      <input type="number" v-model="v.price" class="form-control form-control-sm fs-8" placeholder="Giá bán riêng (0 = Giá gốc)">
                    </div>
                    <div class="col-md-6">
                      <input type="number" v-model="v.salePrice" class="form-control form-control-sm fs-8" placeholder="Giá KM riêng">
                    </div>
                  </div>
                  <button type="button" @click="removeVariant(index)" class="btn btn-sm btn-outline-danger border-0 shadow-none h-100">
                    <i class="bi bi-x-lg"></i>
                  </button>
                </div>
              </div>

              <div class="col-12 mt-4">
                <div class="d-flex justify-content-between align-items-center mb-2 border-top pt-3">
                  <div>
                    <label class="fs-8 fw-bold text-dark text-uppercase mb-0">Phân loại 2 khác (Thay đổi giá)</label>
                    <div class="fs-9 text-muted mt-1">Lưu ý: Hệ thống ưu tiên lấy giá của phân loại này thay vì giá gốc.</div>
                  </div>
                  <button type="button" @click="addCapacity" class="btn btn-sm btn-dark fs-8 fw-bold"><i class="bi bi-plus"></i> Thêm phân loại 2</button>
                </div>
                
                <div v-if="form.capacities.length === 0" class="text-center text-muted fs-8 py-3 border rounded bg-light">
                  Sản phẩm này không chia giá theo phân loại 2.
                </div>
                
                <div v-for="(cap, index) in form.capacities" :key="'cap-'+index" class="d-flex gap-2 mb-2 align-items-start p-2 border rounded bg-light">
                  <div class="flex-grow-1 row g-2">
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Tên phân loại</label>
                      <input type="text" v-model="cap.capacityName" class="form-control form-control-sm fs-8 fw-bold" placeholder="VD: 512GB, Size L...">
                    </div>
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Giá bán riêng</label>
                      <input type="number" v-model="cap.price" class="form-control form-control-sm fs-8" placeholder="Nhập giá bán">
                    </div>
                    <div class="col-md-4">
                      <label class="fs-9 fw-bold text-muted mb-1">Giá KM riêng</label>
                      <input type="number" v-model="cap.salePrice" class="form-control form-control-sm fs-8" placeholder="Để trống nếu không giảm">
                    </div>
                  </div>
                  <button type="button" @click="removeCapacity(index)" class="btn btn-sm btn-outline-danger border-0 shadow-none mt-4">
                    <i class="bi bi-x-lg"></i>
                  </button>
                </div>
              </div>

              <div class="col-12 mt-4">
                <div class="d-flex justify-content-between align-items-center mb-2 border-top pt-3">
                  <div>
                    <label class="fs-8 fw-bold text-dark text-uppercase mb-0">Các tùy chọn hiển thị (Thông tin thêm)</label>
                    <div class="fs-9 text-muted mt-1">Ví dụ: Màn hình - 120Hz, Switch - Linear Red...</div>
                  </div>
                  <button type="button" @click="addAttribute" class="btn btn-sm btn-dark fs-8 fw-bold"><i class="bi bi-plus"></i> Thêm tùy chọn</button>
                </div>
                
                <div v-if="form.attributes.length === 0" class="text-center text-muted fs-8 py-3 border rounded bg-light">
                  Sản phẩm này chưa có tùy chọn thông tin thêm.
                </div>
                
                <div v-for="(attr, index) in form.attributes" :key="'attr-'+index" class="d-flex gap-2 mb-2 align-items-start p-2 border rounded bg-light">
                  <div class="flex-grow-1 row g-2">
                    <div class="col-md-5">
                      <input type="text" v-model="attr.title" class="form-control form-control-sm fs-8 fw-bold" placeholder="Tên (VD: Switch)" maxlength="15">
                    </div>
                    <div class="col-md-7">
                      <input type="text" v-model="attr.desc" class="form-control form-control-sm fs-8" placeholder="Mô tả (VD: Linear Red)" maxlength="30">
                    </div>
                  </div>
                  <button type="button" @click="removeAttribute(index)" class="btn btn-sm btn-outline-danger border-0 shadow-none">
                    <i class="bi bi-x-lg"></i>
                  </button>
                </div>
              </div>

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

const stats = reactive({ total: 0, lowStock: 0, totalStockQuantity: 0 });

const form = reactive({ 
  name: '', 
  categoryId: null,
  brandId: null,
  price: 0, 
  salePrice: null, 
  stockQuantity: 0, 
  imageUrl: '', 
  mainColorName: '',
  capacityLabel: 'Dung lượng (ROM)', // THÊM TÊN NHÓM PHÂN LOẠI
  mainCapacityName: '', 
  description: '',
  variants: [],
  capacities: [],
  attributes: []
});

const calculateDiscount = (price, salePrice) => {
  if (!price || !salePrice || price <= 0 || salePrice >= price) return 0;
  const discount = ((price - salePrice) / price) * 100;
  let roundedDiscount = Math.round(discount);
  if (roundedDiscount >= 100 && salePrice > 0) return 99;
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
    stats.totalStockQuantity = response.data.totalStockQuantity; 
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

const addVariant = () => {
  form.variants.push({ colorName: '', imageUrl: '', price: 0, salePrice: '' });
};

const removeVariant = (index) => {
  form.variants.splice(index, 1);
};

const addCapacity = () => {
  form.capacities.push({ capacityName: '', price: 0, salePrice: '' });
};

const removeCapacity = (index) => {
  form.capacities.splice(index, 1);
};

const addAttribute = () => {
  form.attributes.push({ title: '', desc: '' });
};

const removeAttribute = (index) => {
  form.attributes.splice(index, 1);
};

const openAddModal = () => {
  isEditing.value = false;
  currentId.value = null;
  Object.assign(form, { 
    name: '', categoryId: null, brandId: null, price: 0, salePrice: null, 
    stockQuantity: 0, imageUrl: '', mainColorName: '', capacityLabel: 'Dung lượng (ROM)', mainCapacityName: '', description: '', variants: [], capacities: [], attributes: [] 
  });
  showModal.value = true;
};

const openEditModal = (p) => {
  isEditing.value = true;
  currentId.value = p.id || p.productId; 
  Object.assign(form, p); 
  form.categoryId = p.categoryId; 
  form.brandId = p.brandId; 
  form.salePrice = p.salePrice || null; 
  
  form.mainColorName = '';
  let otherVariants = [];

  if (p.variants && p.variants.length > 0) {
    const mainVariantIndex = p.variants.findIndex(v => v.imageUrl === p.imageUrl);
    if (mainVariantIndex !== -1) {
      form.mainColorName = p.variants[mainVariantIndex].colorName;
      otherVariants = p.variants.filter((v, idx) => idx !== mainVariantIndex);
    } else {
      otherVariants = [...p.variants];
    }
  }
  form.variants = JSON.parse(JSON.stringify(otherVariants));

  form.mainCapacityName = '';
  form.capacityLabel = 'Dung lượng (ROM)';
  let otherCapacities = [];
  if (p.capacities) {
      try {
          let parsedCaps = JSON.parse(p.capacities);
          if (Array.isArray(parsedCaps)) {
              // Tương thích ngược với dữ liệu cũ
              if (parsedCaps.length > 0) {
                  form.mainCapacityName = parsedCaps[0].capacityName;
                  otherCapacities = parsedCaps.slice(1);
              }
          } else {
              // Định dạng JSON mới chứa label
              form.capacityLabel = parsedCaps.label || 'Dung lượng (ROM)';
              let vals = parsedCaps.values || [];
              if (vals.length > 0) {
                  form.mainCapacityName = vals[0].capacityName;
                  otherCapacities = vals.slice(1);
              }
          }
      } catch(e) {}
  }
  form.capacities = otherCapacities;
  form.attributes = p.attributes ? JSON.parse(p.attributes) : [];

  showModal.value = true;
};

const saveProduct = async () => {
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

  let finalVariants = form.variants.filter(v => v.colorName && v.colorName.trim() !== '').map(v => ({
      ...v,
      price: Number(v.price) || 0,
      salePrice: v.salePrice && Number(v.salePrice) > 0 ? Number(v.salePrice) : null
  }));

  if (form.mainColorName && form.mainColorName.trim() !== '' && form.imageUrl) {
    finalVariants.unshift({
      colorName: form.mainColorName.trim(),
      imageUrl: form.imageUrl,
      price: Number(form.price) || 0,
      salePrice: form.salePrice ? Number(form.salePrice) : null
    });
  }

  let validCapacities = form.capacities.filter(c => c.capacityName && c.capacityName.trim() !== '').map(c => ({
      capacityName: c.capacityName.trim(),
      price: Number(c.price) || 0,
      salePrice: c.salePrice && Number(c.salePrice) > 0 ? Number(c.salePrice) : null
  }));

  if (form.mainCapacityName && form.mainCapacityName.trim() !== '') {
    validCapacities.unshift({
        capacityName: form.mainCapacityName.trim(),
        price: Number(form.price) || 0,
        salePrice: form.salePrice ? Number(form.salePrice) : null
    });
  }

  let validAttributes = form.attributes.filter(a => a.title && a.title.trim() !== '');

  try {
    const headers = getAuthHeader();
    const payload = { ...form, variants: finalVariants };
    payload.salePrice = payload.salePrice ? Number(payload.salePrice) : null;
    payload.attributes = validAttributes.length > 0 ? JSON.stringify(validAttributes) : null;

    // LƯU Ý: Chuyển mảng thành dạng object có chứa nhãn label để Backend lưu thành 1 cục String
    if (validCapacities.length > 0) {
         payload.capacities = JSON.stringify({
             label: form.capacityLabel && form.capacityLabel.trim() !== '' ? form.capacityLabel.trim() : 'Phân loại',
             values: validCapacities
         });
    } else {
         payload.capacities = null;
    }

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

.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.border-neon { border-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; }
.btn-neon:hover { background-color: #00e62e; }

.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }

.table-hover tbody tr:hover td {
  background-color: #f8f9fa;
}

.page-link { border: 1px solid #dee2e6; color: #333; }
.page-link:hover { background-color: #e9ecef; }
</style>