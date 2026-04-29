<template>
  <div class="position-fixed bottom-0 end-0 m-3 d-flex flex-column align-items-end" style="z-index: 1050; font-family: 'Space Grotesk', system-ui, sans-serif;">
    
    <transition name="fade-slide">
      <div v-if="isBotOpen" class="bot-widget shadow-lg border rounded-4 overflow-hidden d-flex flex-column bg-white mb-3" style="width: 350px;">
        <div class="bg-primary-green p-2 px-3 d-flex align-items-center justify-content-between text-dark">
          <div class="d-flex align-items-center gap-2">
            <div class="position-relative">
              <div class="avatar-bot bg-dark text-primary-green rounded-circle d-flex align-items-center justify-content-center">
                <span class="material-symbols-outlined fs-5">smart_toy</span>
              </div>
              <span class="position-absolute bottom-0 end-0 border border-2 border-primary-green bg-white rounded-circle" style="width: 10px; height: 10px;"></span>
            </div>
            <div>
              <p class="fw-bold mb-0 text-uppercase" style="font-size: 0.75rem;">Bot TechZone</p>
              <p class="fw-bold text-uppercase opacity-75 mb-0" style="font-size: 9px;">Online 24/7</p>
            </div>
          </div>
          <button @click="isBotOpen = false" class="btn btn-link text-dark p-0 d-flex align-items-center text-decoration-none">
            <span class="material-symbols-outlined fs-5">close</span>
          </button>
        </div>

        <div ref="chatContainer" class="chat-body p-3 bg-light overflow-auto d-flex flex-column gap-3" style="height: 350px;">
          
          <div v-for="(msg, index) in messages" :key="index" 
               class="d-flex align-items-end gap-2" 
               :class="msg.senderType === 'CUSTOMER' ? 'flex-row-reverse' : ''">
            
            <div v-if="msg.senderType !== 'CUSTOMER'" class="avatar-xs bg-secondary-subtle text-dark rounded-circle d-flex align-items-center justify-content-center shrink-0">
              <span class="material-symbols-outlined" style="font-size: 0.8rem;">smart_toy</span>
            </div>

            <div class="p-2 px-3 shadow-sm border border-light text-break"
                 :class="msg.senderType === 'CUSTOMER' ? 'bg-primary-green text-dark rounded-4 rounded-end-0' : 'bg-white rounded-4 rounded-start-0'"
                 style="font-size: 0.85rem; max-width: 80%;">
              <span v-html="formatMessage(msg.content)"></span>
            </div>
          </div>

          <div v-if="isTyping" class="d-flex align-items-end gap-2">
            <div class="avatar-xs bg-secondary-subtle text-dark rounded-circle d-flex align-items-center justify-content-center shrink-0">
              <span class="material-symbols-outlined" style="font-size: 0.8rem;">smart_toy</span>
            </div>
            <div class="bg-white p-2 px-3 rounded-4 rounded-start-0 shadow-sm border border-light text-muted" style="font-size: 0.8rem;">
              <span class="typing-dot">.</span><span class="typing-dot">.</span><span class="typing-dot">.</span>
            </div>
          </div>

        </div>

        <div class="p-2 px-3 bg-white border-top d-flex align-items-center gap-2">
          <input v-model="userMsg" @keyup.enter="sendMsg" :disabled="isTyping" class="form-control form-control-sm rounded-pill border-light bg-light shadow-none" placeholder="Nhập tin nhắn..." style="font-size: 0.85rem;" />
          <button @click="sendMsg" :disabled="isTyping || !userMsg.trim()" class="btn btn-primary-green rounded-circle p-0 d-flex align-items-center justify-content-center shadow-sm shrink-0" style="width: 32px; height: 32px;">
            <span class="material-symbols-outlined text-dark fs-6">send</span>
          </button>
        </div>
      </div>
    </transition>

    <transition name="fade-scale">
      <button v-if="!isBotOpen" @click="isBotOpen = true" class="btn btn-primary-green rounded-circle shadow-lg d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
        <span class="material-symbols-outlined fs-2 text-dark">chat</span>
      </button>
    </transition>

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue';
// Thêm import API từ cấu hình axios dùng chung của bạn
import api from '../utils/axios';

const isBotOpen = ref(false); 
const userMsg = ref('');
const isTyping = ref(false);
const chatContainer = ref(null);
const sessionId = ref('');
let pollingInterval = null; 

// Danh sách tin nhắn
const messages = ref([]);

// Hàm chuyển đổi văn bản thường thành HTML (Click được link, in đậm, xuống dòng...)
const formatMessage = (text) => {
  if (!text) return '';
  
  // 1. Tránh lỗi hiển thị thẻ HTML người dùng cố tình nhập
  let html = text.replace(/</g, '&lt;').replace(/>/g, '&gt;');
  
  // 2. Chuyển Markdown Link: [Tên sản phẩm](http://link) -> Thẻ <a>
  html = html.replace(/\[([^\]]+)\]\((https?:\/\/[^\s)]+)\)/g, '<a href="$2" target="_blank" class="text-primary fw-bold text-decoration-underline">$1</a>');
  
  // 3. Chuyển các link trần trụi (http://...) -> Thẻ <a>
  html = html.replace(/(?<!href=")(https?:\/\/[^\s<]+)/g, '<a href="$1" target="_blank" class="text-primary fw-bold text-decoration-underline">$1</a>');

  // 4. Chuyển Markdown in đậm: **chữ** -> <strong>chữ</strong>
  html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
  
  // 5. Chuyển dấu xuống dòng thành thẻ <br>
  html = html.replace(/\n/g, '<br>');
  
  return html;
};

// Hàm cuộn xuống đáy an toàn và mượt mà
const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
};

