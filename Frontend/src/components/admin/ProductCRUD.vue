<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    <aside class="sidebar bg-white border-end d-flex flex-column sticky-top" style="width: 220px; height: 100vh;">
      <div class="p-3 border-bottom">
        <h4 class="fw-black text-dark m-0 d-flex align-items-center gap-1 fs-5">
          TECHZONE <span class="text-neon fs-6">//</span>
        </h4>
        <small class="text-muted fw-bold" style="font-size: 0.65rem; letter-spacing: 1px;">QUẢN TRỊ</small>
      </div>

      <div class="p-2 flex-grow-1 overflow-auto">
        <ul class="nav flex-column gap-1 custom-nav">
          <li class="nav-item">
            <router-link to="/admin" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-grid-1x2"></i> <span class="fw-bold fs-7">Tổng quan</span>
            </router-link>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link active rounded-3 d-flex align-items-center gap-3">
              <i class="bi bi-box-seam-fill"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </a>
          </li>
          <li class="nav-item">
            <router-link to="/admin/orders" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-receipt"></i> <span class="fw-bold fs-7">Đơn hàng</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/users" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-people"></i> <span class="fw-bold fs-7">Khách hàng</span>
            </router-link>
          </li>
          <li class="nav-item mt-2">
            <router-link to="/admin/report" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-bar-chart-line"></i> <span class="fw-bold fs-7">Báo cáo</span>
            </router-link>
          </li>
        </ul>
      </div>

      <div class="p-3 border-top">
        <a href="#" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark mb-3 px-2">
          <i class="bi bi-gear"></i> <span class="fw-bold fs-7">Cài đặt</span>
        </a>
        <div class="d-flex align-items-center gap-2 px-1">
          <div class="avatar bg-neon text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 35px; height: 35px; font-size: 0.85rem;">
            A
          </div>
          <div>
            <h6 class="m-0 fw-bold fs-7">Admin TechZone</h6>
            <small class="text-muted" style="font-size: 0.65rem;">admin@techzone.vn</small>
          </div>
        </div>
      </div>
    </aside>

    <main class="flex-grow-1 p-4 overflow-auto">
      
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Sản phẩm</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Sản Phẩm</h3>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="input-group bg-white border rounded-3 overflow-hidden shadow-sm" style="width: 250px;">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input type="text" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm sản phẩm...">
          </div>
          <button class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-plus-lg"></i> Thêm Sản Phẩm Mới
          </button>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Tổng sản phẩm</p>
              <h2 class="fw-black text-dark m-0">1,248</h2>
              <span class="text-success fs-8 fw-bold mt-2 d-inline-block"><i class="bi bi-arrow-up-right"></i> +5% so với tháng trước</span>
            </div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-box-seam fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Sắp hết hàng</p>
              <h2 class="fw-black text-dark m-0">18</h2>
              <span class="text-warning fs-8 fw-bold mt-2 d-inline-block">Cần nhập thêm trong tuần này</span>
            </div>
            <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-exclamation-triangle fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1 text-uppercase">Danh mục</p>
              <h2 class="fw-black text-dark m-0">12</h2>
              <span class="text-muted fs-8 fw-bold mt-2 d-inline-block">Danh mục đang hoạt động</span>
            </div>
            <div class="bg-info-subtle text-info rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-tags fs-4"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="p-3 border-bottom d-flex justify-content-between align-items-center bg-white rounded-top-4">
          <div class="d-flex gap-2">
            <select class="form-select form-select-sm border bg-light fw-bold text-dark shadow-none fs-7" style="width: 180px;">
              <option>Tất cả danh mục</option>
              <option>Bàn phím cơ</option>
              <option>Chuột Gaming</option>
              <option>Màn hình</option>
            </select>
          </div>
          <div class="d-flex align-items-center gap-2 fs-7 fw-bold text-muted">
            <span>Sắp xếp:</span>
            <select class="form-select form-select-sm border-0 bg-transparent text-dark shadow-none fw-bold p-0 w-auto">
              <option>Mới nhất</option>
              <option>Giá tăng dần</option>
              <option>Giá giảm dần</option>
            </select>
          </div>
        </div>
        
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-4 fw-bold border-0">Hình Ảnh</th>
                <th scope="col" class="py-3 fw-bold border-0">Tên Sản Phẩm</th>
                <th scope="col" class="py-3 fw-bold border-0">Danh Mục</th>
                <th scope="col" class="py-3 fw-bold border-0">Giá Bán</th>
                <th scope="col" class="py-3 fw-bold border-0">Tồn Kho</th>
                <th scope="col" class="py-3 fw-bold border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in productList" :key="product.id" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <div class="bg-light border rounded-3 d-flex align-items-center justify-content-center overflow-hidden" style="width: 60px; height: 60px;">
                    <i class="bi bi-image text-muted fs-4"></i>
                  </div>
                </td>
                <td class="py-3">
                  <div class="d-flex flex-column">
                    <span class="fw-bold fs-7 text-dark">{{ product.name }}</span>
                    <span class="fs-8 text-muted">SKU: {{ product.sku }}</span>
                  </div>
                </td>
                <td class="py-3">
                  <span class="badge bg-light text-dark border rounded-pill px-3 py-1 fs-8 fw-bold">
                    {{ product.category }}
                  </span>
                </td>
                <td class="fw-bold text-dark fs-7 py-3">
                  {{ formatCurrency(product.price) }}
                </td>
                <td class="py-3">
                  <span class="fs-7 fw-bold" :class="product.stock <= 15 ? 'text-danger' : 'text-dark'">
                    {{ product.stock }}
                  </span>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button class="btn btn-link p-0 text-primary shadow-none" title="Chỉnh sửa"><i class="bi bi-pencil-square fs-6"></i></button>
                    <button class="btn btn-link p-0 text-danger shadow-none" title="Xóa"><i class="bi bi-trash fs-6"></i></button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top bg-white rounded-bottom-4">
          <span class="text-muted fs-8">Hiển thị 1-3 của 1,248 sản phẩm</span>
          <div class="d-flex gap-1">
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-left"></i></button>
            <button class="btn btn-sm btn-neon text-dark px-3 fw-bold">1</button>
            <button class="btn btn-sm btn-white border px-3 fw-bold">2</button>
            <button class="btn btn-sm btn-white border px-3 fw-bold">3</button>
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-right"></i></button>
          </div>
        </div>
      </div>
      
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Hàm format tiền tệ
const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + '₫';
};

