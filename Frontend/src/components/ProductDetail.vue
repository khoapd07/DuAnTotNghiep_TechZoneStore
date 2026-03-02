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
            <img 
              :src="getImageUrl(product.imageUrl)" 
              class="img-fluid object-fit-contain p-4" 
              style="max-height: 450px;" 
              :alt="product.name"
              @error="handleImageError"
            >
          </div>
          <div class="d-flex gap-2 thumbnail-list">
            <div class="thumb-item active border rounded-2">
              <img :src="getImageUrl(product.imageUrl)" class="img-fluid p-1" @error="handleImageError">
            </div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-image"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-box-seam"></i></div>
            <div class="thumb-item border rounded-2 d-flex align-items-center justify-content-center text-muted"><i class="bi bi-play-circle"></i></div>
          </div>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="col-lg-5">
          <h2 class="fw-black mb-1 text-uppercase">{{ product.name }}</h2>
          
          <div class="d-flex align-items-center gap-2 mb-3 fs-8">
            <div class="text-neon d-flex gap-1">
              <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
            </div>
            <span class="text-muted">128 đánh giá</span>
            <span class="text-muted">•</span>
            <span class="text-neon fw-bold">● {{ product.stockQuantity > 0 ? 'CÒN HÀNG' : 'HẾT HÀNG' }}</span>
          </div>

          <div class="d-flex align-items-end gap-3 mb-4">
            <h2 class="text-neon fw-black mb-0 fs-2">{{ formatCurrency(product.salePrice || product.price) }}</h2>
            <span v-if="product.salePrice" class="text-muted text-decoration-line-through fs-6 mb-1">{{ formatCurrency(product.price) }}</span>
            <span class="badge bg-light text-dark border fs-9">-15%</span>
          </div>

          <div class="mb-4">
            <p class="fs-8 text-muted mb-3 line-height-lg">
              Thông số kỹ thuật của {{ product.name }} được thiết kế cho các game thủ chuyên nghiệp, trang bị linh kiện cao cấp cho độ trễ gần như bằng không...
            </p>
          </div>

          <div class="mb-4">
            <label class="fw-bold fs-8 text-uppercase mb-2 d-block">Loại </label>
            <div class="d-flex gap-2">
              <button class="btn btn-outline-dark btn-sm fs-8 px-3 rounded-2 border-neon text-neon">Linear Red <br><small class="text-muted">Nhẹ nhàng, êm ái</small></button>
              <button class="btn btn-outline-secondary btn-sm fs-8 px-3 rounded-2">Clicky Blue <br><small class="text-muted">Cảm giác gõ rõ</small></button>
              <button class="btn btn-outline-secondary btn-sm fs-8 px-3 rounded-2">Tactile Brown <br><small class="text-muted">Cân bằng tốt</small></button>
            </div>
          </div>

          <div class="d-flex gap-2 pt-3">
            <div class="quantity-selector d-flex align-items-center bg-light border rounded-2 px-2">
              <button class="btn btn-sm border-0 fw-bold" @click="quantity > 1 && quantity--">-</button>
              <input type="number" v-model="quantity" class="form-control form-control-sm border-0 text-center fw-bold bg-transparent" style="width: 40px;">
              <button class="btn btn-sm border-0 fw-bold" @click="quantity++">+</button>
            </div>
            
            <button class="btn btn-neon fw-bold flex-grow-1 text-dark rounded-2 py-2 d-flex align-items-center justify-content-center gap-2">
              <i class="bi bi-cart2 fs-5"></i> THÊM VÀO GIỎ HÀNG
            </button>

            <button class="btn btn-outline-dark rounded-2 px-3">
              <i class="bi bi-heart"></i>
            </button>
          </div>
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

      <!-- ==================== ĐÁNH GIÁ SẢN PHẨM (ĐÃ LÀM Y CHANG ẢNH ANH GỬI) ==================== -->
      <div class="reviews-section mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h5 class="fw-bold mb-0 text-uppercase">ĐÁNH GIÁ SẢN PHẨM</h5>
    
        </div>
        
        <div class="row g-4">
          <!-- Cột trái: Tóm tắt đánh giá -->
          <div class="col-md-4">
            <div class="rating-summary bg-light rounded-3 p-4 text-center h-100">
              <h1 class="fw-black mb-0 display-4">4.8<span class="fs-4 text-muted">/5</span></h1>
              <div class="text-neon my-2 fs-3">
                ★★★★☆
              </div>
              <p class="fs-8 text-muted mb-4">Dựa trên 128 đánh giá</p>
              
              <!-- Thanh tiến độ -->
              <div class="star-bars text-start">
                <div class="d-flex align-items-center gap-2 mb-2 fs-8">
                  <span class="text-nowrap">5 <i class="bi bi-star-fill text-neon"></i></span>
                  <div class="flex-grow-1 bg-secondary rounded" style="height: 6px;">
                    <div class="bg-success rounded" style="width: 85%; height: 6px;"></div>
                  </div>
                  <span class="text-muted text-nowrap">85%</span>
                </div>
                <div class="d-flex align-items-center gap-2 mb-2 fs-8">
                  <span class="text-nowrap">4 <i class="bi bi-star-fill text-neon"></i></span>
                  <div class="flex-grow-1 bg-secondary rounded" style="height: 6px;">
                    <div class="bg-success rounded" style="width: 10%; height: 6px;"></div>
                  </div>
                  <span class="text-muted text-nowrap">10%</span>
                </div>
                <div class="d-flex align-items-center gap-2 mb-2 fs-8">
                  <span class="text-nowrap">3 <i class="bi bi-star-fill text-neon"></i></span>
                  <div class="flex-grow-1 bg-secondary rounded" style="height: 6px;">
                    <div class="bg-success rounded" style="width: 3%; height: 6px;"></div>
                  </div>
                  <span class="text-muted text-nowrap">3%</span>
                </div>
                <div class="d-flex align-items-center gap-2 mb-2 fs-8">
                  <span class="text-nowrap">2 <i class="bi bi-star-fill text-neon"></i></span>
                  <div class="flex-grow-1 bg-secondary rounded" style="height: 6px;">
                    <div class="bg-success rounded" style="width: 1%; height: 6px;"></div>
                  </div>
                  <span class="text-muted text-nowrap">1%</span>
                </div>
                <div class="d-flex align-items-center gap-2 fs-8">
                  <span class="text-nowrap">1 <i class="bi bi-star-fill text-neon"></i></span>
                  <div class="flex-grow-1 bg-secondary rounded" style="height: 6px;">
                    <div class="bg-success rounded" style="width: 1%; height: 6px;"></div>
                  </div>
                  <span class="text-muted text-nowrap">1%</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Cột phải: Danh sách đánh giá (4 cái như anh yêu cầu) -->
          <div class="col-md-8">
            <!-- Review 1 -->
            <div class="review-item border-bottom pb-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-2">
                <div class="avatar bg-success text-white rounded-circle d-flex align-items-center justify-content-center fw-bold" style="width: 40px; height: 40px;">N</div>
                <div>
                  <span class="fw-bold">Nguyễn Hoàng</span>
                  <span class="ms-2 text-success"><i class="bi bi-check-circle-fill"></i> Người mua đã xác thực</span>
                </div>
                <small class="text-muted ms-auto">15/05/2024</small>
              </div>
              <div class="text-neon mb-2">★★★★★</div>
              <p class="fs-8 text-muted mb-0">
                Bàn phím cực kỳ nhạy, cảm giác gõ phím switch quang học rất biệt so với switch cơ thông thường. Build nhôm rất chắc chắn, led RGB sáng và đẹp. Đáng đồng tiền bát gạo!
              </p>
            </div>

            <!-- Review 2 -->
            <div class="review-item border-bottom pb-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-2">
                <div class="avatar bg-success text-white rounded-circle d-flex align-items-center justify-content-center fw-bold" style="width: 40px; height: 40px;">T</div>
                <div>
                  <span class="fw-bold">Trần Anh</span>
                  <span class="ms-2 text-success"><i class="bi bi-check-circle-fill"></i> Người mua đã xác thực</span>
                </div>
                <small class="text-muted ms-auto">12/05/2024</small>
              </div>
              <div class="text-neon mb-2">★★★★★</div>
              <p class="fs-8 text-muted mb-0">
                Cảm giác gõ Red Switch rất mượt, phù hợp cho cả chơi game và làm việc văn phòng. Phần kê tay nam châm cực kỳ tiện lợi, tháo lắp dễ dàng. Shop giao hàng nhanh, đóng gói kỹ.
              </p>
            </div>

            <!-- Review 3 -->
            <div class="review-item border-bottom pb-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-2">
                <div class="avatar bg-success text-white rounded-circle d-flex align-items-center justify-content-center fw-bold" style="width: 40px; height: 40px;">L</div>
                <div>
                  <span class="fw-bold">Lê Minh</span>
                  <span class="ms-2 text-success"><i class="bi bi-check-circle-fill"></i> Người mua đã xác thực</span>
                </div>
                <small class="text-muted ms-auto">02/05/2024</small>
              </div>
              <div class="text-neon mb-2">★★★★★</div>
              <p class="fs-8 text-muted mb-0">
                Thiết kế tối giản nhưng vẫn rất gaming. App tùy chỉnh LED và Macro của TechZone dùng khá ổn. Điểm 10 cho chất lượng phục vụ.
              </p>
            </div>

            <!-- Review 4 (thêm để không còn "chỉ 1 cái hơi kì") -->
            <div class="review-item border-bottom pb-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-2">
                <div class="avatar bg-success text-white rounded-circle d-flex align-items-center justify-content-center fw-bold" style="width: 40px; height: 40px;">H</div>
                <div>
                  <span class="fw-bold">Hoàng Thịnh</span>
                  <span class="ms-2 text-success"><i class="bi bi-check-circle-fill"></i> Người mua đã xác thực</span>
                </div>
                <small class="text-muted ms-auto">28/04/2024</small>
              </div>
              <div class="text-neon mb-2">★★★★☆</div>
              <p class="fs-8 text-muted mb-0">
                Giao hàng siêu nhanh chỉ trong 1 ngày. Sản phẩm đúng như mô tả, tiếng gõ rất đã tai. Chỉ hơi tiếc là không có thêm keycap phụ nhưng vẫn rất đáng mua!
              </p>
            </div>
          </div>
        </div>

        <div class="text-center mt-4">
          <a href="#" class="fs-8 fw-bold text-dark text-decoration-none">Xem thêm đánh giá →</a>
        </div>
      </div>

      <!-- SẢN PHẨM TƯƠNG TỰ (giữ nguyên như code anh vừa gửi) -->
      <div class="similar-products mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h5 class="fw-bold mb-0 text-uppercase">SẢN PHẨM TƯƠNG TỰ</h5>
          <router-link to="/products" class="text-neon fs-8 fw-bold text-decoration-none">XEM TẤT CẢ >></router-link>
        </div>
        
        <div class="row g-3">
          <div class="col-md-6 col-lg-3" v-for="item in similarProducts" :key="item.productId || item.id">
            <div class="card h-100 border-0 product-card custom-card-hover rounded-3 overflow-hidden">
              <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none text-dark">
                <div class="img-wrapper position-relative d-flex justify-content-center align-items-center p-3" style="background-color: #111111; height: 180px;">
                  <span v-if="item.salePrice" class="badge bg-danger text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">GIẢM GIÁ</span>
                  <span v-if="item.stockQuantity <= 0" class="badge bg-secondary text-white position-absolute top-0 start-0 m-2 z-1 fw-bold fs-9 px-2 py-1">HẾT HÀNG</span>
                  <i class="bi bi-heart position-absolute top-0 end-0 m-2 fs-6 text-white cursor-pointer heart-icon"></i>
                  <img 
                    :src="getImageUrl(item.imageUrl)" 
                    class="img-fluid object-fit-contain" 
                    style="height: 140px;" 
                    :alt="item.name"
                    @error="handleImageError"
                  >
                </div>
              </router-link>

              <div class="info-wrapper p-3 d-flex flex-column flex-grow-1">
                <router-link :to="'/product/' + (item.productId || item.id)" class="text-decoration-none text-dark">
                  <h6 class="fw-bold mb-1 text-uppercase fs-8 line-clamp-1" :title="item.name">{{ item.name }}</h6>
                </router-link>
                <small class="text-muted mb-2 d-block fs-9">{{ item.categoryName }} • {{ item.brandName }}</small>
                
                <div class="mt-auto mb-2">
                  <h6 class="fw-black m-0 d-inline-block fs-7 text-neon">{{ formatCurrency(item.salePrice || item.price) }}</h6>
                  <span v-if="item.salePrice" class="text-muted text-decoration-line-through ms-2 fs-9">{{ formatCurrency(item.price) }}</span>
                </div>
                
                <div class="d-flex gap-2">
                  <button class="btn btn-outline-dark btn-cart-icon d-flex align-items-center justify-content-center rounded-2 p-1">
                    <i class="bi bi-cart2 fs-6"></i>
                  </button>
                  <button class="btn btn-neon fw-bold flex-grow-1 fs-8 p-1 rounded-2 text-dark">
                    MUA NGAY
                  </button>
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
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const product = ref(null);
const similarProducts = ref([]); 
const quantity = ref(1);

