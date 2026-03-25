<template>
  <div v-if="blog" class="blog-detail-page bg-white pb-5 pt-3">
    <div class="container narrow-container">
      
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb fs-8 fw-medium mb-0">
          <li class="breadcrumb-item"><router-link to="/" class="text-muted text-decoration-none">Trang chủ</router-link></li>
          <li class="breadcrumb-item"><router-link to="/blog" class="text-muted text-decoration-none">Blog</router-link></li>
          <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">{{ blog.title }}</li>
        </ol>
      </nav>

      <div class="row g-5">
        <div class="col-lg-8">
          <article class="post-content">
            <div class="mb-4">
              <span class="badge bg-neon-light text-neon fw-bold mb-2">TIN CÔNG NGHỆ</span>
              <h1 class="fw-black display-6 mb-3 lh-sm text-uppercase">
                {{ blog.title }}
              </h1>
              <div class="d-flex align-items-center gap-3 text-muted fs-8">
                <span><i class="bi bi-person-circle me-1"></i> {{ blog.author }}</span>
                <span><i class="bi bi-calendar3 me-1"></i> {{ blog.date }}</span>
                <span><i class="bi bi-clock me-1"></i> {{ blog.read }}</span>
              </div>
            </div>

            <div class="main-post-image rounded-4 overflow-hidden mb-4 shadow-sm">
              <img :src="blog.image" class="w-100 object-fit-cover" style="max-height: 450px;" :alt="blog.title">
            </div>

            <div class="quote-box border-start border-neon border-4 ps-4 py-1 mb-4 bg-light-gray rounded-end-3">
              <p class="fst-italic text-dark m-0 fs-7 lh-lg">
                "{{ blog.quote }}"
              </p>
            </div>

            <div class="post-text fs-7 lh-lg text-secondary mb-5" style="white-space: pre-line;">
                {{ blog.content }}
            </div>

            <div class="d-flex flex-wrap gap-2 mt-5 pt-3 border-top">
              <span class="fs-8 fw-bold text-dark me-2">TAGS:</span>
              <button class="btn btn-sm btn-light fs-9 rounded-1 text-muted">TECHZONE</button>
              <button class="btn btn-sm btn-light fs-9 rounded-1 text-muted">HARDWARE</button>
              <button class="btn btn-sm btn-light fs-9 rounded-1 text-muted">GAMING</button>
            </div>
          </article>
        </div>

        <div class="col-lg-4">
          <div class="sticky-top" style="top: 20px;">
            <h6 class="fw-black text-uppercase border-start border-neon border-3 ps-3 mb-4">Bài viết khác</h6>
            <div class="related-sidebar d-flex flex-column gap-4">
              <div 
                v-for="item in relatedPosts" 
                :key="item.id" 
                class="related-item d-flex gap-3 align-items-center cursor-pointer"
                @click="$router.push(`/blog/${item.id}`)"
                style="cursor: pointer;"
              >
                <img :src="item.image" class="rounded-2 object-fit-cover" style="width: 80px; height: 60px;">
                <div>
                  <h6 class="fs-8 fw-bold mb-1 line-clamp-2 hover-neon">{{ item.title }}</h6>
                  <small class="text-muted fs-9">{{ item.date }}</small>
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
import { ref, onMounted, watch, computed } from "vue"
import { useRoute } from "vue-router"

const route = useRoute()
const blog = ref(null)

