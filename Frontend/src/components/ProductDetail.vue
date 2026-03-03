<template>
  <div class="product-detail-page bg-white">
    <div class="container narrow-container py-4" v-if="product">
      
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">{{ product.categoryName }}</a></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">{{ product.name }}</li>
        </ol>
      </nav>

      <div class="row g-5 mb-5">
        <div class="col-lg-7">
          <div class="main-image-box border rounded-3 position-relative d-flex justify-content-center align-items-center mb-3 bg-white">
            <span v-if="product.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-3 z-1 fw-bold fs-8 px-3 py-1">GIẢM GIÁ</span>
            <img :src="getImageUrl(product.imageUrl)" class="img-fluid object-fit-contain p-4" style="max-height: 450px;" :alt="product.name" @error="handleImageError">
          </div>
          <div class="d-flex gap-2 thumbnail-list">
            <div class="thumb-item active border border-dark rounded-2 cursor-pointer"><img :src="getImageUrl(product.imageUrl)" class="img-fluid p-1" @error="handleImageError"></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted cursor-pointer"><i class="bi bi-image"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted cursor-pointer"><i class="bi bi-box-seam"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted cursor-pointer"><i class="bi bi-play-circle"></i></div>
          </div>
        </div>

        <div class="col-lg-5">
          <h2 class="fw-black mb-1 text-uppercase text-dark">{{ product.name }}</h2>
          
          <div class="d-flex align-items-center gap-2 mb-3 fs-8">
            <div class="text-dark d-flex gap-1 fw-bold">
              <span v-for="n in 5" :key="n">{{ n <= Math.round(averageRating) ? '★' : '☆' }}</span>
            </div>
            <span class="text-muted">{{ reviews.length }} đánh giá</span>
            <span class="text-muted">•</span>
            <span class="text-dark fw-bold">● {{ product.stockQuantity > 0 ? 'CÒN HÀNG' : 'HẾT HÀNG' }}</span>
          </div>
          
          <div class="d-flex align-items-end gap-3 mb-4">
            <h2 class="text-dark fw-black mb-0 fs-2">{{ formatCurrency(product.salePrice || product.price) }}</h2>
            <span v-if="product.salePrice" class="text-muted text-decoration-line-through fs-6 mb-1">{{ formatCurrency(product.price) }}</span>
            <span v-if="product.salePrice" class="badge bg-light text-dark border fs-9">-15%</span>
          </div>
          <p class="fs-8 text-muted mb-4 line-height-lg">{{ product.description }}</p>

          <div class="switch-section mb-4">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <span class="fw-bold fs-8 text-dark">LOẠI </span>
              <a href="#" class="text-muted fs-8 text-decoration-none">HƯỚNG DẪN CHỌN</a>
            </div>
            <div class="d-flex gap-2">
              <div class="border border-dark rounded-2 p-2 text-center flex-grow-1 cursor-pointer">
                <div class="fs-8 fw-bold text-dark">Linear Red</div>
                <div class="fs-9 text-muted">Nhẹ nhàng, êm ái</div>
              </div>
              <div class="border rounded-2 p-2 text-center flex-grow-1 cursor-pointer text-muted hover-border-dark">
                <div class="fs-8 fw-bold">Clicky Blue</div>
                <div class="fs-9">Cảm giác gõ rõ</div>
              </div>
              <div class="border rounded-2 p-2 text-center flex-grow-1 cursor-pointer text-muted hover-border-dark">
                <div class="fs-8 fw-bold">Tactile Brown</div>
                <div class="fs-9">Cân bằng tốt</div>
              </div>
            </div>
          </div>

          <div class="d-flex flex-column gap-3 mb-4">
            <div class="d-flex gap-3">
              <div class="border rounded-2 d-flex align-items-center px-2">
                <button class="btn btn-sm btn-link text-dark text-decoration-none fw-bold px-2 shadow-none" @click="quantity > 1 ? quantity-- : null">-</button>
                <span class="fw-bold px-3">{{ quantity }}</span>
                <button class="btn btn-sm btn-link text-dark text-decoration-none fw-bold px-2 shadow-none" @click="quantity++">+</button>
              </div>
             <button @click="addToCartMain" :disabled="product.stockQuantity <= 0" class="btn btn-neon fw-bold flex-grow-1 d-flex align-items-center justify-content-center gap-2 shadow-sm text-dark">
                <i class="bi bi-cart-plus fs-5"></i> THÊM VÀO GIỎ HÀNG
              </button>
              <button class="btn btn-outline-dark rounded-2 px-3 d-flex align-items-center justify-content-center shadow-none">
                <i class="bi bi-heart"></i>
              </button>
            </div>
            <button @click="buyNowMain" :disabled="product.stockQuantity <= 0" class="btn btn-neon fw-bold w-100 py-3 fs-6 text-dark shadow-sm">
              MUA NGAY
            </button>
          </div>
          
        </div>
      </div>

      <div class="product-info-section mb-5">
        <h5 class="fw-bold mb-3 border-bottom pb-2 text-dark">Thông tin sản phẩm</h5>
        <div class="fs-8 text-muted">
          <p>{{ product.description }}</p>
          <a href="#" class="text-dark fw-bold text-decoration-none">XEM THÊM >></a>
        </div>
      </div>

      <div class="reviews-section mb-5">
        <h5 class="fw-bold mb-4 border-bottom pb-2 text-uppercase text-dark">ĐÁNH GIÁ SẢN PHẨM</h5>
        
        <div class="row">
          <div class="col-md-3">
             <div class="rating-summary text-center">
              <h1 class="fw-light mb-0" style="font-size: 3.5rem;">{{ averageRating }}<span class="fs-4 text-muted">/5</span></h1>
              <div class="text-dark fw-bold my-1 fs-5">
                <span v-for="n in 5" :key="n">
                  {{ n <= Math.round(averageRating) ? '★' : '☆' }}
                </span>
              </div>
              <p class="fs-9 text-muted mb-0">Dựa trên {{ reviews.length }} đánh giá</p>
            </div>
          </div>
        </div>
      </div>

      <div class="similar-products mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
          <h5 class="fw-bold mb-0 text-uppercase text-dark">SẢN PHẨM TƯƠNG TỰ</h5>
          <router-link to="/products" class="text-primary fs-8 fw-bold text-decoration-none">XEM TẤT CẢ >></router-link>
        </div>
        
        <div class="row g-3">
          <div class="col-md-6 col-lg-3" v-for="item in similarProducts" :key="item.productId || item.id">
            <div class="card h-100 border-0 product-card rounded-3 overflow-hidden shadow-sm">
              <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none">
                <div class="img-wrapper position-relative d-flex justify-content-center align-items-center p-3" style="background-color:#F8F9FA;height:180px;">
                  <span v-if="item.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">GIẢM GIÁ</span>
                  <img :src="getImageUrl(item.imageUrl)" class="img-fluid object-fit-contain" style="height:140px;" :alt="item.name" @error="handleImageError">
                </div>
              </router-link>
              <div class="info-wrapper p-3 d-flex flex-column flex-grow-1" style="background-color:#111111;">
                <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none">
                  <h6 class="fw-bold mb-1 text-uppercase fs-8 text-white line-clamp-1">{{ item.name }}</h6>
                </router-link>
                <div class="mt-auto mb-3">
                  <h6 class="fw-black m-0 d-inline-block fs-6 text-white">{{ formatCurrency(item.salePrice || item.price) }}</h6>
                </div>
                <div class="d-flex gap-2">
                  <button @click="addToCartSimilar(item)" :disabled="item.stockQuantity <= 0" class="btn btn-outline-light btn-cart-icon d-flex align-items-center justify-content-center rounded-2 p-1" style="width: 40px;">
                    <i class="bi bi-cart-plus fs-6"></i>
                  </button>
                  <button @click="buyNowSimilar(item)" :disabled="item.stockQuantity <= 0" class="btn btn-neon fw-bold flex-grow-1 fs-8 p-2 rounded-2 text-dark">
                    MUA NGAY
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const product = ref(null);
const similarProducts = ref([]);
const reviews = ref([]); 
const quantity = ref(1);