// Dữ liệu mock chính xác từ bản thiết kế Figma/PDF
const productList = ref([
  { 
    id: 1, 
    name: 'Razer BlackWidow V4 Pro', 
    sku: 'AZ-BW-AP-77', 
    category: 'Bàn phím cơ', 
    price: 4890000, 
    stock: 65 
  },
  { 
    id: 2, 
    name: 'Logitech G Pro X Superlight', 
    sku: 'LG-GPX-WH-02', 
    category: 'Chuột Gaming', 
    price: 3250000, 
    stock: 15 
  },
  { 
    id: 3, 
    name: 'Samsung Odyssey G7 27"', 
    sku: 'SS-OD-G7-27', 
    category: 'Màn hình', 
    price: 12490000, 
    stock: 8 
  }
]);
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { 
  font-family: 'Inter', system-ui, sans-serif; 
}
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; border: none; }
.btn-neon:hover { background-color: #00cc29; }

/* Sidebar Nav */
.custom-nav .nav-link {
  padding: 0.6rem 1rem;
  transition: all 0.2s ease;
}
.custom-nav .nav-link.active {
  background-color: rgba(0, 255, 51, 0.1);
  color: #00FF33 !important;
  border-left: 4px solid #00FF33;
  border-top-left-radius: 0 !important;
  border-bottom-left-radius: 0 !important;
}
.custom-nav .nav-link:hover:not(.active) {
  background-color: #f8f9fa;
}

/* Utilities cho Table */
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
</style>