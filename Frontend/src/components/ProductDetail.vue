<template>
  <div class="product-detail-page bg-white">
    <div class="container narrow-container py-4" v-if="product">
      
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">{{ product.categoryName }}</a></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">{{ displayName }}</li>
        </ol>
      </nav>

      <div class="row g-5 mb-5">
        <div class="col-lg-7">
          <div class="main-image-box border rounded-3 position-relative d-flex justify-content-center align-items-center mb-3 bg-white overflow-hidden">
            <span v-if="currentSalePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-3 z-1 fw-bold fs-8 px-3 py-1">GIẢM GIÁ</span>
            <img :src="currentImage" 
                 class="img-fluid object-fit-contain w-100 h-100" 
                 style="max-height: 500px; mix-blend-mode: multiply;" 
                 :alt="displayName" 
                 @error="handleImageError">
          </div>
          
          <div class="d-flex gap-2 thumbnail-list flex-wrap">
            <div v-for="(imgUrl, index) in uniqueThumbnails" :key="'thumb-'+index"
                 @click="setMainImage(getImageUrl(imgUrl))" 
                 class="thumb-item border rounded-2 cursor-pointer overflow-hidden" 
                 :class="{'border-dark border-2': currentImage === getImageUrl(imgUrl)}">
              <img :src="getImageUrl(imgUrl)" class="img-fluid object-fit-cover w-100 h-100" style="mix-blend-mode: multiply;" @error="handleImageError">
            </div>
          </div>
        </div>

        <div class="col-lg-5">
          <h2 class="fw-black mb-1 text-uppercase text-dark">{{ displayName }}</h2>
          
          <div class="d-flex align-items-center gap-2 mb-3 fs-8">
            <div class="text-dark d-flex gap-1 fw-bold">
              <span v-for="n in 5" :key="n">{{ n <= Math.round(averageRating) ? '★' : '☆' }}</span>
            </div>
            <span class="text-muted">{{ reviews.length }} đánh giá</span>
            <span class="text-muted">•</span>
            <span class="text-dark fw-bold">● {{ stockStatusText }}</span>
          </div>
          
          <div class="d-flex align-items-end gap-3 mb-4">
            <h2 class="text-primary fw-black mb-0 fs-2">{{ formatCurrency(currentSalePrice || currentPrice) }}</h2>
            <span v-if="currentSalePrice" class="text-muted text-decoration-line-through fs-6 mb-1">{{ formatCurrency(currentPrice) }}</span>
            <span v-if="currentSalePrice" class="badge bg-danger-subtle text-danger border border-danger-subtle fs-9">-{{ calculateDiscount(currentPrice, currentSalePrice) }}%</span>
          </div>
          <p class="fs-8 text-muted mb-4 line-height-lg">{{ product.description }}</p>

          <div class="color-section mb-4" v-if="uniqueColors.length > 0 && uniqueColors[0] !== 'Tiêu chuẩn'">
            <div class="fw-bold fs-8 text-dark mb-2 text-uppercase">MÀU SẮC</div>
            <div class="d-flex gap-2 flex-wrap">
              <button
                v-for="color in uniqueColors"
                :key="color"
                @click="selectColor(color)"
                class="btn bg-white d-flex align-items-center gap-2 px-3 py-2 rounded-2 border transition-all shadow-none"
                :class="selectedColor === color ? 'border-primary border-2' : 'border-light-subtle hover-border-dark'"
              >
                <img v-if="getColorImage(color)" :src="getImageUrl(getColorImage(color))" class="rounded-1 object-fit-cover border" style="width: 24px; height: 24px; mix-blend-mode: multiply;" alt="color">
                <span class="fs-8 fw-bold" :class="selectedColor === color ? 'text-primary' : 'text-dark'">
                  {{ color }}
                </span>
              </button>
            </div>
          </div>

          <div class="capacity-section mb-4" v-if="uniqueOption2s.length > 0">
            <div class="fw-bold fs-8 text-dark mb-2 text-uppercase">TÙY CHỌN PHÂN LOẠI</div>
            <div class="d-flex gap-2 flex-wrap">
              <button
                v-for="opt in uniqueOption2s"
                :key="opt"
                @click="selectedOption2 = opt"
                class="btn bg-white px-4 py-2 rounded-2 border transition-all shadow-none"
                :class="selectedOption2 === opt ? 'border-primary border-2 fw-bold text-primary' : 'border-light-subtle hover-border-dark text-dark'"
              >
                <span class="fs-8">{{ opt }}</span>
              </button>
            </div>
          </div>

          <div class="d-flex flex-column gap-3 mb-4">
            <div class="d-flex gap-3">
              <div class="border rounded-2 d-flex align-items-center px-2">
                <button class="btn btn-sm btn-link text-dark text-decoration-none fw-bold px-2 shadow-none" @click="quantity > 1 ? quantity-- : null">-</button>
                <span class="fw-bold px-3">{{ quantity }}</span>
                <button class="btn btn-sm btn-link text-dark text-decoration-none fw-bold px-2 shadow-none" @click="quantity++">+</button>
              </div>
             <button @click="addToCartMain" :disabled="!selectedVariantData || selectedVariantData.stockQuantity <= 0" class="btn btn-neon fw-bold flex-grow-1 d-flex align-items-center justify-content-center gap-2 shadow-sm text-dark">
                <i class="bi bi-cart-plus fs-5"></i> THÊM VÀO GIỎ HÀNG
              </button>
              <button class="btn btn-outline-dark rounded-2 px-3 d-flex align-items-center justify-content-center shadow-none">
                <i class="bi bi-heart"></i>
              </button>
            </div>
            <button @click="buyNowMain" :disabled="!selectedVariantData || selectedVariantData.stockQuantity <= 0" class="btn btn-neon fw-bold w-100 py-3 fs-6 text-dark shadow-sm">
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

    <div v-if="showSuccessModal" class="custom-modal-overlay d-flex justify-content-center align-items-center">
      <div class="custom-modal bg-white rounded-4 p-4 text-center shadow-lg">
        <div class="mb-3">
          <i class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>
        </div>
        <h5 class="fw-bold mb-2">Thành công!</h5>
        <p class="text-muted fs-8 mb-4">Sản phẩm đã được thêm vào giỏ hàng.</p>
        <div class="d-flex gap-2 justify-content-center">
          <button @click="closeSuccessModal" class="btn btn-outline-dark fs-8 fw-bold px-4 py-2 rounded-2">
            Tiếp tục mua
          </button>
          <router-link to="/cart" class="btn btn-neon text-dark fs-8 fw-bold px-4 py-2 rounded-2 text-decoration-none">
            Đến giỏ hàng
          </router-link>
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

