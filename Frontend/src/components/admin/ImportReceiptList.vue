<template>
  <div class="bg-light min-vh-100">
    <header class="navbar navbar-expand-lg navbar-light bg-white border-bottom sticky-top z-3 px-4 py-3">
      <div class="container-fluid p-0">
        <a class="navbar-brand d-flex align-items-center gap-2" href="#">
          <div class="bg-success rounded p-1 d-flex align-items-center justify-content-center"
            style="width: 32px; height: 32px;">
            <i class="bi bi-lightning-charge-fill text-white"></i>
          </div>
          <span class="fw-bolder fs-5" style="font-family: 'Space Grotesk', sans-serif;">TechZone Admin</span>
        </a>

        <div class="d-none d-md-flex align-items-center gap-4">
          <nav class="nav">
            <a class="nav-link fw-bold text-success border-bottom border-success border-2 pb-1 px-2 mx-2" href="#">Nhập Kho</a>
            <a class="nav-link fw-semibold text-secondary px-2 mx-2 hover-text-dark" href="#">Kho Hàng</a>
            <a class="nav-link fw-semibold text-secondary px-2 mx-2 hover-text-dark" href="#">Báo Cáo</a>
          </nav>

          <div class="d-flex align-items-center gap-3 ms-4 border-start ps-4">
            <i class="bi bi-bell fs-5 text-secondary cursor-pointer hover-text-dark"></i>
            <div class="d-flex align-items-center gap-2 cursor-pointer">
              <span class="fw-bold small">Admin</span>
              <img src="https://ui-avatars.com/api/?name=Admin&background=f4f4f5&color=333" alt="User"
                class="rounded-circle" width="32" height="32" />
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="container-fluid py-5" style="max-width: 1400px;">

      <header class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-5">
        <div>
          <h2 class="display-6 fw-bold text-dark mb-1" style="font-family: 'Space Grotesk', sans-serif;">Danh Sách Phiếu Nhập Kho</h2>
          <p class="text-muted mb-0 fw-medium">Quản lý và theo dõi các giao dịch nhập hàng vào hệ thống.</p>
        </div>
        <div class="d-flex align-items-center gap-2">
          <button class="btn btn-light border bg-white fw-bold d-flex align-items-center gap-2 shadow-sm rounded-3 px-4 py-2">
            <i class="bi bi-box-arrow-up"></i> Xuất File
          </button>
          <button @click="openAddModal" class="btn btn-success fw-bold d-flex align-items-center gap-2 shadow-sm rounded-3 px-4 py-2">
            <i class="bi bi-plus-lg"></i> Tạo Phiếu Mới
          </button>
        </div>
      </header>

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

              <tr v-for="(receipt, index) in filteredReceipts" :key="index" class="bg-white hover-bg-light transition-colors">
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

    <div class="modal fade" id="receiptModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold display-6 fs-4" style="font-family: 'Space Grotesk', sans-serif;">
              Tạo Phiếu Nhập Kho Mới
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>

          <div class="modal-body p-4 p-md-5 pt-3">
            <form @submit.prevent="saveReceipt">
              <h6 class="fw-bold text-success mb-3"><i class="bi bi-info-circle"></i> Thông Tin Chung</h6>
              <div class="row bg-light p-3 rounded-3 mb-4 border">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Nhà cung cấp <span class="text-danger">*</span></label>
                  <select class="form-select" v-model="newReceipt.supplierId" required>
                    <option value="" disabled>-- Chọn Nhà Cung Cấp --</option>
                    <option v-for="sup in suppliers" :key="sup.id" :value="sup.id">
                      {{ sup.name }}
                    </option>
                  </select>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Ghi chú phiếu nhập</label>
                  <input type="text" class="form-control" v-model="newReceipt.note" placeholder="Nhập ghi chú hoặc mã chứng từ...">
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
                    <tr v-for="(item, index) in newReceipt.items" :key="index" class="border-bottom">
                      <td>
                        <select class="form-select form-select-sm mb-2" v-model="item.productId" @change="item.variantId = ''" required>
                          <option value="" disabled>-- Chọn Sản Phẩm --</option>
                          <option v-for="prod in products" :key="prod.id || prod.productId" :value="prod.id || prod.productId">
                            {{ prod.name || prod.productName || 'Sản phẩm ' + (prod.id || prod.productId) }}
                          </option>
                        </select>
                        
                        <select class="form-select form-select-sm border-info" v-model="item.variantId" v-if="getVariantsForProduct(item.productId).length > 0" required>
                          <option value="" disabled>-- Chọn Phân Loại (Bắt buộc) --</option>
                          <option v-for="v in getVariantsForProduct(item.productId)" :key="v.variantId || v.id" :value="v.variantId || v.id">
                            {{ formatVariantName(v) }}
                          </option>
                        </select>
                      </td>
                      <td>
                        <input type="number" class="form-control form-control-sm text-center" v-model.number="item.quantity" min="1" required>
                      </td>
                      <td>
                        <input type="number" class="form-control form-control-sm text-end" v-model.number="item.price" min="0" required>
                      </td>
                      <td class="text-end fw-bold text-dark">
                        {{ formatCurrency(item.quantity * item.price) }}
                      </td>
                      <td class="text-end">
                        <button type="button" @click="removeItem(index)" class="btn btn-sm btn-light text-danger rounded-circle">
                          <i class="bi bi-x-lg"></i>
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <div class="d-flex justify-content-end align-items-center gap-4 bg-light p-3 rounded-3 border">
                <span class="text-muted fw-bold text-uppercase" style="letter-spacing: 1px;">Tổng Tiền Phiếu Nhập:</span>
                <h3 class="fw-black text-danger mb-0">{{ formatCurrency(totalAmount) }}</h3>
              </div>

              <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-light fw-bold px-4" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success fw-bold px-5 shadow-sm" :disabled="newReceipt.items.length === 0">
                  <i class="bi bi-check2-circle"></i> Hoàn Tất Nhập Kho
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import * as bootstrap from 'bootstrap';

