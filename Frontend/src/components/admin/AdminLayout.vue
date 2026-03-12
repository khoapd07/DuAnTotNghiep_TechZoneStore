<template>
  <div class="admin-layout d-flex bg-light-gray min-vh-100">
    
    <aside class="sidebar bg-white border-end d-flex flex-column sticky-top" style="width: 220px; height: 100vh;">
      <div class="p-3 border-bottom">
        <router-link to="/admin" class="text-decoration-none text-dark">
          <h4 class="fw-black text-dark m-0 d-flex align-items-center gap-1 fs-5">
            TECHZONE <span class="text-neon fs-6">//</span>
          </h4>
        </router-link>
        
        <small class="text-muted fw-bold" style="font-size: 0.65rem; letter-spacing: 1px;">QUẢN TRỊ</small>
      </div>

      <div class="p-2 flex-grow-1 overflow-auto">
        <ul class="nav flex-column gap-1 custom-nav">
          <li class="nav-item">
            <router-link to="/admin" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" exact-active-class="active">
              <i class="bi bi-grid-1x2-fill"></i> <span class="fw-bold fs-7">Tổng quan</span>
            </router-link>
          </li>
          
          <li class="nav-item">
            <router-link to="/admin/products" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-box-seam"></i> <span class="fw-bold fs-7">Sản phẩm</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/categories" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-tags"></i> <span class="fw-bold fs-7">Loại sản phẩm</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/brands" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-award"></i> <span class="fw-bold fs-7">Thương hiệu</span>
            </router-link>
          </li>

          <li class="nav-item">
            <router-link to="/admin/orders" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-receipt"></i> <span class="fw-bold fs-7">Đơn hàng</span>
            </router-link>
          </li>

          <li class="nav-item">
            <router-link to="/admin/customers" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-people"></i> <span class="fw-bold fs-7">Khách hàng</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/admin/employees" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-person-vcard"></i> <span class="fw-bold fs-7">Nhân viên</span>
            </router-link>
          </li>

          <li class="nav-item mt-2">
            <router-link to="/admin/report" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-bar-chart-line"></i> <span class="fw-bold fs-7">Báo cáo</span>
            </router-link>
          </li>

          <li class="nav-item">
            <router-link to="/admin/vouchers" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-ticket-detailed"></i> <span class="fw-bold fs-7">Mã giảm giá</span>
            </router-link>
          </li>

          <li class="nav-item">
            <router-link to="/admin/blogs" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-journal-text"></i> <span class="fw-bold fs-7">Blog</span>
            </router-link>
          </li>

          <li class="nav-item">
            <router-link to="/admin/slideshow" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark" active-class="active">
              <i class="bi bi-images"></i> <span class="fw-bold fs-7">Slide Show</span>
            </router-link>
          </li>
        </ul>
      </div>

      <div class="p-3 border-top">
        <router-link to="/" class="nav-link rounded-3 d-flex align-items-center gap-3 text-dark mb-3 px-2">
          <i class="bi bi-arrow-left"></i><span class="fw-bold fs-7">Quay lại trang chủ</span>
        </router-link>
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

    <main class="flex-grow-1 p-3 p-md-4 overflow-auto">
      <router-view></router-view>
    </main>

  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap');

.admin-layout { 
  font-family: 'Inter', system-ui, -apple-system, sans-serif; 
}
.bg-light-gray { background-color: #F4F6F8; }
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }

.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }

/* Sidebar Nav Styles */
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
</style>