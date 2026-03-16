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
          <div class="main-image-box border rounded-3 position-relative d-flex justify-content-center align-items-center mb-3 bg-white overflow-hidden">
            <span v-if="product.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-3 z-1 fw-bold fs-8 px-3 py-1">GIẢM GIÁ</span>
            <img :src="currentImage" 
                 class="img-fluid object-fit-contain w-100 h-100" 
                 style="max-height: 500px; mix-blend-mode: multiply;" 
                 :alt="product.name" 
                 @error="handleImageError">
          </div>
          
          <div class="d-flex gap-2 thumbnail-list">
            <div @click="setMainImage(getImageUrl(product.imageUrl))" 
                 class="thumb-item border rounded-2 cursor-pointer overflow-hidden" 
                 :class="{'border-dark border-2': currentImage === getImageUrl(product.imageUrl)}">
              <img :src="getImageUrl(product.imageUrl)" class="img-fluid object-fit-cover w-100 h-100" style="mix-blend-mode: multiply;" @error="handleImageError">
            </div>
            
            <template v-for="variant in product.variants" :key="'thumb-'+variant.variantId">
              <div v-if="variant.imageUrl && variant.imageUrl !== product.imageUrl"
                   @click="selectVariant(variant)" 
                   class="thumb-item border rounded-2 cursor-pointer overflow-hidden"
                   :class="{'border-dark border-2': currentImage === getImageUrl(variant.imageUrl)}">
                <img :src="getImageUrl(variant.imageUrl)" class="img-fluid object-fit-cover w-100 h-100" style="mix-blend-mode: multiply;" @error="handleImageError">
              </div>
            </template>
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

          <div class="color-section mb-4" v-if="product.variants && product.variants.length > 0">
            <div class="fw-bold fs-8 text-dark mb-2 text-uppercase">MÀU SẮC</div>
            <div class="d-flex gap-2 flex-wrap">
              <button
                v-for="variant in product.variants"
                :key="variant.variantId"
                @click="selectVariant(variant)"
                class="btn bg-white d-flex align-items-center gap-2 px-3 py-2 rounded-2 border transition-all"
                :class="selectedVariantId === variant.variantId ? 'border-dark border-2' : 'border-light-subtle hover-border-dark'"
              >
                <img v-if="variant.imageUrl" :src="getImageUrl(variant.imageUrl)" class="rounded-1 object-fit-cover border" style="width: 24px; height: 24px; mix-blend-mode: multiply;" alt="color">
                <span class="fs-8 fw-bold" :class="selectedVariantId === variant.variantId ? 'text-dark' : 'text-muted'">
                  {{ variant.colorName }}
                </span>
              </button>
            </div>
          </div>

          <div class="switch-section mb-4" v-if="categoryAttributes && categoryAttributes.length > 0">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <span class="fw-bold fs-8 text-dark text-uppercase">TÙY CHỌN </span>
              <a href="#" class="text-muted fs-8 text-decoration-none">HƯỚNG DẪN CHỌN</a>
            </div>
            <div class="d-flex gap-2 flex-wrap">
              <div v-for="(attr, index) in categoryAttributes" :key="index"
                   class="attr-box border border-dark rounded-2 p-2 text-center flex-grow-1 cursor-pointer hover-bg-light">
                <div class="fs-8 fw-bold text-dark">{{ attr.title }}</div>
                <div class="fs-9 text-muted">{{ attr.desc }}</div>
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
          <div class="col-md-3 border-end">
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

          <div class="col-md-9 ps-4">
            <div v-if="reviews && reviews.length > 0" class="d-flex flex-column gap-3">
              <div v-for="review in reviews" :key="review.reviewId" class="review-item border-bottom pb-3">
                <div class="d-flex align-items-center gap-3 mb-2">
                  <img v-if="review.userAvatar" :src="review.userAvatar" class="rounded-circle object-fit-cover" style="width: 45px; height: 45px;" alt="Avatar">
                  <div v-else class="bg-light text-secondary border rounded-circle d-flex align-items-center justify-content-center" style="width: 45px; height: 45px;">
                    <i class="bi bi-person fs-4"></i>
                  </div>
                  <div>
                    <div class="fw-bold fs-8 text-dark">{{ review.userFullName || (review.user && review.user.fullName) || 'Khách hàng' }}</div>
                    <div class="d-flex align-items-center gap-2">
                      <div class="text-dark fs-9 fw-bold">
                        <span v-for="n in 5" :key="n">{{ n <= review.rating ? '★' : '☆' }}</span>
                      </div>
                      <span v-if="review.reviewDate" class="text-muted fs-9">• {{ formatDate(review.reviewDate) }}</span>
                    </div>
                  </div>
                </div>
                <p class="mb-0 fs-8 text-dark">{{ review.comment || 'Đánh giá này không có bình luận.' }}</p>
              </div>
            </div>
            <div v-else class="text-muted fs-8 fst-italic py-4 text-center">
              Chưa có bình luận nào cho sản phẩm này.
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
                  <img :src="getImageUrl(item.imageUrl)" class="img-fluid object-fit-contain" style="height:140px; mix-blend-mode: multiply;" :alt="item.name" @error="handleImageError">
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
const router = useRouter();
const product = ref(null);
const similarProducts = ref([]);
const reviews = ref([]); 
const quantity = ref(1);
const categoryAttributes = ref([]); 

