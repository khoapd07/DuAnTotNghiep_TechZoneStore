<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Blog</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Bài Viết</h3>
        </div>

        <div class="d-flex align-items-center gap-3">
          <div class="input-group bg-white border rounded-3 overflow-hidden shadow-sm" style="width: 280px;">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input type="text" v-model="searchQuery" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm bài viết..." />
          </div>
          <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-plus-lg"></i> Thêm Bài Viết Mới
          </button>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng số bài viết</p><h2 class="fw-black text-dark m-0">{{ stats.total }}</h2></div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-file-earmark-text fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Đang hiển thị</p><h2 class="fw-black text-dark m-0">{{ stats.active }}</h2></div>
            <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-eye fs-4"></i></div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div><p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng lượt xem</p><h2 class="fw-black text-dark m-0">{{ stats.views }}</h2></div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;"><i class="bi bi-bar-chart fs-4"></i></div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0">Ảnh</th>
                <th class="py-3 fw-bold border-0">Tiêu đề</th>
                <th class="py-3 fw-bold border-0">Tóm tắt</th>
                <th class="py-3 fw-bold border-0">Lượt xem</th>
                <th class="py-3 fw-bold border-0">Trạng thái</th>
                <th class="py-3 fw-bold border-0">Ngày tạo</th>
                <th class="py-3 fw-bold border-0 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="blog in paginatedBlogs" :key="blog.blogId" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <div class="bg-light border rounded-3 d-flex align-items-center justify-content-center overflow-hidden" style="width: 60px; height: 60px;">
                    <img v-if="blog.thumbnailUrl" :src="blog.thumbnailUrl" class="img-fluid object-fit-cover w-100 h-100" />
                    <i v-else class="bi bi-image text-muted fs-4"></i>
                  </div>
                </td>
                <td class="py-3"><span class="fw-bold fs-7 text-dark">{{ blog.title }}</span></td>
                <td class="py-3 text-muted fs-7 summary-cell">{{ blog.summary || "(Không có tóm tắt)" }}</td>
                <td class="py-3"><span class="fs-7 fw-bold text-danger">{{ blog.views || 0 }}</span></td>
                <td class="py-3">
                  <span class="badge rounded-pill px-3 py-1 fs-8 fw-bold" :class="blog.active ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'">
                    {{ blog.active ? "Hiển thị" : "Ẩn" }}
                  </span>
                </td>
                <td class="py-3 fs-7">{{ formatDate(blog.createdAt) }}</td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(blog)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="deleteBlog(blog.blogId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
              <tr v-if="paginatedBlogs.length === 0"><td colspan="7" class="text-center py-4 text-muted fs-7">Không tìm thấy bài viết nào.</td></tr>
            </tbody>
          </table>
        </div>
        <div class="d-flex justify-content-between align-items-center p-3 border-top bg-white rounded-bottom-4">
          <span class="text-muted fs-8 fw-medium">Hiển thị {{ paginatedBlogs.length }} / {{ filteredBlogs.length }} bài viết</span>
          <nav v-if="totalPages > 1">
            <ul class="pagination pagination-sm mb-0 gap-1">
              <li class="page-item" :class="{ disabled: currentPage === 1 }"><button class="page-link text-dark shadow-none rounded-2" @click="currentPage--">Trước</button></li>
              <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
                <button class="page-link shadow-none rounded-2" :class="currentPage === page ? 'bg-neon text-dark border-neon fw-bold' : 'text-dark'" @click="currentPage = page">{{ page }}</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }"><button class="page-link text-dark shadow-none rounded-2" @click="currentPage++">Sau</button></li>
            </ul>
          </nav>
        </div>
      </div>
    </main>

    <div v-if="toast.show" class="position-fixed top-0 start-50 translate-middle-x mt-4 px-4 py-3 rounded-3 shadow-lg d-flex align-items-center gap-2" :class="toast.type === 'success' ? 'bg-dark text-white' : 'bg-danger text-white'" style="z-index: 9999; min-width: 300px; transition: all 0.3s;">
      <i class="bi fs-5" :class="toast.type === 'success' ? 'bi-check-circle-fill text-neon' : 'bi-exclamation-triangle-fill'"></i>
      <span class="fw-bold fs-7">{{ toast.message }}</span>
    </div>

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT BÀI VIẾT' : 'THÊM BÀI VIẾT MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>

          <div class="modal-body p-4">
            <div class="row g-3">
              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tiêu đề <span class="text-danger">*</span></label>
                <input type="text" v-model="form.title" class="form-control fs-7" :class="{'is-invalid': errors.title}" @input="errors.title = ''" placeholder="Nhập tiêu đề..." />
                <span v-if="errors.title" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.title }}</span>
              </div>

              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Hình ảnh đại diện <span class="text-danger">*</span></label>
                <div class="d-flex gap-2 align-items-center">
                  <input type="file" @change="uploadImage" class="form-control fs-7" accept="image/*" />
                </div>
                <span v-if="errors.thumbnailUrl" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.thumbnailUrl }}</span>
                <div v-if="form.thumbnailUrl" class="mt-2 bg-dark rounded-3 overflow-hidden text-center" style="height: 150px;">
                  <img :src="form.thumbnailUrl" class="img-fluid h-100 object-fit-contain" />
                </div>
              </div>

              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Nội dung <span class="text-danger">*</span></label>
                <textarea v-model="form.content" class="form-control fs-7" :class="{'is-invalid': errors.content}" @input="errors.content = ''" rows="6" placeholder="Nhập nội dung..."></textarea>
                <span v-if="errors.content" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.content }}</span>
              </div>

              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tóm tắt</label>
                <textarea v-model="form.summary" class="form-control fs-7" rows="2"></textarea>
              </div>

              <div class="col-md-4">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Author ID</label>
                <input type="number" v-model.number="form.authorId" class="form-control fs-7" placeholder="Ví dụ: 1" />
              </div>

              <div class="col-md-4">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Lượt xem</label>
                <input type="number" v-model.number="form.views" class="form-control fs-7" min="0" />
              </div>

              <div class="col-md-4 d-flex align-items-end">
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" id="activeCheck" v-model="form.active" />
                  <label class="form-check-label fs-7 fw-bold" for="activeCheck">Hiển thị bài viết</label>
                </div>
              </div>
            </div>
          </div>

          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" :disabled="saving" @click="saveBlog">
              {{ saving ? "Đang lưu..." : (isEditing ? "Cập Nhật" : "Thêm Mới") }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive, watch } from "vue";
