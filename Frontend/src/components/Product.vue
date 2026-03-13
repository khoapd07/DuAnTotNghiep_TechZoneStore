<template>
  <div class="product-catalog-page pb-5 pt-3">
    <div class="container narrow-container">

      <div class="mb-4">
        <nav aria-label="breadcrumb" class="mb-2">
          <ol class="breadcrumb fs-8 fw-medium mb-0">
            <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang
                chủ</router-link></li>
            <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Thiết thiết bị Gaming</a>
            </li>
            <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Tất cả sản phẩm</li>
          </ol>
        </nav>

        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3">
          <div>
            <h1 class="fw-black text-uppercase fs-3 mb-1">DANH MỤC SẢN PHẨM</h1>
            <p class="text-muted m-0 fs-8">Tìm thấy {{ totalElements }} sản phẩm từ hệ thống.</p>
          </div>
          <div class="d-flex align-items-center gap-2">
            <span class="text-muted fw-bold fs-8 text-nowrap">SẮP XẾP:</span>
            <select v-model="currentSort" @change="updateSort"
              class="form-select form-select-sm border-0 bg-light fw-bold custom-select-width shadow-none fs-8">
              <option value="createdAt-desc">Mới nhất</option>
              <option value="discount-desc">Giảm giá</option>
              <option value="price-asc">Giá tăng dần</option>
              <option value="price-desc">Giá giảm dần</option>
              <option value="name-asc">Tên A-Z</option>
            </select>
          </div>
        </div>
      </div>

      <div class="row g-4">
        <div class="col-lg-3">
          <div class="filter-box border rounded-3 p-3 mb-3">
            <h6 class="fw-bold fs-8 mb-3 text-uppercase">Tìm kiếm sản phẩm</h6>
            <input type="text" v-model="filters.keyword" @input="handleFilterChange"
              class="form-control form-control-sm fs-8 shadow-none" placeholder="Nhập tên sản phẩm...">
          </div>

          <div class="filter-box border rounded-3 p-3 mb-3">
            <h6 class="fw-bold fs-8 mb-3 text-uppercase">Khoảng giá</h6>
            <div class="d-flex flex-column gap-2 fs-8">
              <div class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" name="priceRange" :value="null"
                  v-model="selectedPriceRange" @change="setPriceRange(null)" id="priceAll">
                <label class="form-check-label text-muted" for="priceAll">Tất cả khoảng giá</label>
              </div>
              <div v-for="(range, index) in priceRanges" :key="index" class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" name="priceRange" :value="index"
                  v-model="selectedPriceRange" @change="setPriceRange(range)" :id="'price' + index">
                <label class="form-check-label text-muted" :for="'price' + index">{{ range.label }}</label>
              </div>
            </div>
          </div>

          <div class="filter-box border rounded-3 p-3 mb-3">
            <h6 class="fw-bold fs-8 mb-3 text-uppercase">DANH MỤC</h6>
            <div class="d-flex flex-column gap-2 fs-8">
              <div class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" v-model="filters.categoryId" :value="null"
                  @change="handleFilterChange" id="allCat">
                <label class="form-check-label text-muted" for="allCat">Tất cả danh mục</label>
              </div>
              <div v-for="cat in categories" :key="cat.categoryId" class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" v-model="filters.categoryId" :value="cat.categoryId"
                  @change="handleFilterChange" :id="'cat' + cat.categoryId">
                <label class="form-check-label text-muted" :for="'cat' + cat.categoryId">{{ cat.categoryName }}</label>
              </div>
            </div>
          </div>

          <div class="filter-box border rounded-3 p-3">
            <h6 class="fw-bold fs-8 mb-3 text-uppercase">THƯƠNG HIỆU</h6>
            <div class="d-flex flex-column gap-2 fs-8">
              <div class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" v-model="filters.brandId" :value="null"
                  @change="handleFilterChange" id="allBrand">
                <label class="form-check-label text-muted" for="allBrand">Tất cả thương hiệu</label>
              </div>
              <div v-for="b in brands" :key="b.brandId" class="form-check custom-checkbox">
                <input class="form-check-input" type="radio" v-model="filters.brandId" :value="b.brandId"
                  @change="handleFilterChange" :id="'brand' + b.brandId">
                <label class="form-check-label text-muted" :for="'brand' + b.brandId">{{ b.brandName }}</label>
              </div>
            </div>
          </div>
        </div>

        <div class="col-lg-9">
          <div class="row g-3" v-if="products.length > 0">
            <div class="col-md-6 col-lg-4" v-for="product in products" :key="product.productId">
              <div class="card h-100 border-0 product-card custom-card-hover rounded-3 overflow-hidden">
                <router-link :to="'/product/' + product.productId" class="text-decoration-none text-dark">
                  <div class="img-wrapper position-relative d-flex justify-content-center align-items-center p-3">
                    <span v-if="product.salePrice"
                      class="badge bg-danger text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">GIẢM
                      GIÁ</span>
                    <span v-if="product.stockQuantity <= 0"
                      class="badge bg-secondary text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">HẾT
                      HÀNG</span>
                    <i
                      class="bi bi-heart position-absolute top-0 end-0 m-2 fs-6 text-dark cursor-pointer heart-icon"></i>
                    <img :src="product.imageUrl || 'https://via.placeholder.com/150'"
                      class="img-fluid object-fit-contain" style="height: 140px;" :alt="product.name">
                  </div>
                </router-link>

                <div class="info-wrapper p-3 d-flex flex-column flex-grow-1">
                  <router-link :to="'/product/' + product.productId" class="text-decoration-none text-dark">
                    <h6 class="fw-bold mb-1 text-uppercase fs-8 line-clamp-1" :title="product.name">{{ product.name }}
                    </h6>
                  </router-link>
                  <small class="text-muted mb-2 d-block fs-9">{{ product.categoryName }} • {{ product.brandName
                    }}</small>

                  <div class="mt-auto mb-2">
                    <h6 class="fw-black m-0 d-inline-block fs-7 text-neon">{{ formatCurrency(product.salePrice ||
                      product.price) }}</h6>
                    <span v-if="product.salePrice" class="text-muted text-decoration-line-through ms-2 fs-9">{{
                      formatCurrency(product.price) }}</span>
                  </div>

                  <div class="d-flex gap-2">
                    <button @click="addToCart(product.productId)"
                      class="btn btn-outline-dark btn-cart-icon d-flex align-items-center justify-content-center rounded-2 p-1"
                      :disabled="product.stockQuantity <= 0">
                      <i class="bi bi-cart2 fs-6"></i>
                    </button>

                    <button @click="buyNow(product.productId)"
                      class="btn btn-neon fw-bold flex-grow-1 fs-8 p-1 rounded-2 text-dark"
                      :disabled="product.stockQuantity <= 0">
                      MUA NGAY
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="text-center py-5">
            <div class="spinner-border text-success mb-3" role="status" v-if="loading"></div>
            <p class="text-muted">{{ loading ? 'Đang tải sản phẩm...' : 'Không tìm thấy sản phẩm nào khớp với bộ lọc.'
              }}</p>
          </div>

          <div class="d-flex justify-content-center mt-4" v-if="totalPages > 1">
            <nav>
              <ul class="pagination pagination-sm gap-1 mb-0">
                <li class="page-item" :class="{ disabled: filters.page === 0 }">
                  <button class="page-link border-0 rounded-2" @click="changePage(filters.page - 1)"><i
                      class="bi bi-chevron-left"></i></button>
                </li>
                <li v-for="p in totalPages" :key="p" class="page-item">
                  <button class="page-link border-0 rounded-2 fs-8" :class="{ 'active-page': filters.page === p - 1 }"
                    @click="changePage(p - 1)">{{ p }}</button>
                </li>
                <li class="page-item" :class="{ disabled: filters.page === totalPages - 1 }">
                  <button class="page-link border-0 rounded-2" @click="changePage(filters.page + 1)"><i
                      class="bi bi-chevron-right"></i></button>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const products = ref([]);