// Hàm tải lịch sử chat
const loadChatHistory = async () => {
  if (!sessionId.value) return;
  try {
    // Đổi fetch thành api.get
    const res = await api.get(`/chat/session/${sessionId.value}/messages`);
    const data = res.data; // Axios tự động parse JSON (phân tích JSON)
    
    if (data.length > 0) {
      // Chỉ gán lại mảng messages và cuộn xuống nếu số lượng tin nhắn thực sự tăng lên
      if (messages.value.length < data.length) {
          messages.value = data;
          // Tắt hiệu ứng đang gõ nếu có tin trả lời
          if (data[data.length - 1].senderType !== 'CUSTOMER') {
            isTyping.value = false;
          }
          scrollToBottom();
      }
    } else {
      // Nếu DB rỗng, hiển thị câu chào mặc định (chỉ tạo ở UI, không lưu DB)
      if (messages.value.length === 0) {
          messages.value = [{ senderType: 'AI', content: 'Xin chào! 👋 Mình là trợ lý AI của TechZone. Mình có thể hỗ trợ gì cho bạn hôm nay?' }];
      }
    }
  } catch (error) {
    console.error("Lỗi tải lịch sử:", error);
  }
};

// Khởi tạo khi load trang
onMounted(async () => {
  // Lấy thông tin user đăng nhập
  let loggedInUser = null;
  try {
      const userInfoStr = localStorage.getItem('user_info');
      if(userInfoStr) loggedInUser = JSON.parse(userInfoStr);
  } catch(e) { console.error("Parse user_info error"); }
  
  // Kiểm tra session hiện tại trong LocalStorage
  let storedSession = localStorage.getItem('techzone_chat_session');
  
  if (!storedSession) {
    // Nếu khách đã đăng nhập -> Lấy chính xác username (VD: 'dung')
    if (loggedInUser && loggedInUser.username) {
      storedSession = loggedInUser.username;
    } else {
      // Khách vãng lai
      storedSession = 'Khách_' + Math.random().toString(36).substr(2, 5);
    }
    
    localStorage.setItem('techzone_chat_session', storedSession);
    
    // Khởi tạo phiên dưới Backend
    try {
      // Đổi fetch thành api.post
      await api.post('/chat/session', { 
        sessionId: storedSession,
        customerId: loggedInUser ? loggedInUser.userId : null 
      });
    } catch (e) { console.error("Lỗi tạo session:", e); }
  }
  
  sessionId.value = storedSession;

  // Lần đầu load tin nhắn
  await loadChatHistory();
  // Cuộn xuống đáy sau khi load lần đầu
  scrollToBottom();

  // Quét tin nhắn mới mỗi 2 giây nếu khung chat đang bật
  pollingInterval = setInterval(() => {
    if (isBotOpen.value) {
      loadChatHistory();
    }
  }, 2000);
});

onUnmounted(() => {
  if (pollingInterval) clearInterval(pollingInterval);
});

// Hàm gửi tin nhắn
const sendMsg = async () => {
  const content = userMsg.value.trim();
  if (!content) return;

  // Render ngay lên màn hình
  messages.value.push({ senderType: 'CUSTOMER', content: content });
  userMsg.value = ''; 
  isTyping.value = true; 
  scrollToBottom();

  // Gửi API
  try {
    // Đổi fetch thành api.post
    await api.post('/chat/message', {
      sessionId: sessionId.value,
      senderType: 'CUSTOMER',
      content: content
    });
    
    // Cập nhật lại list sau khi gửi
    await loadChatHistory();
  } catch (error) {
    console.error("Lỗi gọi API Chat:", error);
    isTyping.value = false;
  }
};

// (Tùy chọn) Mỗi lần mở khung chat lên thì tự động cuộn xuống dưới cùng
watch(isBotOpen, (newVal) => {
    if(newVal) {
        scrollToBottom();
    }
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20,400,0,0');

.material-symbols-outlined {
  font-family: 'Material Symbols Outlined';
  display: inline-block;
  vertical-align: middle;
}

.avatar-bot { width: 32px; height: 32px; }
.avatar-xs { width: 28px; height: 28px; }
.shrink-0 { flex-shrink: 0; }
.text-break { word-wrap: break-word; word-break: break-word; }

/* Màu xanh Neon chủ đạo của bạn */
.text-primary-green { color: #3cf91a !important; }
.bg-primary-green { background-color: #3cf91a !important; }
.btn-primary-green { background-color: #3cf91a; border: none; }
.btn-primary-green:hover { background-color: #34d616; transform: scale(1.05); transition: 0.2s;}

/* Animation mượt mà */
.fade-slide-enter-active, .fade-slide-leave-active { transition: all 0.3s ease; }
.fade-slide-enter-from, .fade-slide-leave-to { opacity: 0; transform: translateY(20px); }

.fade-scale-enter-active, .fade-scale-leave-active { transition: all 0.2s ease; }
.fade-scale-enter-from, .fade-scale-leave-to { opacity: 0; transform: scale(0.5); }

/* Animation 3 dấu chấm đang gõ */
.typing-dot {
  animation: typing 1.4s infinite ease-in-out both;
  font-size: 1.2rem;
  line-height: 0.5;
}
.typing-dot:nth-child(1) { animation-delay: -0.32s; }
.typing-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-4px); }
}
</style>