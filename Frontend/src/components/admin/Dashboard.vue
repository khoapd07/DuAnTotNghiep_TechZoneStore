<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    

    <main class="flex-grow-1 p-3 overflow-auto">
      
      <header class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-3">
        <div>
          <h3 class="fw-black text-dark mb-1 fs-4">Tổng quan hệ thống</h3>
          <p class="text-muted m-0 fs-7">Chào mừng quay trở lại. Đây là những gì đang diễn ra hôm nay.</p>
        </div>
        <div class="d-flex gap-2">
          <div class="input-group bg-white border rounded-3 overflow-hidden" style="width: 220px;">
            <span class="input-group-text bg-transparent border-0 text-muted px-2"><i class="bi bi-search"></i></span>
            <input type="text" class="form-control border-0 shadow-none fs-7 px-1" placeholder="Tìm kiếm dữ liệu...">
          </div>
          <button class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3">
            <i class="bi bi-plus-lg"></i> Tạo Báo Cáo
          </button>
        </div>
      </header>

      <div class="row g-3 mb-3">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="icon-box bg-neon-light text-neon rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                <i class="bi bi-cash-stack fs-5"></i>
              </div>
              <span class="badge bg-success-subtle text-success fw-bold rounded-pill fs-8">+12.5% <i class="bi bi-arrow-up-right"></i></span>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Tổng doanh thu</p>
            <h4 class="fw-black text-dark m-0 mt-1">150.000.000₫</h4>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="icon-box bg-neon-light text-neon rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                <i class="bi bi-bag-check fs-5"></i>
              </div>
              <span class="badge bg-danger-subtle text-danger fw-bold rounded-pill fs-8">-8.2% <i class="bi bi-arrow-down-right"></i></span>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Đơn hàng mới</p>
            <h4 class="fw-black text-dark m-0 mt-1">42 Đơn</h4>
            <div class="progress mt-2" style="height: 4px;">
              <div class="progress-bar bg-neon" style="width: 65%;"></div>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="icon-box bg-warning-subtle text-warning rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                <i class="bi bi-exclamation-triangle fs-5"></i>
              </div>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Sản phẩm hết hàng</p>
            <h4 class="fw-black text-dark m-0 mt-1">15 Items</h4>
          </div>
        </div>
      </div>

      <div class="row g-3 mb-3">
        <div class="col-lg-7">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h6 class="fw-black text-dark m-0 fs-6">Xu hướng doanh thu</h6>
              <select class="form-select form-select-sm border-0 bg-light fw-bold text-muted shadow-none w-auto fs-8">
                <option>Hàng tuần</option>
                <option>Hàng tháng</option>
              </select>
            </div>
            <div class="chart-wrapper position-relative w-100" style="height: 180px;">
              <svg viewBox="0 0 500 150" class="w-100 h-100" preserveAspectRatio="none">
                <path d="M0,100 Q40,20 100,60 T200,80 T300,20 T400,120 T500,40 L500,150 L0,150 Z" fill="rgba(0, 255, 51, 0.1)" />
                <path d="M0,100 Q40,20 100,60 T200,80 T300,20 T400,120 T500,40" fill="none" stroke="#00FF33" stroke-width="4" />
              </svg>
              <div class="position-absolute w-100 h-100 top-0 d-flex flex-column justify-content-between" style="z-index: 0; pointer-events: none;">
                <div class="border-bottom opacity-10"></div>
                <div class="border-bottom opacity-10"></div>
                <div class="border-bottom opacity-10"></div>
                <div class="border-bottom opacity-10"></div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-lg-5">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h6 class="fw-black text-dark m-0 fs-6">Phân bổ đơn hàng</h6>
              <div class="d-flex align-items-center gap-1 fs-8 text-muted">
                <i class="bi bi-circle-fill text-neon" style="font-size: 6px;"></i> Hoàn thành
              </div>
            </div>
            <div class="d-flex align-items-end justify-content-between h-100 pt-2 pb-1 px-2 border-bottom opacity-75">
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 30%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 60%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 50%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 80%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 40%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 70%;"></div>
              <div class="bg-neon rounded-top-1" style="width: 12%; height: 95%;"></div>
            </div>
            <div class="d-flex justify-content-between mt-2 fs-9 text-muted px-2">
              <span>T2</span><span>T3</span><span>T4</span><span>T5</span><span>T6</span><span>T7</span><span>CN</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-3 mb-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h6 class="fw-black text-dark m-0 fs-6">Đơn hàng gần đây</h6>
          <a href="#" class="text-neon fw-bold fs-8 text-decoration-none">XEM TẤT CẢ</a>
        </div>
        
        <div class="table-responsive">
          <table class="table table-borderless align-middle mb-0">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="pb-2 fw-bold">Mã Đơn</th>
                <th scope="col" class="pb-2 fw-bold">Khách Hàng</th>
                <th scope="col" class="pb-2 fw-bold">Ngày Đặt</th>
                <th scope="col" class="pb-2 fw-bold">Tổng Tiền</th>
                <th scope="col" class="pb-2 fw-bold">Trạng Thái</th>
                <th scope="col" class="pb-2 fw-bold text-end">Thao Tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in recentOrders" :key="order.id" class="border-bottom-dashed">
                <td class="fw-bold fs-7 text-dark py-2">{{ order.id }}</td>
                <td class="py-2">
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-text bg-light text-secondary fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 28px; height: 28px; font-size: 0.7rem;">
                      {{ order.initials }}
                    </div>
                    <span class="fw-bold fs-7 text-dark">{{ order.customer }}</span>
                  </div>
                </td>
                <td class="text-muted fs-7 py-2">{{ order.date }}</td>
                <td class="fw-bold fs-7 text-dark py-2">{{ formatCurrency(order.total) }}</td>
                <td class="py-2">
                  <span class="badge fw-bold px-2 py-1 rounded-2 fs-8" :class="getStatusClass(order.status)">
                    {{ order.status }}
                  </span>
                </td>
                <td class="text-end py-2">
                  <button class="btn btn-link text-muted p-0 shadow-none"><i class="bi bi-three-dots-vertical"></i></button>
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

