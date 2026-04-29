import axios from 'axios';

// Khởi tạo một Instance (thực thể) của Axios
const api = axios.create({
  // Sử dụng Environment Variable (biến môi trường) trong Vite. Nếu không có sẽ lấy localhost mặc định
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 60000, // Timeout (thời gian chờ tối đa) 10 giây
});

// Thêm Request Interceptor (bộ đánh chặn yêu cầu)
api.interceptors.request.use(
  (config) => {
    // Tự động lấy Token (mã thông báo) từ Local Storage (bộ nhớ cục bộ)
    const token = localStorage.getItem('jwt_token');
    if (token) {
      // Tự động gắn Token (mã thông báo) vào Headers (tiêu đề) cho mọi API
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;