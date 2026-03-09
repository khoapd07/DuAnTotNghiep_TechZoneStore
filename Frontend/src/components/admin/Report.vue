<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    <main class="flex-grow-1 p-4 overflow-auto">
      
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 class="fw-black text-dark mb-1 fs-4">Báo cáo & Thống kê</h3>
          <p class="text-muted m-0 fs-7">Phân tích chuyên sâu về tình hình kinh doanh hệ thống TechZone.</p>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="d-flex align-items-center bg-white border rounded-3 px-2 shadow-sm py-1">
            <span class="text-muted fs-8 fw-bold me-2">Từ:</span>
            <input type="date" v-model="startDateInput" 
                   class="form-control border-0 fw-bold text-dark shadow-none bg-transparent p-0" 
                   style="width: 130px; cursor: pointer;">
          </div>
          <div class="d-flex align-items-center bg-white border rounded-3 px-2 shadow-sm py-1">
            <span class="text-muted fs-8 fw-bold me-2">Đến:</span>
            <input type="date" v-model="endDateInput" 
                   class="form-control border-0 fw-bold text-dark shadow-none bg-transparent p-0" 
                   style="width: 130px; cursor: pointer;">
          </div>
          
          <button @click="applyFilter" class="btn btn-dark fw-bold rounded-3 d-flex align-items-center gap-2 px-3 shadow-sm">
            <i class="bi bi-funnel"></i> Lọc dữ liệu
          </button>
        </div>
      </div>

      <div v-if="activeTab === 1">
        <div class="row g-4 mb-4">
          <div class="col-md-4">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100 card-hover" @click="goToTab2" style="cursor: pointer;" title="Click để xem chi tiết doanh thu">
              <div class="d-flex justify-content-between align-items-start mb-3">
                <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                  <i class="bi bi-cash-stack fs-5"></i>
                </div>
                <span class="text-success fs-8 fw-bold">Xem chi tiết <i class="bi bi-arrow-right"></i></span>
              </div>
              <p class="text-muted fs-8 fw-bold mb-1">Tổng Doanh Thu</p>
              <h4 class="fw-black text-dark m-0">{{ formatCurrency(generalStats.totalRevenue) }}</h4>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100 card-hover" @click="goToTab6" style="cursor: pointer;" title="Click để xem chi tiết đơn hàng">
              <div class="d-flex justify-content-between align-items-start mb-3">
                <div class="bg-warning-subtle text-warning rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                  <i class="bi bi-bag-check fs-5"></i>
                </div>
                <span class="text-warning fs-8 fw-bold">Xem chi tiết <i class="bi bi-arrow-right"></i></span>
              </div>
              <p class="text-muted fs-8 fw-bold mb-1">Số Đơn Hàng Thành Công</p>
              <h4 class="fw-black text-dark m-0">{{ generalStats.totalOrders }} đơn</h4>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
              <div class="d-flex justify-content-between align-items-start mb-3">
                <div class="bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                  <i class="bi bi-wallet2 fs-5"></i>
                </div>
              </div>
              <p class="text-muted fs-8 fw-bold mb-1">Lợi Nhuận (Chưa active)</p>
              <h4 class="fw-black text-dark m-0">0₫</h4>
            </div>
          </div>
        </div>

        <div class="row g-4 mb-4">
          <div class="col-md-6">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h6 class="fw-black text-dark m-0 fs-5">Top 5 SP bán chạy nhất</h6>
                <button @click="goToAllProducts" class="btn btn-link text-success fw-bold p-0 text-decoration-none fs-7 shadow-none">Xem thêm</button>
              </div>
              <div class="table-responsive">
                <table class="table align-middle mb-0 border-0">
                  <thead class="border-bottom">
                    <tr class="text-muted fs-8 text-uppercase">
                      <th scope="col" class="pb-3 fw-bold border-0">Sản Phẩm</th>
                      <th scope="col" class="pb-3 fw-bold border-0 text-end">Đã Bán</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="topProducts.length === 0">
                      <td colspan="2" class="text-center py-4 text-muted">Chưa có dữ liệu</td>
                    </tr>
                    <tr v-for="(product, index) in topProducts" :key="index" class="border-bottom-dashed">
                      <td class="py-3">
                        <div class="d-flex align-items-center gap-3">
                          <div class="bg-light border rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                            <span class="fw-bold text-muted">#{{ index + 1 }}</span>
                          </div>
                          <span class="fw-bold fs-7 text-dark">{{ product.productName }}</span>
                        </div>
                      </td>
                      <td class="py-3 text-end text-success fw-bold fs-7">{{ product.totalSold }} cái</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h6 class="fw-black text-dark m-0 fs-5">Top 5 KH mua nhiều nhất</h6>
                <button @click="goToAllCustomers" class="btn btn-link text-primary fw-bold p-0 text-decoration-none fs-7 shadow-none">Xem thêm</button>
              </div>
              <div class="table-responsive">
                <table class="table align-middle mb-0 border-0">
                  <thead class="border-bottom">
                    <tr class="text-muted fs-8 text-uppercase">
                      <th scope="col" class="pb-3 fw-bold border-0">Khách Hàng</th>
                      <th scope="col" class="pb-3 fw-bold border-0 text-end">Tổng Chi Tiêu</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="topCustomers.length === 0">
                      <td colspan="2" class="text-center py-4 text-muted">Chưa có dữ liệu</td>
                    </tr>
                    <tr v-for="(customer, index) in topCustomers" :key="index" class="border-bottom-dashed">
                      <td class="py-3">
                        <div class="d-flex align-items-center gap-3">
                          <div class="bg-primary-subtle text-primary border border-primary-subtle rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                            <i class="bi bi-person-fill fs-5"></i>
                          </div>
                          <span class="fw-bold fs-7 text-dark">{{ customer.customerName }}</span>
                        </div>
                      </td>
                      <td class="py-3 text-end fw-bold fs-7 text-dark">{{ formatCurrency(customer.totalSpent) }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h6 class="fw-black text-dark m-0 fs-5">Báo cáo Shipper duyệt/giao đơn nhiều nhất</h6>
            <button @click="goToAllShippers" class="btn btn-link text-warning fw-bold p-0 text-decoration-none fs-7 shadow-none">Xem thêm</button>
          </div>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Tên Shipper</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-center">Xếp Hạng</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Số Đơn Đã Giao</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="topShippers.length === 0">
                  <td colspan="3" class="text-center py-4 text-muted">Chưa có dữ liệu giao hàng</td>
                </tr>
                <tr v-for="(shipper, index) in topShippers" :key="index" class="border-bottom-dashed">
                  <td class="py-3">
                    <div class="d-flex align-items-center gap-3">
                      <div class="bg-warning-subtle text-warning border border-warning-subtle rounded-3 d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                        <i class="bi bi-truck fs-5"></i>
                      </div>
                      <span class="fw-bold fs-7 text-dark">{{ shipper.shipperName }}</span>
                    </div>
                  </td>
                  <td class="py-3 text-center">
                    <span class="badge bg-dark rounded-pill px-3 py-1 fs-8">Top {{ index + 1 }}</span>
                  </td>
                  <td class="py-3 text-end fw-bold fs-7 text-success">{{ shipper.totalOrders }} đơn</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 2">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <button @click="activeTab = 1" class="btn btn-light border text-dark fw-bold shadow-sm rounded-3">
            <i class="bi bi-arrow-left"></i> Quay lại
          </button>
          <button @click="exportToExcel(tab2Data.soldProducts, 'ChiTietDoanhThu')" class="btn btn-success fw-bold rounded-3 shadow-sm">
            <i class="bi bi-file-earmark-excel"></i> Xuất Excel
          </button>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4 mb-4 bg-success-subtle border-start border-success border-4">
          <p class="text-success fs-7 fw-bold mb-1">Tổng tiền thu nhập trong giai đoạn</p>
          <h2 class="fw-black text-dark m-0">{{ formatCurrency(tab2Data.totalMonthlyIncome) }}</h2>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4">
          <h6 class="fw-black text-dark m-0 fs-5 mb-4">Chi tiết Sản phẩm đã bán</h6>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Tên Sản Phẩm</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-center">Số Lượng Đã Bán</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Tổng Tiền Thu Được</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="tab2Data.soldProducts.length === 0">
                  <td colspan="3" class="text-center py-4 text-muted">Không có giao dịch nào.</td>
                </tr>
                <tr v-for="(item, index) in tab2Data.soldProducts" :key="index" class="border-bottom-dashed">
                  <td class="py-3 fw-bold fs-7 text-dark">{{ item.productName }}</td>
                  <td class="py-3 text-center text-dark fw-medium fs-7">{{ item.totalSold }} cái</td>
                  <td class="py-3 fw-bold fs-7 text-success text-end">+ {{ formatCurrency(item.totalRevenue) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 3">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <button @click="activeTab = 1" class="btn btn-light border text-dark fw-bold shadow-sm rounded-3">
            <i class="bi bi-arrow-left"></i> Quay lại
          </button>
          <button @click="exportToExcel(allProducts, 'TatCaSanPhamDaBan')" class="btn btn-success fw-bold rounded-3 shadow-sm">
            <i class="bi bi-file-earmark-excel"></i> Xuất Excel
          </button>
        </div>
        <div class="card border-0 shadow-sm rounded-4 p-4">
          <h6 class="fw-black text-dark mb-4 fs-5">Tất cả Sản phẩm bán ra</h6>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Xếp hạng</th>
                  <th scope="col" class="pb-3 fw-bold border-0">Tên Sản Phẩm</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Tổng Đã Bán <i class="bi bi-arrow-down-short"></i></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="allProducts.length === 0">
                  <td colspan="3" class="text-center py-4 text-muted">Không có dữ liệu.</td>
                </tr>
                <tr v-for="(item, index) in allProducts" :key="index" class="border-bottom-dashed">
                  <td class="py-3">
                    <span class="badge bg-light text-dark border px-3 py-1 fs-8">Top {{ index + 1 }}</span>
                  </td>
                  <td class="py-3 fw-bold fs-7 text-dark">{{ item.productName }}</td>
                  <td class="py-3 fw-bold fs-7 text-success text-end">{{ item.totalSold }} cái</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <button @click="activeTab = 1" class="btn btn-light border text-dark fw-bold shadow-sm rounded-3">
            <i class="bi bi-arrow-left"></i> Quay lại
          </button>
          <button @click="exportToExcel(allCustomers, 'TatCaKhachHangMua')" class="btn btn-success fw-bold rounded-3 shadow-sm">
            <i class="bi bi-file-earmark-excel"></i> Xuất Excel
          </button>
        </div>
        <div class="card border-0 shadow-sm rounded-4 p-4">
          <h6 class="fw-black text-dark mb-4 fs-5">Tất cả Khách hàng đã mua</h6>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Xếp hạng</th>
                  <th scope="col" class="pb-3 fw-bold border-0">Tên Khách Hàng</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Tổng Chi Tiêu <i class="bi bi-arrow-down-short"></i></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="allCustomers.length === 0">
                  <td colspan="3" class="text-center py-4 text-muted">Không có dữ liệu.</td>
                </tr>
                <tr v-for="(item, index) in allCustomers" :key="index" class="border-bottom-dashed">
                  <td class="py-3">
                    <span class="badge bg-primary-subtle text-primary border px-3 py-1 fs-8">Top {{ index + 1 }}</span>
                  </td>
                  <td class="py-3 fw-bold fs-7 text-dark">{{ item.customerName }}</td>
                  <td class="py-3 fw-bold fs-7 text-dark text-end">{{ formatCurrency(item.totalSpent) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 5">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <button @click="activeTab = 1" class="btn btn-light border text-dark fw-bold shadow-sm rounded-3">
            <i class="bi bi-arrow-left"></i> Quay lại
          </button>
          <button @click="exportToExcel(allShippers, 'TatCaShipper')" class="btn btn-success fw-bold rounded-3 shadow-sm">
            <i class="bi bi-file-earmark-excel"></i> Xuất Excel
          </button>
        </div>
        <div class="card border-0 shadow-sm rounded-4 p-4">
          <h6 class="fw-black text-dark mb-4 fs-5">Tất cả Shipper đã giao đơn</h6>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Xếp hạng</th>
                  <th scope="col" class="pb-3 fw-bold border-0">Tên Shipper</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Tổng Đơn Đã Giao <i class="bi bi-arrow-down-short"></i></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="allShippers.length === 0">
                  <td colspan="3" class="text-center py-4 text-muted">Không có dữ liệu.</td>
                </tr>
                <tr v-for="(item, index) in allShippers" :key="index" class="border-bottom-dashed">
                  <td class="py-3">
                    <span class="badge bg-warning-subtle text-warning border border-warning-subtle px-3 py-1 fs-8">Top {{ index + 1 }}</span>
                  </td>
                  <td class="py-3 fw-bold fs-7 text-dark">{{ item.shipperName }}</td>
                  <td class="py-3 fw-bold fs-7 text-success text-end">{{ item.totalOrders }} đơn</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 6">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <button @click="activeTab = 1" class="btn btn-light border text-dark fw-bold shadow-sm rounded-3">
            <i class="bi bi-arrow-left"></i> Quay lại
          </button>
          <button @click="exportToExcel(successfulOrders, 'ChiTietDonHang')" class="btn btn-success fw-bold rounded-3 shadow-sm">
            <i class="bi bi-file-earmark-excel"></i> Xuất Excel
          </button>
        </div>
        
        <div class="card border-0 shadow-sm rounded-4 p-4 mb-4 bg-warning-subtle border-start border-warning border-4">
          <p class="text-warning-emphasis fs-7 fw-bold mb-1">Tổng số đơn hàng thành công trong giai đoạn</p>
          <h2 class="fw-black text-dark m-0">{{ generalStats.totalOrders }} đơn</h2>
        </div>

        <div class="card border-0 shadow-sm rounded-4 p-4">
          <h6 class="fw-black text-dark mb-4 fs-5">Lịch sử giao dịch thành công</h6>
          <div class="table-responsive">
            <table class="table align-middle mb-0 border-0">
              <thead class="border-bottom">
                <tr class="text-muted fs-8 text-uppercase">
                  <th scope="col" class="pb-3 fw-bold border-0">Mã Đơn</th>
                  <th scope="col" class="pb-3 fw-bold border-0">Khách Hàng</th>
                  <th scope="col" class="pb-3 fw-bold border-0">Ngày Đặt</th>
                  <th scope="col" class="pb-3 fw-bold border-0 text-end">Thành Tiền <i class="bi bi-arrow-down-short"></i></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="successfulOrders.length === 0">
                  <td colspan="4" class="text-center py-4 text-muted">Không có dữ liệu đơn hàng.</td>
                </tr>
                <tr v-for="(item, index) in successfulOrders" :key="index" class="border-bottom-dashed">
                  <td class="py-3">
                    <span class="fw-bold text-muted fs-8">#{{ item.orderId }}</span>
                  </td>
                  <td class="py-3 fw-bold fs-7 text-dark">{{ item.customerName }}</td>
                  <td class="py-3 text-muted fs-7">{{ formatDate(item.orderDate) }}</td>
                  <td class="py-3 fw-bold fs-7 text-success text-end">+ {{ formatCurrency(item.finalAmount) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import * as XLSX from 'xlsx'; // Require: npm install xlsx

const activeTab = ref(1);

// Lấy ngày đầu tháng và ngày hiện tại làm mặc định
const now = new Date();
const currentYear = now.getFullYear();
const currentMonth = String(now.getMonth() + 1).padStart(2, '0');
const currentDay = String(now.getDate()).padStart(2, '0');

const startDateInput = ref(`${currentYear}-${currentMonth}-01`);
const endDateInput = ref(`${currentYear}-${currentMonth}-${currentDay}`);

const generalStats = ref({ totalRevenue: 0, totalOrders: 0 });
const topProducts = ref([]);
const topCustomers = ref([]);
const topShippers = ref([]);
const allProducts = ref([]);
const allCustomers = ref([]);
const allShippers = ref([]);
const successfulOrders = ref([]);
const tab2Data = ref({ totalMonthlyIncome: 0, soldProducts: [] });

const formatCurrency = (value) => {
  if (!value) return '0₫';
  return Number(value).toLocaleString('vi-VN') + '₫';
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN', {
    day: '2-digit', month: '2-digit', year: 'numeric'
  });
};

// Hàm lấy params ngày gửi xuống DB
const getDateParams = () => {
  let start = startDateInput.value ? new Date(startDateInput.value) : new Date(currentYear, now.getMonth(), 1);
  let end = endDateInput.value ? new Date(endDateInput.value) : new Date();

  // Đảm bảo end date lấy đến cuối ngày (23:59:59) để lấy trọn vẹn dữ liệu của ngày đó
  end.setHours(23, 59, 59, 999);

  const formatSQL = (d) => {
    const pad = (n) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  };

  return { startDate: formatSQL(start), endDate: formatSQL(end) };
};

// Hàm Load lại dữ liệu khi nhấn nút Lọc
const applyFilter = () => {
  if (activeTab.value === 1) fetchTab1Data();
  else if (activeTab.value === 2) goToTab2();
  else if (activeTab.value === 3) goToAllProducts();
  else if (activeTab.value === 4) goToAllCustomers();
  else if (activeTab.value === 5) goToAllShippers();
  else if (activeTab.value === 6) goToTab6();
};

const mapProduct = (p) => ({ productName: p.productName || p.productname, totalSold: p.totalSold || p.totalsold, totalRevenue: p.totalRevenue || p.totalrevenue });
const mapCustomer = (c) => ({ customerName: c.customerName || c.customername, totalSpent: c.totalSpent || c.totalspent });
const mapShipper = (s) => ({ shipperName: s.shipperName || s.shippername, totalOrders: s.totalOrders || s.totalorders });
const mapOrder = (o) => ({ orderId: o.orderId || o.orderid, customerName: o.customerName || o.customername, orderDate: o.orderDate || o.orderdate, finalAmount: o.finalAmount || o.finalamount });

const fetchTab1Data = async () => {
  try {
    const params = getDateParams();
    const [statsRes, prodRes, custRes, shipRes] = await Promise.all([
      axios.get('http://localhost:8080/api/reports/general-stats', { params }),
      axios.get('http://localhost:8080/api/reports/top-products-month', { params }),
      axios.get('http://localhost:8080/api/reports/top-customers-month', { params }),
      axios.get('http://localhost:8080/api/reports/top-shippers', { params })
    ]);

    generalStats.value = {
      totalRevenue: statsRes.data.totalRevenue || statsRes.data.totalrevenue || 0,
      totalOrders: statsRes.data.totalOrders || statsRes.data.totalorders || 0
    };
    topProducts.value = prodRes.data.map(mapProduct);
    topCustomers.value = custRes.data.map(mapCustomer);
    topShippers.value = shipRes.data.map(mapShipper);
  } catch (error) { console.error("Lỗi tải Tab 1:", error); }
};

const goToTab2 = async () => {
  activeTab.value = 2;
  try {
    const res = await axios.get('http://localhost:8080/api/reports/tab2-details', { params: getDateParams() });
    tab2Data.value = {
      totalMonthlyIncome: res.data.totalMonthlyIncome || res.data.totalmonthlyincome || 0,
      soldProducts: (res.data.soldProducts || res.data.soldproducts || []).map(mapProduct)
    };
  } catch (error) { console.error("Lỗi Tab 2:", error); }
};

const goToAllProducts = async () => {
  activeTab.value = 3;
  try {
    const res = await axios.get('http://localhost:8080/api/reports/all-products-month', { params: getDateParams() });
    allProducts.value = res.data.map(mapProduct);
  } catch (error) { console.error("Lỗi Tab 3:", error); }
};

const goToAllCustomers = async () => {
  activeTab.value = 4;
  try {
    const res = await axios.get('http://localhost:8080/api/reports/all-customers-month', { params: getDateParams() });
    allCustomers.value = res.data.map(mapCustomer);
  } catch (error) { console.error("Lỗi Tab 4:", error); }
};

const goToAllShippers = async () => {
  activeTab.value = 5;
  try {
    const res = await axios.get('http://localhost:8080/api/reports/all-shippers', { params: getDateParams() });
    allShippers.value = res.data.map(mapShipper);
  } catch (error) { console.error("Lỗi Tab 5:", error); }
};

const goToTab6 = async () => {
  activeTab.value = 6;
  try {
    const res = await axios.get('http://localhost:8080/api/reports/successful-orders-month', { params: getDateParams() });
    successfulOrders.value = res.data.map(mapOrder);
  } catch (error) { console.error("Lỗi Tab 6:", error); }
};

// Hàm xuất file Excel
// Hàm xuất file Excel
const exportToExcel = (data, fileName) => {
  if (!data || data.length === 0) {
    alert("Không có dữ liệu để xuất!");
    return;
  }
  
  let exportData = [];
  let wscols = []; // Mảng cấu hình độ rộng các cột (canh chỉnh cho đẹp)
  
  // 1. Chi Tiết Doanh Thu
  if (fileName === 'ChiTietDoanhThu') {
    let totalQuantity = 0;
    let totalRevenue = 0;

    exportData = data.map(item => {
      totalQuantity += Number(item.totalSold) || 0;
      totalRevenue += Number(item.totalRevenue) || 0;
      return {
        'Tên Sản Phẩm': item.productName,
        'Số Lượng Bán': item.totalSold,
        'Tổng Doanh Thu (VNĐ)': item.totalRevenue
      };
    });

    // Thêm dòng Tổng Cộng
    exportData.push({
      'Tên Sản Phẩm': 'TỔNG CỘNG',
      'Số Lượng Bán': totalQuantity,
      'Tổng Doanh Thu (VNĐ)': totalRevenue
    });

    // Cài đặt độ rộng cột (đơn vị: ký tự)
    wscols = [{ wch: 45 }, { wch: 15 }, { wch: 25 }];
  } 
  
  // 2. Tất cả sản phẩm đã bán
  else if (fileName === 'TatCaSanPhamDaBan') {
    let totalQuantity = 0;

    exportData = data.map((item, idx) => {
      totalQuantity += Number(item.totalSold) || 0;
      return {
        'STT': idx + 1,
        'Tên Sản Phẩm': item.productName,
        'Tổng Số Lượng Đã Bán': item.totalSold
      };
    });

    exportData.push({
      'STT': '',
      'Tên Sản Phẩm': 'TỔNG CỘNG',
      'Tổng Số Lượng Đã Bán': totalQuantity
    });

    wscols = [{ wch: 10 }, { wch: 45 }, { wch: 25 }];
  } 
  
  // 3. Tất cả khách hàng mua
  else if (fileName === 'TatCaKhachHangMua') {
    let totalSpent = 0;

    exportData = data.map((item, idx) => {
      totalSpent += Number(item.totalSpent) || 0;
      return {
        'STT': idx + 1,
        'Tên Khách Hàng': item.customerName,
        'Tổng Chi Tiêu (VNĐ)': item.totalSpent
      };
    });

    exportData.push({
      'STT': '',
      'Tên Khách Hàng': 'TỔNG CỘNG',
      'Tổng Chi Tiêu (VNĐ)': totalSpent
    });

    wscols = [{ wch: 10 }, { wch: 35 }, { wch: 25 }];
  } 
  
  // 4. Tất cả Shipper
  else if (fileName === 'TatCaShipper') {
    let totalOrders = 0;

    exportData = data.map((item, idx) => {
      totalOrders += Number(item.totalOrders) || 0;
      return {
        'STT': idx + 1,
        'Tên Shipper': item.shipperName,
        'Tổng Đơn Giao Thành Công': item.totalOrders
      };
    });

    exportData.push({
      'STT': '',
      'Tên Shipper': 'TỔNG CỘNG',
      'Tổng Đơn Giao Thành Công': totalOrders
    });

    wscols = [{ wch: 10 }, { wch: 35 }, { wch: 30 }];
  } 
  
  // 5. Chi tiết Đơn Hàng Thành Công
  else if (fileName === 'ChiTietDonHang') {
    let totalAmount = 0;

    exportData = data.map(item => {
      totalAmount += Number(item.finalAmount) || 0;
      return {
        'Mã Đơn Hàng': item.orderId,
        'Tên Khách Hàng': item.customerName,
        'Ngày Đặt Hàng': formatDate(item.orderDate),
        'Thành Tiền (VNĐ)': item.finalAmount
      };
    });

    exportData.push({
      'Mã Đơn Hàng': 'TỔNG CỘNG',
      'Tên Khách Hàng': '',
      'Ngày Đặt Hàng': '',
      'Thành Tiền (VNĐ)': totalAmount
    });

    wscols = [{ wch: 15 }, { wch: 35 }, { wch: 20 }, { wch: 25 }];
  }

  // Khởi tạo Sheet
  const worksheet = XLSX.utils.json_to_sheet(exportData);
  
  // Áp dụng canh chỉnh độ rộng cột để không bị khép lại
  worksheet['!cols'] = wscols;

  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Báo Cáo");
  
  // Format tên file chống lỗi ký tự đặc biệt
  const dateStr = `${startDateInput.value}_Den_${endDateInput.value}`;
  XLSX.writeFile(workbook, `BaoCao_${fileName}_${dateStr}.xlsx`);
};

onMounted(() => {
  fetchTab1Data();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { font-family: 'Inter', system-ui, sans-serif; }
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; border: none; }
.btn-neon:hover { background-color: #00cc29; }
.card-hover { transition: transform 0.2s ease, box-shadow 0.2s ease; }
.card-hover:hover { transform: translateY(-5px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important; }
.border-bottom-dashed { border-bottom: 1px dashed #EAEAEA; }
.border-bottom-dashed:last-child { border-bottom: none; }
</style>