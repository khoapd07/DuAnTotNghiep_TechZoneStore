<template>
  <div class="bg-white min-vh-100 py-5">
    <main class="container-fluid" style="max-width: 1400px;">
      
      <header class="d-flex justify-content-between align-items-center mb-5">
        <div>
          <h2 class="display-6 fw-bolder text-dark mb-1" style="font-family: 'Space Grotesk', sans-serif;">
            Quản Lý Nhà Cung Cấp
          </h2>
          <p class="text-muted mb-0">Hệ sinh thái đối tác chiến lược TechZone</p>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div class="input-group input-group-lg shadow-sm" style="width: 320px;">
            <span class="input-group-text bg-light border-end-0" id="search-icon">
              <i class="bi bi-search text-muted"></i>
            </span>
            <input 
              type="text" 
              class="form-control border-start-0 bg-light fs-6" 
              placeholder="Tìm kiếm theo tên, ID..." 
              v-model="searchQuery"
            />
          </div>
        </div>
      </header>

      <div class="card border-0 shadow-sm rounded-4 mb-4" style="background: rgba(255, 255, 255, 1); border: 1px solid rgba(171, 173, 174, 0.2) !important;">
        <div class="card-body p-4 p-md-5">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="fw-bold mb-0" style="font-family: 'Space Grotesk', sans-serif;">Danh Sách Đối Tác</h4>
            
            <button @click="openAddModal" class="btn btn-success px-4 py-2 fw-bold d-flex align-items-center gap-2 shadow-sm rounded-3">
              <i class="bi bi-person-plus-fill"></i> Thêm Nhà Cung Cấp
            </button>
          </div>

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead>
                <tr class="text-muted" style="font-size: 12px; letter-spacing: 0.1em;">
                  <th class="pb-3 text-uppercase border-bottom">ID</th>
                  <th class="pb-3 text-uppercase border-bottom">Nhà cung cấp</th>
                  <th class="pb-3 text-uppercase border-bottom">Người liên hệ</th>
                  <th class="pb-3 text-uppercase border-bottom">Số điện thoại</th>
                  <th class="pb-3 text-uppercase border-bottom">Email</th>
                  <th class="pb-3 text-uppercase border-bottom">Địa chỉ</th>
                  <th class="pb-3 text-uppercase border-bottom">Trạng thái</th>
                  <th class="pb-3 border-bottom text-end">Thao tác</th>
                </tr>
              </thead>
              <tbody class="border-top-0">
                <tr v-if="filteredSuppliers.length === 0">
                  <td colspan="8" class="text-center py-4 text-muted">Không tìm thấy dữ liệu.</td>
                </tr>

                <tr v-for="(supplier, index) in filteredSuppliers" :key="index" class="position-relative group-hover-show">
                  <td class="py-3 text-muted font-monospace" style="font-size: 13px;">SUP-{{ supplier.id }}</td>
                  <td class="py-3"><p class="fw-bold text-dark mb-0">{{ supplier.name }}</p></td>
                  <td class="py-3 text-dark" style="font-size: 14px;">{{ supplier.contact }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.phone }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.email }}</td>
                  <td class="py-3 text-muted" style="font-size: 14px;">{{ supplier.address }}</td>
                  <td class="py-3">
                    <span 
                      class="badge rounded-pill border py-2 px-3 text-uppercase"
                      :class="supplier.status ? 'bg-success-subtle text-success border-success-subtle' : 'bg-secondary-subtle text-secondary border-secondary-subtle'"
                      style="font-size: 10px; font-weight: 700;"
                    >
                      {{ supplier.status ? 'Đang hợp tác' : 'Ngừng hợp tác' }}
                    </span>
                  </td>
                  <td class="py-3 text-end">
                    <div class="d-flex justify-content-end gap-1 action-buttons">
                      
                      <button @click="openEditModal(supplier)" class="btn btn-sm btn-light text-primary hover-bg-primary rounded-circle" title="Sửa">
                        <i class="bi bi-pencil-square"></i>
                      </button>
                      <button @click="deleteSupplier(supplier.id)" class="btn btn-sm btn-light text-danger hover-bg-danger rounded-circle" title="Ngừng hợp tác (Xóa)">
                        <i class="bi bi-trash"></i>
                      </button>
                      
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </main>

    <div class="modal fade" id="supplierModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold" style="font-family: 'Space Grotesk', sans-serif;">
              {{ isEdit ? 'Cập Nhật Nhà Cung Cấp' : 'Thêm Nhà Cung Cấp Mới' }}
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveSupplier">
              <div class="mb-3">
                <label class="form-label fw-semibold small text-muted">Tên nhà cung cấp <span class="text-danger">*</span></label>
                <input type="text" class="form-control bg-light" v-model="formData.name" required placeholder="Nhập tên đối tác...">
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Người liên hệ</label>
                  <input type="text" class="form-control bg-light" v-model="formData.contact" placeholder="Tên đại diện...">
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold small text-muted">Số điện thoại</label>
                  <input type="text" class="form-control bg-light" v-model="formData.phone" placeholder="09xx...">
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold small text-muted">Email</label>
                <input type="email" class="form-control bg-light" v-model="formData.email" placeholder="email@example.com">
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold small text-muted">Địa chỉ</label>
                <textarea class="form-control bg-light" v-model="formData.address" rows="2" placeholder="Nhập địa chỉ chi tiết..."></textarea>
              </div>
              
              <div class="mb-3 form-check form-switch" v-if="isEdit">
                <input class="form-check-input" type="checkbox" id="statusSwitch" v-model="formData.status">
                <label class="form-check-label fw-semibold" for="statusSwitch">Đang hợp tác</label>
              </div>

              <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="button" class="btn btn-light fw-bold" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success fw-bold px-4">
                  {{ isEdit ? 'Cập Nhật' : 'Lưu Thay Đổi' }}
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
// Import bootstrap để dùng Modal
import * as bootstrap from 'bootstrap';

