<template>
  <div class="techzone-support min-vh-screen bg-light py-4">
    <main class="container">
      <section class="text-center mb-4 pb-2">
        <h1 class="h2 fw-black text-uppercase mb-2">Liên Hệ Hỗ Trợ</h1>
        <p class="text-muted mx-auto mb-0" style="max-width: 600px; font-size: 0.95rem;">
          Chúng tôi luôn ở đây để đồng hành cùng bạn. Mọi thắc mắc về sản phẩm và dịch vụ sẽ được giải đáp ngay lập tức.
        </p>
      </section>

      <div class="row g-4">
        <div class="col-lg-5">
          <div class="card border-0 shadow-sm rounded-4 p-3 mb-3">
            <div class="d-flex align-items-center gap-3 mb-3">
              <div class="zalo-box rounded-3 d-flex align-items-center justify-center text-white fw-bold shadow-sm">
                Zalo
              </div>
              <div>
                <h3 class="h6 fw-bold mb-0">Hỗ trợ qua Zalo</h3>
                <p class="text-zalo fw-semibold mb-0" style="font-size: 0.8rem;">Phản hồi nhanh chóng 24/7</p>
              </div>
            </div>
            <p class="text-secondary mb-3" style="font-size: 0.85rem;">
              Kết nối trực tiếp với đội ngũ tư vấn kỹ thuật chuyên nghiệp của TechZone để được hỗ trợ tốt nhất.
            </p>
            <a href="#"><button class="btn btn-zalo btn-sm w-100 rounded-3 fw-bold d-flex align-items-center justify-content-center gap-2 py-2">
              <span class="material-symbols-outlined fs-6">chat_bubble</span>
              Mở Zalo ngay
            </button></a>
          </div>

          <div class="card border-0 shadow-sm rounded-4 p-3">
            <h6 class="text-muted text-uppercase fw-bold mb-3 tracking-widest" style="font-size: 0.7rem;">Thông tin liên hệ khác</h6>
            <div class="d-flex flex-column gap-3">
              <div v-for="info in contactInfo" :key="info.label" class="d-flex align-items-center gap-3">
                <div class="icon-box bg-light-green text-primary-green rounded-3 d-flex align-items-center justify-content-center shrink-0">
                  <span class="material-symbols-outlined fs-5">{{ info.icon }}</span>
                </div>
                <div>
                  <p class="text-muted text-uppercase fw-bold mb-0" style="font-size: 0.65rem;">{{ info.label }}</p>
                  <p class="fw-bold mb-0" style="font-size: 0.9rem;">{{ info.value }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-lg-7">
          <div class="d-flex align-items-center gap-2 mb-3">
            <div class="icon-circle bg-primary-green text-dark rounded-circle d-flex align-items-center justify-content-center">
              <span class="material-symbols-outlined fs-5">help</span>
            </div>
            <h3 class="h6 fw-bold text-uppercase mb-0">Câu Hỏi Thường Gặp</h3>
          </div>

          <div class="accordion-custom d-flex flex-column gap-2">
            <div v-for="(faq, index) in faqs" :key="index" 
                 class="card border rounded-4 shadow-sm overflow-hidden pointer transition-all"
                 @click="faq.open = !faq.open">
              <div class="card-body p-3">
                <div class="d-flex justify-content-between align-items-center">
                  <h5 class="fw-bold mb-0" style="font-size: 0.9rem;">{{ faq.question }}</h5>
                  <span class="material-symbols-outlined fs-5 transition-rotate" :class="{'rotate-180': faq.open}">
                    expand_more
                  </span>
                </div>
                <div v-if="faq.open" class="mt-2 text-secondary animate-fade-in" style="font-size: 0.85rem;">
                  {{ faq.answer }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div class="position-fixed bottom-0 end-0 m-3 d-flex flex-column align-items-end" style="z-index: 1050;">
      
      <transition name="fade-slide">
        <div v-if="isBotOpen" class="bot-widget shadow-lg border rounded-4 overflow-hidden d-flex flex-column bg-white mb-3" style="width: 320px;">
          <div class="bg-primary-green p-2 px-3 d-flex align-items-center justify-content-between text-dark">
            <div class="d-flex align-items-center gap-2">
              <div class="position-relative">
                <div class="avatar-bot bg-dark text-primary-green rounded-circle d-flex align-items-center justify-content-center">
                  <span class="material-symbols-outlined fs-5">smart_toy</span>
                </div>
                <span class="position-absolute bottom-0 end-0 border border-2 border-primary-green bg-white rounded-circle" style="width: 10px; height: 10px;"></span>
              </div>
              <div>
                <p class="fw-bold mb-0 text-uppercase" style="font-size: 0.75rem;">Bot hỗ trợ</p>
                <p class="fw-bold text-uppercase opacity-75 mb-0" style="font-size: 9px;">Online 24/7</p>
              </div>
            </div>
            <button @click="isBotOpen = false" class="btn btn-link text-dark p-0 d-flex align-items-center text-decoration-none">
              <span class="material-symbols-outlined fs-5">close</span>
            </button>
          </div>

          <div class="chat-body p-3 bg-light overflow-auto" style="height: 280px;">
            <div class="text-center mb-2">
              <span class="badge bg-secondary-subtle text-secondary rounded-pill fw-bold text-uppercase" style="font-size: 8px;">Hôm nay {{ currentTime }}</span>
            </div>
            <div class="d-flex align-items-end gap-2 mb-2">
              <div class="avatar-xs bg-secondary-subtle rounded-circle d-flex align-items-center justify-content-center shrink-0">
                <span class="material-symbols-outlined" style="font-size: 0.8rem;">smart_toy</span>
              </div>
              <div class="bg-white p-2 px-3 rounded-4 rounded-start-0 shadow-sm border border-light" style="font-size: 0.8rem;">
                Chào bạn! Tôi có thể giúp gì cho bạn?
              </div>
            </div>
          </div>

          <div class="p-2 px-3 bg-white border-top d-flex align-items-center gap-2">
            <input v-model="userMsg" @keyup.enter="sendMsg" class="form-control form-control-sm rounded-pill border-light bg-light shadow-none" placeholder="Nhập tin nhắn..." style="font-size: 0.8rem;" />
            <button @click="sendMsg" class="btn btn-primary-green rounded-circle p-0 d-flex align-items-center justify-content-center shadow-sm" style="width: 32px; height: 32px;">
              <span class="material-symbols-outlined text-dark fs-6">send</span>
            </button>
          </div>
        </div>
      </transition>

      <transition name="fade-scale">
        <button v-if="!isBotOpen" @click="isBotOpen = true" class="btn btn-primary-green rounded-circle shadow-lg d-flex align-items-center justify-content-center" style="width: 56px; height: 56px;">
          <span class="material-symbols-outlined fs-3 text-dark">chat</span>
        </button>
      </transition>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const isBotOpen = ref(false); // Đổi thành false để mặc định thu nhỏ lúc mới vào web (tùy chọn)
const userMsg = ref('');
const currentTime = ref('09:41');

const contactInfo = [
  { label: 'Hotline', value: '1900 1234', icon: 'call' },
  { label: 'Email', value: 'support@techzone.vn', icon: 'mail' },
  { label: 'Địa chỉ', value: 'TP. Hồ Chí Minh', icon: 'location_on' },
];

const faqs = ref([
  { 
    question: 'Chính sách bảo hành tại TechZone như thế nào?', 
    answer: 'Tất cả sản phẩm đều được bảo hành chính hãng từ 12-24 tháng. Chúng tôi cam kết 1 đổi 1 trong vòng 30 ngày đầu tiên nếu phát sinh lỗi phần cứng từ nhà sản xuất.',
    open: true 
  },
  { 
    question: 'Thời gian và chi phí giao hàng tính thế nào?', 
    answer: 'TechZone giao hàng hỏa tốc trong 2 giờ tại nội thành TP.HCM. Với các tỉnh thành khác, thời gian nhận hàng từ 2-3 ngày làm việc. Miễn phí vận chuyển cho đơn hàng từ 2.000.000đ.',
    open: false 
  },
  { 
    question: 'Tôi có được kiểm tra hàng trước khi thanh toán không?', 
    answer: 'Chắc chắn rồi! Bạn hoàn toàn có quyền kiểm tra ngoại quan sản phẩm và đối chiếu thông tin trên hóa đơn trước khi thanh toán cho đơn vị vận chuyển.',
    open: false 
  },
  { 
    question: 'TechZone có hỗ trợ lắp đặt PC tại nhà không?', 
    answer: 'Có, đội ngũ kỹ thuật viên của chúng tôi hỗ trợ lắp đặt miễn phí tại nhà cho các đơn hàng Build PC hoặc lắp đặt thiết bị thông minh (Smarthome) trong bán kính 15km.',
    open: false 
  },
  { 
    question: 'Nếu sản phẩm hết thời hạn bảo hành, tôi có được hỗ trợ không?', 
    answer: 'TechZone có trung tâm sửa chữa dịch vụ. Các sản phẩm mua tại hệ thống khi hết hạn bảo hành sẽ được giảm 20% chi phí sửa chữa và thay thế linh kiện.',
    open: false 
  },
  { 
    question: 'Làm sao để tôi tra cứu tình trạng đơn hàng?', 
    answer: 'Bạn có thể vào mục "Lịch sử đơn hàng" trên website hoặc cung cấp mã đơn hàng cho Chatbot ngay góc phải màn hình để được cập nhật trạng thái mới nhất.',
    open: false 
  },
  { 
    question: 'TechZone có chương trình thu cũ đổi mới (Trade-in) không?', 
    answer: 'Chúng tôi hỗ trợ thu mua các dòng VGA, CPU và Laptop đời cũ với giá cực tốt để bạn lên đời linh kiện mới. Vui lòng mang máy đến cửa hàng gần nhất để được thẩm định.',
    open: false 
  }
]);

const sendMsg = () => {
  if (userMsg.value.trim()) {
    console.log("Gửi:", userMsg.value);
    userMsg.value = '';
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20,400,0,0');
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300..700&display=swap');

.material-symbols-outlined {
  font-family: 'Material Symbols Outlined';
  display: inline-block;
  vertical-align: middle;
}

/* Kích thước các Box được thu nhỏ */
.zalo-box { width: 52px; height: 52px; background-color: #0068FF; font-size: 0.9rem; }
.icon-box { width: 36px; height: 36px; }
.icon-circle { width: 32px; height: 32px; }
.avatar-bot { width: 32px; height: 32px; }
.avatar-xs { width: 24px; height: 24px; }

/* Màu sắc */
.text-primary-green { color: #3cf91a !important; }
.bg-primary-green { background-color: #3cf91a !important; }
.bg-light-green { background-color: rgba(60, 249, 26, 0.1) !important; }
.btn-primary-green { background-color: #3cf91a; border: none; }
.btn-primary-green:hover { background-color: #34d616; transform: scale(1.05); transition: 0.2s;}

.text-zalo { color: #0068FF; }
.btn-zalo { background-color: #0068FF; color: white; border: none; }

.pointer { cursor: pointer; }
.transition-all { transition: all 0.25s ease; }
.transition-rotate { transition: transform 0.25s ease; }
.rotate-180 { transform: rotate(180deg); }

.animate-fade-in { animation: fadeIn 0.2s ease-in-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-5px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Hiệu ứng trượt lên/xuống cho khung chat */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

/* Hiệu ứng zoom in/out cho nút bong bóng */
.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 0.2s ease;
}
.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.5);
}

.techzone-support {
  font-family: 'Space Grotesk', system-ui, sans-serif;
  letter-spacing: -0.01em;
}

.tracking-widest { letter-spacing: 0.1em; }
.fw-black { font-weight: 800; }
</style>