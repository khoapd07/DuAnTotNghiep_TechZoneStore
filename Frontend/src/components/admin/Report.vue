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
            <a href="#" class="nav-link active rounded-3 d-flex align-items-center gap-3">
              <i class="bi bi-grid-1x2-fill"></i> <span class="fw-bold fs-7">Tổng quan</span>
            </a>
          </li>
          <li class="nav-item">
            <router-link to="/admin/products" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-box-seam"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </router-link>
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
            <a href="#" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark">
              <i class="bi bi-bar-chart-line"></i> <span class="fw-bold fs-7">Báo cáo</span>
            </a>
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
          <h3 class="fw-black text-dark mb-1 fs-4">Báo cáo & Thống kê</h3>
          <p class="text-muted m-0 fs-7">Phân tích chuyên sâu về tình hình kinh doanh hệ thống TechZone.</p>
        </div>
        <div class="d-flex align-items-center gap-3">
          <select class="form-select border bg-white fw-bold text-dark shadow-sm" style="width: 140px;">
            <option>Tháng này</option>
            <option>Tháng trước</option>
            <option>Năm nay</option>
          </select>
          <button class="btn btn-neon fw-bold text-dark rounded-3 d-flex align-items-center gap-2 px-3 shadow-sm">
            <i class="bi bi-download"></i> Xuất báo cáo
          </button>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
            <div class="d-flex justify-content-between align-items-start mb-3">
              <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                <i class="bi bi-cash-stack fs-5"></i>
              </div>
              <span class="text-success fs-8 fw-bold">+12.5%</span>
            </div>
            <p class="text-muted fs-8 fw-bold mb-1">Tổng Doanh Thu</p>
            <h4 class="fw-black text-dark m-0">2.450.000.000₫</h4>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
            <div class="d-flex justify-content-between align-items-start mb-3">
              <div class="bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                <i class="bi bi-wallet2 fs-5"></i>
              </div>
              <span class="text-success fs-8 fw-bold">+8.2%</span>
            </div>
            <p class="text-muted fs-8 fw-bold mb-1">Lợi Nhuận</p>
            <h4 class="fw-black text-dark m-0">850.000.000₫</h4>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
            <div class="d-flex justify-content-between align-items-start mb-3">
              <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                <i class="bi bi-bag-check fs-5"></i>
              </div>
              <span class="text-success fs-8 fw-bold">+5.4%</span>
            </div>
            <p class="text-muted fs-8 fw-bold mb-1">Số Đơn Hàng</p>
            <h4 class="fw-black text-dark m-0">1,240</h4>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
            <div class="d-flex justify-content-between align-items-start mb-3">
              <div class="bg-purple-subtle text-purple rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                <i class="bi bi-cursor fs-5"></i>
              </div>
              <span class="text-danger fs-8 fw-bold">-0.5%</span>
            </div>
            <p class="text-muted fs-8 fw-bold mb-1">Tỷ Lệ Chuyển Đổi</p>
            <h4 class="fw-black text-dark m-0">3.2%</h4>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h6 class="fw-black text-dark m-0 fs-5">Xu hướng doanh thu theo thời gian</h6>
          <button class="btn btn-link text-muted p-0 shadow-none"><i class="bi bi-three-dots"></i></button>
        </div>
        <div class="chart-wrapper position-relative w-100" style="height: 250px;">
          <svg viewBox="0 0 1000 200" class="w-100 h-100" preserveAspectRatio="none">
            <path d="M0,150 C200,80 400,180 600,120 C800,60 950,20 1000,10 L1000,200 L0,200 Z" fill="rgba(0, 255, 51, 0.1)" />
            <path d="M0,150 C200,80 400,180 600,120 C800,60 950,20 1000,10" fill="none" stroke="#00FF33" stroke-width="6" stroke-linecap="round" />
          </svg>
          <div class="d-flex justify-content-between text-muted fs-8 fw-bold mt-2 px-1 text-uppercase" style="letter-spacing: 1px;">
            <span>Tuần 01</span>
            <span>Tuần 02</span>
            <span>Tuần 03</span>
            <span>Tuần 04</span>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h6 class="fw-black text-dark m-0 fs-5">Top 5 Sản phẩm bán chạy nhất</h6>
          <a href="#" class="text-success fw-bold fs-7 text-decoration-none">Xem tất cả</a>
        </div>
        
        <div class="table-responsive">
          <table class="table align-middle mb-0 border-0">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="pb-3 fw-bold border-0">Sản Phẩm</th>
                <th scope="col" class="pb-3 fw-bold border-0">Số Lượng Đã Bán</th>
                <th scope="col" class="pb-3 fw-bold border-0">Doanh Thu</th>
                <th scope="col" class="pb-3 fw-bold border-0">Trạng Thái Kho</th>
                <th scope="col" class="pb-3 border-0 text-end"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in topProducts" :key="product.id" class="border-bottom-dashed">
                <td class="py-3">
                  <div class="d-flex align-items-center gap-3">
                    <div class="bg-light border rounded-3 d-flex justify-content-center align-items-center" style="width: 45px; height: 45px;">
                      <i class="bi bi-image text-muted fs-5"></i>
                    </div>
                    <span class="fw-bold fs-7 text-dark">{{ product.name }}</span>
                  </div>
                </td>
                <td class="py-3 text-dark fw-medium fs-7">{{ product.sold }}</td>
                <td class="py-3 fw-bold fs-7 text-dark">{{ formatCurrency(product.revenue) }}</td>
                <td class="py-3">
                  <span class="badge rounded-pill px-3 py-1 fs-8 fw-bold" :class="getStockClass(product.stockStatus)">
                    {{ product.stockStatus }}
                  </span>
                </td>
                <td class="py-3 text-end">
                  <button class="btn btn-link text-muted p-0 shadow-none"><i class="bi bi-eye fs-5"></i></button>
                </td>
              </tr>
            </tbody>
          </table>
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

// Hàm định dạng màu sắc cho Trạng thái kho
const getStockClass = (status) => {
  switch(status) {
    case 'Còn hàng': return 'bg-success-subtle text-success';
    case 'Sắp hết': return 'bg-warning-subtle text-warning';
    case 'Hết hàng': return 'bg-danger-subtle text-danger';
    default: return 'bg-light text-dark';
  }
};

// Dữ liệu mock chính xác từ bản thiết kế Báo Cáo
const topProducts = ref([
  { id: 1, name: 'Bàn phím cơ Razer BlackWidow V4', sold: 452, revenue: 1580000000, stockStatus: 'Còn hàng' },
  { id: 2, name: 'Chuột Logitech G Pro X Superlight', sold: 385, revenue: 847000000, stockStatus: 'Sắp hết' },
  { id: 3, name: 'VGA ASUS ROG Strix RTX 4080', sold: 124, revenue: 4216000000, stockStatus: 'Còn hàng' },
  { id: 4, name: 'Màn hình Samsung Odyssey G7', sold: 92, revenue: 1288000000, stockStatus: 'Hết hàng' },
  { id: 5, name: 'Tay cầm DualSense Edge Wireless', sold: 240, revenue: 580000000, stockStatus: 'Còn hàng' }
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

/* Thêm màu Tím (Purple) cho thẻ Tỷ lệ chuyển đổi */
.text-purple { color: #8A2BE2 !important; }
.bg-purple-subtle { background-color: rgba(138, 43, 226, 0.1) !important; }

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