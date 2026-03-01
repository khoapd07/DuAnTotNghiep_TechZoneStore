<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom py-3">
    <div class="container narrow-container">
      <router-link class="navbar-brand fw-black fs-4 d-flex align-items-center gap-1" to="/">
        TECHZONE <span class="text-neon">//</span>
      </router-link>

      <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mx-auto fw-bold fs-7 gap-3 text-uppercase">
          <li class="nav-item"><router-link class="nav-link text-dark" to="/">Trang chủ</router-link></li>
          <li class="nav-item"><router-link class="nav-link text-dark" to="/products">Sản phẩm</router-link></li>
          <li class="nav-item"><router-link class="nav-link text-dark" to="/blog">Blog</router-link></li>
          <li class="nav-item"><router-link class="nav-link text-dark" to="/support">Hỗ trợ</router-link></li>
        </ul>

        <ul class="navbar-nav align-items-center gap-3">
          
          <li class="nav-item d-none d-lg-block">
            <div class="input-group bg-light rounded-pill px-3 py-2" style="width: 250px;">
              <i class="bi bi-search text-muted me-2 mt-1"></i>
              <input type="text" class="form-control border-0 bg-transparent p-0 fs-7 shadow-none" placeholder="Tìm kiếm thiết bị...">
            </div>
          </li>

          <li class="nav-item dropdown cart-dropdown">
            <router-link to="/cart" class="nav-link position-relative text-dark p-0">
              <i class="bi bi-cart2 fs-5"></i>
              <span v-if="isLoggedIn && cartCount > 0" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-neon text-dark" style="font-size: 0.6rem;">
                {{ cartCount }}
              </span>
            </router-link>

            <div class="cart-content" v-if="isLoggedIn">
              <div v-if="cartItems.length === 0" class="text-center py-4 text-muted fs-7">
                Giỏ hàng trống 😢
              </div>

              <div v-else>
                <div v-for="item in cartItems" :key="item.maGH" class="cart-item d-flex align-items-center py-2 border-bottom">
                  <img :src="item.sanPham.imgUrl" class="rounded object-fit-cover me-2" style="width: 50px; height: 50px;" alt="SP">
                  <div class="flex-grow-1">
                    <div class="fw-bold fs-7 text-truncate" style="max-width: 180px;">{{ item.sanPham.tenSP }}</div>
                    <div class="d-flex justify-content-between align-items-center mt-1">
                      <small class="text-muted fs-8">SL: {{ item.soLuong }}</small>
                      <div class="text-neon fw-bold fs-7">{{ formatCurrency(item.sanPham.donGiaBan * item.soLuong) }}</div>
                    </div>
                  </div>
                </div>
                <div class="cart-footer mt-3">
                  <router-link to="/cart" class="btn btn-dark btn-sm w-100 fw-bold border-neon text-neon">Xem giỏ hàng</router-link>
                </div>
              </div>
            </div>
          </li>

          <li class="nav-item dropdown">
            <a class="nav-link p-0 text-dark dropdown-toggle-hide-arrow" href="#" role="button" data-bs-toggle="dropdown">
              <i class="bi bi-person fs-5"></i>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-3 fs-7">
              <template v-if="isLoggedIn">
                <li><h6 class="dropdown-header fw-bold text-dark">Xin chào, {{ userInfo }}</h6></li>
                <li v-if="isAdmin">
                    <router-link class="dropdown-item fw-bold text-neon-dark" to="/admin">
                      <i class="bi bi-speedometer2 me-2"></i>Trang quản trị
                    </router-link>
                </li>
                <li><router-link class="dropdown-item" to="/profile">Tài khoản</router-link></li>
                <li><router-link class="dropdown-item" to="/orders">Đơn hàng</router-link></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-danger fw-bold" href="#" @click.prevent="handleLogout">Đăng xuất</a></li>
              </template>
              <template v-else>
                <li><router-link class="dropdown-item fw-bold" to="/login">Đăng nhập</router-link></li>
                <li><router-link class="dropdown-item" to="/register">Đăng ký</router-link></li>
              </template>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const isLoggedIn = ref(false);
const userInfo = ref('');
const cartItems = ref([]);
const isAdmin = ref(false);

const checkLoginStatus = () => {
  const token = localStorage.getItem('jwt_token');
  const userRolesString = localStorage.getItem('user_role'); 

  if (token) {
    isLoggedIn.value = true;
    userInfo.value = localStorage.getItem('user_info') || 'User';
    
    if (userRolesString) {
      try {
        const roles = JSON.parse(userRolesString);
        isAdmin.value = roles.includes('ROLE_ADMIN') || roles.includes('ROLE_STAFF');
      } catch (e) {
        console.error("Lỗi đọc quyền user:", e);
        isAdmin.value = false;
      }
    } else {
      isAdmin.value = false;
    }
    fetchMiniCart(); 
  } else {
    isLoggedIn.value = false;
    userInfo.value = '';
    isAdmin.value = false;
    cartItems.value = [];
  }
};

const fetchMiniCart = async () => {
  try {
    const token = localStorage.getItem('jwt_token');
    const response = await axios.get('http://localhost:8080/api/cart', {
      headers: { Authorization: `Bearer ${token}` }
    });
    cartItems.value = response.data;
  } catch (error) {
    console.error("Lỗi tải mini cart:", error);
  }
};

const cartCount = computed(() => {
  return cartItems.value.length;
});

const handleLogout = () => {
  localStorage.removeItem('jwt_token');
  localStorage.removeItem('user_role');
  localStorage.removeItem('user_info');
  isLoggedIn.value = false;
  cartItems.value = [];
  window.dispatchEvent(new Event('auth-change'));
  router.push('/login');
};

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

onMounted(() => {
  checkLoginStatus();
  window.addEventListener('auth-change', checkLoginStatus);
});

onUnmounted(() => {
  window.removeEventListener('auth-change', checkLoginStatus);
});
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.text-neon-dark { color: #00cc29 !important; }

/* Custom Container */
.narrow-container {
  max-width: 1200px !important; 
  margin-left: auto;
  margin-right: auto;
}

/* Ẩn mũi tên mặc định của Dropdown User */
.dropdown-toggle-hide-arrow::after {
  display: none !important;
}

/* CSS Mini Cart */
.cart-dropdown { position: relative; }
.cart-content {
  display: none;
  position: absolute;
  right: -10px;
  top: 100%;
  width: 320px;
  background-color: #fff;
  border: 1px solid #EAEAEA;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.08);
  padding: 15px;
  z-index: 1000;
}
.cart-dropdown:hover .cart-content { display: block; }

/* Fix lỗi di chuột bị mất menu Dropdown */
.dropdown {
  position: relative; /* Đảm bảo menu con căn theo thẻ cha này */
}

.dropdown:hover > .dropdown-menu {
  display: block;
  margin-top: 0; /* Xóa margin mặc định của Bootstrap gây ra khoảng hở */
  top: 100%; /* Ép menu dính sát ngay mép dưới của header */
}

/* Tùy chọn: Thêm một vùng đệm tàng hình để nối liền icon và menu */
.dropdown-menu::before {
  content: "";
  position: absolute;
  top: -15px; /* Kéo vùng cảm ứng lên trên 15px */
  left: 0;
  width: 100%;
  height: 15px;
  background: transparent;
}
.nav-link {
  transition: color 0.2s;
}
.nav-link:hover {
  color: #00FF33 !important;
}
</style>