const blogs = [
  {
    id: 1,
    title: "Đánh giá chi tiết NVIDIA RTX 4090: Sức mạnh hủy diệt cho Game thủ",
    author: "Admin TechZone",
    date: "24 Tháng 5, 2024",
    read: "8 phút đọc",
    image: "https://images.unsplxash.com/photo-1695056721201-081467bd18de?q=80&w=2000",
    quote: "RTX 4090 là GPU mạnh nhất mà Nvidia từng sản xuất, mở ra kỷ nguyên chơi game 4K không giới hạn.",
    content: `RTX 4090 được xây dựng trên kiến trúc Ada Lovelace với 16.384 nhân CUDA và 24GB VRAM GDDR6X.

    1. Hiệu năng gaming vượt trội:
    Trong các bài thử nghiệm thực tế, RTX 4090 cho thấy sức mạnh gấp đôi so với thế hệ tiền nhiệm RTX 3090 ở một số tựa game nặng. Bạn có thể dễ dàng đạt 100+ FPS ở độ phân giải 4K với mọi thiết lập cao nhất.

    2. Công nghệ DLSS 3.0:
    Đây là "vũ khí bí mật" của NVIDIA. Bằng cách sử dụng AI để tạo ra các khung hình hoàn toàn mới, DLSS 3 giúp tăng hiệu năng lên tới 4 lần trong khi vẫn giữ được chất lượng hình ảnh sắc nét.`
  },
  {
    id: 2,
    title: "Sự kiện ra mắt CPU thế hệ mới của Intel",
    author: "Admin TechZone",
    date: "20 Tháng 5, 2024",
    read: "6 phút đọc",
    image: "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?q=80&w=2000",
    quote: "Intel tiếp tục đẩy mạnh cuộc đua CPU hiệu năng cao với kiến trúc Hybrid cải tiến.",
    content: `Dòng CPU mới mang lại hiệu năng đa nhân vượt trội, tối ưu hóa cho cả làm việc đồ họa nặng và chơi game.

    Với việc tăng số lượng nhân E-core, Intel giúp người dùng có trải nghiệm đa nhiệm mượt mà hơn. Bạn có thể vừa livestream vừa chơi game AAA mà không gặp tình trạng giật lag.`
  },
  {
    id: 3,
    title: "Trên tay bàn phím cơ Custom cực hot: Tuyệt phẩm cho góc Set-up",
    author: "Admin TechZone",
    date: "18 Tháng 5, 2024",
    read: "7 phút đọc",
    image: "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?q=80&w=2000",
    quote: "Bàn phím cơ Custom không chỉ là công cụ gõ, nó là một tác phẩm nghệ thuật trên bàn làm việc.",
    content: `Phong trào chơi bàn phím cơ Custom chưa bao giờ hạ nhiệt. Hôm nay TechZone sẽ trên tay mẫu bàn phím nhôm nguyên khối với cấu hình "End-game".

    1. Thiết kế và Cảm quan:
    Cầm trên tay chiếc phím nặng gần 2kg, bạn sẽ cảm nhận được sự chắc chắn tuyệt đối. Lớp sơn mịn màng cùng các đường vát CNC tinh tế giúp góc làm việc của bạn sang trọng hơn hẳn.

    2. Cấu hình bên trong:
    Switch Linear đã được lube sẵn, mang lại cảm giác gõ mượt như lướt trên bơ. Bộ Stab được cân chỉnh kỹ lưỡng giúp âm thanh gõ cực kỳ nịnh tai.`
  },
  {
    id: 4,
    title: "Cách tối ưu hóa Windows để chơi game mượt mà nhất 2024",
    author: "Admin TechZone",
    date: "15 Tháng 5, 2024",
    read: "10 phút đọc",
    image: "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=2000",
    quote: "Tối ưu hóa phần mềm là cách rẻ nhất để tăng FPS mà không cần tốn tiền nâng cấp linh kiện.",
    content: `Nhiều bạn sở hữu cấu hình mạnh nhưng chơi game vẫn bị drop FPS. Thủ phạm thường nằm ở việc Windows chưa được tối ưu đúng cách.

    - Bật Game Mode: Windows sẽ ưu tiên tài nguyên CPU/GPU cho game.
    - Chỉnh Power Plan sang Ultimate Performance.
    - Tắt các ứng dụng chạy ngầm không cần thiết để giải phóng RAM.`
  },
  {
    id: 5,
    title: "Đánh giá tai nghe Gaming không dây: Độ trễ gần như bằng không",
    author: "Admin TechZone",
    date: "12 Tháng 5, 2024",
    read: "6 phút đọc",
    image: "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?q=80&w=2000",
    quote: "Sự tự do của tai nghe không dây là trải nghiệm mà bạn không bao giờ muốn từ bỏ.",
    content: `Thời đại của dây nhợ lằng nhằng đã qua. Các mẫu tai nghe Wireless đời mới sử dụng kết nối 2.4GHz mang lại độ trễ cực thấp.

    Bạn hoàn toàn có thể nghe tiếng chân trong các tựa game bắn súng chuẩn xác như dùng dây. Thời lượng pin lên tới 30 giờ cũng giúp bạn chơi game cả tuần mới cần sạc một lần.`
  },
  {
    id: 6,
    title: "Thị trường linh kiện PC quý 2: Giá GPU tiếp tục giảm sâu",
    author: "Admin TechZone",
    date: "10 Tháng 5, 2024",
    read: "5 phút đọc",
    image: "https://images.unsplash.com/photo-1591488320449-011701bb6704?q=80&w=2000",
    quote: "Đây là thời điểm vàng để game thủ build cho mình một dàn máy mơ ước với chi phí hợp lý.",
    content: `Sau giai đoạn bão giá, thị trường linh kiện PC đang bước vào thời kỳ cực tốt cho người dùng.

    Sự ra mắt của dòng Super đã đẩy giá các dòng card cũ xuống mức cực thấp. Ngoài ra, giá SSD và RAM cũng đang ở mức rất dễ tiếp cận. Nếu bạn đang có ý định nâng cấp máy, hãy thực hiện ngay trong tháng này!`
  }
]

// Hàm tải chi tiết bài viết
const loadBlogDetail = () => {
  const blogId = Number(route.params.id)
  blog.value = blogs.find(b => b.id === blogId)
  window.scrollTo(0, 0) // Tự động cuộn lên đầu khi vào bài mới
}

// Lấy danh sách 3 bài viết khác để hiển thị ở sidebar
const relatedPosts = computed(() => {
  return blogs
    .filter(b => b.id !== blog.value?.id)
    .sort(() => 0.5 - Math.random()) // Trộn ngẫu nhiên
    .slice(0, 3)
})

onMounted(() => {
  loadBlogDetail()
})

watch(() => route.params.id, () => {
  loadBlogDetail()
})
</script>

<style scoped>
/* Code style cũ của bạn giữ nguyên, mình thêm vài class mới */
.hover-neon:hover { color: #00FF33 !important; transition: 0.3s; }
.cursor-pointer { cursor: pointer; }
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }
.border-neon { border-color: #00FF33 !important; }
.bg-light-gray { background-color: #F8F9FA; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
</style>