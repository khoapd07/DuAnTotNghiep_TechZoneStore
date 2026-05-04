<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Slide Show</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Slide Trang Chủ</h3>
        </div>
        <div class="d-flex align-items-center gap-3">
          <button @click="openAddModal" class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-plus-lg"></i> Thêm Slide Mới
          </button>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th class="py-3 px-4 fw-bold border-0" style="width: 15%">Hình Ảnh</th>
                <th class="py-3 fw-bold border-0" style="width: 25%">Tiêu Đề</th>
                <th class="py-3 fw-bold border-0" style="width: 15%">Thẻ Badge</th>
                <th class="py-3 fw-bold border-0 text-center" style="width: 10%">Thứ tự</th>
                <th class="py-3 fw-bold border-0 text-center" style="width: 15%">Trạng thái</th>
                <th class="py-3 fw-bold border-0 text-center" style="width: 20%">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="slide in slideShows" :key="slide.slideId" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <div class="bg-dark border rounded-3 d-flex align-items-center justify-content-center overflow-hidden" style="width: 120px; height: 60px;">
                    <img v-if="slide.imageUrl" :src="slide.imageUrl" class="img-fluid object-fit-cover w-100 h-100">
                    <i v-else class="bi bi-image text-muted fs-4"></i>
                  </div>
                </td>
                <td class="py-3">
                  <span class="fw-bold fs-7 text-dark d-block">{{ slide.title }}</span>
                  <span class="fs-8 fw-black" :style="{ color: slide.highlightClass }">{{ slide.titleHighlight }}</span>
                </td>
                <td class="py-3">
                  <span v-if="slide.badgeText" class="badge px-2 py-1 fs-9 text-dark" :style="{ backgroundColor: slide.badgeClass }">{{ slide.badgeText }}</span>
                  <span v-else class="text-muted fs-8">Không có</span>
                </td>
                <td class="py-3 text-center fw-bold">{{ slide.displayOrder }}</td>
                <td class="py-3 text-center">
                  <span v-if="slide.active" class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">Đang hiện</span>
                  <span v-else class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-2 py-1">Đã ẩn</span>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button @click="openEditModal(slide)" class="btn btn-link p-0 text-primary shadow-none"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button @click="confirmDelete(slide.slideId)" class="btn btn-link p-0 text-danger shadow-none"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
              <tr v-if="slideShows.length === 0"><td colspan="6" class="text-center py-4 text-muted fs-7">Chưa có Slide nào.</td></tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <div v-if="toast.show" class="position-fixed top-0 start-50 translate-middle-x mt-4 px-4 py-3 rounded-3 shadow-lg d-flex align-items-center gap-2" :class="toast.type === 'success' ? 'bg-dark text-white' : 'bg-danger text-white'" style="z-index: 9999; min-width: 300px; transition: all 0.3s;">
      <i class="bi fs-5" :class="toast.type === 'success' ? 'bi-check-circle-fill text-neon' : 'bi-exclamation-triangle-fill'"></i>
      <span class="fw-bold fs-7">{{ toast.message }}</span>
    </div>

    <div v-if="showDeleteModal" class="custom-modal-overlay d-flex justify-content-center align-items-center">
      <div class="custom-modal bg-white rounded-4 p-4 text-center shadow-lg">
        <div class="mb-3">
          <i class="bi bi-exclamation-triangle-fill text-danger" style="font-size: 3.5rem;"></i>
        </div>
        <h5 class="fw-bold mb-2">Xác nhận xóa?</h5>
        <p class="text-muted fs-8 mb-4">Hành động này không thể hoàn tác. Bạn chắc chắn muốn xóa slide này?</p>
        <div class="d-flex gap-2 justify-content-center">
          <button @click="showDeleteModal = false" class="btn btn-light border fs-8 fw-bold px-4 py-2 rounded-2">Hủy bỏ</button>
          <button @click="executeDelete" class="btn btn-danger fs-8 fw-bold px-4 py-2 rounded-2">Xác nhận xóa</button>
        </div>
      </div>
    </div>

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-6">{{ isEditing ? 'CẬP NHẬT SLIDE' : 'THÊM SLIDE MỚI' }}</h5>
            <button type="button" class="btn-close shadow-none" @click="showModal = false"></button>
          </div>
          <div class="modal-body p-4" style="max-height: 75vh; overflow-y: auto;">
            <div class="row g-3">
              
              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Hình ảnh Banner <span class="text-danger">*</span></label>
                <div class="d-flex gap-2 align-items-center">
                  <input type="file" @change="uploadImage" class="form-control fs-7" accept="image/*">
                </div>
                <span v-if="errors.imageUrl" class="text-danger fs-8 fw-bold mt-1 d-block"><i class="bi bi-exclamation-circle"></i> {{ errors.imageUrl }}</span>

                <div v-if="form.imageUrl" class="mt-2 bg-dark rounded-3 overflow-hidden text-center" style="height: 150px;">
                  <img :src="form.imageUrl" class="img-fluid h-100 object-fit-contain">
                </div>
              </div>

              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Tiêu đề chính (Dòng 1)</label>
                <input type="text" v-model="form.title" class="form-control fs-7" placeholder="VD: NÂNG TẦM TRẢI NGHIỆM">
              </div>
              
              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Chữ Nổi Bật (Highlight)</label>
                <input type="text" v-model="form.titleHighlight" class="form-control fs-7" placeholder="VD: ESPORTS">
              </div>

              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Thẻ Badge (Nhãn nhỏ)</label>
                <input type="text" v-model="form.badgeText" class="form-control fs-7" placeholder="VD: NEW ERA OF GAMING">
              </div>

              <div class="col-md-6">
                <div class="row">
                  <div class="col-6">
                    <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Màu nền Nhãn (Badge)</label>
                    <input type="color" v-model="form.badgeClass" class="form-control form-control-color w-100 p-1" title="Chọn màu nền">
                  </div>
                  <div class="col-6">
                    <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Màu chữ Nổi bật</label>
                    <input type="color" v-model="form.highlightClass" class="form-control form-control-color w-100 p-1" title="Chọn màu chữ">
                  </div>
                </div>
              </div>

              <div class="col-12">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Mô tả ngắn</label>
                <textarea v-model="form.description" class="form-control fs-7" rows="2" placeholder="Nhập mô tả nằm dưới tiêu đề..."></textarea>
              </div>

              <div class="col-md-6">
                <label class="fs-8 fw-bold text-muted text-uppercase mb-1">Thứ tự hiển thị (Số nhỏ xếp trước)</label>
                <input type="number" v-model="form.displayOrder" class="form-control fs-7">
              </div>

              <div class="col-md-6 d-flex align-items-center mt-4">
                <div class="form-check form-switch fs-6">
                  <input class="form-check-input" type="checkbox" v-model="form.active" id="activeSwitch">
                  <label class="form-check-label fw-bold" for="activeSwitch">Hiển thị lên Trang chủ</label>
                </div>
              </div>

            </div>
          </div>
          <div class="modal-footer border-top p-3">
            <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="showModal = false">Hủy</button>
            <button type="button" class="btn btn-neon fs-7 fw-bold px-4 text-dark" @click="saveSlide">{{ isEditing ? 'Cập Nhật' : 'Lưu' }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import api from '../../utils/axios';

const slideShows = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentId = ref(null);
const errors = reactive({});

// BIẾN MODAL XÓA
const showDeleteModal = ref(false);
const idToDelete = ref(null);

const toast = reactive({ show: false, message: '', type: 'success' });
const showToast = (message, type = 'success') => {
  toast.message = message; toast.type = type; toast.show = true;
  setTimeout(() => { toast.show = false; }, 3000);
};

const form = reactive({
  imageUrl: '', title: '', titleHighlight: '', badgeText: '',
  badgeClass: '#00FF33', highlightClass: '#00FF33', description: '', displayOrder: 1, active: true
});

const fetchSlides = async () => {
  try {
    const response = await api.get('/slideshows');
    slideShows.value = response.data;
  } catch (error) { console.error("Lỗi lấy slide:", error); }
};

const uploadImage = async (event) => {
  errors.imageUrl = ''; 
  const file = event.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await api.post('/upload', formData, { 
      headers: { 'Content-Type': 'multipart/form-data' } 
    });
    form.imageUrl = response.data; 
  } catch (error) {
    showToast("Upload ảnh thất bại!", "error");
  }
};

