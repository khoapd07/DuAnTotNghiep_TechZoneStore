<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    

    <main class="flex-grow-1 p-3 overflow-auto position-relative">
      
      <header class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản lý đơn hàng</h3>
          <div class="d-flex align-items-center gap-2 text-muted fs-7">
            <i class="bi bi-search"></i> <span>Tìm kiếm mã đơn hàng, khách hàng...</span>
          </div>
        </div>
        <div class="d-flex gap-3 align-items-center">
          <div class="d-flex align-items-center gap-2">
            <div class="avatar bg-light text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center border" style="width: 35px; height: 35px;">
              A
            </div>
            <span class="fw-bold fs-7">Admin TechZone</span>
          </div>
        </div>
      </header>

      <div class="row g-3 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-primary">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Tổng đơn hàng</p>
            <h3 class="fw-black text-dark m-0 mt-1">1,250</h3>
            <span class="text-success fs-8 fw-bold mt-2 d-inline-block"><i class="bi bi-arrow-up-right"></i> 12.5% tháng này</span>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-warning">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Chờ xử lý</p>
            <h3 class="fw-black text-dark m-0 mt-1">45</h3>
            <span class="text-warning fs-8 fw-bold mt-2 d-inline-block">Cần xử lý ngay</span>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-info">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Đang giao</p>
            <h3 class="fw-black text-dark m-0 mt-1">120</h3>
            <span class="text-info fs-8 fw-bold mt-2 d-inline-block">Đang vận chuyển</span>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100 border-start border-4 border-success">
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Hoàn thành</p>
            <h3 class="fw-black text-dark m-0 mt-1">1,085</h3>
            <span class="text-success fs-8 fw-bold mt-2 d-inline-block">Tỷ lệ thành công cao</span>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-3 mb-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <div>
            <h6 class="fw-black text-dark m-0 fs-5">Danh sách đơn hàng</h6>
            <small class="text-muted fs-7">248 đơn hàng mới</small>
          </div>
          <div class="d-flex gap-2">
            <select class="form-select form-select-sm border bg-light fw-bold text-muted shadow-none fs-8" style="width: 150px;">
              <option>Tất cả trạng thái</option>
              <option>Chờ xử lý</option>
              <option>Đang giao</option>
              <option>Hoàn thành</option>
            </select>
            <button class="btn btn-light border fw-bold fs-8 text-dark d-flex align-items-center gap-2">
              <i class="bi bi-file-earmark-arrow-down"></i> Xuất File
            </button>
            <button class="btn btn-dark fw-bold fs-8 text-white d-flex align-items-center gap-2 rounded-3">
              <i class="bi bi-plus-lg"></i> Thêm đơn hàng
            </button>
          </div>
        </div>
        
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="border-bottom bg-light">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-3 fw-bold">Mã Đơn</th>
                <th scope="col" class="py-3 fw-bold">Khách Hàng</th>
                <th scope="col" class="py-3 fw-bold">Ngày Đặt</th>
                <th scope="col" class="py-3 fw-bold">Tổng Tiền</th>
                <th scope="col" class="py-3 fw-bold">Thanh Toán</th>
                <th scope="col" class="py-3 fw-bold">Trạng Thái</th>
                <th scope="col" class="py-3 fw-bold text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orderList" :key="order.id" class="border-bottom-dashed cursor-pointer">
                <td class="fw-bold fs-7 text-dark px-3">{{ order.id }}</td>
                <td>
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-text bg-secondary text-white fw-bold rounded-circle d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; font-size: 0.75rem;">
                      {{ order.initials }}
                    </div>
                    <span class="fw-bold fs-7 text-dark">{{ order.customer }}</span>
                  </div>
                </td>
                <td class="text-muted fs-7">{{ order.date }}</td>
                <td class="fw-bold fs-7 text-dark">{{ formatCurrency(order.total) }}</td>
                <td>
                  <span class="badge bg-light text-dark border fs-8 px-2 py-1">{{ order.payment }}</span>
                </td>
                <td>
                  <span class="badge fw-bold px-2 py-1 rounded-2 fs-8" :class="getStatusClass(order.status)">
                    {{ order.status }}
                  </span>
                </td>
                <td class="text-center">
                  <button 
                    class="btn btn-sm btn-outline-dark fs-8 fw-bold px-3 py-1 rounded-pill"
                    data-bs-toggle="offcanvas" 
                    data-bs-target="#orderDetailOffcanvas" 
                    @click="viewOrderDetail(order)">
                    Xem chi tiết
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-3 pt-2 px-2 border-top">
          <span class="text-muted fs-8">Hiển thị 10 trong 1,250 đơn hàng</span>
          <div class="d-flex gap-1">
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-left"></i></button>
            <button class="btn btn-sm btn-dark px-3 fw-bold">1</button>
            <button class="btn btn-sm btn-light border px-3 fw-bold">2</button>
            <button class="btn btn-sm btn-light border px-3 fw-bold">3</button>
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-right"></i></button>
          </div>
        </div>
      </div>

    </main>

    <div class="offcanvas offcanvas-end shadow" tabindex="-1" id="orderDetailOffcanvas" style="width: 400px; border-left: none;">
      <div class="offcanvas-header border-bottom bg-light">
        <h5 class="offcanvas-title fw-black fs-5">Đơn hàng {{ selectedOrder?.id }}</h5>
        <button type="button" class="btn-close shadow-none" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body p-0" v-if="selectedOrder">
        
        <div class="p-3 border-bottom bg-light-gray">
          <p class="fs-8 text-muted fw-bold mb-1">NGÀY ĐẶT: {{ selectedOrder.date }}</p>
          <label class="fs-8 fw-bold text-dark mb-2">TRẠNG THÁI XỬ LÝ</label>
          <div class="d-flex gap-2">
            <select class="form-select form-select-sm fw-bold border-dark shadow-none" v-model="selectedOrder.status">
              <option value="CHỜ XỬ LÝ">Chờ xử lý</option>
              <option value="ĐANG GIAO">Đang giao</option>
              <option value="HOÀN THÀNH">Hoàn thành</option>
            </select>
            <button class="btn btn-sm btn-dark fw-bold px-3" data-bs-dismiss="offcanvas">Lưu</button>
          </div>
        </div>

        <div class="p-3 border-bottom">
          <h6 class="fw-bold fs-6 mb-3">Thông tin khách hàng</h6>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">HỌ TÊN</small>
            <span class="fs-7 fw-bold text-dark">{{ selectedOrder.customer }}</span>
          </div>
          <div class="mb-2">
            <small class="text-muted d-block fs-8 fw-bold">ĐIỆN THOẠI</small>
            <span class="fs-7 fw-bold text-dark">0987 123 456</span>
          </div>
          <div>
            <small class="text-muted d-block fs-8 fw-bold">ĐỊA CHỈ NHẬN HÀNG</small>
            <span class="fs-7 text-dark">123 Đường ABC, Phường 10, Quận 1, TP. Hồ Chí Minh</span>
          </div>
        </div>

        <div class="p-3 border-bottom">
          <h6 class="fw-bold fs-6 mb-3">Sản phẩm (2)</h6>
          <div class="d-flex align-items-center gap-3 mb-3">
            <div class="bg-light rounded p-2 border" style="width: 50px; height: 50px;">
              <i class="bi bi-laptop fs-4 text-muted d-flex justify-content-center"></i>
            </div>
            <div class="flex-grow-1">
              <h6 class="m-0 fs-7 fw-bold">MacBook Pro 14 M2</h6>
              <span class="fs-8 text-muted">x1</span>
            </div>
            <span class="fw-bold fs-7">42,000,000₫</span>
          </div>
          <div class="d-flex align-items-center gap-3">
            <div class="bg-light rounded p-2 border" style="width: 50px; height: 50px;">
              <i class="bi bi-mouse3 fs-4 text-muted d-flex justify-content-center"></i>
            </div>
            <div class="flex-grow-1">
              <h6 class="m-0 fs-7 fw-bold">Magic Mouse 2 - Black</h6>
              <span class="fs-8 text-muted">x1</span>
            </div>
            <span class="fw-bold fs-7">3,000,000₫</span>
          </div>
        </div>

        <div class="p-3 bg-light mt-auto">
          <div class="d-flex justify-content-between mb-2 fs-7">
            <span class="text-muted">Tạm tính</span>
            <span class="fw-bold">45,000,000₫</span>
          </div>
          <div class="d-flex justify-content-between mb-3 fs-7">
            <span class="text-muted">Phí vận chuyển</span>
            <span class="fw-bold">0₫</span>
          </div>
          <div class="d-flex justify-content-between pt-2 border-top mb-3">
            <span class="fw-bold text-dark">Tổng cộng</span>
            <span class="fw-black text-danger fs-5">{{ formatCurrency(selectedOrder.total) }}</span>
          </div>
          <button class="btn btn-outline-dark w-100 fw-bold d-flex justify-content-center align-items-center gap-2">
            <i class="bi bi-printer"></i> In hóa đơn
          </button>
        </div>

      </div>
    </div>
    
  </div>
