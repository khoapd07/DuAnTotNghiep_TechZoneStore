import { createApp } from 'vue'
import App from './App.vue'
import router from './router' 
import axios from 'axios'

// CSS của Bootstrap 5
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

// Thêm dòng này để nạp font Bootstrap Icons
import 'bootstrap-icons/font/bootstrap-icons.css' 

axios.defaults.baseURL = 'http://localhost:8080';

// Thêm Interceptor: Trước khi gửi bất kỳ request nào đi, hãy chạy hàm này
axios.interceptors.request.use(
  (config) => {
    // Lấy token từ localStorage
    const token = localStorage.getItem('jwt_token');
    
    // Nếu có token, nhét nó vào Header Authorization
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

const app = createApp(App) 
app.use(router) 
app.mount('#app')