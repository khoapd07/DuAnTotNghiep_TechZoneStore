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
import OrderDetail from '../components/OrderDetail.vue';
import Checkout from '../components/Checkout.vue'
import Blog from '../components/Blog.vue'
import BlogDetail from '../components/BlogDetail.vue'
import ForgotPassword from '../components/ForgotPassword.vue'
import Historyproduct from '../components/Historyproduct.vue'
import OrderVote from '../components/OrderVote.vue'
import Order from '../components/Order.vue'
import Support from '../components/Support.vue'
// 2. Import các trang của Quản trị viên (Admin)
import AdminLayout from '../components/admin/AdminLayout.vue'
import AdminDashboard from '../components/admin/Dashboard.vue'
import AdminOrderStatus from '../components/admin/OrderStatus.vue'
import AdminProductCRUD from '../components/admin/ProductCRUD.vue'
import Customer from '../components/admin/Customer.vue'
import Employee from '../components/admin/Employee.vue'
import AdminCategoryCRUD from '../components/admin/CategoryCRUD.vue'
import AdminBrandCRUD from '../components/admin/BrandCRUD.vue'
import Report from '../components/admin/Report.vue'
import VoucherCRUD from '../components/admin/VoucherCRUD.vue'

import BlogCRUD from '../components/admin/BlogCRUD.vue'
import SlideShowHome from '../components/admin/SlideShowHome.vue'
import AdminChat from '../components/admin/AdminChat.vue'
import ImportReceiptDetail from '../components/admin/ImportReceiptDetail.vue'
import Suppliers from '../components/admin/Suppliers.vue'
import ImportReceiptList from '../components/admin/ImportReceiptList.vue'



//import cho shipper
import Shipping from '../components/shipping/Shipping.vue'
// import { patch } from '@n8n/chat/dist/api/generic.js'


const routes = [
  // --- ROUTES CHO KHÁCH HÀNG ---
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword },
  { path: '/my-account', name: 'MyAccount', component: MyAccount },
  { path: '/products', name: 'Product', component: Product },

  { path: '/product/:id', name: 'ProductDetail', component: ProductDetail }, 

  { path: '/cart', name: 'Cart', component: Cart },
  { path: '/orders', name: 'Order', component: Order },
  { path: '/checkout', name: 'Checkout', component: Checkout },
  { path: '/order/:id', name: 'OrderDetail', component: OrderDetail },
  { path: '/blog', name: 'Blog', component: Blog },
  { path: '/blog/:id', name: 'BlogDetail', component: BlogDetail },
  // { path: '/Blogdetail', name: 'Blogdetail', component: Blogdetail },
  { path: '/ForgotPassword', name: 'ForgotPassword', component: ForgotPassword },
  { path: '/Historyproduct', name: 'Historyproduct', component: Historyproduct},
  { path: '/OrderVote', name: 'OrderVote', component: OrderVote},
  { path: '/support', name: 'Support', component: Support },





  // --- ROUTES CHO ADMIN ---
  
  { 
    path: '/admin', 
    component: AdminLayout, // Khai báo Layout cha
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { 
        path: '', // Khi vào /admin sẽ load Dashboard
        name: 'AdminDashboard', 
        component: AdminDashboard ,
        meta: { requiresStrictAdmin: true }
      },
      { 
        path: 'blogs',
        name: 'AdminBlogCRUD',
        component: BlogCRUD
      },
      {
        path: 'slideshow',
        name: 'SlideShowHome',
        component: SlideShowHome
      },
      { 
        path: 'orders', // URL sẽ là /admin/orders
        name: 'AdminOrderStatus', 
        component: AdminOrderStatus 
      },
      { 
        path: 'products', 
        name: 'AdminProductCRUD', 
        component: AdminProductCRUD 
      },
      { 
        path: 'customers', 
        name: 'Customer', 
        component: Customer ,
        meta: { requiresStrictAdmin: true }
      },
      {
        path: 'employees',
        name: 'Employee',
        component: Employee,
        meta: { requiresStrictAdmin: true }
      },
      { 
        path: 'categories', 
        name: 'AdminCategoryCRUD', 
        component: AdminCategoryCRUD 
      },
      { 
        path: 'brands', 
        name: 'AdminBrandCRUD', 
        component: AdminBrandCRUD 
      },
      { 
        path: 'report', 
        name: 'Report', 
        component: Report ,
        meta: { requiresStrictAdmin: true }
      },
      {
        path: 'vouchers',
        name: 'VoucherCRUD',
        component: VoucherCRUD
      },
      {
        path: 'chat',
        name: 'AdminChat',
        component: AdminChat
      },
    {
        path: 'import-receipts',
        name: 'ImportReceiptList',
        component: ImportReceiptList 
      },
      {
        path: 'ImportReceiptDetail/:id', 
        name: 'ImportReceiptDetail',
        component: ImportReceiptDetail 
      },
      { 
        path: 'suppliers',
        name: 'Suppliers',
        component: Suppliers
      }
    ]
  },

  // --- ROUTES CHO SHIPPER ---
  {
    path: '/shipping',
    name: 'Shipping',
    component: Shipping,
    meta: { requiresAuth: true, requiresShipper: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  // Lấy các yêu cầu bảo mật từ meta của route
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin);
  const requiresStrictAdmin = to.matched.some(record => record.meta.requiresStrictAdmin);
  const requiresShipper = to.matched.some(record => record.meta.requiresShipper);

  // Lấy thông tin user từ LocalStorage
  const token = localStorage.getItem('jwt_token');
  const userStr = localStorage.getItem('user_info');
  let user = null;
  if (userStr) {
    try { user = JSON.parse(userStr); } catch (e) {}
  }

  // Khai báo các biến kiểm tra quyền (Hỗ trợ cả chuỗi 'Admin' hoặc roleId = 2)
  const role = user ? (user.role || user.roleId) : null;
  const isUserAdmin = role === 'Admin' || role === 2;
  const isUserStaff = role === 'Staff' || role === 1;
  const isUserShipper = role === 'Shipper' || role === 3;

  // 1. Chưa đăng nhập
  if (requiresAuth && !token) {
    // alert("Vui lòng đăng nhập để tiếp tục!");
    return next('/login');
  }

  // 2. Chặn trang dành RIÊNG cho Shipper (Staff cũng ko được vào trang giao hàng)
  if (requiresShipper && !isUserShipper && !isUserAdmin) {
    // alert("Trang này chỉ dành cho nhân viên giao hàng!");
    return next('/');
  }

  // 3. Chặn trang Quản trị (Chỉ Admin và Staff được vào)
  if (requiresAdmin && !isUserAdmin && !isUserStaff) {
    // alert("Bạn không có quyền truy cập trang quản trị!");
    return next('/'); 
  }

  // 4. Chặn Staff vào các trang STRICT ADMIN (Tổng quan, Báo cáo)
  if (requiresStrictAdmin && !isUserAdmin) {
    // alert("Bạn không có quyền xem trang này! Đang chuyển hướng...");
    return next('/admin/orders'); // Nếu Staff cố tình vào Dashboard, đẩy sang trang Đơn hàng
  }

  // Hợp lệ -> Cho qua
  next();
});

export default router