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
              <span v-if="cartCount > 0" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-neon text-dark" style="font-size: 0.6rem;">
                {{ cartCount }}
              </span>
            </router-link>

            <div class="cart-content">
              <div v-if="cartItems.length === 0" class="text-center py-4 text-muted fs-7">
                Giỏ hàng trống 😢
              </div>

              <div v-else>
                <div v-for="item in cartItems" :key="item.maGH || item.id" class="cart-item d-flex align-items-center py-2 border-bottom">
                  <img :src="item.img" class="rounded object-fit-cover me-2" style="width: 50px; height: 50px;" alt="SP">
                  <div class="flex-grow-1">
                    <div class="fw-bold fs-7 text-truncate" style="max-width: 180px;">{{ item.name }}</div>
                    <div class="d-flex justify-content-between align-items-center mt-1">
                      <small class="text-muted fs-8">SL: {{ item.quantity }}</small>
                      <div class="text-neon fw-bold fs-7">{{ formatCurrency(item.price * item.quantity) }}</div>
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
                <li v-if="isShipper">
                    <router-link class="dropdown-item fw-bold text-primary" to="/shipping">
                      <i class="bi bi-truck me-2"></i>Trang vận chuyển
                    </router-link>
                </li>
                <li><router-link class="dropdown-item" to="/my-account">Tài khoản</router-link></li>
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
const isShipper = ref(false);

const checkLoginStatus = () => {
  const token = localStorage.getItem('jwt_token');
  const userRolesString = localStorage.getItem('user_role'); 
  const userInfoString = localStorage.getItem('user_info'); 

  if (token) {
    isLoggedIn.value = true;
    
    // XỬ LÝ LẤY TÊN HIỂN THỊ
    if (userInfoString) {
      try {
        const parsedUser = JSON.parse(userInfoString);
        userInfo.value = parsedUser.fullName || parsedUser.username || 'User';
      } catch (e) {
        userInfo.value = 'User';
      }
    } else {
      userInfo.value = 'User';
    }
    
    // XỬ LÝ QUYỀN (ROLE)
    if (userRolesString) {
      try {
        const roles = JSON.parse(userRolesString);
        isAdmin.value = roles.includes('ROLE_ADMIN') || roles.includes('ROLE_STAFF');
        isShipper.value = roles.includes('ROLE_SHIPPER');
      } catch (e) {
        isAdmin.value = false;
      }
    } else if (userInfoString) {
      try {
        const parsedUser = JSON.parse(userInfoString);
        isAdmin.value = parsedUser.role === 'Admin' || parsedUser.role === 'Staff';
        isShipper.value = parsedUser.role === 'Shipper';
      } catch (e) {
        isAdmin.value = false;
        isShipper.value = false;
      }
    } else {
      isAdmin.value = false;
    }
    
  } else {
    // KHÁCH VÃNG LAI
    isLoggedIn.value = false;
    userInfo.value = '';
    isAdmin.value = false;
    isShipper.value = false;
  }
  
  // LƯU Ý MỚI: Dù đăng nhập hay chưa đều phải gọi hàm lấy giỏ hàng
  fetchMiniCart(); 
};

const fetchMiniCart = async () => {
  try {
    const token = localStorage.getItem('jwt_token');
    
    if (token) {
      // 1. NẾU CÓ TOKEN (Đã đăng nhập) => Gọi API lên Backend
      const userInfoString = localStorage.getItem('user_info');
      if (!userInfoString) return;

      const parsedUser = JSON.parse(userInfoString);
      const customerId = parsedUser.userId; 

      if (!customerId) return;

      const response = await axios.get(`http://localhost:8080/api/cart/${customerId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      // ĐÃ SỬA: Lấy mảng items từ response giống bên Cart.vue
      const backendItems = response.data.items || response.data; // Phòng hờ nếu trả về thẳng list
      
      // Map dữ liệu cho giống với template mới sửa ở trên
      cartItems.value = backendItems.map(bItem => ({
        productId: bItem.productId,
        name: bItem.productName,
        price: bItem.salePrice || bItem.price, // Ưu tiên giá sale
        quantity: bItem.quantity,
        img: bItem.imageUrl
      }));

    } else {
      // 2. NẾU KHÔNG CÓ TOKEN (Khách vãng lai) => Lấy từ localStorage Frontend
      const guestCart = localStorage.getItem('guest_cart');
      if (guestCart) {
        cartItems.value = JSON.parse(guestCart);
      } else {
        cartItems.value = [];
      }
    }
  } catch (error) {
    console.error("Lỗi tải mini cart:", error);
    cartItems.value = []; 
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
  
  // Chuyển về load giỏ hàng khách thay vì clear sạch
  fetchMiniCart(); 
  
  window.dispatchEvent(new Event('auth-change'));
  router.push('/login');
};

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

onMounted(() => {
  checkLoginStatus();
  window.addEventListener('auth-change', checkLoginStatus);
  window.addEventListener('cart-updated', fetchMiniCart);
});

onUnmounted(() => {
  window.removeEventListener('auth-change', checkLoginStatus);
  window.removeEventListener('cart-updated', fetchMiniCart);
});
</script>

<style scoped>
nav.navbar {
  position: sticky; /* Giữ thanh header đứng yên khi cuộn trang */
  top: 0;
  z-index: 1050 !important; /* Đảm bảo luôn đè lên mọi thứ (kể cả cột tóm tắt đơn hàng z-index 1020) */
}
/* Phần CSS bạn cứ giữ nguyên giống file cũ của bạn */
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.text-neon { color: #00FF33 !important; }
.bg-neon { background-color: #00FF33 !important; }
.text-neon-dark { color: #00cc29 !important; }

.narrow-container {
  max-width: 1200px !important; 
  margin-left: auto;
  margin-right: auto;
}

.dropdown-toggle-hide-arrow::after { display: none !important; }

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

.dropdown { position: relative; }
.dropdown:hover > .dropdown-menu {
  display: block;
  margin-top: 0; 
  top: 100%; 
}

.dropdown-menu::before {
  content: "";
  position: absolute;
  top: -15px; 
  left: 0;
  width: 100%;
  height: 15px;
  background: transparent;
}
.nav-link { transition: color 0.2s; }
.nav-link:hover { color: #00FF33 !important; }
</style>