const getImageUrl = (url) => {
  if (!url) return 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image';
  if (url.startsWith('http')) return url;
  return `http://localhost:8080${url.startsWith('/') ? '' : '/'}${url}`;
};

const handleImageError = (e) => { e.target.src = 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image'; };

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + ' VNĐ' : "0 VNĐ";

// Đã cập nhật đúng dữ liệu thật, mặc định là 0.0 nếu chưa có ai rate
const averageRating = computed(() => reviews.value.length ? (reviews.value.reduce((a, r) => a + r.rating, 0) / reviews.value.length).toFixed(1) : "0.0");

const fetchProductDetail = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/product/${id}`);
    product.value = res.data;
    await Promise.all([fetchSimilarProducts(), fetchReviews()]);
  } catch (e) { console.error(e); }
};

const fetchSimilarProducts = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/product`, { params: { categoryId: product.value.categoryId, page: 0, size: 4 } });
    const all = res.data.content || res.data;
    similarProducts.value = all.filter(item => (item.productId || item.id) !== (product.value.productId || product.value.id));
  } catch (e) {}
};

const fetchReviews = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/reviews/product/${product.value.productId || product.value.id}`);
    reviews.value = res.data;
  } catch (e) { reviews.value = []; }
};

//thêm vào giỏ hàng
const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      return JSON.parse(userInfoString).userId;
    } catch (e) { return null; }
  }
  return null;
};

const handleAddToCart = async (productObj, qtyToAdd) => {
  if (!productObj) return;
  const pId = productObj.productId || productObj.id;
  const userId = getCurrentUserId();

  if (userId) {
    // ĐÃ ĐĂNG NHẬP: GỌI API BACKEND
    try {
      await axios.post(`http://localhost:8080/api/cart/${userId}/add`, { 
        productId: pId, 
        quantity: qtyToAdd 
      });
      alert("Đã thêm sản phẩm vào giỏ hàng!");
      window.dispatchEvent(new Event('cart-updated')); 
    } catch (error) {
      alert(error.response?.data || "Không thể thêm vào giỏ hàng");
    }
  } else {
    // KHÁCH VÃNG LAI: LƯU LOCALSTORAGE
    let guestCart = JSON.parse(localStorage.getItem('guest_cart')) || [];
    const existingItemIndex = guestCart.findIndex(i => i.productId === pId);

    if (existingItemIndex !== -1) {
      guestCart[existingItemIndex].quantity += qtyToAdd;
    } else {
      guestCart.push({
        productId: pId,
        name: productObj.name,
        price: productObj.salePrice || productObj.price,
        quantity: qtyToAdd,
        img: productObj.imageUrl || 'https://via.placeholder.com/150'
      });
    }
    localStorage.setItem('guest_cart', JSON.stringify(guestCart));
    alert("Đã thêm sản phẩm vào giỏ hàng!");
    window.dispatchEvent(new Event('cart-updated'));
  }
};