const categories = ref([]);
const brands = ref([]);
const totalPages = ref(0);
const totalElements = ref(0);
const loading = ref(false);

const selectedPriceRange = ref(null);

const currentSort = ref('createdAt-desc');

const priceRanges = [
  { label: '0 - 10 triệu', min: 0, max: 10000000 },
  { label: '10 - 20 triệu', min: 10000000, max: 20000000 },
  { label: '20 - 30 triệu', min: 20000000, max: 30000000 },
  { label: '30 - 50 triệu', min: 30000000, max: 50000000 },
  { label: '50 - 100 triệu', min: 50000000, max: 100000000 },
  { label: 'Trên 100 triệu', min: 100000000, max: 999999999 },
];

const filters = reactive({
  keyword: '',
  categoryId: null,
  brandId: null,
  minPrice: null,
  maxPrice: null,
  isSale: null, // MỚI THÊM: Truyền trạng thái sale lên API
  page: 0,
  size: 15,
  sortBy: 'createdAt',
  sortDir: 'desc'
});

// CẬP NHẬT: Xử lý khi chọn "Giảm giá"
const updateSort = () => {
  if (currentSort.value === 'discount-desc') {
    filters.isSale = true; // Kích hoạt bộ lọc giảm giá
    filters.sortBy = 'createdAt'; // Mặc định xếp mới nhất cho giảm giá
    filters.sortDir = 'desc';
  } else {
    filters.isSale = null; // Tắt bộ lọc giảm giá
    const parts = currentSort.value.split('-');
    filters.sortBy = parts[0];
    filters.sortDir = parts[1];
  }
  handleFilterChange();
};