const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + '₫';
};

const getStatusClass = (status) => {
  switch(status) {
    case 'HOÀN THÀNH': return 'bg-neon text-dark';
    case 'ĐANG XỬ LÝ': return 'bg-dark text-white';
    case 'ĐANG GIAO': return 'bg-warning text-dark';
    case 'HỦY ĐƠN': return 'bg-danger text-white';
    default: return 'bg-secondary text-white';
  }
};

const recentOrders = ref([
  { id: '#TZ-8842', initials: 'TH', customer: 'Trần Hoàng Nam', date: '12/05/2024', total: 12500000, status: 'HOÀN THÀNH' },
  { id: '#TZ-8841', initials: 'LM', customer: 'Lê Minh Anh', date: '12/05/2024', total: 5200000, status: 'ĐANG XỬ LÝ' },
  { id: '#TZ-8840', initials: 'VP', customer: 'Vũ Phương Thảo', date: '11/05/2024', total: 2100000, status: 'ĐANG GIAO' },
  { id: '#TZ-8839', initials: 'NH', customer: 'Nguyễn Huy', date: '11/05/2024', total: 18000000, status: 'HOÀN THÀNH' }
]);
</script>

<style scoped>
/* Nạp font Inter chính chủ hỗ trợ tiếng Việt từ Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

/* Fonts & Utilities: Bổ sung font hệ thống làm backup chống lỗi */
.admin-layout { 
  font-family: 'Inter', system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; 
}
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.fs-9 { font-size: 0.65rem; }

/* Custom Colors */
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.15) !important; }

/* Nút bấm Neon */
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-1px); }

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

/* Table Style */
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
</style>