</template>

<script setup>
import { ref } from 'vue';

// State lưu đơn hàng đang được xem chi tiết
const selectedOrder = ref(null);

// Hàm hiển thị chi tiết đơn hàng lên Offcanvas
const viewOrderDetail = (order) => {
  selectedOrder.value = { ...order };
};

// Hàm format tiền tệ
const formatCurrency = (value) => {
  return value.toLocaleString('vi-VN') + '₫';
};

// Hàm cấp màu Badge chuẩn theo thiết kế
const getStatusClass = (status) => {
  switch(status) {
    case 'HOÀN THÀNH': return 'bg-success text-white';
    case 'CHỜ XỬ LÝ': return 'bg-warning text-dark';
    case 'ĐANG GIAO': return 'bg-info text-dark';
    case 'HỦY ĐƠN': return 'bg-danger text-white';
    default: return 'bg-secondary text-white';
  }
};

// Dữ liệu Mock mô phỏng chính xác theo hình ảnh Figma
const orderList = ref([
  { id: '#TZ-1001', initials: 'NA', customer: 'Nguyễn Văn An', date: '12/10/2023 14:30', total: 15500000, payment: 'COD', status: 'HOÀN THÀNH' },
  { id: '#TZ-1002', initials: 'TB', customer: 'Trần Thị Bích', date: '12/10/2023 10:15', total: 2300000, payment: 'CHUYỂN KHOẢN', status: 'CHỜ XỬ LÝ' },
  { id: '#TZ-1003', initials: 'LC', customer: 'Lê Văn Cường', date: '11/10/2023 18:20', total: 45000000, payment: 'CHUYỂN KHOẢN', status: 'ĐANG GIAO' },
  { id: '#TZ-1004', initials: 'MD', customer: 'Phạm Minh Duy', date: '11/10/2023 09:45', total: 8900000, payment: 'COD', status: 'HOÀN THÀNH' }
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
.bg-neon { background-color: #00FF33 !important; }

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

/* Bảng dữ liệu */
.table th { letter-spacing: 0.5px; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
.cursor-pointer { cursor: pointer; }

/* Tránh giật UI khi hover dòng bảng */
.table-hover tbody tr:hover td {
  background-color: #f8f9fa;
}
</style>