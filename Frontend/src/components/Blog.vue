<template>
  <div class="blog-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">
      
      <div class="d-flex align-items-center gap-2 mb-4">
        <div class="title-line bg-neon"></div>
        <h4 class="fw-black m-0 text-dark">Bài viết nổi bật</h4>
      </div>

      <div class="hero-post-card rounded-4 overflow-hidden position-relative mb-5 shadow-sm">
        <img :src="heroPost.img" class="w-100 h-100 object-fit-cover absolute-img" alt="Hero Post">
        <div class="hero-overlay position-absolute top-0 start-0 w-100 h-100"></div>
        
        <div class="position-relative z-1 d-flex flex-column justify-content-end h-100 p-4 p-md-5 col-md-8 col-lg-7">
          <span class="badge bg-neon text-dark fw-bold fs-8 px-2 py-1 align-self-start mb-3">{{ heroPost.badge }}</span>
          <h2 class="display-6 fw-black text-white mb-3 lh-sm">{{ heroPost.title }}</h2>
          <p class="text-light opacity-75 mb-4 fs-7 line-clamp-2">
            {{ heroPost.desc }}
          </p>
          <div>
            <button class="btn btn-neon fw-bold px-4 py-2 fs-7 rounded-2">
              ĐỌC BÀI VIẾT <i class="bi bi-arrow-right ms-1"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="d-flex flex-wrap gap-2 mb-4">
        <button 
          v-for="(cat, index) in categories" 
          :key="index"
          class="btn rounded-pill fw-bold fs-8 px-4 py-2"
          :class="activeCategory === cat ? 'btn-neon text-dark' : 'bg-white text-dark border-0 shadow-sm'"
          @click="activeCategory = cat"
        >
          {{ cat }}
        </button>
      </div>

      <div class="row g-4 mb-5">
        <div class="col-md-6 col-lg-4" v-for="post in posts" :key="post.id">
          <div class="card border-0 h-100 bg-white rounded-3 shadow-sm custom-card-hover overflow-hidden d-flex flex-column">
            
            <div class="position-relative">
              <span class="badge bg-neon text-dark position-absolute top-0 start-0 m-3 z-1 fw-bold fs-9 px-2 py-1">{{ post.badge }}</span>
              <img :src="post.img" class="card-img-top object-fit-cover" style="height: 200px;" :alt="post.title">
            </div>

            <div class="card-body p-4 d-flex flex-column flex-grow-1">
              <div class="d-flex align-items-center text-muted mb-2 fs-8">
                <i class="bi bi-calendar3 me-2"></i> {{ post.date }}
              </div>
              <h6 class="fw-bold mb-2 fs-6 lh-base line-clamp-2" :title="post.title">
                {{ post.title }}
              </h6>
              <p class="text-muted fs-8 mb-4 line-clamp-3">
                {{ post.desc }}
              </p>
              
              <div class="mt-auto">
                <a href="#" class="text-neon text-decoration-none fw-bold fs-8 d-inline-flex align-items-center gap-1 hover-arrow">
                  Xem chi tiết <i class="bi bi-chevron-right" style="font-size: 0.7rem;"></i>
                </a>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div class="text-center">
        <button class="btn btn-outline-neon fw-bold px-4 py-2 fs-8 rounded-2">
          XEM THÊM BÀI VIẾT <i class="bi bi-chevron-down ms-1"></i>
        </button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Danh mục bộ lọc
const categories = ref(['Tất cả', 'Review', 'Tin tức', 'Hướng dẫn', 'Cấu hình PC']);
const activeCategory = ref('Tất cả');

// Dữ liệu bài viết nổi bật (Hero)
const heroPost = ref({
  badge: 'ĐÁNH GIÁ CHI TIẾT',
  title: 'Đánh giá chi tiết RTX 4090: Sức mạnh hủy diệt cho Game thủ',
  desc: 'Khám phá hiệu năng đỉnh cao và công nghệ mới nhất từ Nvidia dành cho cộng đồng game thủ chuyên nghiệp. Liệu mức giá có xứng đáng với trải nghiệm vượt trội này?',
  img: 'https://images.unsplash.com/photo-1695056721201-081467bd18de?q=80&w=2070&auto=format&fit=crop' // Thay bằng ảnh card màn hình của bạn
});