// --- ÁP DỤNG CHO SẢN PHẨM CHÍNH (Lấy số lượng từ input) ---
const addToCartMain = () => handleAddToCart(product.value, quantity.value);
const buyNowMain = async () => {
  await addToCartMain();
  router.push('/cart');
};

// --- ÁP DỤNG CHO SẢN PHẨM TƯƠNG TỰ (Mặc định số lượng = 1) ---
const addToCartSimilar = (item) => handleAddToCart(item, 1);
const buyNowSimilar = async (item) => {
  await addToCartSimilar(item);
  router.push('/cart');
};

watch(() => route.params.id, (newId) => { if (newId) fetchProductDetail(newId); });
onMounted(() => fetchProductDetail(route.params.id));
</script>

<style scoped>
/* CSS Reset / Base Setup */
.cursor-pointer { cursor: pointer; }
.hover-border-dark:hover { border-color: #212529 !important; color: #212529 !important; }

/* Thumbnail List Styling */
.thumbnail-list .thumb-item {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}
.thumbnail-list .thumb-item:hover {
  border-color: #212529 !important;
}

/* Custom Neon Button */
.btn-neon { 
  background-color: #00FF33; 
  border: none; 
}
.btn-neon:hover { 
  background-color: #00e62e; 
}

/* Utility for line clamp */
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>