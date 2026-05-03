<template>
  <div class="bg-light min-vh-100 pb-5">
    <header class="fixed-top bg-white shadow-sm border-bottom py-3 px-4 z-3 d-print-none">
      <div class="d-flex justify-content-between align-items-center max-w-7xl mx-auto">
        <div class="d-flex align-items-center gap-3">
          <button class="btn btn-light rounded-circle" @click="goBack">
            <i class="bi bi-arrow-left fs-5"></i>
          </button>
          <h4 class="mb-0 fw-bold">Chi Tiết Phiếu Nhập</h4>
        </div>

        <div class="d-flex align-items-center gap-2">
          <button @click="printReceipt" class="btn btn-outline-secondary d-flex align-items-center gap-2 fw-bold">
            <i class="bi bi-printer"></i> In Phiếu Nhập
          </button>
        </div>
      </div>
    </header>

    <div v-if="!receiptData" class="d-flex justify-content-center align-items-center min-vh-100">
      <div class="spinner-border text-success" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <main v-if="receiptData" class="container-fluid pt-5 mt-5 px-4 print-container" style="max-width: 1200px;">
      <div class="card shadow-sm border-0 rounded-4 mb-4">
        <div class="card-body p-4 p-md-5">
          <div class="row">
            <div class="col-md-8 mb-4 mb-md-0">
              <span class="badge bg-secondary mb-3 px-3 py-2 text-uppercase tracking-wide">
                Mã Phiếu: {{ receiptData.receiptCode || receiptData.code }}
              </span>
              <h1 class="fw-bold mb-4 display-6">{{ receiptData.supplierName }}</h1>

              <div class="row g-4 mt-2">
                <div class="col-sm-4">
                  <p class="text-muted text-uppercase fw-bold mb-1" style="font-size: 11px;">Ngày lập phiếu</p>
                  <p class="fw-semibold mb-0">{{ receiptData.receiptDate || receiptData.date }}</p>
                </div>
                <div class="col-sm-4">
                  <p class="text-muted text-uppercase fw-bold mb-1" style="font-size: 11px;">Nhân viên</p>
                  <p class="fw-semibold mb-0">{{ receiptData.employeeName || receiptData.creatorName || 'Admin' }}</p>
                </div>
                <div class="col-sm-4">
                  <p class="text-muted text-uppercase fw-bold mb-1" style="font-size: 11px;">Nhà cung cấp</p>
                  <p class="fw-semibold mb-0">{{ receiptData.supplierName }}</p>
                </div>
              </div>
            </div>

            <div class="col-md-4 d-flex align-items-center justify-content-md-end">
              <div class="bg-light p-4 rounded-4 border w-100 text-end">
                <p class="text-muted text-uppercase fw-bold tracking-wide mb-2" style="font-size: 11px;">Tổng Giá Trị Phiếu</p>
                <div class="d-flex justify-content-end align-items-baseline gap-2">
                  <h2 class="fw-bold text-dark mb-0 display-6">{{ formatCurrency(receiptData.totalValue || receiptData.total) }}</h2>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card shadow-sm border-0 rounded-4 mb-4 overflow-hidden">
        <div class="card-header bg-white border-bottom p-4 d-flex justify-content-between align-items-center">
          <h5 class="fw-bold mb-0">Chi Tiết Mặt Hàng</h5>
        </div>

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="px-4 py-3 text-uppercase text-muted" style="font-size: 11px; font-weight: 700;">Sản phẩm</th>
                <th class="px-4 py-3 text-uppercase text-muted text-center" style="font-size: 11px; font-weight: 700;">Số lượng</th>
                <th class="px-4 py-3 text-uppercase text-muted text-end" style="font-size: 11px; font-weight: 700;">Đơn giá nhập</th>
                <th class="px-4 py-3 text-uppercase text-muted text-end" style="font-size: 11px; font-weight: 700;">Thành tiền</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in (receiptData.items || receiptData.details)" :key="index">
                <td class="px-4 py-3">
                  <div class="d-flex align-items-center gap-3">
                    <div class="bg-light rounded p-2 d-print-none" style="width: 50px; height: 50px;">
                      <img :src="item.image || 'https://via.placeholder.com/50'" alt="Product" class="img-fluid object-fit-contain w-100 h-100" />
                    </div>
                    <div>
                      <p class="fw-bold mb-0">
                        {{ item.name || item.productName }}
                        <span class="badge bg-info-subtle text-info border border-info-subtle ms-2" v-if="item.variantName && item.variantName !== 'Mặc định'">
                          {{ item.variantName }}
                        </span>
                      </p>
                      <small class="text-muted font-monospace">{{ item.code || item.productCode }}</small>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-3 text-center">
                  <span class="fw-bold fs-5">{{ item.quantity < 10 ? '0' + item.quantity : item.quantity }}</span>
                </td>
                <td class="px-4 py-3 text-end">
                  <span class="text-muted">{{ formatCurrency(item.importPrice || item.price) }}</span>
                </td>
                <td class="px-4 py-3 text-end">
                  <span class="fw-bold text-dark">{{ formatCurrency(item.lineTotal || (item.quantity * (item.importPrice || item.price))) }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="card-footer bg-light p-4 border-top text-end">
          <div class="d-flex justify-content-end align-items-center gap-4">
            <span class="fw-bold text-dark">TỔNG CỘNG:</span>
            <div>
              <h3 class="fw-bold text-dark mb-0">{{ formatCurrency(receiptData.totalValue || receiptData.total) }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="row g-4 align-items-start mb-5">
        <div class="col-md-6">
          <div class="bg-white rounded-4 p-4 border shadow-sm h-100">
            <div class="d-flex align-items-center gap-2 mb-3">
              <i class="bi bi-file-earmark-text fs-5 text-secondary"></i>
              <h6 class="fw-bold mb-0">Ghi chú phiếu nhập</h6>
            </div>
            <p class="text-muted fst-italic mb-0" style="font-size: 14px;">
              "{{ receiptData.note || 'Không có ghi chú' }}"
            </p>
          </div>
        </div>

        <div class="col-md-6">
          <div class="bg-white rounded-4 p-4 border shadow-sm h-100 d-flex flex-column gap-3 d-print-none">
            <div class="d-flex gap-3">
              <button @click="cancelReceipt" class="btn btn-outline-danger flex-grow-1 py-2 fw-bold rounded-3">
                <i class="bi bi-x-circle"></i> Hủy Phiếu Nhập
              </button>
              <button @click="editReceipt" class="btn btn-outline-secondary flex-grow-1 py-2 fw-bold rounded-3">
                <i class="bi bi-pencil-square"></i> Cập Nhật Ghi Chú
              </button>
            </div>
            <p class="text-center text-muted mt-auto pt-3 mb-0" style="font-size: 11px;">
              Hệ thống quản lý kho TechZone v4.2.0 • Bản quyền © 2023<br>
            </p>
          </div>
        </div>
      </div>

    </main>

    <!-- CUSTOM MODAL THÔNG BÁO -->
    <div v-if="customModal.show" class="custom-modal-overlay d-flex justify-content-center align-items-center">
      <div class="custom-modal bg-white rounded-4 p-4 text-center shadow-lg mx-3">
        <div class="mb-3">
          <i v-if="customModal.icon === 'success'" class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>
          <i v-else-if="customModal.icon === 'error'" class="bi bi-x-circle-fill text-danger" style="font-size: 3.5rem;"></i>
          <i v-else-if="customModal.icon === 'warning'" class="bi bi-exclamation-triangle-fill text-warning" style="font-size: 3.5rem;"></i>
          <i v-else-if="customModal.icon === 'prompt'" class="bi bi-pencil-square text-primary" style="font-size: 3.5rem;"></i>
        </div>
        <h5 class="fw-bold mb-2 text-dark">{{ customModal.title }}</h5>
        <p class="text-muted small mb-3" v-html="customModal.message"></p>
        
        <div v-if="customModal.icon === 'prompt'" class="mb-4">
          <input type="text" class="form-control shadow-none border-primary" v-model="customModal.inputValue" placeholder="Nhập nội dung...">
        </div>

        <div class="d-flex justify-content-center gap-2">
          <button v-if="customModal.icon === 'warning' || customModal.icon === 'prompt'" @click="handleModalCancel" class="btn btn-light fw-bold px-4 py-2 rounded-3 flex-grow-1 text-uppercase" style="font-size: 13px;">
            Hủy
          </button>
          <button @click="handleModalConfirm" class="btn btn-dark fw-bold px-4 py-2 rounded-3 flex-grow-1 text-uppercase" style="font-size: 13px;">
            {{ customModal.icon === 'warning' || customModal.icon === 'prompt' ? 'Xác nhận' : 'Đóng' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// Thay thế import axios từ thư viện gốc bằng instance api đã cấu hình
import api from '../../utils/axios';

export default {
  name: "ImportReceiptDetail",
  data() {
    return {
      receiptData: null,
      customModal: {
        show: false,
        icon: 'success', // 'success', 'error', 'warning', 'confirm', 'prompt'
        title: '',
        message: '',
        inputValue: '',
        onConfirm: null,
        onCancel: null
      }
    };
  },
  methods: {
    showNotification(icon, title, message, onConfirm = null, onCancel = null, isPrompt = false, defaultValue = '') {
      this.customModal = { 
        show: true, 
        icon, 
        title, 
        message, 
        inputValue: defaultValue,
        onConfirm, 
        onCancel 
      };
    },
    closeCustomModal() {
      this.customModal.show = false;
    },
    handleModalConfirm() {
      const value = this.customModal.inputValue;
      const callback = this.customModal.onConfirm;
      this.closeCustomModal();
      if (callback) callback(value);
    },
    handleModalCancel() {
      const callback = this.customModal.onCancel;
      this.closeCustomModal();
      if (callback) callback();
    },
    formatCurrency(value) {
      if (!value) return "0 ₫";
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND"
      }).format(value);
    },
    goBack() {
      this.$router.push('/admin/import-receipts');
    },
    printReceipt() {
      window.print();
    },

    async fetchReceiptDetail() {
      const receiptId = this.$route.params.id;
      if (!receiptId) {
        this.showNotification('error', 'Lỗi', 'Không tìm thấy ID phiếu nhập!', () => this.goBack());
        return;
      }
      try {
        const response = await api.get(`/admin/import-receipts/${receiptId}`);
        this.receiptData = response.data;
      } catch (error) {
        console.error("Lỗi khi tải chi tiết phiếu nhập:", error);
        this.showNotification('error', 'Lỗi hệ thống', 'Có lỗi xảy ra hoặc phiếu nhập không tồn tại!', () => this.goBack());
      }
    },

    // GỌI API XÓA PHIẾU NHẬP
    async cancelReceipt() {
      this.showNotification(
        'warning', 
        'Xác nhận hủy', 
        'Bạn có chắc chắn muốn hủy phiếu nhập này không? Hành động này không thể hoàn tác!',
        async () => {
          try {
            const receiptId = this.$route.params.id;
            const response = await api.delete(`/admin/import-receipts/${receiptId}`);
            this.showNotification('success', 'Thành công', response.data || 'Đã hủy phiếu nhập thành công!', () => this.goBack());
          } catch (error) {
            console.error("Lỗi khi hủy phiếu:", error);
            const errorMsg = error.response?.data || 'Không thể hủy phiếu nhập này!';
            this.showNotification('error', 'Lỗi', typeof errorMsg === 'string' ? errorMsg : 'Không thể hủy phiếu nhập này!');
          }
        }
      );
    },

    // GỌI API SỬA GHI CHÚ
    async editReceipt() {
      this.showNotification(
        'prompt',
        'Cập nhật ghi chú',
        'Nhập nội dung Ghi chú mới cho phiếu nhập này:',
        async (newNote) => {
          if (newNote !== null) {
            try {
              const receiptId = this.$route.params.id;
              const payload = { ...this.receiptData, note: newNote };
              await api.put(`/admin/import-receipts/${receiptId}`, payload);
              this.showNotification('success', 'Thành công', 'Cập nhật ghi chú thành công!');
              this.receiptData.note = newNote; 
            } catch (error) {
              console.error("Lỗi khi cập nhật phiếu:", error);
              const errorMsg = error.response?.data || 'Có lỗi xảy ra khi cập nhật!';
              this.showNotification('error', 'Lỗi', typeof errorMsg === 'string' ? "Lỗi: " + errorMsg : 'Có lỗi xảy ra khi cập nhật!');
            }
          }
        },
        null,
        true,
        this.receiptData.note || ""
      );
    }
  },
  mounted() {
    this.fetchReceiptDetail();
  }
};
</script>

<style scoped>
.tracking-wide { letter-spacing: 0.05em; }
.table-responsive::-webkit-scrollbar { height: 6px; }
.table-responsive::-webkit-scrollbar-thumb { background-color: #cbd5e1; border-radius: 10px; }
.z-3 { z-index: 1030; }

@media print {
  .d-print-none { display: none !important; }
  .print-container { padding-top: 0 !important; margin-top: 0 !important; }
  .card { box-shadow: none !important; border: 1px solid #dee2e6 !important; }
  .bg-light { background-color: transparent !important; }
}

/* --- CSS CHO MODAL THÔNG BÁO --- */
.custom-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 3000; 
  backdrop-filter: blur(4px);
}

.custom-modal {
  width: 100%;
  max-width: 400px;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>