// Dữ liệu các bài viết lưới (Grid)
const posts = ref([
  {
    id: 1,
    badge: 'TIN TỨC',
    date: '20/05/2024',
    title: 'Sự kiện ra mắt CPU thế hệ mới của Intel: Những cải tiến đáng chú ý',
    desc: 'Cùng TechZone điểm qua những cải tiến vượt bậc trên dòng vi xử lý mới nhất với kiến trúc hybrid đột phá, mang lại hiệu suất chưa từng có.',
    img: 'https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?w=500'
  },
  {
    id: 2,
    badge: 'REVIEW',
    date: '18/05/2024',
    title: 'Trên tay bàn phím cơ Custom cực hot: Tuyệt phẩm cho góc Set-up',
    desc: 'Trải nghiệm gõ phím cực đã với bộ switch cao cấp và thiết kế độc bản. Đánh giá chi tiết mẫu bàn phím cơ đang làm mưa làm gió cộng đồng.',
    img: 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=500'
  },
  {
    id: 3,
    badge: 'HƯỚNG DẪN',
    date: '15/05/2024',
    title: 'Cách tối ưu hóa Windows để chơi game mượt mà nhất 2024',
    desc: 'Những mẹo nhỏ nhưng cực kỳ hiệu quả giúp tăng FPS, giảm độ trễ và giải phóng tài nguyên hệ thống cho chiếc PC của bạn chỉ với vài thao tác.',
    img: 'https://images.unsplash.com/photo-1587831990711-23ca6441447b?w=500'
  },
  {
    id: 4,
    badge: 'REVIEW',
    date: '12/05/2024',
    title: 'Đánh giá tai nghe Gaming không dây: Độ trễ gần như bằng không',
    desc: 'Thử nghiệm thực tế khả năng truyền tải âm thanh và thời lượng pin của dòng tai nghe gaming cao cấp mới nhất hiện nay.',
    img: 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=500'
  },
  {
    id: 5,
    badge: 'TIN TỨC',
    date: '10/05/2024',
    title: 'Thị trường linh kiện PC quý 2: Giá GPU tiếp tục giảm sâu',
    desc: 'Tin vui cho cộng đồng build PC khi giá card đồ họa đang có xu hướng giảm mạnh, tạo cơ hội sở hữu cấu hình mạnh với chi phí hợp lý.',
    img: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500'
  },
  {
    id: 6,
    badge: 'HƯỚNG DẪN',
    date: '07/05/2024',
    title: 'Những lưu ý quan trọng khi chọn màn hình chơi game FPS',
    desc: 'Panel IPS hay TN? Tần số quét hay độ phân giải? Mọi câu hỏi của bạn về màn hình gaming sẽ được giải đáp chi tiết trong bài viết này.',
    img: 'https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?w=500'
  }
]);
</script>

<style scoped>
/* Typography & Reset */
.blog-page {
  font-family: 'Inter', sans-serif;
}
.bg-light-gray { background-color: #F8F9FA; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }

/* Giới hạn số dòng hiển thị */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}

/* Biến màu chủ đạo */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }

/* Custom Container (Bóp nhỏ layout) */
.narrow-container {
  max-width: 1000px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Tiêu đề mục (Đường line xanh lá) */
.title-line {
  width: 24px;
  height: 4px;
  border-radius: 2px;
}

/* ------ HERO POST ------ */
.hero-post-card {
  height: 400px;
}
.absolute-img {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}
.hero-overlay {
  background: linear-gradient(90deg, rgba(0,0,0,0.95) 0%, rgba(0,0,0,0.7) 40%, rgba(0,0,0,0.1) 100%);
  z-index: 1;
}

/* Các nút Neon */
.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
  transition: all 0.2s ease;
}
.btn-neon:hover {
  background-color: #00cc29;
}

/* Nút outline xanh neon (Xem thêm) */
.btn-outline-neon {
  background-color: transparent;
  color: #00FF33;
  border: 1px solid #00FF33;
  transition: all 0.2s ease;
}
.btn-outline-neon:hover {
  background-color: rgba(0, 255, 51, 0.1);
  color: #00FF33;
}

/* Hiệu ứng hover Card */
.custom-card-hover {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.custom-card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.08) !important;
}

/* Hiệu ứng mũi tên link Xem chi tiết */
.hover-arrow i {
  transition: transform 0.2s ease;
}
.hover-arrow:hover i {
  transform: translateX(4px);
}
</style>