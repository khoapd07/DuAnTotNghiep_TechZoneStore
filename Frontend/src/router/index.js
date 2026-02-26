import { createRouter, createWebHistory } from 'vue-router'

// 1. Import các trang của Khách hàng (User)
import Home from '../components/Home.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import ResetPassword from '../components/ResetPassword.vue'
import MyAccount from '../components/MyAccount.vue'
import Product from '../components/Product.vue'
import ProductDetail from '../components/ProductDetail.vue'
import Cart from '../components/Cart.vue'
import Order from '../components/Order.vue'
import OrderDetail from '../components/OrderDetail.vue'

// 2. Import các trang của Quản trị viên (Admin)
import AdminDashboard from '../components/admin/Dashboard.vue'
import AdminOrderStatus from '../components/admin/OrderStatus.vue'
import AdminProductCRUD from '../components/admin/ProductCRUD.vue'
import AdminUserCRUD from '../components/admin/UserCRUD.vue'

const routes = [
  // --- ROUTES CHO KHÁCH HÀNG ---
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword },
  { path: '/my-account', name: 'MyAccount', component: MyAccount },
  { path: '/products', name: 'Product', component: Product },
  { path: '/product/:id', name: 'ProductDetail', component: ProductDetail }, // Dùng :id để lấy ID sản phẩm trên URL
  { path: '/cart', name: 'Cart', component: Cart },
  { path: '/orders', name: 'Order', component: Order },
  { path: '/order/:id', name: 'OrderDetail', component: OrderDetail }, // Dùng :id để lấy ID đơn hàng

  // --- ROUTES CHO ADMIN ---
  { path: '/admin', name: 'AdminDashboard', component: AdminDashboard },
  { path: '/admin/orders', name: 'AdminOrderStatus', component: AdminOrderStatus },
  { path: '/admin/products', name: 'AdminProductCRUD', component: AdminProductCRUD },
  { path: '/admin/users', name: 'AdminUserCRUD', component: AdminUserCRUD }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router