const openAddModal = () => {
  isEditing.value = false; currentId.value = null;
  Object.keys(errors).forEach(k => delete errors[k]);
  Object.assign(form, {
    imageUrl: '', title: '', titleHighlight: '', badgeText: '', 
    badgeClass: '#00FF33', highlightClass: '#00FF33', 
    description: '', displayOrder: slideShows.value.length + 1, active: true
  });
  showModal.value = true;
};

const openEditModal = (slide) => {
  isEditing.value = true; currentId.value = slide.slideId;
  Object.keys(errors).forEach(k => delete errors[k]);
  Object.assign(form, slide);
  if (!form.badgeClass || !form.badgeClass.startsWith('#')) form.badgeClass = '#00FF33';
  if (!form.highlightClass || !form.highlightClass.startsWith('#')) form.highlightClass = '#00FF33';
  showModal.value = true;
};

const saveSlide = async () => {
  Object.keys(errors).forEach(k => delete errors[k]);
  
  if (!form.imageUrl) {
    errors.imageUrl = "Vui lòng tải lên hoặc dán link Hình ảnh Banner!"; return;
  }

  const payload = { ...form };

  try {
    if (isEditing.value) {
      await api.put(`/slideshows/${currentId.value}`, payload);
      showToast("Cập nhật Slide thành công!");
    } else {
      await api.post('/slideshows', payload);
      showToast("Thêm Slide mới thành công!");
    }
    showModal.value = false;
    fetchSlides();
  } catch (error) {
    showToast("Lỗi lưu dữ liệu!", "error");
  }
};

// HÀM XÓA DÙNG MODAL
const confirmDelete = (id) => {
  idToDelete.value = id;
  showDeleteModal.value = true;
};

const executeDelete = async () => {
  try {
    await api.delete(`/slideshows/${idToDelete.value}`);
    fetchSlides();
    showDeleteModal.value = false;
    idToDelete.value = null;
    showToast("Xóa slide thành công!");
  } catch (error) {
    showToast("Lỗi khi xóa slide!", "error");
    showDeleteModal.value = false;
  }
};

onMounted(() => { fetchSlides(); });
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');
.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; transition: all 0.2s; }
.btn-neon:hover { background-color: #00e62e; }
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }

/* --- CSS MODAL --- */
.custom-modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  animation: fadeIn 0.2s ease-in-out;
}
.custom-modal {
  width: 90%;
  max-width: 400px;
  animation: slideDown 0.3s ease-out;
}
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideDown { from { transform: translateY(-20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
</style>