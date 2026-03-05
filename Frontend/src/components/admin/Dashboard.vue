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
              <div class="icon-box bg-neon-light text-neon rounded-3 d-flex justify-content-center align-items-center"
                style="width: 40px; height: 40px;">
                <i class="bi bi-cash-stack fs-5"></i>
              </div>
              <span v-if="stats.revenueGrowth" class="badge fw-bold rounded-pill fs-8"
                :class="stats.revenueGrowth >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'">
                {{ stats.revenueGrowth >= 0 ? '+' : '' }}{{ stats.revenueGrowth }}%
                <i :class="stats.revenueGrowth >= 0 ? 'bi bi-arrow-up-right' : 'bi bi-arrow-down-right'"></i>
              </span>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Tổng doanh thu</p>
            <h4 class="fw-black text-dark m-0 mt-1">{{ formatCurrency(stats.totalRevenue) }}</h4>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="icon-box bg-neon-light text-neon rounded-3 d-flex justify-content-center align-items-center"
                style="width: 40px; height: 40px;">
                <i class="bi bi-bag-check fs-5"></i>
              </div>
              <span v-if="stats.orderGrowth" class="badge fw-bold rounded-pill fs-8"
                :class="stats.orderGrowth >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'">
                {{ stats.orderGrowth >= 0 ? '+' : '' }}{{ stats.orderGrowth }}%
                <i :class="stats.orderGrowth >= 0 ? 'bi bi-arrow-up-right' : 'bi bi-arrow-down-right'"></i>
              </span>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Đơn hàng mới (7 ngày)</p>
            <h4 class="fw-black text-dark m-0 mt-1">{{ stats.newOrdersCount }} Đơn</h4>
            <div class="progress mt-2" style="height: 4px;">
              <div class="progress-bar bg-neon" style="width: 100%;"></div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div
                class="icon-box bg-warning-subtle text-warning rounded-3 d-flex justify-content-center align-items-center"
                style="width: 40px; height: 40px;">
                <i class="bi bi-exclamation-triangle fs-5"></i>
              </div>
            </div>
            <p class="text-muted fs-8 fw-bold text-uppercase m-0">Sản phẩm hết hàng</p>
            <h4 class="fw-black text-dark m-0 mt-1">{{ stats.outOfStockCount }} Sản Phẩm</h4>
          </div>
        </div>
      </div>

      <div class="row g-3 mb-3">
        <div class="col-lg-7">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h6 class="fw-black text-dark m-0 fs-6">Xu hướng doanh thu (7 ngày)</h6>
            </div>

            <div class="chart-wrapper w-100 h-100 d-flex flex-column" style="min-height: 180px;">
              <div
                class="d-flex align-items-end justify-content-between flex-grow-1 pt-2 pb-1 px-2 border-bottom opacity-75">
                <div v-for="(item, index) in chartData" :key="'rev-bar-' + index" class="bg-neon rounded-top-1"
                  :style="{ width: '8%', height: getRevenueBarHeight(item.revenue) + '%' }"
                  :title="formatCurrency(item.revenue)">
                </div>
              </div>
              <div class="d-flex justify-content-between mt-2 fs-9 text-muted px-2">
                <span v-for="(item, index) in chartData" :key="'rev-label-' + index">{{ item.label }}</span>
              </div>
            </div>

          </div>
        </div>

        <div class="col-lg-5">
          <div class="card border-0 shadow-sm rounded-4 p-3 h-100">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h6 class="fw-black text-dark m-0 fs-6">Phân bổ đơn hàng</h6>
              <div class="d-flex align-items-center gap-1 fs-8 text-muted">
              </div>
            </div>

            <div class="d-flex align-items-end justify-content-between h-100 pt-2 pb-1 px-2 border-bottom opacity-75">
              <div v-for="(item, index) in chartData" :key="'bar-' + index" class="bg-neon rounded-top-1"
                :style="{ width: '12%', height: getBarHeight(item.orderCount) + '%' }"
                :title="`${item.orderCount} đơn`">
              </div>
            </div>

            <div class="d-flex justify-content-between mt-2 fs-9 text-muted px-2">
              <span v-for="(item, index) in chartData" :key="'label-' + index">{{ item.label }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-3 mb-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h6 class="fw-black text-dark m-0 fs-6">Đơn hàng gần đây</h6>
          <router-link to="/admin/orders" class="text-neon fw-bold fs-8 text-decoration-none">XEM TẤT CẢ</router-link>
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
              <tr v-if="loading">
                <td colspan="6" class="text-center py-4 text-muted fs-7">Đang tải dữ liệu...</td>
              </tr>
              <tr v-else-if="recentOrders.length === 0">
                <td colspan="6" class="text-center py-4 text-muted fs-7">Chưa có dữ liệu đơn hàng...</td>
              </tr>
              <tr v-for="order in recentOrders" :key="order.id" class="border-bottom-dashed">
                <td class="fw-bold fs-7 text-dark py-2">{{ order.id }}</td>
                <td class="py-2">
                  <div class="d-flex align-items-center gap-2">
                    <div
                      class="avatar-text bg-light text-secondary fw-bold rounded-circle d-flex align-items-center justify-content-center"
                      style="width: 28px; height: 28px; font-size: 0.7rem;">
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
                  <button class="btn btn-link text-muted p-0 shadow-none"><i
                      class="bi bi-three-dots-vertical"></i></button>
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
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

// 1. Biến chứa dữ liệu
const stats = ref({});
const recentOrders = ref([]);
const chartData = ref([]);
const loading = ref(true);

// 2. Format tiền tệ
const formatCurrency = (value) => {
  return (value || 0).toLocaleString('vi-VN') + '₫';
};

// 3. Màu sắc trạng thái
const getStatusClass = (status) => {
  if (!status) return 'bg-secondary text-white';
  const s = status.toUpperCase();
  if (s.includes('HOÀN THÀNH')) return 'bg-neon text-dark';
  if (s.includes('XỬ LÝ') || s.includes('PENDING')) return 'bg-dark text-white';
  if (s.includes('GIAO') || s.includes('SHIPPING')) return 'bg-warning text-dark';
  if (s.includes('HỦY')) return 'bg-danger text-white';
  return 'bg-secondary text-white';
};

// Lấy chữ cái đầu
const getInitials = (name) => {
  if (!name || name === "Khách vãng lai") return 'KH';
  return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
};

// --- LOGIC CHO BIỂU ĐỒ CỘT (Phân bổ đơn hàng) ---
// Tính chiều cao % của từng cột dựa vào ngày có số đơn cao nhất
const getBarHeight = (count) => {
  if (!chartData.value.length || count === 0) return 5; // Chiều cao tối thiểu 5% để không bị tàng hình
  const maxOrders = Math.max(...chartData.value.map(d => d.orderCount));
  return maxOrders === 0 ? 5 : (count / maxOrders) * 95; // Tối đa 95% chiều cao div
};

// --- LOGIC CHO BIỂU ĐỒ CỘT DOANH THU ---
const getRevenueBarHeight = (revenue) => {
  if (!chartData.value.length || revenue === 0) return 5; // Tối thiểu 5% để không bị tàng hình
  const maxRev = Math.max(...chartData.value.map(d => d.revenue));
  return maxRev === 0 ? 5 : (revenue / maxRev) * 95; // Tính tỷ lệ phần trăm (Max là 95% thẻ div)
};

// --- LOGIC CHO BIỂU ĐỒ ĐƯỜNG (Xu hướng doanh thu) ---
// Tự động tính toán đường line SVG d nối các điểm lại với nhau
const linePath = computed(() => {
  if (chartData.value.length === 0) return '';
  const maxRev = Math.max(...chartData.value.map(d => d.revenue), 1);

  return chartData.value.map((d, index) => {
    const x = (index / 6) * 500; // Trải dài 7 điểm trên chiều rộng 500px
    const y = 140 - ((d.revenue / maxRev) * 120); // Đỉnh cao nhất là Y=20, thấp nhất là Y=140
    return `${index === 0 ? 'M' : 'L'}${x},${y}`;
  }).join(' ');
});

// Tự động vẽ mảng màu xanh nhạt phía dưới đường line
const areaPath = computed(() => {
  if (!linePath.value) return '';
  return `${linePath.value} L500,150 L0,150 Z`; // Bo vòng xuống đáy biểu đồ
});


// Lấy dữ liệu từ Backend
const fetchDashboardData = async () => {
  try {
    loading.value = true;

    // Gọi đồng thời 3 API
    const [statsRes, ordersRes, chartRes] = await Promise.all([
      axios.get('http://localhost:8080/api/admin/dashboard/stats'),
      axios.get('http://localhost:8080/api/admin/dashboard/recent-orders'),
      axios.get('http://localhost:8080/api/admin/dashboard/chart-data')
    ]);

    stats.value = statsRes.data;
    chartData.value = chartRes.data; // Gán data vẽ biểu đồ

    recentOrders.value = ordersRes.data.map(order => ({
      id: order.orderCode,
      initials: getInitials(order.customerName),
      customer: order.customerName,
      date: new Date(order.orderDate).toLocaleDateString('vi-VN'),
      total: order.finalAmount,
      status: order.statusName
    }));

  } catch (error) {
    console.error("Lỗi khi kết nối Backend:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchDashboardData();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout {
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
}

.bg-light-gray {
  background-color: #F4F6F8;
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

.fs-9 {
  font-size: 0.65rem;
}

.text-neon {
  color: #00FF33 !important;
}

.bg-neon {
  background-color: #00FF33 !important;
}

.bg-neon-light {
  background-color: rgba(0, 255, 51, 0.15) !important;
}

.btn-neon {
  background-color: #00FF33;
  border: none;
  transition: 0.2s;
}

.btn-neon:hover {
  background-color: #00cc29;
  transform: translateY(-1px);
}

.table th {
  letter-spacing: 0.5px;
}

.border-bottom-dashed {
  border-bottom: 1px dashed #EAEAEA;
}

.border-bottom-dashed:last-child {
  border-bottom: none;
}

/* Thêm animation cho cột mượt mà */
.bg-neon.rounded-top-1 {
  transition: height 0.8s cubic-bezier(0.4, 0, 0.2, 1);
}
</style>