// Xóa import axios từ thư viện gốc, thay bằng api instance của bạn
import api from "../../utils/axios";

// Thay đổi API_BLOG thành dạng relative path
const API_BLOG = "/blogs";

const blogList = ref([]);
const searchQuery = ref("");
const currentPage = ref(1);
const itemsPerPage = 10;

const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);
const saving = ref(false);

const errors = reactive({});
const stats = reactive({ total: 0, active: 0, views: 0 });

const toast = reactive({ show: false, message: '', type: 'success' });
const showToast = (message, type = 'success') => {
  toast.message = message; toast.type = type; toast.show = true;
  setTimeout(() => { toast.show = false; }, 3000);
};

const form = reactive({ title: "", summary: "", content: "", thumbnailUrl: "", authorId: null, views: 0, active: true });

// Hàm getAuthHeader() đã bị loại bỏ vì interceptor xử lý việc gắn Token tự động.

const uploadImage = async (event) => {
  errors.thumbnailUrl = '';
  const file = event.target.files[0];
  if (!file) return;
  const formData = new FormData(); formData.append('file', file);
  try {
    // Thay axios thành api và cấu hình Header cho Upload
    const response = await api.post('/upload', formData, { 
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    form.thumbnailUrl = response.data; 
  } catch (error) {
    showToast("Upload ảnh thất bại!", "error");
  }
};

const normalizeBlog = (b) => ({
  blogId: b.blogId ?? b.id, title: b.title ?? "", summary: b.summary ?? "", content: b.content ?? "",
  thumbnailUrl: b.thumbnailUrl ?? "", authorId: b.authorId ?? null, views: Number(b.views ?? 0),
  active: b.active ?? false, createdAt: b.createdAt ?? null, updatedAt: b.updatedAt ?? null,
});

const calcStats = () => {
  stats.total = blogList.value.length;
  stats.active = blogList.value.filter((b) => b.active).length;
  stats.views = blogList.value.reduce((sum, b) => sum + Number(b.views || 0), 0);
};

const fetchBlogs = async () => {
  try {
    // Thay axios thành api, loại bỏ header thủ công
    const response = await api.get(API_BLOG);
    const data = response.data;
    const arr = Array.isArray(data) ? data : (data.content || data.data || []);
    blogList.value = arr.map(normalizeBlog);
    calcStats();
  } catch (error) { console.error("Lỗi tải blog:", error); blogList.value = []; calcStats(); }
};

const filteredBlogs = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  if (!q) return [...blogList.value];
  return blogList.value.filter((b) => b.title.toLowerCase().includes(q) || b.summary.toLowerCase().includes(q) || b.content.toLowerCase().includes(q));
});