const getImageUrl = (url) => {
  if (!url) return 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image';
  if (url.startsWith('http')) return url;
  return `http://localhost:8080${url.startsWith('/') ? '' : '/'}${url}`;
};

const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/300x200/eeeeee/000000?text=No+Image';
};

const formatCurrency = (value) => {
  if (!value) return "0 VNĐ";
  return value.toLocaleString('vi-VN') + ' VNĐ';
};

const fetchProductDetail = async (id) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/product/${id}`);
    product.value = response.data;
    await fetchSimilarProducts();
  } catch (error) {
    console.error("Lỗi khi tải chi tiết sản phẩm:", error);
  }
};

const fetchSimilarProducts = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/product`, {
      params: { page: 0, size: 4, sortBy: 'createdAt', sortDir: 'desc' }
    });
    const allProducts = response.data.content || response.data;
    if (allProducts && product.value) {
      similarProducts.value = allProducts
        .filter(item => (item.productId || item.id) !== (product.value.productId || product.value.id))
        .slice(0, 4);
    }
  } catch (error) {
    console.error("Lỗi khi tải sản phẩm tương tự:", error);
  }
};

watch(() => route.params.id, (newId) => {
  if (newId) {
    fetchProductDetail(newId);
    window.scrollTo(0, 0);
  }
});

