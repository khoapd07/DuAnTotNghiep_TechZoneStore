<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    

    <main class="flex-grow-1 p-4 overflow-auto">
      
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-1 fs-8 fw-bold">
              <li class="breadcrumb-item"><a href="#" class="text-muted text-decoration-none">Admin</a></li>
              <li class="breadcrumb-item active text-dark" aria-current="page">Khách hàng</li>
            </ol>
          </nav>
          <h3 class="fw-black text-dark mb-1 fs-4">Quản Lý Người Dùng</h3>
          <p class="text-muted m-0 fs-7">Theo dõi, quản lý và phân quyền thành viên trong hệ thống.</p>
        </div>
        <div class="d-flex align-items-center gap-3">
          <button class="btn btn-light rounded-circle shadow-sm" style="width: 40px; height: 40px;">
            <i class="bi bi-bell"></i>
          </button>
          <button class="btn btn-neon fw-bold fs-7 text-dark rounded-3 d-flex align-items-center gap-2 px-3 py-2 shadow-sm">
            <i class="bi bi-person-plus-fill"></i> Thêm quản trị viên
          </button>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1">Tổng người dùng</p>
              <h2 class="fw-black text-dark m-0">12,840</h2>
              <span class="text-success fs-8 fw-bold mt-2 d-inline-block"><i class="bi bi-arrow-up-right"></i> +12% so với tháng trước</span>
            </div>
            <div class="bg-success-subtle text-success rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-people-fill fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1">Khách hàng mới (tháng này)</p>
              <h2 class="fw-black text-dark m-0">+450</h2>
              <span class="text-success fs-8 fw-bold mt-2 d-inline-block"><i class="bi bi-arrow-up-right"></i> +5% tăng trưởng</span>
            </div>
            <div class="bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-person-plus fs-4"></i>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 h-100 d-flex flex-row justify-content-between align-items-center">
            <div>
              <p class="text-muted fs-8 fw-bold mb-1">Tài khoản bị khóa</p>
              <h2 class="fw-black text-dark m-0">12</h2>
              <span class="text-danger fs-8 fw-bold mt-2 d-inline-block"><i class="bi bi-arrow-down-right"></i> Giảm 2% tỷ lệ vi phạm</span>
            </div>
            <div class="bg-danger-subtle text-danger rounded-3 d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
              <i class="bi bi-person-x-fill fs-4"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm rounded-4 p-0">
        <div class="p-3 border-bottom d-flex gap-2 bg-white rounded-top-4">
          <div class="input-group bg-light border rounded-3 overflow-hidden flex-grow-1">
            <span class="input-group-text bg-transparent border-0 text-muted px-3"><i class="bi bi-search"></i></span>
            <input type="text" class="form-control border-0 bg-transparent shadow-none fs-7 px-1" placeholder="Tìm kiếm theo tên, email hoặc username...">
          </div>
          <select class="form-select form-select-sm border bg-white fw-bold text-dark shadow-none fs-7" style="width: 150px;">
            <option>Tất cả vai trò</option>
            <option>Admin</option>
            <option>Khách hàng</option>
          </select>
          <select class="form-select form-select-sm border bg-white fw-bold text-dark shadow-none fs-7" style="width: 140px;">
            <option>Trạng thái</option>
            <option>Đang hoạt động</option>
            <option>Bị khóa</option>
          </select>
          <button class="btn btn-light border fw-bold fs-7 text-dark d-flex align-items-center gap-2 px-3">
            <i class="bi bi-funnel"></i> Lọc
          </button>
        </div>
        
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="border-bottom">
              <tr class="text-muted fs-8 text-uppercase">
                <th scope="col" class="py-3 px-4 fw-bold border-0">Thành viên</th>
                <th scope="col" class="py-3 fw-bold border-0">Email / Username</th>
                <th scope="col" class="py-3 fw-bold border-0">Vai trò</th>
                <th scope="col" class="py-3 fw-bold border-0">Ngày tham gia</th>
                <th scope="col" class="py-3 fw-bold border-0">Trạng thái</th>
                <th scope="col" class="py-3 fw-bold border-0 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in userList" :key="user.id" class="border-bottom-dashed">
                <td class="px-4 py-3">
                  <div class="d-flex flex-column">
                    <span class="fw-bold fs-7 text-dark" :class="{'text-decoration-line-through text-muted': user.status === 'BỊ KHÓA'}">
                      {{ user.name }}
                    </span>
                    <span class="fs-8 text-muted" :class="{'text-decoration-line-through': user.status === 'BỊ KHÓA'}">
                      @{{ user.username }}
                    </span>
                  </div>
                </td>
                <td class="py-3">
                  <span class="fs-7 text-dark fw-medium" :class="{'text-decoration-line-through text-muted': user.status === 'BỊ KHÓA'}">
                    {{ user.email }}
                  </span>
                </td>
                <td class="py-3">
                  <span class="badge bg-light text-dark border rounded-pill px-3 py-1 fs-8 fw-bold">
                    {{ user.role }}
                  </span>
                </td>
                <td class="text-muted fs-7 py-3">{{ user.joinDate }}</td>
                <td class="py-3">
                  <div class="d-flex align-items-center gap-2 fs-8 fw-bold" :class="user.status === 'BỊ KHÓA' ? 'text-danger' : 'text-success'">
                    <i class="bi bi-circle-fill" style="font-size: 6px;"></i> 
                    {{ user.status }}
                  </div>
                </td>
                <td class="text-center py-3">
                  <div class="d-flex justify-content-center gap-3">
                    <button class="btn btn-link p-0 text-primary shadow-none" title="Chỉnh sửa"><i class="bi bi-pencil-fill"></i></button>
                    <button class="btn btn-link p-0 text-warning shadow-none" title="Khôi phục mật khẩu"><i class="bi bi-arrow-counterclockwise"></i></button>
                    <button v-if="user.status === 'ĐANG HOẠT ĐỘNG'" class="btn btn-link p-0 text-danger shadow-none" title="Khóa tài khoản">
                      <i class="bi bi-slash-circle"></i>
                    </button>
                    <button v-else class="btn btn-link p-0 text-success shadow-none" title="Mở khóa tài khoản">
                      <i class="bi bi-check-circle-fill"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top bg-white rounded-bottom-4">
          <span class="text-muted fs-8">Hiển thị 1-10 của 12,840 người dùng</span>
          <div class="d-flex gap-1">
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-left"></i></button>
            <button class="btn btn-sm btn-neon text-dark px-3 fw-bold">1</button>
            <button class="btn btn-sm btn-white border px-3 fw-bold">2</button>
            <button class="btn btn-sm btn-white border px-3 fw-bold">3</button>
            <span class="px-2 text-muted">...</span>
            <button class="btn btn-sm btn-white border px-3 fw-bold">1284</button>
            <button class="btn btn-sm btn-light border px-2"><i class="bi bi-chevron-right"></i></button>
          </div>
        </div>
      </div>
      
      <footer class="text-center mt-5 mb-3 text-muted fs-8">
        © 2024 TechZone Management System. All rights reserved.
      </footer>

    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Mock data khớp với thiết kế
const userList = ref([
  { id: 1, name: 'Nguyễn Văn A', username: 'nguyen_tech', email: 'nguyenvana@gmail.com', role: 'Admin', joinDate: '12/05/2023', status: 'ĐANG HOẠT ĐỘNG' },
  { id: 2, name: 'Trần Thị B', username: 'btran_customer', email: 'tranb@outlook.com', role: 'Khách hàng', joinDate: '05/01/2024', status: 'ĐANG HOẠT ĐỘNG' },
  { id: 3, name: 'Lê Văn C', username: 'levanc_blocked', email: 'levanc@company.vn', role: 'Khách hàng', joinDate: '20/12/2022', status: 'BỊ KHÓA' },
  { id: 4, name: 'Hoàng Long', username: 'longh_admin', email: 'long.hoang@techzone.vn', role: 'Admin', joinDate: '15/02/2024', status: 'ĐANG HOẠT ĐỘNG' }
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
.text-decoration-line-through { text-decoration: line-through; }
</style>