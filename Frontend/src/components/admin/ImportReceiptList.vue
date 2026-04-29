<template>
  <div class="bg-light min-vh-100">
    <main class="container-fluid py-4" style="max-width: 1400px;">
      
      <div class="d-flex justify-content-between align-items-center mb-4 mt-2">
        <div>
          <div class="text-muted fw-semibold small mb-1">
            Admin <span class="mx-1">/</span> <span class="text-dark fw-bold">Nhập kho</span>
          </div>
          <h2 class="fw-bolder text-dark mb-0" style="font-size: 1.8rem; letter-spacing: -0.5px;">Quản Lý Phiếu Nhập</h2>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-white border fw-bold text-dark shadow-sm px-3 py-2">
            <i class="bi bi-box-arrow-up"></i> Xuất File
          </button>
          
          <button @click="openAddModal" 
                  class="btn fw-bold text-white shadow-sm px-4 py-2 d-flex align-items-center gap-2" 
                  style="background-color: #00DF3A; border-color: #00DF3A;">
            <i class="bi bi-plus-lg"></i> Tạo Phiếu Mới
          </button>
        </div>
      </div>

      <div class="d-flex flex-wrap align-items-center gap-3 mb-4">
        <div class="input-group shadow-sm" style="max-width: 450px;">
          <span class="input-group-text bg-white border-end-0 text-muted">
            <i class="bi bi-search"></i>
          </span>
          <input type="text" class="form-control border-start-0 ps-0 py-2"
            placeholder="Tìm kiếm theo mã phiếu, người lập hoặc nhà cung cấp..." v-model="searchQuery">
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="bg-light">
              <tr class="text-muted" style="font-size: 11px; letter-spacing: 0.05em;">
                <th class="px-4 py-3 text-uppercase border-bottom-0">ID</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0">Mã Phiếu</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0">Ngày Nhập</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0 text-end">Tổng Tiền (VNĐ)</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0">Người Lập</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0">Nhà Cung Cấp</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0">Ghi Chú</th>
                <th class="px-4 py-3 text-uppercase border-bottom-0 text-center">Thao Tác</th>
              </tr>
            </thead>
            <tbody class="border-top-0">
              <tr v-if="filteredReceipts.length === 0">
                <td colspan="8" class="text-center py-4 text-muted">Không tìm thấy phiếu nhập nào.</td>
              </tr>

              <tr v-for="(receipt, index) in filteredReceipts" :key="index"
                class="bg-white hover-bg-light transition-colors">
                <td class="px-4 py-3 text-muted small">{{ receipt.id }}</td>
                <td class="px-4 py-3 fw-bold text-dark">{{ receipt.code }}</td>
                <td class="px-4 py-3 text-muted small">{{ receipt.date }}</td>
                <td class="px-4 py-3 text-end fw-bold text-danger">{{ formatCurrency(receipt.total) }}</td>
                <td class="px-4 py-3">
                  <div class="d-flex align-items-center gap-2">
                    <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center fw-bold"
                      style="width: 24px; height: 24px; font-size: 10px;">
                      {{ receipt.creatorInitials || 'AD' }}
                    </div>
                    <span class="small">{{ receipt.creatorName || 'Admin' }}</span>
                  </div>
                </td>
                <td class="px-4 py-3 fw-medium small">{{ receipt.supplier }}</td>
                <td class="px-4 py-3">
                  <span class="d-inline-block text-truncate text-muted small" style="max-width: 150px;">
                    {{ receipt.note || '—' }}
                  </span>
                </td>
                <td class="px-4 py-3 text-center">
                  <button @click="viewDetail(receipt.id)"
                    class="btn btn-sm btn-light text-primary hover-text-dark border-0 shadow-sm">
                    <i class="bi bi-eye-fill"></i> Xem
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="card-footer bg-light border-top px-4 py-3 d-flex align-items-center justify-content-between">
          <span class="text-muted small">
            Đang hiển thị <span class="fw-bold text-dark">{{ filteredReceipts.length }}</span> phiếu
          </span>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div v-if="showModal" class="modal d-block" tabindex="-1">
      <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content rounded-4 border-0 shadow-lg">
          
          <div class="modal-header border-bottom p-3">
            <h5 class="fw-black m-0 fs-5 text-dark">TẠO PHIẾU NHẬP KHO MỚI</h5>
            <button type="button" class="btn-close shadow-none" @click="closeModal"></button>
          </div>

          <div class="modal-body p-4 p-md-5 pt-3 bg-white">
            <form @submit.prevent="saveReceipt">
              
              <h6 class="fw-bold text-success mb-3"><i class="bi bi-info-circle"></i> Thông Tin Chung</h6>
              <div class="row bg-light p-3 rounded-3 mb-4 border">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Nhà cung cấp <span class="text-danger">*</span></label>
                  <select class="form-select shadow-none" v-model="newReceipt.supplierId" required>
                    <option value="" disabled>-- Chọn Nhà Cung Cấp --</option>
                    <option v-for="sup in suppliers" :key="sup.id" :value="sup.id">{{ sup.name }}</option>
                  </select>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Ghi chú phiếu nhập</label>
                  <input type="text" class="form-control shadow-none" v-model="newReceipt.note" placeholder="Nhập ghi chú hoặc mã chứng từ...">
                </div>
              </div>

              <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="fw-bold text-primary mb-0"><i class="bi bi-box-seam"></i> Chi Tiết Hàng Nhập</h6>
                <button type="button" @click="addItem" class="btn btn-sm btn-outline-primary fw-bold rounded-pill">
                  <i class="bi bi-plus"></i> Thêm Mặt Hàng
                </button>
              </div>

              <div class="table-responsive mb-4 border rounded-3">
                <table class="table table-borderless align-middle mb-0">
                  <thead class="table-light border-bottom">
                    <tr class="small text-muted text-uppercase">
                      <th style="width: 40%">Sản phẩm <span class="text-danger">*</span></th>
                      <th style="width: 15%">Số lượng</th>
                      <th style="width: 25%">Đơn giá nhập (VNĐ)</th>
                      <th style="width: 15%" class="text-end">Thành tiền</th>
                      <th style="width: 5%"></th>
                    </tr>
                  </thead>
                  <tbody class="border-top-0">
                    <tr v-if="newReceipt.items.length === 0">
                      <td colspan="5" class="text-center py-4 text-muted fst-italic">Chưa có sản phẩm nào. Vui lòng bấm "Thêm Mặt Hàng".</td>
                    </tr>
                    <tr v-for="(item, index) in newReceipt.items" :key="index" class="border-bottom-dashed">
                      <td class="py-3">
                        <select class="form-select form-select-sm mb-2 shadow-none" v-model="item.productId" @change="item.variantId = ''" required>
                          <option value="" disabled>-- Chọn Sản Phẩm --</option>
                          <option v-for="prod in products" :key="prod.id || prod.productId" :value="prod.id || prod.productId">
                            {{ prod.name || prod.productName || 'SP ' + (prod.id || prod.productId) }}
                          </option>
                        </select>
                        <select class="form-select form-select-sm border-info shadow-none" v-model="item.variantId" v-if="getVariantsForProduct(item.productId).length > 0" required>
                          <option value="" disabled>-- Chọn Phân Loại (Bắt buộc) --</option>
                          <option v-for="v in getVariantsForProduct(item.productId)" :key="v.variantId || v.id" :value="v.variantId || v.id">
                            {{ formatVariantName(v) }}
                          </option>
                        </select>
                      </td>
                      <td class="py-3">
                        <input type="number" class="form-control form-control-sm text-center shadow-none" v-model.number="item.quantity" min="1" required>
                      </td>
                      <td class="py-3">
                        <input type="number" class="form-control form-control-sm text-end shadow-none" v-model.number="item.price" min="0" required>
                      </td>
                      <td class="text-end fw-bold text-dark py-3">
                        {{ formatCurrency(item.quantity * item.price) }}
                      </td>
                      <td class="text-end py-3">
                        <button type="button" @click="removeItem(index)" class="btn btn-sm btn-link text-danger shadow-none p-0">
                          <i class="bi bi-trash fs-6"></i>
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <div class="d-flex justify-content-end align-items-center gap-4 bg-light p-3 rounded-3 border">
                <span class="text-muted fw-bold text-uppercase" style="letter-spacing: 1px;">Tổng Tiền:</span>
                <h3 class="fw-black text-danger mb-0">{{ formatCurrency(totalAmount) }}</h3>
              </div>

              <div class="modal-footer border-top px-0 pb-0 pt-4 mt-4">
                <button type="button" class="btn btn-light fs-7 fw-bold px-4" @click="closeModal">Hủy</button>
                <button type="submit" class="btn fs-7 fw-bold px-4 text-dark shadow-sm" style="background-color: #00DF3A; border-color: #00DF3A;" :disabled="newReceipt.items.length === 0">
                  Hoàn Tất Nhập Kho
                </button>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