const currentImage = ref('');
const selectedVariantId = ref(null);

const getImageUrl = (url) => {
  if (!url) return 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image';
  if (url.startsWith('http')) return url;
  return `http://localhost:8080${url.startsWith('/') ? '' : '/'}${url}`;
};

const handleImageError = (e) => { e.target.src = 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image'; };

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + ' VNĐ' : "0 VNĐ";

const averageRating = computed(() => reviews.value.length ? (reviews.value.reduce((a, r) => a + r.rating, 0) / reviews.value.length).toFixed(1) : "0.0");

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN', { 
    day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit'
  });
};

const setMainImage = (url) => {
  currentImage.value = url;
  if (product.value.variants && product.value.variants.length > 0) {
    const matched = product.value.variants.find(v => v.imageUrl && getImageUrl(v.imageUrl) === url);
    selectedVariantId.value = matched ? matched.variantId : null;
  } else {
    selectedVariantId.value = null;
  }
};

// ĐÃ SỬA: Nếu click vào màu có hình thì đổi hình, không có thì về hình mặc định của sản phẩm
const selectVariant = (variant) => {
  selectedVariantId.value = variant.variantId;
  if (variant.imageUrl) {
    currentImage.value = getImageUrl(variant.imageUrl);
  } else {
    currentImage.value = getImageUrl(product.value.imageUrl);
  }
};

const fetchProductDetail = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/product/${id}`);
    product.value = res.data;
    
    currentImage.value = getImageUrl(product.value.imageUrl);
    selectedVariantId.value = null;

    if (product.value.variants && product.value.variants.length > 0) {
      const mainVariantIndex = product.value.variants.findIndex(v => v.imageUrl && v.imageUrl === product.value.imageUrl);
      if (mainVariantIndex > 0) {
        const [mainVariant] = product.value.variants.splice(mainVariantIndex, 1);
        product.value.variants.unshift(mainVariant);
      }
      if (product.value.variants[0].imageUrl === product.value.imageUrl) {
        selectedVariantId.value = product.value.variants[0].variantId;
      }
    }

    if (product.value.categoryId) {
      try {
        const catRes = await axios.get(`http://localhost:8080/api/categories/${product.value.categoryId}`);
        if (catRes.data && catRes.data.attributes) {
          categoryAttributes.value = JSON.parse(catRes.data.attributes);
        } else {
          categoryAttributes.value = [];
        }
      } catch (e) { console.error("Không lấy được thuộc tính danh mục", e); }
    }

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
    try {
      await axios.post(`http://localhost:8080/api/cart/${userId}/add`, { productId: pId, quantity: qtyToAdd });
      alert("Đã thêm sản phẩm vào giỏ hàng!");
      window.dispatchEvent(new Event('cart-updated')); 
    } catch (error) {
      alert(error.response?.data || "Không thể thêm vào giỏ hàng");
    }
  } else {
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

const addToCartMain = () => handleAddToCart(product.value, quantity.value);
const buyNowMain = async () => {
  await addToCartMain();
  router.push('/cart');
};

const addToCartSimilar = (item) => handleAddToCart(item, 1);
const buyNowSimilar = async (item) => {
  await addToCartSimilar(item);
  router.push('/cart');
};

watch(() => route.params.id, (newId) => { if (newId) fetchProductDetail(newId); });
onMounted(() => fetchProductDetail(route.params.id));
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.hover-border-dark:hover { border-color: #212529 !important; }
.transition-all { transition: all 0.2s ease-in-out; }

.attr-box {
  word-break: break-word;
  overflow-wrap: break-word;
  min-width: 100px;
}

.thumbnail-list .thumb-item {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  background-color: white; 
}
.thumbnail-list .thumb-item:hover {
  border-color: #212529 !important;
}

.btn-neon { background-color: #00FF33; border: none; }
.btn-neon:hover { background-color: #00e62e; }

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>