const currentImage = ref('');
const selectedColor = ref('');
const selectedOption2 = ref('');
const lastClicked = ref('none');

// State (trạng thái) để quản lý hiển thị Modal
const showSuccessModal = ref(false);

const closeSuccessModal = () => {
  showSuccessModal.value = false;
};

const uniqueThumbnails = computed(() => {
  if (!product.value) return [];
  const urls = new Set();
  if (product.value.imageUrl) urls.add(product.value.imageUrl);
  if (product.value.variants) {
    product.value.variants.forEach(v => {
      if (v.imageUrl) urls.add(v.imageUrl);
    });
  }
  return Array.from(urls);
});

const uniqueColors = computed(() => {
  if (!product.value || !product.value.variants) return [];
  return [...new Set(product.value.variants.map(v => v.colorName).filter(Boolean))];
});

const uniqueOption2s = computed(() => {
  if (!product.value || !product.value.variants) return [];
  return [...new Set(product.value.variants.map(v => v.option2Value).filter(Boolean))];
});

const selectedVariantData = computed(() => {
  if (!product.value || !product.value.variants) return null;
  return product.value.variants.find(v => 
    v.colorName === selectedColor.value && 
    ((!v.option2Value && !selectedOption2.value) || v.option2Value === selectedOption2.value)
  );
});

const displayName = computed(() => {
  if (!product.value) return '';
  let name = product.value.name;
  if (selectedColor.value && selectedColor.value !== 'Tiêu chuẩn') name += ` - ${selectedColor.value}`;
  if (selectedOption2.value) name += ` ${selectedOption2.value}`;
  return name;
});

const currentPrice = computed(() => {
  if (!product.value) return 0;
  if (lastClicked.value === 'color' && selectedVariantData.value && selectedVariantData.value.price > 0) {
    return selectedVariantData.value.price;
  }
  if (selectedVariantData.value && selectedVariantData.value.price > 0) return selectedVariantData.value.price;
  return product.value.price;
});

const currentSalePrice = computed(() => {
  if (!product.value) return null;
  if (lastClicked.value === 'color' && selectedVariantData.value && selectedVariantData.value.price > 0) {
    return selectedVariantData.value.salePrice || null;
  }
  if (selectedVariantData.value && selectedVariantData.value.price > 0) return selectedVariantData.value.salePrice || null;
  return product.value.salePrice;
});

const stockStatusText = computed(() => {
  if (!selectedVariantData.value) return 'LỰA CHỌN KHÔNG CÓ SẴN';
  return selectedVariantData.value.stockQuantity > 0 ? `CÒN HÀNG (${selectedVariantData.value.stockQuantity})` : 'TẠM HẾT HÀNG';
});

const getColorImage = (color) => {
   const match = product.value.variants.find(v => v.colorName === color && v.imageUrl);
   return match ? match.imageUrl : null;
};

const calculateDiscount = (price, salePrice) => {
  if (!price || !salePrice || price <= 0 || salePrice >= price) return 0;
  return Math.round(((price - salePrice) / price) * 100);
};