// Import api instance thay vì axios gốc
import api from '../../utils/axios';

const router = useRouter(); 
const searchQuery = ref(""); 
const receipts = ref([]); 
const suppliers = ref([]); 
const products = ref([]); 
const showModal = ref(false); 

const newReceipt = ref({
  supplierId: '',
  note: '',
  items: []
});

// Computed properties
const filteredReceipts = computed(() => {
  if (!searchQuery.value) return receipts.value;
  const lowerQuery = searchQuery.value.toLowerCase();
  return receipts.value.filter(receipt =>
    (receipt.code && receipt.code.toLowerCase().includes(lowerQuery)) ||
    (receipt.creatorName && receipt.creatorName.toLowerCase().includes(lowerQuery)) ||
    (receipt.supplier && receipt.supplier.toLowerCase().includes(lowerQuery))
  );
});

const totalAmount = computed(() => {
  return newReceipt.value.items.reduce((total, item) => total + (item.quantity * item.price), 0);
});

// UI Helpers
const formatCurrency = (value) => {
  if (!value) return "0 ₫";
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value);
};

const formatVariantName = (v) => {
  let opt1 = v.option1_value || v.option1Value || v.colorName || v.name || "";
  let opt2 = v.option2_value || v.option2Value || v.sizeName || "";
  if (opt1 && opt2) return `${opt1} - ${opt2}`;
  return opt1 || opt2 || `Phân loại ${v.variantId || v.id}`;
};