const totalPages = computed(() => Math.max(1, Math.ceil(filteredBlogs.value.length / itemsPerPage)));
const paginatedBlogs = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredBlogs.value.slice(start, start + itemsPerPage);
});

watch(searchQuery, () => { currentPage.value = 1; });

const resetForm = () => {
  Object.keys(errors).forEach(k => delete errors[k]);
  Object.assign(form, { title: "", summary: "", content: "", thumbnailUrl: "", authorId: null, views: 0, active: true });
};

const openAddModal = () => { isEditing.value = false; currentId.value = null; resetForm(); showModal.value = true; };

const openEditModal = (blog) => {
  isEditing.value = true; currentId.value = blog.blogId; Object.keys(errors).forEach(k => delete errors[k]);
  Object.assign(form, { title: blog.title, summary: blog.summary, content: blog.content, thumbnailUrl: blog.thumbnailUrl, authorId: blog.authorId, views: blog.views ?? 0, active: !!blog.active });
  showModal.value = true;
};

const validateForm = () => {
  Object.keys(errors).forEach(k => delete errors[k]);
  let isValid = true;
  if (!form.title?.trim()) { errors.title = "Tiêu đề không được để trống."; isValid = false; }
  if (!form.content?.trim()) { errors.content = "Nội dung không được để trống."; isValid = false; }
  if (!form.thumbnailUrl?.trim()) { errors.thumbnailUrl = "Hình ảnh đại diện không được để trống."; isValid = false; }
  return isValid;
};

const saveBlog = async () => {
  if (!validateForm()) return;

  try {
    saving.value = true;
    
    const payload = {
      title: form.title.trim(), summary: form.summary?.trim() || "", content: form.content.trim(),
      thumbnailUrl: form.thumbnailUrl.trim(), authorId: form.authorId ? Number(form.authorId) : null,
      views: Number(form.views || 0), active: !!form.active,
    };

    if (isEditing.value) {
      // Thay axios thành api
      await api.put(`${API_BLOG}/${currentId.value}`, payload);
      showToast("Cập nhật bài viết thành công!");
    } else {
      // Thay axios thành api
      await api.post(API_BLOG, payload);
      showToast("Thêm bài viết thành công!");
    }

    showModal.value = false;
    await fetchBlogs();
  } catch (error) {
    showToast(error?.response?.data?.message || "Không thể lưu bài viết. Kiểm tra quyền/backend.", "error");
  } finally {
    saving.value = false;
  }
};

const deleteBlog = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
    try {
      // Thay axios thành api, loại bỏ header thủ công
      await api.delete(`${API_BLOG}/${id}`);
      await fetchBlogs();
      showToast("Xóa bài viết thành công!");
    } catch (error) {
      showToast(error?.response?.data?.message || "Lỗi khi xóa bài viết!", "error");
    }
  }
};

const formatDate = (date) => date ? new Date(date).toLocaleDateString("vi-VN") : "";
onMounted(() => { fetchBlogs(); });
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap");
.admin-layout { font-family: "Inter", system-ui, sans-serif; }
.bg-light-gray { background-color: #f4f6f8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.bg-neon { background-color: #00ff33 !important; }
.border-neon { border-color: #00ff33 !important; }
.btn-neon { background-color: #00ff33; border: none; }
.btn-neon:hover { background-color: #00e62e; }
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #eaeaea; }
.border-bottom-dashed:last-child { border-bottom: none; }
.table-hover tbody tr:hover td { background-color: #f8f9fa; }
.summary-cell { max-width: 420px; }
.page-link { border: 1px solid #dee2e6; color: #333; }
.page-link:hover { background-color: #e9ecef; }
</style>