export default {
  name: "SupplierManagement",
  data() {
    return {
      searchQuery: "",
      suppliers: [],
      
      // Biến phục vụ cho Modal Thêm/Sửa
      modalInstance: null,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        contact: '',
        phone: '',
        email: '',
        address: '',
        status: true
      },
      
      // Đổi port nếu Backend của bạn đang chạy port khác (VD: 8080)
      apiUrl: 'http://localhost:8080/api/admin/suppliers' 
    };
  },
  computed: {
    filteredSuppliers() {
      if (!this.searchQuery) return this.suppliers;
      const lowerQuery = this.searchQuery.toLowerCase();
      return this.suppliers.filter(
        sup => 
          (sup.name && sup.name.toLowerCase().includes(lowerQuery)) || 
          (sup.id && sup.id.toString().includes(lowerQuery))
      );
    }
  },
  methods: {
    // 1. Lấy danh sách (GET)
    async fetchSuppliers() {
      try {
        const response = await axios.get(this.apiUrl);
        this.suppliers = response.data;
      } catch (error) {
        console.error("Lỗi khi tải danh sách:", error);
      }
    },

    // 2. Mở form Thêm Mới
    openAddModal() {
      this.isEdit = false;
      this.formData = { id: null, name: '', contact: '', phone: '', email: '', address: '', status: true };
      this.modalInstance.show();
    },

    // 3. Mở form Sửa
    openEditModal(supplier) {
      this.isEdit = true;
      // Copy dữ liệu ra form để tránh sửa trực tiếp vào bảng khi chưa bấm Lưu
      this.formData = { ...supplier };
      this.modalInstance.show();
    },

    // 4. Lưu dữ liệu (POST hoặc PUT)
    async saveSupplier() {
      try {
        // Backend yêu cầu biến 'supplierName' thay vì 'name', ta map lại cho đúng Entity/DTO
        const payload = {
          supplierName: this.formData.name,
          contactName: this.formData.contact,
          phoneNumber: this.formData.phone,
          email: this.formData.email,
          address: this.formData.address,
          status: this.formData.status
        };

        if (this.isEdit) {
          // Gọi API Cập nhật
          await axios.put(`${this.apiUrl}/${this.formData.id}`, payload);
          alert('Cập nhật nhà cung cấp thành công!');
        } else {
          // Gọi API Thêm mới
          await axios.post(this.apiUrl, payload);
          alert('Thêm nhà cung cấp thành công!');
        }
        
        this.modalInstance.hide();
        this.fetchSuppliers(); // Load lại bảng
      } catch (error) {
        console.error("Lỗi khi lưu:", error);
        alert('Có lỗi xảy ra, vui lòng kiểm tra lại!');
      }
    },

    // 5. Xóa / Ngừng hợp tác (DELETE)
    async deleteSupplier(id) {
      if (confirm('Bạn có chắc chắn muốn ngừng hợp tác với nhà cung cấp này không?')) {
        try {
          await axios.delete(`${this.apiUrl}/${id}`);
          alert('Đã cập nhật trạng thái ngừng hợp tác!');
          this.fetchSuppliers(); // Load lại bảng
        } catch (error) {
          console.error("Lỗi khi xóa:", error);
          alert('Không thể thực hiện thao tác này!');
        }
      }
    }
  },
  mounted() {
    this.fetchSuppliers();
    // Khởi tạo Bootstrap Modal
    this.modalInstance = new bootstrap.Modal(document.getElementById('supplierModal'), {
      keyboard: false
    });
  }
};
</script>

<style scoped>
.action-buttons {
  opacity: 0;
  transition: opacity 0.2s ease-in-out;
}
.group-hover-show:hover .action-buttons {
  opacity: 1;
}

@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap');

.hover-bg-primary:hover {
  background-color: #0d6efd !important;
  color: #fff !important;
}
.hover-bg-danger:hover {
  background-color: #dc3545 !important;
  color: #fff !important;
}
</style>