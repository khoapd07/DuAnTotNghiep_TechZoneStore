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
import Blogdetail from '../components/Blogdetail.vue'
import ForgotPassword from '../components/ForgotPassword.vue'
import Historyproduct from '../components/Historyproduct.vue'
import OrderVote from '../components/OrderVote.vue'
import Order from '../components/Order.vue'
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
  { path: '/Blogdetail', name: 'Blogdetail', component: Blogdetail },
  { path: '/ForgotPassword', name: 'ForgotPassword', component: ForgotPassword },
  { path: '/Historyproduct', name: 'Historyproduct', component: Historyproduct},
  { path: '/OrderVote', name: 'OrderVote', component: OrderVote},

  // --- ROUTES CHO ADMIN ---
  
  { 
    path: '/admin', 
    component: AdminLayout, // Khai báo Layout cha
    children: [
      { 
        path: '', // Khi vào /admin sẽ load Dashboard
        name: 'AdminDashboard', 
        component: AdminDashboard 
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
        component: Customer 
      },
      {
        path: 'employees',
        name: 'Employee',
        component: Employee
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
        component: Report 
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router