const getImageUrl = (url) => {
  if (!url) return 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image';
  if (url.startsWith('http')) return url;
  return `http://localhost:8080${url.startsWith('/') ? '' : '/'}${url}`;
};

const handleImageError = (e) => { e.target.src = 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image'; };

const formatCurrency = (value) => value ? value.toLocaleString('vi-VN') + ' đ' : "0 đ";

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
};

const selectColor = (color) => {
  selectedColor.value = color;
  lastClicked.value = 'color';
  const img = getColorImage(color);
  if (img) currentImage.value = getImageUrl(img);
  else currentImage.value = getImageUrl(product.value.imageUrl);
};

const fetchProductDetail = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/product/${id}`);
    product.value = res.data;
    
    currentImage.value = getImageUrl(product.value.imageUrl);
    lastClicked.value = 'none';

    if (uniqueColors.value.length > 0) selectedColor.value = uniqueColors.value[0];
    if (uniqueOption2s.value.length > 0) selectedOption2.value = uniqueOption2s.value[0];

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

// ==========================================
// CẬP NHẬT: THÊM parameter (tham số) showNotification
// ==========================================
const handleAddToCart = async (productObj, qtyToAdd, isSimilarProduct = false, showNotification = true) => {
  if (!productObj) return;
  const pId = productObj.productId || productObj.id;
  const userId = getCurrentUserId();
  
  let vId = null;
  let vColor = '';
  let vOpt2 = '';
  let vPrice = productObj.salePrice || productObj.price;
  let vImg = productObj.imageUrl || 'https://via.placeholder.com/150';

  if (!isSimilarProduct && selectedVariantData.value) {
    vId = selectedVariantData.value.variantId;
    vColor = selectedVariantData.value.colorName;
    vOpt2 = selectedVariantData.value.option2Value;
    vPrice = currentSalePrice.value || currentPrice.value;
    vImg = selectedVariantData.value.imageUrl || vImg;
  } else if (isSimilarProduct && productObj.variants && productObj.variants.length > 0) {
    const firstVariant = productObj.variants[0];
    vId = firstVariant.variantId;
    vColor = firstVariant.colorName;
    vOpt2 = firstVariant.option2Value;
    vPrice = firstVariant.salePrice || firstVariant.price;
    vImg = firstVariant.imageUrl || vImg;
  }

  if (userId) {
    try {
      await axios.post(`http://localhost:8080/api/cart/${userId}/add`, { 
          productId: pId, 
          variantId: vId, 
          quantity: qtyToAdd 
      });
      if (showNotification) showSuccessModal.value = true;
      window.dispatchEvent(new Event('cart-updated')); 
    } catch (error) {
      alert(error.response?.data || "Không thể thêm vào giỏ hàng");
    }
  } else {
    let guestCart = JSON.parse(localStorage.getItem('guest_cart')) || [];
    const existingItemIndex = guestCart.findIndex(i => i.productId === pId && i.variantId === vId);

    if (existingItemIndex !== -1) {
      guestCart[existingItemIndex].quantity += qtyToAdd;
    } else {
      guestCart.push({
        productId: pId,
        variantId: vId,
        name: productObj.name,
        colorName: vColor,
        option2Value: vOpt2,
        price: vPrice,
        quantity: qtyToAdd,
        img: vImg
      });
    }
    localStorage.setItem('guest_cart', JSON.stringify(guestCart));
    if (showNotification) showSuccessModal.value = true;
    window.dispatchEvent(new Event('cart-updated'));
  }
};

// Cập nhật lại các hàm gọi: Thêm argument (đối số) tương ứng
const addToCartMain = () => handleAddToCart(product.value, quantity.value, false, true);

const buyNowMain = async () => { 
  await handleAddToCart(product.value, quantity.value, false, false); 
  router.push('/cart'); 
};

const addToCartSimilar = (item) => handleAddToCart(item, 1, true, true);

const buyNowSimilar = async (item) => { 
  await handleAddToCart(item, 1, true, false); 
  router.push('/cart'); 
};

//////////////////////////////////////

watch(() => route.params.id, (newId) => { if (newId) fetchProductDetail(newId); });
onMounted(() => fetchProductDetail(route.params.id));
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.hover-border-dark:hover { border-color: #212529 !important; }
.transition-all { transition: all 0.2s ease-in-out; }

.text-primary { color: #0047FF !important; }
.border-primary { border-color: #0047FF !important; }

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

/* --- CSS THÊM MỚI CHO MODAL --- */
.custom-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5); /* Lớp nền đen mờ */
  z-index: 1050; /* Z-index (chỉ mục chiều Z) cao hơn để đè lên UI */
  animation: fadeIn 0.2s ease-in-out;
}

.custom-modal {
  width: 90%;
  max-width: 400px;
  animation: slideDown 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>