onMounted(() => {
  fetchProductDetail(route.params.id);
});
</script>

<style scoped>
/* Giữ nguyên style cũ + thêm style cho reviews */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.narrow-container { max-width: 1100px !important; margin: auto; }

.main-image-box { height: 480px; transition: 0.3s; }
.thumbnail-list .thumb-item { width: 70px; height: 70px; cursor: pointer; transition: 0.2s; }
.thumbnail-list .thumb-item:hover, .thumbnail-list .thumb-item.active { border-color: #00FF33 !important; }

.btn-neon { background-color: #00FF33; border: none; color: #000; }
.btn-neon:hover { background-color: #00e62e; color: #000; }

.product-card { background-color: transparent; }
.img-wrapper { background-color: #111111; height: 180px; }
.heart-icon { opacity: 0.5; transition: 0.2s; }
.heart-icon:hover { opacity: 1; color: #00FF33 !important; }
.info-wrapper { background-color: #F4F6F8; }
.line-clamp-1 { display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }
.btn-cart-icon { width: 32px; height: 32px; border-color: #333; }

/* Style đánh giá mới */
.avatar { font-size: 1.1rem; }
.star-bars .bg-success { background-color: #00FF33 !important; }
.review-item .text-neon { font-size: 1.1rem; }

footer { position: relative; width: 100%; }
</style>