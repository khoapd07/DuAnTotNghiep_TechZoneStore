<template>
  <div class="product-detail-page bg-white">
    <div class="container narrow-container py-4" v-if="product">
      
      <!-- Breadcrumb -->
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
          <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">{{ product.categoryName }}</a></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">{{ product.name }}</li>
        </ol>
      </nav>

      <div class="row g-5 mb-5">
        <!-- Ảnh chính + thumbnail -->
        <div class="col-lg-7">
          <div class="main-image-box border rounded-3 position-relative d-flex justify-content-center align-items-center mb-3 bg-light">
            <span v-if="product.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-3 z-1 fw-bold fs-8 px-3 py-1">GIẢM GIÁ</span>
            <img :src="getImageUrl(product.imageUrl)" class="img-fluid object-fit-contain p-4" style="max-height: 450px;" :alt="product.name" @error="handleImageError">
          </div>
          <div class="d-flex gap-2 thumbnail-list">
            <div class="thumb-item active border rounded-2"><img :src="getImageUrl(product.imageUrl)" class="img-fluid p-1" @error="handleImageError"></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-image"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-box-seam"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-play-circle"></i></div>
          </div>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="col-lg-5">
          <h2 class="fw-black mb-1 text-uppercase">{{ product.name }}</h2>
          <div class="d-flex align-items-center gap-2 mb-3 fs-8">
            <div class="text-neon d-flex gap-1">★★★★★</div>
            <span class="text-muted">128 đánh giá</span>
            <span class="text-muted">•</span>
            <span class="text-neon fw-bold">● {{ product.stockQuantity > 0 ? 'CÒN HÀNG' : 'HẾT HÀNG' }}</span>
          </div>
          <div class="d-flex align-items-end gap-3 mb-4">
            <h2 class="text-neon fw-black mb-0 fs-2">{{ formatCurrency(product.salePrice || product.price) }}</h2>
            <span v-if="product.salePrice" class="text-muted text-decoration-line-through fs-6 mb-1">{{ formatCurrency(product.price) }}</span>
            <span class="badge bg-light text-dark border fs-9">-15%</span>
          </div>
          <p class="fs-8 text-muted mb-3 line-height-lg">{{ product.description }}</p>
        </div>
      </div>

      <!-- Thông tin sản phẩm -->
      <div class="product-info-section mb-5">
        <h5 class="fw-bold mb-3 border-bottom pb-2">Thông tin sản phẩm</h5>
        <div class="fs-8 text-muted">
          <p>{{ product.description }}</p>
          <a href="#" class="text-dark fw-bold text-decoration-none">XEM THÊM >></a>
        </div>
      </div>

      <!-- ĐÁNH GIÁ THẬT TỪ API -->
      <div class="reviews-section mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h5 class="fw-bold mb-0 text-uppercase">ĐÁNH GIÁ SẢN PHẨM</h5>
        </div>
        
        <div class="row g-4">
          <!-- Cột trái: Tóm tắt -->
          <div class="col-md-4">
            <div class="rating-summary bg-light rounded-3 p-4 text-center h-100">
              <h1 class="fw-black mb-0 display-4">{{ averageRating }}<span class="fs-4 text-muted">/5</span></h1>
              <div class="text-neon my-2 fs-3">★★★★☆</div>
              <p class="fs-8 text-muted mb-4">Dựa trên {{ reviews.length }} đánh giá</p>
            </div>
          </div>

          <!-- Cột phải: Đánh giá thật -->
          <div class="col-md-8">
            <div v-for="(review, index) in reviews.slice(0,4)" :key="index" class="review-item border-bottom pb-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-2">
                <div class="avatar bg-success text-white rounded-circle d-flex align-items-center justify-content-center fw-bold" style="width:40px;height:40px;">
                  {{ review.reviewerName ? review.reviewerName[0] : 'U' }}
                </div>
                <div>
                  <span class="fw-bold">{{ review.reviewerName || 'Khách hàng' }}</span>
                </div>
                <small class="text-muted ms-auto">{{ review.reviewDate }}</small>
              </div>
              <div class="text-neon mb-2">★★★★★</div>
              <p class="fs-8 text-muted mb-0">{{ review.comment }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- SẢN PHẨM TƯƠNG TỰ – NỀN TRẮNG (giống ảnh anh gửi) -->
      <div class="similar-products mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h5 class="fw-bold mb-0 text-uppercase">SẢN PHẨM TƯƠNG TỰ</h5>
          <router-link to="/products" class="text-neon fs-8 fw-bold text-decoration-none">XEM TẤT CẢ >></router-link>
        </div>
        
        <div class="row g-3">
          <div class="col-md-6 col-lg-3" v-for="item in similarProducts" :key="item.productId || item.id">
            <div class="card h-100 border-0 product-card custom-card-hover rounded-3 overflow-hidden">
              <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none">
                <div class="img-wrapper position-relative d-flex justify-content-center align-items-center p-3" style="background-color:#111111;height:180px;">
                  <span v-if="item.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">GIẢM GIÁ</span>
                  <img :src="getImageUrl(item.imageUrl)" class="img-fluid object-fit-contain" style="height:140px;" :alt="item.name" @error="handleImageError">
                </div>
              </router-link>
              <div class="info-wrapper p-3 d-flex flex-column flex-grow-1 bg-white text-dark">
                <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none text-dark">
                  <h6 class="fw-bold mb-1 text-uppercase fs-8 line-clamp-1">{{ item.name }}</h6>
                </router-link>
                <small class="text-muted mb-2 d-block fs-9">{{ item.categoryName }} • {{ item.brandName }}</small>
                <div class="mt-auto mb-2">
                  <h6 class="fw-black m-0 d-inline-block fs-7 text-neon">{{ formatCurrency(item.salePrice || item.price) }}</h6>
                  <span v-if="item.salePrice" class="text-muted text-decoration-line-through ms-2 fs-9">{{ formatCurrency(item.price) }}</span>
                </div>
                <div class="d-flex gap-2">
                  <button class="btn btn-outline-dark btn-cart-icon d-flex align-items-center justify-content-center rounded-2 p-1"><i class="bi bi-cart2 fs-6"></i></button>
                  <button class="btn btn-neon fw-bold flex-grow-1 fs-8 p-1 rounded-2 text-dark">MUA NGAY</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- FOOTER -->
    <footer class="bg-dark text-white pt-5 pb-3 mt-5">
      <div class="container">
        <div class="row mb-4">
          <div class="col-md-4">
            <h4 class="fw-black text-neon mb-3">TECHZONE //</h4>
            <p class="fs-8 text-secondary">Điểm đến tin cậy cho cộng đồng đam mê công nghệ và game thủ. Chúng tôi cung cấp những đánh giá khách quan và cập nhật tin tức công nghệ nhanh nhất.</p>
          </div>
          <div class="col-md-2 ms-auto">
            <h6 class="fw-bold mb-3">DANH MỤC</h6>
            <ul class="list-unstyled fs-8 text-secondary">
              <li>Bàn phím</li><li>Chuột</li><li>Tai nghe</li>
            </ul>
          </div>
          <div class="col-md-2">
            <h6 class="fw-bold mb-3">HỖ TRỢ</h6>
            <ul class="list-unstyled fs-8 text-secondary">
              <li>Liên hệ</li><li>Chính sách</li><li>Điều khoản</li>
            </ul>
          </div>
        </div>
        <hr class="border-secondary">
        <div class="text-center fs-9 text-secondary mt-3">
          © 2026 TechZone Media. All rights reserved. | Designed for Gaming Excellence
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { useRoute } from 'vue-router';
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

const averageRating = computed(() => reviews.value.length ? (reviews.value.reduce((a, r) => a + r.rating, 0) / reviews.value.length).toFixed(1) : "4.8");

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

watch(() => route.params.id, (newId) => { if (newId) fetchProductDetail(newId); });
onMounted(() => fetchProductDetail(route.params.id));
</script>

<style scoped>
.product-card { background-color: transparent; }
.img-wrapper { background-color: #111111; height: 180px; }
.info-wrapper { background-color: #ffffff; color: #000; border-top: 1px solid #eee; }
</style>