const getVariantsForProduct = (productId) => {
  if (!productId) return [];
  const product = products.value.find(p => p.productId === productId || p.id === productId);
  return product && product.variants ? product.variants : [];
};

// Modal Logic
const openAddModal = () => {
  newReceipt.value = { supplierId: '', note: '', items: [] };
  addItem(); 
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const addItem = () => {
  newReceipt.value.items.push({ productId: '', variantId: '', quantity: 1, price: 0 });
};

const removeItem = (index) => {
  newReceipt.value.items.splice(index, 1);
};

// API Functions
const fetchReceipts = async () => {
  try {
    // Dùng api thay vì axios
    const response = await api.get('/admin/import-receipts');
    receipts.value = response.data;
  } catch (error) { console.error("Lỗi khi tải phiếu nhập:", error); }
};

const fetchSuppliers = async () => {
  try {
    // Dùng api thay vì axios
    const response = await api.get('/admin/suppliers');
    suppliers.value = response.data.filter(s => s.status === true);
  } catch (error) { console.error("Lỗi tải NCC:", error); }
};

const fetchProducts = async () => {
  try {
    // Dùng api thay vì axios
    const response = await api.get('/product?size=9999');
    products.value = (response.data && response.data.content) ? response.data.content : response.data;
  } catch (error) { console.error("Lỗi tải Sản phẩm:", error); }
};

const saveReceipt = async () => {
  if (newReceipt.value.items.length === 0) {
    alert("Vui lòng thêm ít nhất 1 mặt hàng vào phiếu!");
    return;
  }

  const payload = {
    supplier: { supplierId: newReceipt.value.supplierId },
    note: newReceipt.value.note,
    totalAmount: totalAmount.value,
    details: newReceipt.value.items.map(item => {
      const detail = {
        product: { productId: item.productId },
        quantity: item.quantity,
        importPrice: item.price,
        lineTotal: item.quantity * item.price
      };
      if (item.variantId) detail.variant = { variantId: item.variantId };
      return detail;
    })
  };

  try {
    // Dùng api thay vì axios
    await api.post('/admin/import-receipts', payload);
    
    alert("Tạo phiếu nhập kho thành công!");
    closeModal(); 
    fetchReceipts(); 
  } catch (error) {
    console.error("Lỗi khi tạo phiếu:", error);
    const errorMsg = error.response?.data?.message || error.response?.data || "Có lỗi xảy ra, vui lòng thử lại!";
    alert(typeof errorMsg === 'string' ? errorMsg : "Lỗi không xác định từ máy chủ.");
  }
};

const viewDetail = (id) => {
  router.push({ name: 'ImportReceiptDetail', params: { id: id } });
};

// Lifecycle
onMounted(() => {
  fetchReceipts();
  fetchSuppliers();
  fetchProducts();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap');

.cursor-pointer { cursor: pointer; }
.hover-text-dark:hover { color: #212529 !important; }
.transition-colors { transition: background-color 0.2s, color 0.2s; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.fw-black { font-weight: 900; }
.border-bottom-dashed { border-bottom: 1px dashed #dee2e6; }

.form-control:focus, .form-select:focus {
  border-color: #00DF3A;
  box-shadow: 0 0 0 0.2rem rgba(0, 223, 58, 0.25);
}
</style>