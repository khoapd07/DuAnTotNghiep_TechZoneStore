<template>
  <div class="blog-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">

      <div class="d-flex align-items-center gap-2 mb-4">
        <div class="title-line bg-neon"></div>
        <h4 class="fw-black m-0 text-dark">Bài viết nổi bật</h4>
      </div>

      <div v-if="heroPost" class="hero-post-card rounded-4 overflow-hidden position-relative mb-5 shadow-sm">
        <img :src="heroPost.thumbnailUrl" class="w-100 h-100 object-fit-cover absolute-img">

        <div class="hero-overlay position-absolute top-0 start-0 w-100 h-100"></div>

        <div class="position-relative z-1 d-flex flex-column justify-content-end h-100 p-4 p-md-5 col-md-8 col-lg-7">
          <span class="badge bg-neon text-dark fw-bold fs-8 px-2 py-1 align-self-start mb-3">
            BLOG
          </span>

          <h2 class="display-6 fw-black text-white mb-3 lh-sm">
            {{ heroPost.title }}
          </h2>

          <p class="text-light opacity-75 mb-4 fs-7 line-clamp-2">
            {{ heroPost.summary }}
          </p>

          <router-link
              :to="`/blog/${heroPost.blogId}`"
              class="btn btn-neon fw-bold px-4 py-2 fs-7 rounded-2"
          >
            ĐỌC BÀI VIẾT
            <i class="bi bi-arrow-right ms-1"></i>
          </router-link>
        </div>
      </div>

      <div class="row g-4 mb-5">

        <div
            class="col-md-6 col-lg-4"
            v-for="post in posts"
            :key="post.blogId"
        >
<div
  class="card border-0 h-100 bg-white rounded-3 shadow-sm custom-card-hover overflow-hidden d-flex flex-column"
  @click="goToBlog(post.blogId)"
  style="cursor:pointer"
>
            <div class="position-relative">

              <img
                  :src="post.thumbnailUrl"
                  class="card-img-top object-fit-cover"
                  style="height: 200px;"
              >

            </div>

            <div class="card-body p-4 d-flex flex-column flex-grow-1">

              <div class="d-flex align-items-center text-muted mb-2 fs-8">
                <i class="bi bi-calendar3 me-2"></i>
                {{ formatDate(post.createdAt) }}
              </div>

              <h6 class="fw-bold mb-2 fs-6 lh-base line-clamp-2">
                {{ post.title }}
              </h6>

              <p class="text-muted fs-8 mb-4 line-clamp-3">
                {{ post.summary }}
              </p>

              <div class="mt-auto">

                <router-link
                    :to="`/blog/${post.blogId}`"
                    class="text-neon text-decoration-none fw-bold fs-8 d-inline-flex align-items-center gap-1 hover-arrow"
                >
                  Xem chi tiết
                  <i class="bi bi-chevron-right" style="font-size: 0.7rem;"></i>
                </router-link>

              </div>

            </div>

          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
// Xóa import axios từ thư viện gốc, thay bằng Instance (thực thể) api của bạn
import api from '../utils/axios'; 

const router = useRouter();

const posts = ref([]);
const heroPost = ref(null);

const goToBlog = (id) => {
  router.push(`/blog/${id}`);
};

const loadBlogs = async () => {
  try {
    // Thay đổi axios.get thành api.get và xóa bỏ Base URL (địa chỉ URL cơ sở)
    const res = await api.get("/blogs");

    // ✅ CHỈ HIỂN THỊ BÀI active = true
    const activePosts = (Array.isArray(res.data) ? res.data : []).filter(
      (item) => item.active === true
    );

    posts.value = activePosts;
    heroPost.value = activePosts.length > 0 ? activePosts[0] : null;
  } catch (error) {
    console.error("Lỗi load blog:", error);
  }
};

const formatDate = (date) => {
  if (!date) return "";
  return new Date(date).toLocaleDateString("vi-VN");
};

onMounted(() => {
  loadBlogs();
});
</script>

<style scoped>

.blog-page {
  font-family: 'Inter', sans-serif;
}

.bg-light-gray {
  background-color: #F8F9FA;
}

.fw-black {
  font-weight: 900;
}

.fs-7 {
  font-size: 0.85rem;
}

.fs-8 {
  font-size: 0.75rem;
}

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

.text-neon {
  color: #00FF33 !important;
}

.bg-neon {
  background-color: #00FF33 !important;
}

.narrow-container {
  max-width: 1000px !important;
  margin-left: auto;
  margin-right: auto;
}

.title-line {
  width: 24px;
  height: 4px;
  border-radius: 2px;
}

.hero-post-card {
  height: 400px;
}


.absolute-img {
  position: absolute;
  top: 0;
  left: 0;
}

.hero-overlay {
  background: linear-gradient(
      90deg,
      rgba(0,0,0,0.95) 0%,
      rgba(0,0,0,0.7) 40%,
      rgba(0,0,0,0.1) 100%
  );
}

.btn-neon {
  background-color: #00FF33;
  color: #000;
  border: none;
}

.btn-neon:hover {
  background-color: #00cc29;
}

.custom-card-hover {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.custom-card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.08) !important;
}

.hover-arrow i {
  transition: transform 0.2s ease;
}

.hover-arrow:hover i {
  transform: translateX(4px);
}

</style>