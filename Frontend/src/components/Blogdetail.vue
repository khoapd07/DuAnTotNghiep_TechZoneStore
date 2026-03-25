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
import { ref, onMounted, watch } from "vue"
import { useRoute, useRouter } from "vue-router"
import axios from "axios" // Thêm thư viện axios

const route = useRoute()
const router = useRouter()
const blog = ref(null)
const relatedPosts = ref([])

// Hàm tải chi tiết bài viết từ Backend API
const loadBlogDetail = async () => {
  const blogId = Number(route.params.id)
  try {
    // Gọi API chi tiết blog mà bạn đã định nghĩa ở Backend
    const res = await axios.get(`http://localhost:8080/api/blogs/${blogId}`)
    blog.value = res.data
    window.scrollTo(0, 0)
    
    // Sau khi tải xong chi tiết, gọi API để load Sidebar (bài viết liên quan)
    loadRelatedPosts(blogId)
  } catch (error) {
    console.error("Lỗi khi lấy chi tiết blog:", error)
    // Nếu không tìm thấy, có thể redirect về trang danh sách blog
    // router.push("/blog")
  }
}

// Lấy danh sách bài viết khác để hiển thị ở sidebar
const loadRelatedPosts = async (currentBlogId) => {
  try {
    const res = await axios.get("http://localhost:8080/api/blogs")
    
    const activePosts = (Array.isArray(res.data) ? res.data : []).filter(
      // Chú ý: dùng đúng tên biến ID (thường là blogId theo như bên Blog.vue)
      (item) => item.active === true && item.blogId !== currentBlogId 
    )

    // Trộn ngẫu nhiên và cắt lấy 3 bài
    relatedPosts.value = activePosts
      .sort(() => 0.5 - Math.random())
      .slice(0, 3)
  } catch (error) {
    console.error("Lỗi khi tải bài viết liên quan:", error)
  }
}

// Hàm format ngày tháng nếu database trả về timestamp (như bên Blog.vue)
const formatDate = (date) => {
  if (!date) return "";
  return new Date(date).toLocaleDateString("vi-VN");
};

onMounted(() => {
  loadBlogDetail()
})

// Lắng nghe khi URL thay đổi (click vào bài liên quan ở sidebar) để load lại
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