const formatCurrency = (value) => {
  if (!value) return "0 VNĐ";
  return value.toLocaleString('vi-VN') + ' VNĐ';
};

const fetchProducts = async () => {
  loading.value = true;
  try {
    const response = await axios.get('http://localhost:8080/api/product', { params: filters });
    products.value = response.data.content;
    totalPages.value = response.data.totalPages;
    totalElements.value = response.data.totalElements;
  } catch (error) {
    console.error("Lỗi tải sản phẩm:", error);
  } finally {
    loading.value = false;
  }
};

const setPriceRange = (range) => {
  if (range) {
    filters.minPrice = range.min;
    filters.maxPrice = range.max;
  } else {
    filters.minPrice = null;
    filters.maxPrice = null;
  }
  handleFilterChange();
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories');
    categories.value = response.data;
  } catch (error) { console.error("Lỗi tải danh mục:", error); }
};

const fetchBrands = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/brands');
    brands.value = response.data;
  } catch (error) { console.error("Lỗi tải thương hiệu:", error); }
};

const handleFilterChange = () => {
  filters.page = 0;
  fetchProducts();
};

const changePage = (p) => {
  filters.page = p;
  fetchProducts();
  window.scrollTo(0, 0);
};

onMounted(() => {
  fetchProducts();
  fetchCategories();
  fetchBrands();
});

const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      return JSON.parse(userInfoString).userId;
    } catch (e) { return null; }
  }
  return null;
};

const addToCart = async (productId) => {
  const userId = getCurrentUserId();
  const productToAdd = products.value.find(p => p.productId === productId);
  if (!productToAdd) return;

  if (userId) {
    try {
      const payload = {
        productId: productId,
        quantity: 1 
      };

      await axios.post(`http://localhost:8080/api/cart/${userId}/add`, payload);

      alert("Đã thêm sản phẩm vào giỏ hàng!");
      window.dispatchEvent(new Event('cart-updated')); 

    } catch (error) {
      alert(error.response?.data || "Không thể thêm vào giỏ hàng");
    }
  } else {
    let guestCart = JSON.parse(localStorage.getItem('guest_cart')) || [];
    const existingItemIndex = guestCart.findIndex(i => i.productId === productId);

    if (existingItemIndex !== -1) {
      guestCart[existingItemIndex].quantity += 1;
    } else {
      guestCart.push({
        productId: productToAdd.productId,
        name: productToAdd.name,
        price: productToAdd.salePrice || productToAdd.price,
        quantity: 1,
        img: productToAdd.imageUrl || 'https://via.placeholder.com/150'
      });
    }

    localStorage.setItem('guest_cart', JSON.stringify(guestCart));
    alert("Đã thêm sản phẩm vào giỏ hàng!");
    window.dispatchEvent(new Event('cart-updated'));
  }
};

const buyNow = async (productId) => {
  await addToCart(productId);
  router.push('/cart');
};
</script>

<style scoped>
.fw-black {
  font-weight: 900;
}

.fs-7 {
  font-size: 0.85rem;
}

.fs-8 {
  font-size: 0.75rem;
}

.fs-9 {
  font-size: 0.65rem;
}

.cursor-pointer {
  cursor: pointer;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.text-neon {
  color: #00FF33 !important;
}

.bg-neon {
  background-color: #00FF33 !important;
}

.narrow-container {
  max-width: 1000px !important;
  margin: 0 auto;
}

.custom-select-width {
  width: 120px;
}

.filter-box {
  background-color: #FFFFFF;
}

.product-card {
  background-color: transparent;
}

.img-wrapper {
  background-color: #FFFFFF;
  height: 180px;
}

.heart-icon {
  opacity: 0.5;
  transition: 0.2s;
}

.heart-icon:hover {
  opacity: 1;
  color: #00FF33 !important;
}

.info-wrapper {
  background-color: #F4F6F8;
}

.custom-card-hover {
  transition: 0.3s;
}

.custom-card-hover:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
}

.btn-neon {
  background-color: #00FF33;
  border: none;
}

.btn-cart-icon {
  width: 32px;
  height: 32px;
  border-color: #333;
}

.active-page {
  background-color: #00FF33 !important;
  color: #000 !important;
  font-weight: bold;
}

/* Thêm style cho checkbox/radio để giống mockup */
.custom-checkbox .form-check-input:checked {
  background-color: #00FF33;
  border-color: #00FF33;
}
</style>