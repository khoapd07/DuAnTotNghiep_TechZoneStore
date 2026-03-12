<template>
  <main class="orders-page py-5 bg-light min-vh-100">
    <div class="container" style="max-width: 1000px;">
      
      <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
          <h1 class="fw-black text-dark mb-1 fs-3">Đơn hàng của tôi</h1>
          <p class="text-muted m-0 fs-7">Quản lý và theo dõi các đơn hàng gần đây của bạn</p>
        </div>
        <button class="btn btn-link text-muted text-decoration-none fw-bold fs-7 d-flex align-items-center gap-2 hover-dark shadow-none p-0">
          <i class="bi bi-question-circle fs-5"></i> Trung tâm trợ giúp
        </button>
      </div>

      <div class="card border-0 shadow-sm rounded-4 mb-4 overflow-hidden">
        <div class="d-flex flex-nowrap overflow-auto hide-scrollbar">
          <button 
            v-for="tab in tabs" 
            :key="tab"
            @click="activeTab = tab"
            class="btn rounded-0 fw-bold fs-7 py-3 px-4 text-nowrap border-0 border-bottom border-3 transition-colors"
            :class="activeTab === tab ? 'border-dark text-dark bg-light' : 'border-transparent text-muted hover-bg-light'"
          >
            {{ tab }}
          </button>
        </div>
      </div>

      <div class="d-flex flex-column gap-4">
        
        <div v-if="loading" class="text-center py-5">
           <div class="spinner-border text-neon" role="status"></div>
           <p class="mt-2 text-muted">Đang tải đơn hàng...</p>
        </div>

        <div v-else-if="filteredOrders.length === 0" class="text-center py-5 bg-white rounded-4 shadow-sm">
           <i class="bi bi-bag-x fs-1 text-muted opacity-25"></i>
           <p class="mt-3 fw-bold text-muted">Không có đơn hàng nào trong mục này.</p>
        </div>

        <div v-for="order in filteredOrders" :key="order.orderId" class="card border-0 shadow-sm rounded-4 overflow-hidden order-card transition-all" :class="{'opacity-75': translateStatus(order.statusName) === 'Đã hủy'}">
          
          <div class="card-header bg-white border-bottom p-3 px-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
            <div class="d-flex align-items-center gap-3 fs-7">
              <span class="fw-bold text-dark">#{{ order.orderCode }}</span>
              <span class="text-muted">|</span>
              <span class="text-muted">{{ formatDate(order.orderDate) }}</span>
            </div>
            <div>
              <span class="badge rounded-pill px-3 py-2 d-flex align-items-center gap-1 fs-8 fw-bold border" :class="getStatusBadge(translateStatus(order.statusName))">
                <i :class="getStatusIcon(translateStatus(order.statusName))" class="fs-7"></i> {{ translateStatus(order.statusName) }}
              </span>
            </div>
          </div>

          <div class="card-body p-4">
            <div v-for="(item, index) in order.orderDetails" :key="index" class="d-flex flex-column flex-sm-row gap-3" :class="{'mt-4 pt-4 border-top': index > 0}">
              <div class="bg-light rounded-3 d-flex align-items-center justify-content-center border flex-shrink-0 overflow-hidden" style="width: 80px; height: 80px;" :class="{'grayscale': translateStatus(order.statusName) === 'Đã hủy'}">
                <img v-if="item.imageUrl" :src="item.imageUrl" class="img-fluid object-fit-contain p-2" alt="product">
                <i v-else class="bi bi-box text-secondary opacity-50 fs-2"></i>
              </div>
              
              <div class="flex-grow-1">
                <h6 class="fw-bold text-dark mb-1 line-clamp-2">{{ item.productName }}</h6>
                <p class="text-muted fs-8 mb-2">Giá niêm yết: {{ formatCurrency(item.price) }}</p>
                <div class="d-flex align-items-center justify-content-between mt-auto">
                  <span class="text-muted fs-7">x{{ item.quantity }}</span>
                  <span class="fw-bold text-dark fs-7">{{ formatCurrency(item.subTotal) }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer bg-white border-top p-3 px-4 d-flex flex-column flex-sm-row align-items-sm-center justify-content-between gap-3">
            <div class="text-end text-sm-start order-1 order-sm-0">
              <span class="text-muted fs-7">Tổng tiền thực trả:</span>
              <span class="fw-black text-dark fs-5 ms-2" :class="{'text-decoration-line-through text-muted': translateStatus(order.statusName) === 'Đã hủy'}">
                {{ formatCurrency(order.finalAmount) }}
              </span>
              <span v-if="order.paymentMethod === 'BANK'" class="d-block fs-8 mt-1" :class="order.paymentStatus ? 'text-success fw-bold' : 'text-warning fw-bold'">
                <i class="bi" :class="order.paymentStatus ? 'bi-check-circle-fill' : 'bi-hourglass-split'"></i> 
                {{ order.paymentStatus ? 'Đã xác nhận thanh toán' : 'Đang chờ Admin xác nhận CK' }}
              </span>
            </div>
            
            <div class="d-flex flex-wrap justify-content-end gap-2 order-0 order-sm-1">
              <router-link :to="`/order/${order.orderCode}`" class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2 text-decoration-none">
                Xem chi tiết
              </router-link>
              
              <template v-if="translateStatus(order.statusName) === 'Giao hàng thành công'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
                <button v-if="hasUnreviewedItems(order)" @click="openReviewModal(order)" data-bs-toggle="modal" data-bs-target="#reviewModal" class="btn btn-neon fw-bold text-dark fs-8 rounded-3 px-3 py-2 shadow-sm">
                  Đánh giá
                </button>
                <button v-else class="btn btn-light border-success text-success fw-bold fs-8 rounded-3 px-3 py-2 shadow-sm disabled">
                  <i class="bi bi-check2-all"></i> Đã đánh giá
                </button>
              </template>
              
              <template v-if="translateStatus(order.statusName) === 'Đang giao hàng'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2 disabled opacity-50">Đánh giá</button>
              </template>
              
              <template v-if="translateStatus(order.statusName) === 'Chờ xác nhận'">
                <button @click="cancelOrder(order.orderId)" class="btn btn-outline-danger fw-bold fs-8 rounded-3 px-3 py-2">
                  <span v-if="isCancelling === order.orderId" class="spinner-border spinner-border-sm"></span>
                  Hủy đơn hàng
                </button>
              </template>

              <template v-if="translateStatus(order.statusName) === 'Đã hủy'">
                <button class="btn btn-outline-dark fw-bold fs-8 rounded-3 px-3 py-2">Mua lại</button>
              </template>
            </div>
          </div>

        </div>

      </div>

    </div>

    <div class="modal fade" id="reviewModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow rounded-4">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-black fs-4" id="reviewModalLabel">Đánh giá sản phẩm</h5>
            <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body pt-2" v-if="selectedOrderForReview">
            <p class="text-muted fs-7 mb-4">Mã đơn hàng: <span class="fw-bold text-dark">#{{ selectedOrderForReview.orderCode }}</span></p>
            
            <div v-for="item in selectedOrderForReview.orderDetails" :key="item.productId" class="mb-4 pb-4 border-bottom">
              <div class="d-flex gap-3 mb-3">
                <img :src="item.imageUrl || 'https://via.placeholder.com/80'" class="rounded border object-fit-contain p-1" style="width: 60px; height: 60px;" alt="">
                <div>
                  <h6 class="fw-bold fs-7 mb-1">{{ item.productName }}</h6>
                  <span class="text-muted fs-8">Phân loại: Mặc định</span>
                </div>
              </div>

              <div v-if="userReviewedProductIds.includes(item.productId)" class="bg-light p-3 rounded-3 text-center border">
                <i class="bi bi-check-circle-fill text-success fs-5 mb-1 d-block"></i>
                <span class="text-success fw-bold fs-7">Sản phẩm này đã được bạn đánh giá!</span>
              </div>

              <div v-else>
                <div class="d-flex align-items-center gap-3 mb-3">
                  <span class="fw-bold fs-7">Chất lượng:</span>
                  <div class="d-flex gap-1">
                    <i v-for="n in 5" :key="n" 
                       class="bi cursor-pointer fs-4 transition-all"
                       :class="n <= reviewForms[item.productId].rating ? 'bi-star-fill text-warning' : 'bi-star text-muted'"
                       @click="reviewForms[item.productId].rating = n">
                    </i>
                  </div>
                  <span class="fs-8 fw-bold text-warning" v-if="reviewForms[item.productId].rating === 5">Tuyệt vời!</span>
                </div>

                <textarea 
                  v-model="reviewForms[item.productId].comment"
                  class="form-control bg-light border-0 shadow-none fs-7 p-3 rounded-3" 
                  rows="3" 
                  placeholder="Hãy chia sẻ nhận xét của bạn về sản phẩm này nhé (tối thiểu 10 ký tự)...">
                </textarea>
              </div>

            </div>

          </div>
          <div class="modal-footer border-top-0 pt-0">
            <button type="button" class="btn btn-light fw-bold px-4" data-bs-dismiss="modal">Trở lại</button>
            <button v-if="hasUnreviewedItems(selectedOrderForReview)" type="button" class="btn btn-neon fw-bold px-5 text-dark" @click="submitReviews" :disabled="isSubmittingReview">
              <span v-if="isSubmittingReview" class="spinner-border spinner-border-sm me-2"></span>
              GỬI ĐÁNH GIÁ
            </button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router'; 
import axios from 'axios';

const router = useRouter();
const activeTab = ref('Tất cả');
const tabs = ['Tất cả', 'Chờ xác nhận', 'Đang giao', 'Đã giao', 'Đã hủy'];
const orders = ref([]);
const loading = ref(true);

const userReviewedProductIds = ref([]);
const isCancelling = ref(null); // Lưu ID đơn đang xử lý hủy

const getCurrentUserId = () => {
  const userInfoString = localStorage.getItem('user_info');
  if (userInfoString) {
    try {
      return JSON.parse(userInfoString).userId;
    } catch (e) { return null; }
  }
  return null;
};

// Hàm hỗ trợ lấy Token (cần cho API Hủy đơn)
const getAuthConfig = () => {
  const token = localStorage.getItem('jwt_token'); 
  return { headers: { Authorization: `Bearer ${token}` } };
};

const fetchOrders = async () => {
  const userId = getCurrentUserId();
  if (!userId) {
    alert("Vui lòng đăng nhập để xem lịch sử đơn hàng!");
    router.push('/login');
    return;
  }

  loading.value = true;
  try {
    const orderRes = await axios.get(`http://localhost:8080/api/orders/${userId}/history`);
    orders.value = orderRes.data;

    const reviewRes = await axios.get(`http://localhost:8080/api/reviews/user/${userId}`);
    userReviewedProductIds.value = reviewRes.data.map(r => r.productId);

  } catch (error) {
    console.error("Lỗi tải dữ liệu:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchOrders();
});

// ==========================================
// LOGIC HỦY ĐƠN HÀNG (DO KHÁCH HÀNG THỰC HIỆN)
// ==========================================
const cancelOrder = async (orderId) => {
  // Hỏi xác nhận trước khi hủy
  if (!confirm("Bạn có chắc chắn muốn hủy đơn hàng này không?")) return;

  isCancelling.value = orderId;
  const userId = getCurrentUserId();

  try {
    // 4 là mã của trạng thái "Đã hủy" (Cancelled)
    await axios.put(`http://localhost:8080/api/orders/admin/${orderId}/status?statusId=4&employeeId=${userId}`, null, getAuthConfig());
    
    alert("Đã hủy đơn hàng thành công!");
    fetchOrders(); // Load lại danh sách để UI cập nhật màu xám

  } catch (error) {
    console.error("Lỗi hủy đơn:", error);
    alert("Không thể hủy đơn hàng lúc này: " + (error.response?.data || error.message));
  } finally {
    isCancelling.value = null;
  }
};

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0).replace('₫', 'đ');
};