export default {
  name: "ImportReceiptList",
  data() {
    return {
      searchQuery: "",
      receipts: [],
      modalInstance: null,
      suppliers: [],
      products: [],
      newReceipt: {
        supplierId: '',
        note: '',
        items: []
      }
    };
  },
  computed: {
    filteredReceipts() {
      if (!this.searchQuery) return this.receipts;
      const lowerQuery = this.searchQuery.toLowerCase();
      return this.receipts.filter(
        (receipt) =>
          (receipt.code && receipt.code.toLowerCase().includes(lowerQuery)) ||
          (receipt.creatorName && receipt.creatorName.toLowerCase().includes(lowerQuery)) ||
          (receipt.supplier && receipt.supplier.toLowerCase().includes(lowerQuery))
      );
    },
    totalAmount() {
      return this.newReceipt.items.reduce((total, item) => {
        return total + (item.quantity * item.price);
      }, 0);
    }
  },
  methods: {
    formatCurrency(value) {
      if (!value) return "0 ₫";
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND"
      }).format(value);
    },

    // Hàm mới: Nối chuỗi để hiển thị đầy đủ Option 1 và Option 2 (Ví dụ: Đen - 256GB)
    formatVariantName(v) {
      // Ưu tiên lấy các trường phổ biến mà backend thường trả về cho Option 1
      let opt1 = v.option1_value || v.option1Value || v.colorName || v.name || "";
      // Ưu tiên lấy Option 2
      let opt2 = v.option2_value || v.option2Value || v.sizeName || "";

      if (opt1 && opt2) {
        return `${opt1} - ${opt2}`;
      }
      return opt1 || opt2 || `Phân loại ${v.variantId || v.id}`;
    },

    async fetchReceipts() {
      try {
        const response = await axios.get('/api/admin/import-receipts');
        this.receipts = response.data;
      } catch (error) {
        console.error("Lỗi khi tải phiếu nhập:", error);
      }
    },

    async fetchSuppliers() {
      try {
        const response = await axios.get('/api/admin/suppliers');
        this.suppliers = response.data.filter(s => s.status === true);
      } catch (error) {
        console.error("Lỗi tải NCC:", error);
      }
    },

    async fetchProducts() {
      try {
        const response = await axios.get('/api/product');
        if (response.data && response.data.content) {
          this.products = response.data.content;
        } else {
          this.products = response.data;
        }
      } catch (error) {
        console.error("Lỗi tải Sản phẩm:", error);
      }
    },

    getVariantsForProduct(productId) {
      if (!productId) return [];
      const product = this.products.find(p => p.productId === productId || p.id === productId);
      return product && product.variants ? product.variants : [];
    },

    openAddModal() {
      this.newReceipt = { supplierId: '', note: '', items: [] };
      this.addItem();
      this.modalInstance.show();
    },

    addItem() {
      this.newReceipt.items.push({ productId: '', variantId: '', quantity: 1, price: 0 });
    },

    removeItem(index) {
      this.newReceipt.items.splice(index, 1);
    },

    async saveReceipt() {
      if (this.newReceipt.items.length === 0) {
        alert("Vui lòng thêm ít nhất 1 mặt hàng vào phiếu!");
        return;
      }

      // Đã sửa lại Payload: Gọi thẳng 'productId', 'variantId', 'supplierId' để khớp chuẩn Entity
      const payload = {
        supplier: { supplierId: this.newReceipt.supplierId }, 
        note: this.newReceipt.note,
        totalAmount: this.totalAmount, 
        
        // Chú ý: Cột này trong Java thường đặt là importReceiptDetails hoặc details
        details: this.newReceipt.items.map(item => {
          const detail = {
            product: { productId: item.productId }, // Đã fix lỗi Hibernate TransientPropertyValueException
            quantity: item.quantity,
            importPrice: item.price, 
            lineTotal: item.quantity * item.price
          };
          
          if (item.variantId) {
            detail.variant = { variantId: item.variantId }; // Đã fix mapping cho Variant
          }
          return detail;
        })
      };

      try {
        await axios.post('/api/admin/import-receipts', payload);
        alert("Tạo phiếu nhập kho thành công!");
        this.modalInstance.hide();
        this.fetchReceipts(); // Load lại danh sách
      } catch (error) {
        console.error("Lỗi khi tạo phiếu:", error);
        
        if (error.response && error.response.data) {
           alert("Lỗi từ server: " + error.response.data);
        } else {
           alert("Có lỗi xảy ra khi tạo phiếu. Vui lòng kiểm tra lại kết nối mạng hoặc console log!");
        }
      }
    },

    viewDetail(id) {
      this.$router.push({ name: 'ImportReceiptDetail', params: { id: id } });
    }
  },
  mounted() {
    this.fetchReceipts();
    this.fetchSuppliers();
    this.fetchProducts();

    this.modalInstance = new bootstrap.Modal(document.getElementById('receiptModal'), {
      keyboard: false,
      backdrop: 'static'
    });
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap');
.cursor-pointer { cursor: pointer; }
.hover-text-dark:hover { color: #212529 !important; }
.transition-colors { transition: background-color 0.2s, color 0.2s; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.fw-black { font-weight: 900; }
.form-control:focus, .form-select:focus { border-color: #198754; box-shadow: 0 0 0 0.2rem rgba(25, 135, 84, 0.25); }
</style>