const formatDate = (dateString) => {
  if(!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('vi-VN', { 
    day: '2-digit', month: '2-digit', year: 'numeric', 
    hour: '2-digit', minute: '2-digit' 
  });
};

const translateStatus = (status) => {
  const s = String(status).toLowerCase();
  if (s === '0' || s === 'pending') return 'Chờ xác nhận';
  if (s === '1' || s === 'confirmed') return 'Chờ lấy hàng';
  if (s === '2' || s === 'shipping') return 'Đang giao hàng';
  if (s === '3' || s === 'delivered') return 'Giao hàng thành công';
  if (s === '4' || s === 'cancelled') return 'Đã hủy';
  return status;
};

const getStatusBadge = (status) => {
  switch(status) {
    case 'Giao hàng thành công': return 'bg-success-subtle text-success border-success-subtle';
    case 'Đang giao hàng': return 'bg-warning-subtle text-warning border-warning-subtle';
    case 'Chờ xác nhận': 
    case 'Chờ lấy hàng': return 'bg-primary-subtle text-primary border-primary-subtle';
    case 'Đã hủy': return 'bg-danger-subtle text-danger border-danger-subtle';
    default: return 'bg-light text-dark border-light';
  }
};

const getStatusIcon = (status) => {
  switch(status) {
    case 'Giao hàng thành công': return 'bi-check-circle-fill';
    case 'Đang giao hàng': return 'bi-truck';
    case 'Chờ xác nhận': 
    case 'Chờ lấy hàng': return 'bi-hourglass-split';
    case 'Đã hủy': return 'bi-x-circle-fill';
    default: return 'bi-box';
  }
};

const filteredOrders = computed(() => {
  if (activeTab.value === 'Tất cả') return orders.value;
  if (activeTab.value === 'Đã giao') return orders.value.filter(o => translateStatus(o.statusName || o.status) === 'Giao hàng thành công');
  if (activeTab.value === 'Đang giao') return orders.value.filter(o => translateStatus(o.statusName || o.status) === 'Đang giao hàng');
  return orders.value.filter(o => translateStatus(o.statusName || o.status) === activeTab.value);
});

const selectedOrderForReview = ref(null);
const reviewForms = ref({}); 
const isSubmittingReview = ref(false);

const hasUnreviewedItems = (order) => {
  if (!order || !order.orderDetails) return false;
  return order.orderDetails.some(item => !userReviewedProductIds.value.includes(item.productId));
};

const openReviewModal = (order) => {
  selectedOrderForReview.value = order;
  reviewForms.value = {};
  
  order.orderDetails.forEach(item => {
    if (!userReviewedProductIds.value.includes(item.productId)) {
      reviewForms.value[item.productId] = {
        rating: 5,
        comment: ''
      };
    }
  });
};

const submitReviews = async () => {
  const userId = getCurrentUserId();
  if (!userId) return;

  isSubmittingReview.value = true;
  let successCount = 0;

  try {
    for (const item of selectedOrderForReview.value.orderDetails) {
      const formData = reviewForms.value[item.productId];
      if (!formData || !formData.comment.trim()) continue;

      const payload = {
        userId: userId,
        productId: item.productId,
        rating: formData.rating,
        comment: formData.comment
      };

      await axios.post('http://localhost:8080/api/reviews/send', payload);
      userReviewedProductIds.value.push(item.productId);
      successCount++;
    }

    if (successCount > 0) {
      alert("Cảm ơn bạn đã đánh giá sản phẩm!");
      document.querySelector('#reviewModal .btn-close').click();
    } else {
      alert("Vui lòng nhập bình luận cho ít nhất 1 sản phẩm trước khi gửi!");
    }

  } catch (error) {
    console.error("Lỗi khi gửi đánh giá:", error);
    alert("Có lỗi xảy ra khi gửi đánh giá. Vui lòng thử lại!");
  } finally {
    isSubmittingReview.value = false;
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&family=Space+Grotesk:wght@700&display=swap');

.orders-page { font-family: 'Inter', system-ui, sans-serif; }
.fw-black { font-weight: 900; font-family: 'Space Grotesk', sans-serif; }
.fs-7 { font-size: 0.9rem; }
.fs-8 { font-size: 0.8rem; }
.line-clamp-2 { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.border-transparent { border-color: transparent !important; }
.grayscale { filter: grayscale(100%); }
.text-neon { color: #00FF33 !important; }
.btn-neon { background-color: #00FF33; border: none; transition: 0.2s all; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); box-shadow: 0 4px 15px rgba(0,255,51,0.3) !important; }
.hover-bg-light:hover { background-color: #f8f9fa !important; }
.hover-dark:hover { color: #212529 !important; }
.transition-colors { transition: color 0.2s, background-color 0.2s, border-color 0.2s; }
.order-card { transition: 0.3s ease; }
.order-card:hover { transform: translateY(-2px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.08)!important; }
.hide-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
.hide-scrollbar::-webkit-scrollbar { display: none; }
</style>